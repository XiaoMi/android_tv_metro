package com.tv.ui.metro.loader;

import android.content.Context;

/**
 * Created by tv metro on 9/1/14.
 */
public abstract class ItemLoader<T> extends BaseGsonLoader<T>{
    public static final int GAME_LOADER_ID  = 0x501;
    public static final int APP_LOADER_ID   = 0x502;
    public static final int VIDEO_LOADER_ID = 0x503;

    public ItemLoader(Context context) {
        super(context);
    }
}
