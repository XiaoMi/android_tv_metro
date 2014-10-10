package com.tv.ui.metro;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.loader.BaseGsonLoader;
import com.tv.ui.metro.loader.TabsGsonLoader;
import com.tv.ui.metro.menu.MainMenuMgr;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericSubjectItem;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.utils.ViewUtils;
import com.tv.ui.metro.view.*;
import com.xiaomi.mitv.app.view.UserView;

public class MainActivity extends FragmentActivity implements MainMenuMgr.OnMenuCancelListener , LoaderManager.LoaderCallbacks<GenericSubjectItem<DisplayItem>> {
    private final static String TAG = "TVMetro-MainActivity";

    protected BaseGsonLoader mLoader;
    TabHost    mTabHost;
    TabWidget  mTabs;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    EmptyLoadingView mLoadingView;
    GenericSubjectItem<DisplayItem>   _contents;
    boolean mTabChanging;
    int mPrePagerPosition = 0;

    protected DisplayItem albumItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabs    = (TabWidget)findViewById(android.R.id.tabs);

        ViewStub vStub = (ViewStub) findViewById(R.id.new_home_menu);
        mMenuContainer = (FrameLayout) vStub.inflate();
        mViewPager = (ViewPager)findViewById(R.id.pager);

        mLoadingView = makeEmptyLoadingView(this, (RelativeLayout)findViewById(R.id.tabs_content));

        setScrollerTime(800);

