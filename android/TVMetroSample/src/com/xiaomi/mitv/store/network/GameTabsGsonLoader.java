package com.xiaomi.mitv.store.network;

import android.content.Context;

import com.tv.ui.metro.loader.TabsGsonLoader;
import com.tv.ui.metro.model.DisplayItem;

public class GameTabsGsonLoader  extends TabsGsonLoader {

    public GameTabsGsonLoader(Context context, DisplayItem item) {
        super(context, item);
    }

    @Override
    public void setLoaderURL(DisplayItem item) {
        String url = "http://172.27.15.32:7071/v2/game";
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }
}
