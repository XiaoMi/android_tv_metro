package com.tv.ui.metro.loader;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
    public void setLoaderURL() {
        calledURL = "http://172.27.9.104:9300/testdata/1/1/1/zh/CN?api=index";
    }

    public TabsGsonLoader(Context context) {
        super(context);
    }

    @Override
    protected void loadDataByGson() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        GsonRequest<Tabs> gsonRequest = new GsonRequest<Tabs>(calledURL, Tabs.class, null, listener,  errorListener);

        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        requestQueue.add(gsonRequest);
    }

}