        albumItem = (DisplayItem) getIntent().getSerializableExtra("item");
        setUserFragmentClass();
        getSupportLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, this);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.unbindDrawables(findViewById(R.id.main_tabs_container));
    }

    //please override this fun
    protected void createTabsLoader(){
        mLoader = new TabsGsonLoader(this, albumItem);
    }
    
    @Override
    public Loader<GenericSubjectItem<DisplayItem>> onCreateLoader(int loaderId, Bundle bundle) {
        if(loaderId == TabsGsonLoader.LOADER_ID){
        	createTabsLoader();
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }else{
            return null;
        }
    }

    final static String buildInData="{\"data\":[{\"items\":[{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p017VHRusz5g/R2BoGcjC9rNir1.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"高德地图\",\"times\":{\"updated\":1404466152,\"created\":1404454443},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":1,\"w\":1,\"h\":2}},\"id\":\"180\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"album\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{\"translate\":{\"duration\":500,\"y_delta\":-15,\"interpolator\":0,\"startDelay\":0,\"x_delta\":0}},\"pos\":{\"y\":90,\"x\":342}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01rl7EaZ0XN/doB3Y9Zx35fa4W.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":0,\"x\":48}}},\"name\":\"铁皮人儿童馆\",\"times\":{\"updated\":1401849669,\"created\":0},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":2,\"w\":2,\"h\":1}},\"id\":\"576\",\"type\":\"album\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01OiJJiUlpr/gmj9vgPhOureuX.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"不可思议的妈妈\",\"times\":{\"updated\":1401850237,\"created\":1378090812},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":5,\"w\":1,\"h\":1}},\"id\":\"176\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01shnaa7gKl/98UnssjDNwXMAZ.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"WPS Office\",\"times\":{\"updated\":1387526511,\"created\":1376617877},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":3,\"w\":1,\"h\":1}},\"id\":\"109\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCW8r0R2l/fkBbsIN6I5wI35.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCKbqwnYo/KqHUcCcSmzBm0O.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"大众点评\",\"times\":{\"updated\":1401344334,\"created\":1392277260},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":8,\"w\":1,\"h\":1}},\"id\":\"354\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"album\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":75,\"x\":291}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p019cDUtcXZj/jy0zdtgegX3SUl.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":0,\"x\":0}}},\"name\":\"铁皮人儿童馆\",\"times\":{\"updated\":1401849669,\"created\":0},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":4,\"w\":2,\"h\":1}},\"id\":\"576\",\"type\":\"album\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01yDxlUKG1s/bLtew5tzXRluVv.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"布丁酒店\",\"times\":{\"updated\":1399978027,\"created\":1392964753},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":2,\"w\":1,\"h\":1}},\"id\":\"359\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01lvwJ5nO0g/NoUqlnpclsuxRs.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"不可思议的妈妈\",\"times\":{\"updated\":1401850237,\"created\":1378090812},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":8,\"w\":1,\"h\":1}},\"id\":\"176\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01av5Akioaq/rqODKssj5sWyKj.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"大姨吗\",\"times\":{\"updated\":1399544098,\"created\":1378294032},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":4,\"w\":1,\"h\":1}},\"id\":\"183\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01YGuGzzMWm/rV3uxv8scGgVd1.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"旅游攻略\",\"times\":{\"updated\":1410854752,\"created\":1395399765},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":6,\"w\":1,\"h\":1}},\"id\":\"387\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01C3W0LR82K/HxLBL7NTIuLWya.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"万花筒相册\",\"times\":{\"updated\":1401950825,\"created\":1401882640},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":6,\"w\":1,\"h\":1}},\"id\":\"241\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01JiLwbqnZS/voMex4XV9dXvqm.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"万年历\",\"times\":{\"updated\":1399189032,\"created\":1398650342},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":7,\"w\":1,\"h\":1}},\"id\":\"272\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p014VjmdJ2gi/aO2VnUOhGU2nP9.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"驾考宝典\",\"times\":{\"updated\":1404181894,\"created\":1403775113},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":7,\"w\":1,\"h\":1}},\"id\":\"681\",\"type\":\"item\",\"ns\":\"game\"}],\"images\":{},\"name\":\"推荐\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"type\":\"metro\"},\"id\":\"recommend\",\"type\":\"album\",\"ns\":\"game\"},{\"items\":[{\"target\":{\"type\":\"billboard\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p0126XVsd5Gq/2GgC8oaZghOWFd.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"图书资讯\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":1,\"w\":1,\"h\":2},\"type\":\"metro_cell\"},\"id\":\"7\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01d28NYdNuC/5shzAQ1yH28uLU.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01hama7b3dq/qob3Q6urR3JDdd.png\",\"ani\":{\"translate\":{\"duration\":500,\"y_delta\":0,\"startDelay\":0,\"interpolator\":0,\"x_delta\":10}},\"pos\":{\"y\":0,\"x\":0}}},\"name\":\"娱乐休闲\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":2,\"w\":2,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"1\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01HPE9quenU/4v76OTHjvor8pN.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01nIJvWYrk4/uL0jexuFYZHVK9.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"实用生活\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":5,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"80\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01gT26uzM0J/USFXpIjwUIW62q.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"教育学习\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":4,\"w\":1,\"h\":2},\"type\":\"metro_cell\"},\"id\":\"22\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01LzZgiJgql/XmyB1EeSl4TYXg.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01dU2NZjNAv/nPZrDmqU4JkAgs.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"影音视听\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":2,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"19\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p0106nqgJeV9/g3m1kLf0QB3PyV.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01FYEhauQdZ/UuKDRarFievkqn.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"图书资讯\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":3,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"7\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01b4nagMOxj/9rJs162KwT0N7O.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01h405dti3t/skYNEt5pNJJeOK.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"健康健美\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":5,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"81\",\"type\":\"category\",\"ns\":\"game\"}],\"images\":{},\"name\":\"分类\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"type\":\"metro\"},\"id\":\"categories\",\"type\":\"album\",\"ns\":\"game\"}],\"preload\":{\"images\":[\"\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p017VHRusz5g/R2BoGcjC9rNir1.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01rl7EaZ0XN/doB3Y9Zx35fa4W.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01OiJJiUlpr/gmj9vgPhOureuX.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01shnaa7gKl/98UnssjDNwXMAZ.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCW8r0R2l/fkBbsIN6I5wI35.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCKbqwnYo/KqHUcCcSmzBm0O.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p019cDUtcXZj/jy0zdtgegX3SUl.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01yDxlUKG1s/bLtew5tzXRluVv.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01lvwJ5nO0g/NoUqlnpclsuxRs.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01av5Akioaq/rqODKssj5sWyKj.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01YGuGzzMWm/rV3uxv8scGgVd1.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01C3W0LR82K/HxLBL7NTIuLWya.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01JiLwbqnZS/voMex4XV9dXvqm.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p014VjmdJ2gi/aO2VnUOhGU2nP9.png\",null,\"http://image.box.xiaomi.com/mfsv2/download/s010/p0126XVsd5Gq/2GgC8oaZghOWFd.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01d28NYdNuC/5shzAQ1yH28uLU.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01hama7b3dq/qob3Q6urR3JDdd.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01HPE9quenU/4v76OTHjvor8pN.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01nIJvWYrk4/uL0jexuFYZHVK9.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01gT26uzM0J/USFXpIjwUIW62q.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01LzZgiJgql/XmyB1EeSl4TYXg.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01dU2NZjNAv/nPZrDmqU4JkAgs.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p0106nqgJeV9/g3m1kLf0QB3PyV.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01FYEhauQdZ/UuKDRarFievkqn.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01b4nagMOxj/9rJs162KwT0N7O.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01h405dti3t/skYNEt5pNJJeOK.png\"]},\"update_time\":0}";
    @Override
    public void onLoadFinished(Loader<GenericSubjectItem<DisplayItem>> tabsLoader, final GenericSubjectItem<DisplayItem> tabs) {
        if(tabs != null && tabs.data != null && tabs.data.size() > 0){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateTabsAndMetroUI(tabs);
                    mTabHost.requestLayout();
                }
            });
        }else {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //this is the code for test
                    mLoadingView.stopLoading(true, false);
                    //load test code for out of companny
                    Gson gson = new Gson();
                    GenericSubjectItem<DisplayItem> fromJson = gson.fromJson(buildInData, new TypeToken<GenericSubjectItem<DisplayItem>>() {
                    }.getType());

                    updateTabsAndMetroUI(fromJson);
                    mTabHost.requestLayout();
                    final View tabView = mTabs.getChildTabViewAt(mViewPager.getCurrentItem());
                    tabView.post(new Runnable() {
                        @Override
                        public void run() {
                            tabView.requestFocus();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<GenericSubjectItem<DisplayItem>> tabsLoader) {

    }

    protected void addVideoTestData(GenericSubjectItem<DisplayItem> _content){
        Log.d(TAG, "addVideoTestData");
    }

    protected void updateTabsAndMetroUI(GenericSubjectItem<DisplayItem> content){
        if(_contents != null ){
            if(_contents.update_time == content.update_time) {
                Log.d(TAG, "same content, no need to update UI");
                return;
            }
        }
        mTabs.removeAllViews();
        mViewPager.removeAllViews();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        addVideoTestData(content);
        _contents = content;


        for(int i=0;i<content.data.size();i++) {
            Bundle args = new Bundle();
            args.putSerializable("tab",     content.data.get(i));
            args.putInt("index",            i);
            args.putInt("tab_count",        content.data.size()+1);
            
            //Log.d(TAG, content.tabs.get(i).toString());

            mTabsAdapter.addTab(mTabHost.newTabSpec(content.data.get(i).name).setIndicator(newTabIndicator(content.data.get(i).name, i==0)),
                        MetroFragment.class, args);

        }

        //for user fragment
        if(isNeedUserTab && (albumItem == null || (albumItem != null && albumItem.ns.equals("home")))){
            Bundle args = new Bundle();
            args.putInt("index",                content.data.size());
            args.putInt("tab_count",            content.data.size()+1);
            args.putBoolean("user_fragment", true);
            mTabsAdapter.addTab(mTabHost.newTabSpec(mUserTabName).setIndicator(newTabIndicator(getString(R.string.user_tab), false)), mUserFragmentClass, args);
        }
    }    
    
    protected boolean isNeedUserTab = true;
    protected String mUserTabName = "";
    protected Class  mUserFragmentClass = null;
    protected void setUserFragmentClass(){  
        isNeedUserTab      = true;
    	mUserTabName       = getResources().getString(R.string.user_tab); 
    	mUserFragmentClass = MetroFragment.class;

        //please call this
        UserViewFactory.getInstance().setFactory(new UserViewFactory.ViewCreatorFactory(){
            @Override
            public ArrayList<View> create(Context context) {
                ArrayList<View> views = new ArrayList<View>();
                views.add(new UserView(context, "title 1"));
                views.add(new UserView(context, "title 2"));
                views.add(new UserView(context, "title 2"));
                return  views;
            }

            @Override
            public int getPadding(Context context) {
                return getResources().getDimensionPixelSize(R.dimen.user_view_padding);
            }
        });

        RecommendCardViewClickListenerFactory.getInstance().setFactory(new RecommendCardViewClickListenerFactory.ClickCreatorFactory() {
            @Override
            public View.OnClickListener getRecommendCardViewClickListener() {
                return null;
            }
        });
    }

    private FixedSpeedScroller scroller=null;
    public void setScrollerTime(int scrollerTime){
        try {
            if(scroller!=null){
                scroller.setTime(scrollerTime);
            }else{
                Field mScroller;
                mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                scroller= new FixedSpeedScroller(mViewPager.getContext(),new DecelerateInterpolator());
                scroller.setTime(scrollerTime);
                mScroller.set(mViewPager, scroller);
            }
        } catch (Exception e) {
        }
    }

    private View newTabIndicator(String tabName, boolean focused){
        final String name = tabName;
        View viewC  = View.inflate(this, R.layout.tab_view_indicator_item, null);

        TextViewWithTTF view = (TextViewWithTTF)viewC.findViewById(R.id.tv_tab_indicator);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        mTabs.setPadding(getResources().getDimensionPixelSize(R.dimen.tab_left_offset), 0, 0, 0);

        view.setText(name);

        if(focused == true){
            Resources res = getResources();
            view.setTextColor(res.getColor(android.R.color.white));
            view.setTypeface(null, Typeface.BOLD);
            view.requestFocus();
        }
        return viewC;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        showStatusBar(this, true);
        if(mMenuReceiver == null) {
            mMenuReceiver = new MenuReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.tv.ui.metro.action.SEARCH");
            registerReceiver(mMenuReceiver, filter);
        }
    }

    protected void showStatusBar(Context context, boolean isShow){
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        showStatusBar(this, false);
        if(mMenuReceiver != null) {
            unregisterReceiver(mMenuReceiver);
            mMenuReceiver = null;
        }
    }

    private MainMenuMgr mMainMenu;
    private FrameLayout mMenuContainer;
    private MenuReceiver mMenuReceiver;
    private boolean mIsTabFocusedShowMenu = false;
    private void showActionMenu(FrameLayout container) {
        if( mMainMenu == null) {
            mMainMenu  = new MainMenuMgr(getApplicationContext(), container, this);
        }
        mMainMenu.showMenu(container);
    }


    private boolean isContentMoveLeft = false;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        isContentMoveLeft = false;
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            Utils.playKeySound(mTabs, Utils.SOUND_KEYSTONE_KEY);
            showActionMenu(mMenuContainer);
            return true;
        }

        //
        //fix for one bug for up key and change the tab
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            View view = this.getCurrentFocus();
            Object obj = view.getTag(R.integer.tag_view_postion);
            if(obj != null){
                int position = (Integer)obj;
                if(position == 0){
                    mTabHost.setCurrentTab(mViewPager.getCurrentItem());

                    Utils.playKeySound(mTabs, Utils.SOUND_KEYSTONE_KEY);
                    //set highlight
                    final View tabView = mTabs.getChildTabViewAt(mViewPager.getCurrentItem());
                    tabView.post(new Runnable(){
                        @Override
                        public void run() {
                            tabView.requestFocus();
                        }
                    });

                    return true;
                }
            }
        }

        View view = getCurrentFocus();
        isContentMoveLeft = (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && TextViewWithTTF.class.isInstance(view) == false);

        if(event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT)&& TextViewWithTTF.class.isInstance(view) == true){

            //already in left or right, no need do focus move
            if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && mViewPager.getCurrentItem() == 0) || (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && mViewPager.getCurrentItem() == mViewPager.getChildCount()-1)){

                Utils.playKeySound(mTabs, Utils.SOUND_ERROR_KEY);
                return true;
            }

        	setScrollerTime(500);
        }else{
        	setScrollerTime(500);
        }

        if(event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN||event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            view = this.getCurrentFocus();
            if(view.getId() == R.id.tv_tab_indicator){
                MetroFragment fragment = (MetroFragment)mTabsAdapter.getItem(mViewPager.getCurrentItem());
                fragment.focusMoveToLeft();
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onMenuCancel() {
        if (mIsTabFocusedShowMenu) {
            //mNavBar.backToLastFocusTabView();
            mIsTabFocusedShowMenu = false;
        }
    }

    protected String dataSchemaForSearchString = "misearch://applicationsearch/";
    protected void setSeachSchema(String schema){
    	dataSchemaForSearchString = schema;
    }

    public class MenuReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if( action.equals("com.tv.ui.metro.action.SEARCH")) {
            	try{
	                Intent searchIntent = new Intent(Intent.ACTION_VIEW);	                
	                searchIntent.setData(Uri.parse(dataSchemaForSearchString));
	                startActivity(searchIntent);
            	}catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
            }
        }
    }


    public class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private final FragmentManager fm;

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            fm = activity.getSupportFragmentManager();
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            //container.removeView(fragments.get(new Integer(position)).getView());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Fragment fragment = this.getItem(position);

            if (!fragment.isAdded()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());

                ft.commit();

                fm.executePendingTransactions();
            }

            if (fragment.getView() != null && fragment.getView().getParent() == null) {
                container.addView(fragment.getView());
            }

            return fragment;
        }

        HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
        @Override
        public Fragment getItem(int position) {
            Fragment fg = fragments.get(new Integer(position));
            if(fg == null) {
                TabInfo info = mTabs.get(position);
                fg = Fragment.instantiate(mContext, info.clss.getName(), info.args);
                fragments.put(new Integer(position), fg);
            }            
            return fg;
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mTabChanging = true;
            mViewPager.setCurrentItem(position);
            mTabChanging = false;
            switchTabView(position);

            if(position < _contents.data.size()) {
                ImageGroup ig = _contents.data.get(position).images;
                if (ig != null) {
                    if (ig.back() != null && ig.back().url != null) {
                        //VolleyHelper.getInstance(MainActivity.this).getImageLoader().get(ig.back.url, ImageLoader.getCommonViewImageListener(findViewById(R.id.main_tabs_container), 0, 0));
                    }
                }
            }
        }
        
        private void switchTabView(int index){
            switchTab(index);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
            if(!mTabChanging){
                if(position<mPrePagerPosition) {
                    MetroFragment mf = (MetroFragment) fragments.get(new Integer(position));
                    mf.focusMoveToRight();
                }
                else if(position>mPrePagerPosition) {
                    MetroFragment mf = (MetroFragment) fragments.get(new Integer(position));
                    mf.focusMoveToLeft();
                }
            }else{
                if(position<mPrePagerPosition) {
                    MetroFragment mf = (MetroFragment) fragments.get(new Integer(position));
                    mf.scrollToLeft(true);
                    MetroFragment premf = (MetroFragment) fragments.get(new Integer(mPrePagerPosition));
                    premf.scrollToLeft(false);
                }
                else if(position>mPrePagerPosition) {
                    MetroFragment mf = (MetroFragment) fragments.get(new Integer(position));
                    mf.scrollToLeft(false);
                    MetroFragment premf = (MetroFragment) fragments.get(new Integer(mPrePagerPosition));
                    premf.scrollToRight(false);
                }
            }
            mPrePagerPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    
    public void switchTab(int index){
        TabWidget tw = mTabHost.getTabWidget();
        for(int i=0;i<tw.getChildCount();i++) {
            View viewC = tw.getChildTabViewAt(i);
            //Log.d(TAG, "tab width="+viewC.getWidth() + " left="+viewC.getLeft());
            if(i == index) {
                TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(android.R.color.white));
                view.setTypeface(null, Typeface.BOLD);
            }else{
                TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(R.color.white_50));
                view.setTypeface(null, Typeface.NORMAL);                        
            }
        }
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }
}
