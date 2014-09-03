package com.tv.ui.metro.loader;

import android.content.Context;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tv.ui.metro.model.Tabs;
import org.json.JSONObject;

/**
 * Created by tv metro on 8/28/14.
 */
public class TabsLoader extends BaseLoader<Tabs> {

    private static final  String TAG = "TabsLoader";

    public static int LOADER_ID = 0x401;
    public static final String cacheFileName = "tabs_content.cache";
    public TabsLoader(Context context) {
        super(context);

        //we use file to save the content
        //setNeedFile(true);
        setNeedServer(true);
    }

    @Override
    protected FileLoader getFileLoader(){
        return new TabsFileLoader();
    }

    @Override
    protected UpdateLoader getUpdateLoader() {
        return new TabsUpdateLoader();
    }

    public class TabsFileLoader extends FileLoader{

        //read content from data/tabs_content
        @Override
        protected String localDataFileName() {
            String cachePath = getContext().getCacheDir() + "/" + cacheFileName;
            StringBuilder sb = readCacheFromFile(cachePath);

            return sb.toString();
        }

        @Override
        protected Tabs parseResult(String data) {
            return new Gson().fromJson(data, Tabs.class);
        }
    }

    public class TabsUpdateLoader extends UpdateLoader{

        Object mLock = new Object();
        @Override
        protected void loadData(){
            loadDataByGson();

            synchronized (mLock) {
                try {
                    mLock.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Response.Listener<Tabs> listener = new Response.Listener<Tabs>() {
            @Override
            public void onResponse(Tabs response) {
                Log.d(TAG, "response tabs:" + new Gson().toJson(response));
                synchronized (mLock) {
                    mLock.notifyAll();
                }
                onPostExecute(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse error:" + error.toString());

                synchronized (mLock) {
                    mLock.notifyAll();
                }
                onPostExecute(null);
            }
        };

        private void loadDataByGson() {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
            String url = "http://172.27.9.104:9300/testdata/1/1/1/zh/CN?api=index";
            GsonRequest<Tabs> gsonRequest = new GsonRequest<Tabs>(url, Tabs.class, null, listener,  errorListener);

            gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
            requestQueue.add(gsonRequest);
        }

        @Override
        protected Tabs parseResult(JSONObject json) {
            return null;
        }
    }
}
