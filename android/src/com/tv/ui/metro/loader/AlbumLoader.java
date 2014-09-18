package com.tv.ui.metro.loader;

import android.content.Context;

import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by tv metro on 9/1/14.
 */
public class AlbumLoader<T> extends BaseGsonLoader<T> {
    public static final int GAME_CATEGORY_LOADER_ID  = 0x601;
    public static final int GAME_SUBJECT_LOADER_ID   = 0x602;
    public static final int APP_SUBJECT_ID           = 0x603;
    public static final int APP_CATEGORY_ID          = 0x604;
    public static final int VIDEO_SUBJECT_ID         = 0x605;
    public static final int VIDEO_CATEGORY_ID        = 0x606;

    @Override
    public void setCacheFileName() {

    }

    @Override
    public void setLoaderURL(DisplayItem item) {

    }

    public AlbumLoader(Context context) {
        super(context);
    }

    @Override
    protected void loadDataByGson() {

    }
}
