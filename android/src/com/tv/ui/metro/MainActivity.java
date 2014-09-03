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
import android.os.Bundle;
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

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.tv.ui.metro.loader.TabsGsonLoader;
import com.tv.ui.metro.menu.MainMenuMgr;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.Tabs;
import com.tv.ui.metro.view.EmptyLoadingView;
import com.tv.ui.metro.view.MetroFragment;
import com.tv.ui.metro.view.TextViewWithTTF;
import com.tv.ui.metro.view.UserViewFactory;
import com.xiaomi.mitv.app.view.UserView;

public class MainActivity extends FragmentActivity implements MainMenuMgr.OnMenuCancelListener , LoaderManager.LoaderCallbacks<Tabs> {
    private final static String TAG = "TVMetro-MainActivity";

    protected  TabsGsonLoader mLoader;
    TabHost    mTabHost;
    TabWidget  mTabs;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    EmptyLoadingView mLoadingView;
    Tabs             _contents;
    
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
        
        setUserFragmentClass();
        
        getSupportLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, this);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        volleyLoadData();
    }

    //please override this fun
    protected void createTabsLoader(){
        mLoader = new TabsGsonLoader(this);
    }
    
    @Override
    public Loader<Tabs> onCreateLoader(int loaderId, Bundle bundle) {
        if(loaderId == TabsGsonLoader.LOADER_ID){
        	createTabsLoader();
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Tabs> tabsLoader, Tabs tabs) {
        if(tabs != null ){
            updateTabsAndMetroUI(tabs);
            
            mTabHost.requestLayout();
        }
    }

    @Override
    public void onLoaderReset(Loader<Tabs> tabsLoader) {

    }

    //TODO
    //we need update the content and tab here, when call this
    //
    protected void updateTabsAndMetroUI(Tabs content){
        if(_contents != null ){
            if(_contents.update_time == content.update_time) {
                Log.d(TAG, "same content, no need to update UI");
                return;
            }
        }
        mTabs.removeAllViews();
        mViewPager.removeAllViews();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        _contents = content;
        
        
        for(int i=0;i<content.tabs.size();i++) {
            Bundle args = new Bundle();
            args.putSerializable("tab",     content.tabs.get(i));
            args.putInt("index",            i);
            args.putInt("tab_count",        content.tabs.size()+1);
            
            //Log.d(TAG, content.tabs.get(i).toString());

            mTabsAdapter.addTab(mTabHost.newTabSpec(content.tabs.get(i).tab.name).setIndicator(newTabIndicator(content.tabs.get(i).tab.name, i==0)),
                        MetroFragment.class, args);

        }

        //for user fragment
        if(isNeedUserTab){
            Bundle args = new Bundle();
            args.putInt("index",                content.tabs.size());
            args.putInt("tab_count",            content.tabs.size()+1);
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
            filter.addAction("com.xiaomi.mitv.gamecenter.action.SEARCH");
            registerReceiver(mMenuReceiver, filter);
        }
    }

    public static void showStatusBar(Context context, boolean isShow){

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
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onMenuCancel() {
        if (mIsTabFocusedShowMenu) {
            //mNavBar.backToLastFocusTabView();
            mIsTabFocusedShowMenu = false;
        }
    }

    public class MenuReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if( action.equals("com.xiaomi.mitv.gamecenter.action.SEARCH")) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivty.class);
                startActivity(searchIntent);
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
            mViewPager.setCurrentItem(position);
            switchTabView(position);

            if(position < _contents.tabs.size()) {
                ImageGroup ig = _contents.tabs.get(position).album.images;
                if (ig != null) {
                    if (ig.back != null && ig.back.url != null) {
                        //VolleyHelper.getInstance(MainActivity.this).getImageLoader().get(ig.back.url, ImageLoader.getCommonViewImageListener(findViewById(R.id.main_tabs_container), 0, 0));
                    }
                }
            }
            //process the last position
            View view = getCurrentFocus();
            if( isContentMoveLeft) {
                MetroFragment mf = (MetroFragment)fragments.get(new Integer(position));
                if (mf != null) {
                    View lastPositionView = mf.getLastPositionView();
                    Log.d(TAG, "last view = " + lastPositionView);
                    if (lastPositionView != null) {
                        lastPositionView.requestFocus();
                    }
                }
            }
        }
        
        private void switchTabView(int index){
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {
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
    
	private void volleyLoadData() {
		String url = "http://172.27.9.104:9300/testdata/1/1/1/zh/CN?api=index";
		Listener<Tabs> listener = new Listener<Tabs>() {

			@Override
			public void onResponse(Tabs response) {
				Log.d("xxx", "response tabs:" + response);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("xxx", "onErrorResponse error:" + error.toString());
			}
		};
		GsonRequest<Tabs> gsonRequest = new GsonRequest<Tabs>(url, Tabs.class, null, listener,
				errorListener);
		VolleyHelper.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
	}

}
