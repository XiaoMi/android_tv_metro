package com.tv.ui.metro.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tv.ui.metro.idata.iDataORM;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tv metro on 8/28/14.
 */
public class BaseLoader<T> extends Loader<T> {
    private static final String TAG = "BaseLoader";

    protected T mResult;
    protected volatile boolean mIsLoading;
    protected boolean mNeedFile;
    protected boolean mNeedDatabase;
    protected boolean mNeedServer;
    protected boolean mNeedDeliverResult;

    private boolean mHasDeliverdResult;

    private ArrayList<AsyncTask<Void, Void, T>> mTaskList;
    private int mNextExecuteTask;

    private ProgressNotifiable mProgressNotifiable;
    private volatile boolean mDatabaseSave;//更新时间是否保存过了

    protected volatile int mCurrPageGameCount = -1;//如果此值小于要请求的个数表示没有下页了

    protected long mStampTime = -1;//时间戳

    protected final static int FIRST_PAGE_NUM = 1;
    protected int mPage = FIRST_PAGE_NUM;//分页从1开始
    protected boolean mNeedNextPage;

    public BaseLoader(Context context) {
        super(context);
        init();
    }

    public void nextPage() {
        if (mNeedNextPage) {
            mPage++;
        }

        // 只要翻页了，就不需要从数据库取数据，只从服务器取数据
        setNeedDatabase(false);
    }

    private void init(){
        mIsLoading = false;
        mNeedFile = false;
        mNeedDatabase = true;
        mNeedServer = true;
        mNeedDeliverResult = true;
        mHasDeliverdResult = false;

        mTaskList = new ArrayList<AsyncTask<Void, Void, T>>();
        mNextExecuteTask = 0;

        mPage = FIRST_PAGE_NUM;
        mNeedNextPage = false;
    }

    public void setNeedFile(boolean needFile) {
        this.mNeedFile = needFile;
    }

    public void setNeedDatabase(boolean needDatabase) {
        this.mNeedDatabase = needDatabase;
    }

    public void setNeedServer(boolean needServer) {
        this.mNeedServer = needServer;
    }

    /**
     * 保存更新时间
     * @param key
     * @param updateTime
     */
    protected void saveUpdateTime(String key, long updateTime){
        if(!mDatabaseSave && updateTime != -1){
            mDatabaseSave = true;
            iDataORM.getInstance(getContext()).addSetting(key, String.valueOf(updateTime));
        }
    }


    //获取上次更新的时间
    protected long getLastTime(String key){
        return iDataORM.getInstance(getContext()).getLongValue(key, -1);
    }

    //设置该值后，后续任务执行完均会根据设定值处理数据
    public void setNeedDeliverResult(boolean needDeliverResult) {
        this.mNeedDeliverResult = needDeliverResult;//BUG
    }

