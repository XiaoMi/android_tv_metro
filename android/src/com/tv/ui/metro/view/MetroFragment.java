package com.tv.ui.metro.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tv.ui.metro.R;
import com.tv.ui.metro.model.Album;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericAlbum;

public class MetroFragment extends Fragment {
    private final String TAG = "MetroFragment";
	public MetroLayout mMetroLayout;
    SmoothHorizontalScrollView mHorizontalScrollView;
	private int tab_count;
	private int tab_index;
    private GenericAlbum<DisplayItem> tab;
    private boolean isUserTab = false;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView ="+this);
        View v = inflater.inflate(R.layout.metrofragment, container, false);
        mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);
        mMetroLayout.setMetroCursorView((MetroCursorView)v.findViewById(R.id.metrocursor));
        
        mHorizontalScrollView = (SmoothHorizontalScrollView)v.findViewById(R.id.horizontalScrollView);
        mHorizontalScrollView.setFadingEdgeLength(getResources().getDimensionPixelSize(R.dimen.fading_edge));
        mHorizontalScrollView.setSmoothScrollingEnabled(true);
        mHorizontalScrollView.setFillViewport(true);

        //setScrollerTime(400);

        tab = (GenericAlbum) this.getArguments().getSerializable("tab");
        mHorizontalScrollView.setTabIndex(tab_index = getArguments().getInt("index", -1));
        mHorizontalScrollView.setTabCount(tab_count = getArguments().getInt("tab_count", -1));        

        isUserTab = getArguments().getBoolean("user_fragment", false);
        initViews();
        return v;
    }
    
    public View addView(View view, int celltype, int row){	
    	return mMetroLayout.addItemView(view, celltype, row);
    }

    public View addView(View view, int celltype, int row, int padding){
        return mMetroLayout.addItemView(view, celltype, row, padding);
    }

    public ArrayList<View> createUserView(){
        return UserViewFactory.getInstance().createUserView(getActivity());
    }

    public void initViews(){

        if(isUserTab == true){
            ArrayList<View> views = createUserView();
            for(View item: views){
                addView(item,  MetroLayout.Vertical,  0, UserViewFactory.getInstance().getPadding(getActivity()));
            }
        }
        else if(tab != null && tab.items != null){
        	//
        	Collections.sort(tab.items);
        	
            for(DisplayItem item:tab.items){
    
                int type = MetroLayout.Normal;
                if(item._ui.layout.w == 2 && item._ui.layout.h ==1)
                    type = MetroLayout.Horizontal;
                else if(item._ui.layout.w == 1 && item._ui.layout.h ==2)
                    type = MetroLayout.Vertical;
    
                addView(new RecommendCardView(getActivity(), type).bindData(item),  type,  item._ui.layout.y-1);
            }
        }
    }

    private View   lastPostionView;
    public View getLastPositionView(){
        return lastPostionView;
    }

    public void focusMoveToLeft(){
        mMetroLayout.focusMoveToLeft();
    }

    public void focusMoveToRight(){
        mMetroLayout.focusMoveToRight();
    }

    public void focusMoveToPreFocused(){
        mMetroLayout.focusMoveToPreFocused();
    }

    public void scrollToLeft(boolean fullscroll){
        if(fullscroll) {
            mHorizontalScrollView.scrollTo(mMetroLayout.getRight(), 0);
            mHorizontalScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHorizontalScrollView.smoothScrollTo(0, 0);
                }
            }, 200);
        }else{
            mHorizontalScrollView.smoothScrollTo(0, 0);
        }
    }

    public void scrollToRight(boolean fullscroll){
        if(fullscroll) {
            mHorizontalScrollView.scrollTo(0, 0);
            mHorizontalScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHorizontalScrollView.smoothScrollTo(mMetroLayout.getRight(), 0);
                }
            }, 200);
        }else{
            mHorizontalScrollView.smoothScrollTo(mMetroLayout.getRight(), 0);
        }
    }
}
