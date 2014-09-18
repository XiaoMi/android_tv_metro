package com.xiaomi.mitv.app.loader;

import android.content.Context;

import com.tv.ui.metro.loader.ItemLoader;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Item;

/**
 * Created by tv metro on 9/1/14.
 */
public class AppItemLoader extends ItemLoader<Item> {

    public AppItemLoader(Context context) {
        super(context);
    }

    @Override
    public void setCacheFileName() {

    }

    @Override
    public void setLoaderURL(DisplayItem _item) {

    }

    @Override
    protected void loadDataByGson() {

    }
}
