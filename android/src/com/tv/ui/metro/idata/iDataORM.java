package com.tv.ui.metro.idata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuhuadong on 7/7/14.
 *
 */
public class iDataORM {
    public static final String AUTHORITY                 = "com.tv.ui.metro";
    public static final Uri SETTINGS_CONTENT_URI         = Uri.parse("content://" + AUTHORITY + "/settings");

    public static final String is_show_friends            = "is_show_friends";
    private static final String data_collect_interval     = "data_collect_interval";

    private static  String TAG = "iDataCenterORM";

    private static iDataORM _instance;
    public static iDataORM getInstance(Context con){
        if(_instance == null){
            _instance = new iDataORM(con);
        }

        return _instance;
    }

    //Hope I can get from server
    public static boolean showFriends(Context context){
        return  getBooleanValue(context, is_show_friends, false);
    }

    private Context mContext;
    private iDataORM(Context con){
        mContext = con.getApplicationContext();
    }

    public static String[]settingsProject =  new String[]{
            "_id",
            "name",
            "application",
            "value",
    };

    public long getLastDataCollectionTime() {
        long lastSyncTime = 0;
        String value = this.getSettingValue(is_show_friends);
        if(value != null){
            try {
                lastSyncTime = Long.valueOf(value);
            }catch (Exception ne){}
        }
        return lastSyncTime;
    }

    public int getDataCollectionInterval(int defaultValue) {
        return 120;
    }


    public static int getIntValue(Context con, String name, int defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        int valueB = defaultValue;
        try{
            if(value != null){
                valueB = Integer.valueOf(value);
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static boolean getBooleanValue(Context con, String name, boolean defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        boolean valueB = defaultValue;
        try{
            if(value != null){
                valueB = value.equals("1")?true:false;
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }


    public static String getStringValue(Context con, String name, String defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        String valueB = defaultValue;
        try{
            if(value != null && value.length() > 0){
                valueB = value;
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static class SettingsCol{
        public static final String ID         = "_id";
        public static final String Name       = "name";
        public static final String Value      = "value";
        public static final String Application= "application";
        public static final String ChangeDate = "date_time";
    }



    //settings
    public String getSettingValue(String name) {
        String va = null;
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return va;
    }

    public long getLongValue(String name, long defaultV) {
        String va = String.valueOf(defaultV);
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return  Long.valueOf(va);
    }

    public boolean getBooleanValue(String name, boolean defaultV) {
        Boolean va = defaultV;
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value)).equals("0")?false:true;
            }
            cursor.close();
        }
        return  va;
    }

    public Uri addSetting(String name, String value) {
        return addSetting(mContext, name, value);
    }

    public static Uri addSetting(Context context, String name, String value) {
        return addSetting(context, SETTINGS_CONTENT_URI, name, value);
    }

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String getSettingValue(Context con, Uri settingUri, String name) {
        String va = null;
        String where = SettingsCol.Name +"='"+name+"'";
        Cursor cursor = con.getContentResolver().query(settingUri, settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
            cursor = null;
        }
        return va;
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

    private static Uri addSetting(Context context, Uri settingUri, String name, String value) {
        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Name, name);
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        //if exist, update
        if(null != getSettingValue(context, settingUri, name))
        {
            updateSetting(context, settingUri, name, value);
        }
        else
        {
            ret = context.getContentResolver().insert(settingUri, ct);
        }

        return ret;
    }

    public static boolean updateSetting(Context context, Uri settingUri, String name, String value) {
        boolean ret = false;
        String where = String.format(" name = \"%1$s\" ", name);
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));

        if(context.getContentResolver().update(settingUri, ct, where, null) > 0)
        {
            ret = true;
        }
        return ret;
    }

}
