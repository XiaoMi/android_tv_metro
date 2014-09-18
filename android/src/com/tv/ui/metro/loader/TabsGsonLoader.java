package com.tv.ui.metro.loader;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.VolleyHelper;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Tabs;

/**
 * Created by tv metro on 9/1/14.
 */
public class TabsGsonLoader extends BaseGsonLoader<Tabs> {
    public static int LOADER_ID = 0x401;
    @Override
    public void setCacheFileName() {
        cacheFileName = "tabs_content.cache";
    }

    @Override
    public void setLoaderURL(DisplayItem item) {
        calledURL = "http://appstore.duokanbox.com/v2/app/home";
    }

    public TabsGsonLoader(Context context) {
        super(context);
    }

    @Override
    protected void loadDataByGson() {
        RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
        GsonRequest<Tabs> gsonRequest = new GsonRequest<Tabs>(calledURL, Tabs.class, null, listener,  errorListener);

        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        requestQueue.add(gsonRequest);
    }

}