    public boolean hasMoreData(){
        if(-1 == mCurrPageGameCount ){
            return false;
        }else{
            return true;
        }
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setProgressNotifiable(ProgressNotifiable progressNotifiable) {
        this.mProgressNotifiable = progressNotifiable;
        // 注册监听器时直接告知当前状态
        if (progressNotifiable != null) {
            progressNotifiable.init(dataExists(), mIsLoading);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {

            //这里浅克隆结果避免由于结果完全一致，不会刷新界面
            deliverResult((T)mResult);
        }

        if (!mIsLoading && (mResult == null || takeContentChanged())) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        mTaskList.clear();
        mNextExecuteTask = 0;
        initTaskList(mTaskList);
        executeNextTask();
    }


    //子类可以继承该方法自定义任务列表，此时默认列表不起作用（一个数据库任务和一个网络任务）
    protected void initTaskList(ArrayList<AsyncTask<Void, Void, T>> tasks) {
        if (mNeedDatabase) {
            DatabaseLoader task = getDatabaseLoader();
            if (task != null) {
                tasks.add(task);
            }
        }
        if(mNeedFile){
            FileLoader task = getFileLoader();
            if(task != null){
                tasks.add(task);
            }
        }
        if (mNeedServer) {
            UpdateLoader task = getUpdateLoader();
            if (task != null) {
                tasks.add(task);
            }
        }
    }


    //调用该方法将会执行下一个任务
    protected void executeNextTask() {
        if (hasNextTask()) {
            AsyncTask<Void, Void, T> task = null;
            while (task == null && hasNextTask()) {
                task = mTaskList.get(mNextExecuteTask);
                mNextExecuteTask ++;
            }
            if (task != null) {
                task.execute();
            }
        }
    }

    protected boolean hasNextTask() {
        return mNextExecuteTask < mTaskList.size();
    }

    protected FileLoader getFileLoader() {
        return null;
    }

    protected DatabaseLoader getDatabaseLoader() {
        return null;
    }

    protected UpdateLoader getUpdateLoader() {
        return null;
    }

    protected boolean dataExists() {
        // 数据存在并且已经发送给界面过
        return mResult != null && mHasDeliverdResult;
    }


    //子类的任务都需要继承该类以获得任务管理和进度通知特性
    protected abstract class BaseTaskLoader extends AsyncTask<Void, Void, T> {
        @Override
        protected void onPreExecute() {
            mIsLoading = true;
            if (mProgressNotifiable != null) {
                mProgressNotifiable.startLoading(dataExists());
            }
        }

        @Override
        protected void onPostExecute(T result) {
            mIsLoading = false;
            if (isResultValid(result)) {
                mResult = result;
                if (mNeedDeliverResult) {
                    BaseLoader.this.deliverResult(result);
                    mHasDeliverdResult = true;
                }
            }
            if (mProgressNotifiable != null) {
                mProgressNotifiable.stopLoading(dataExists(), hasNextTask());
            }
            BaseLoader.this.executeNextTask();
        }


        //检查结果是否有效，如果有效，则会更新数据，并告知界面
        protected boolean isResultValid(T result) {
            return result != null;
        }
    }


    //从数据库将数据载入缓存
    protected abstract class DatabaseLoader extends BaseTaskLoader {

        @Override
        protected T doInBackground(Void... params) {
            Cursor cursor = loadFromDB();
            T newResult = parseResult(cursor);
            if(cursor != null){
                cursor.close();
                cursor = null;
            }
            T result = onDataLoaded(mResult, newResult);
            return result;
        }


        //从数据库中读取数据
        protected abstract Cursor loadFromDB();


        //从cursor中获取数据，如果没有数据则返回null
        protected abstract T parseResult(Cursor cursor);

        //子类可以对获取的数据进行处理，如果该方法返回null，则数据和界面不会更新
        protected T onDataLoaded(T oldResult, T newResult) {
            return newResult;
        }
    }


    //从文件将数据载入缓存
    protected abstract class FileLoader extends BaseTaskLoader {

        @Override
        protected T doInBackground(Void... params) {
            String data = loadFromLocalFile();
            if(!TextUtils.isEmpty(data)){
                T newResult = parseResult(data);
                T result = onDataLoaded(mResult, newResult);
                return result;
            }else{
                return null;
            }
        }


        //从文件流中读取数据
        protected String loadFromLocalFile() {
            InputStream fis = null;
            String data = null;
            try {
                File f = new File(localDataFileName());
                if(f.exists() && f.length() > 0){
                    fis = new FileInputStream(f);
                    byte[] buffer = new byte[(int)f.length()];
                    fis.read(buffer);
                    data = new String(buffer);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fis = null;
                }
            }
            return data;
        }

        protected abstract String localDataFileName();

        // 从String中获取数据，如果没有数据则返回null
        protected abstract T parseResult(String data);


        //子类可以对获取的数据进行处理，如果该方法返回null，则数据和界面不会更新
        protected T onDataLoaded(T oldResult, T newResult) {
            return newResult;
        }
    }


    public class GsonRequest<T> extends Request<T> {
        private final Gson gson = new Gson();
        private final Class<T> clazz;
        private final Map<String, String> headers;
        private final Response.Listener<T> listener;
        private String cacheFile;

        public void setCacheNeed(String _cacheFile){
            cacheFile = _cacheFile;
        }

        /**
         * Make a GET request and return a parsed object from JSON.
         *
         * @param url
         *            URL of the request to make
         * @param clazz
         *            Relevant class object, for Gson's reflection
         * @param headers
         *            Map of request headers
         */
        public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.clazz = clazz;
            this.headers = headers;
            this.listener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
				Log.d(TAG, "response json:" + json);
                long timeStart = System.currentTimeMillis();
                T fromJson = gson.fromJson(json, clazz);
                long timeEnd = System.currentTimeMillis();
                Log.d(TAG, "fromJson take time in ms: " + (timeEnd - timeStart));
                Response<T> res =  Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response));

                if(cacheFile != null && cacheFile.length() > 0){
                    //save to files
                    updateToFile(cacheFile, json);
                }
                return  res;
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public static void updateToFile(String fileName, String response){
        if(null == response || TextUtils.isEmpty(fileName)){
            return;
        }
        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(response.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static StringBuilder readCacheFromFile(String filePath){
        StringBuilder sb = new StringBuilder();
        File f = new File(filePath);
        if(f.exists() == false){
            return  sb;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(filePath));
            byte []buffer = new byte[4096*2];
            int len = -1;
            while((len = fin.read(buffer, 0, 4096*2)) > 0){
                sb.append(buffer);
            }
            buffer = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fin != null){
                try {
                    fin.close();
                    fin = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb;
    }

    //从服务器将数据载入缓存和数据库
    protected abstract class UpdateLoader extends BaseTaskLoader {

        @Override
        protected T doInBackground(Void... params) {
            loadData();
            return null;
        }

        protected abstract void loadData();

        // 从Json中获取数据，如果没有数据则返回null/
        protected abstract T parseResult(JSONObject json);

        //子类可以对获取的数据进行处理，如果该方法返回null，则数据和界面不会更新/
        protected T onDataLoaded(T oldResult, T newResult) {
            return newResult;
        }
    }
}
