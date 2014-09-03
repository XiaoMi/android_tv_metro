package com.tv.ui.metro.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by tv metro on 9/2/14.
 */
public class MainHandler extends Handler {

    private static MainHandler sInstance;
    private Context mContext;

    public static void init(Context context){
        sInstance = new MainHandler(context);
    }

    public static MainHandler getInstance(){
        return sInstance;
    }

    private MainHandler(Context context){
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        if(null == mContext) return;
        super.handleMessage(msg);
    }
}