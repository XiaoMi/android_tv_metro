package com.tv.ui.metro.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.tv.ui.metro.R;
import com.tv.ui.metro.Utils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MetroLayout extends FrameLayout implements View.OnFocusChangeListener{
    public static final int Vertical   = 0; //occupy two vertical cells
    public static final int Horizontal = 1; //occupy two horizontal cells
    public static final int Normal     = 2; //square rectangle
    
	Context mContext;
	int[] rowOffset = new int[2];
	static  int DIVIDE_SIZE = 6;
	boolean mMirror = true;
	AnimatorSet mScaleAnimator;
	List<WeakReference<View>> mViewList = new ArrayList<WeakReference<View>>();
	HashMap<View, WeakReference<MirrorItemView>> mViewMirrorMap = new HashMap<View, WeakReference<MirrorItemView>>();
	MetroCursorView mMetroCursorView;

    View mLeftView;
    View mRightView;
	
	float mDensityScale = 1.0f;
    private static int ITEM_V_WIDTH  = -1;
    private static int ITEM_V_HEIGHT = -1;
    private static int ITEM_H_WIDTH  = -1;
    private static int ITEM_H_HEIGHT  = -1;
    private static int ITEM_NORMAL_SIZE = -1;
    private static int mirror_ref_height= -1;


    public class Item{
		public Item( int type, int row){
			mType = type;
			mRow = row;
		}
		public int mType;
		public int mRow;
	}
	
	public MetroLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public MetroLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init(){
        if(ITEM_V_WIDTH == -1){
            DIVIDE_SIZE   = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
            ITEM_V_WIDTH  = getResources().getDimensionPixelSize(R.dimen.ITEM_V_WIDTH);
            ITEM_V_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_V_HEIGHT);
            ITEM_H_WIDTH  = getResources().getDimensionPixelSize(R.dimen.ITEM_H_WIDTH);
            ITEM_H_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_H_HEIGHT);
            ITEM_NORMAL_SIZE = getResources().getDimensionPixelSize(R.dimen.ITEM_NORMAL_SIZE);
            mirror_ref_height = getResources().getDimensionPixelSize(R.dimen.mirror_ref_height);
        }

		mDensityScale = 1;//mContext.getResources().getDisplayMetrics().densityDpi/320.0f;
        setClipChildren(false);
        setClipToPadding(false);
	}

	public View getItemView(int index){
		if(index>=mViewList.size()) return null;
		return mViewList.get(index).get();
	}

    public View addItemView(View child, int celltype , int row){
        return addItemView(child, celltype , row, DIVIDE_SIZE);
    }

    public void clearItems(){
        removeAllViews();
        rowOffset[1]=rowOffset[0]=0;
        mViewList.clear();
        mViewMirrorMap.clear();
        mLeftView = null;
        mRightView = null;
    }

    public View addItemView(View child, int celltype , int row, int padding){
        if(mLeftView==null){
            mLeftView = child;
        }
        if(row==0) {
            mRightView = child;
        }
		child.setFocusable(true);
		child.setOnFocusChangeListener(this);
		LayoutParams flp;
		mViewList.add(new WeakReference<View>(child));
		View result = child;
		switch(celltype){
		case Vertical:
			flp = new LayoutParams(
					(int)(ITEM_V_WIDTH*mDensityScale),
					(int)(ITEM_V_HEIGHT*mDensityScale));
			if(mMirror){
				MirrorItemView mirror = new MirrorItemView(mContext);
				mirror.setContentView(child, flp);
				flp.bottomMargin = mirror_ref_height;
				flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				flp.leftMargin = rowOffset[0];
				flp.topMargin = getPaddingTop();
				flp.rightMargin = getPaddingRight();
				mirror.setOnFocusChangeListener(this);
				child.setTag(R.integer.tag_view_postion, 0);
				addView(mirror,flp);
				result = mirror;
				mViewMirrorMap.put(child, new WeakReference<MirrorItemView>(mirror));
			}else{
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
                flp.leftMargin = rowOffset[0];
    			flp.topMargin = getPaddingTop();
    			flp.rightMargin = getPaddingRight();
				addView(child, flp);
			}
			rowOffset[0]+=ITEM_V_WIDTH*mDensityScale+padding;
			rowOffset[1]=rowOffset[0];
			break;
		case Horizontal:
			flp = new LayoutParams((int)(ITEM_H_WIDTH*mDensityScale), (int)(ITEM_H_HEIGHT*mDensityScale));
			switch(row){
			case 0:
				flp.leftMargin = rowOffset[0];
				flp.topMargin = getPaddingTop();
				flp.rightMargin = getPaddingRight();
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
				addView(child,flp);
				rowOffset[0]+=ITEM_H_WIDTH*mDensityScale+padding;
				break;
			case 1:
				if(mMirror){
					MirrorItemView mirror = new MirrorItemView(mContext);
					mirror.setContentView(child, flp);
					flp.bottomMargin = mirror_ref_height;
					flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					child.setTag(R.integer.tag_view_postion, 1);
					addView(mirror,flp);
					mirror.setOnFocusChangeListener(this);
					result = mirror;
					mViewMirrorMap.put(child, new WeakReference<MirrorItemView>(mirror));
				}else{
					child.setFocusable(true);
					child.setOnFocusChangeListener(this);
                    child.setTag(R.integer.tag_view_postion, 1);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					addView(child,flp);
				}
				rowOffset[1]+=ITEM_H_WIDTH*mDensityScale+padding;
				break;
			}
			break;
		case Normal:
			flp = new LayoutParams(
					(int)(ITEM_NORMAL_SIZE*mDensityScale),
					(int)(ITEM_NORMAL_SIZE*mDensityScale));
			switch(row){
			case 0:
				flp.leftMargin = rowOffset[0];
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
    			flp.topMargin = getPaddingTop();
    			flp.rightMargin = getPaddingRight();
				addView(child,flp);
				rowOffset[0]+=ITEM_NORMAL_SIZE*mDensityScale+padding;
				break;
			case 1:
				if(mMirror){
					MirrorItemView mirror = new MirrorItemView(mContext);
					mirror.setContentView(child, flp);
					flp.bottomMargin = mirror_ref_height;
					flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					addView(mirror,flp);
					child.setTag(R.integer.tag_view_postion, 1);
					mirror.setOnFocusChangeListener(this);
					result = mirror;
					mViewMirrorMap.put(child, new WeakReference<MirrorItemView>(mirror));
				}else{
					child.setFocusable(true);
                    child.setTag(R.integer.tag_view_postion, 1);
					child.setOnFocusChangeListener(this);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					addView(child,flp);
				}
				rowOffset[1]+=ITEM_NORMAL_SIZE*mDensityScale+padding;
				break;
			}
			break;
		}
		return result;
	}

    private View lastFocusedView;
    
    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (lastFocusedView!=null&&lastFocusedView.requestFocus(direction, previouslyFocusedRect)) {
            return true;
        }

        int index;
        int increment;
        int end;
        int count = this.getChildCount();
        if ((direction & FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for (int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            {
                if (child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child,focused);
    }
    
    public void onFocusChange(final View v, boolean hasFocus){
    	if(mScaleAnimator!=null) mScaleAnimator.end();
    	if(mMetroCursorView!=null){
    		if(hasFocus){
    			if(mViewMirrorMap.get(v)!=null){
    				mMetroCursorView.setFocusView(mViewMirrorMap.get(v).get());
    			}else{
        			mMetroCursorView.setFocusView(v);
    			}
    	    	v.setTag(R.integer.tag_view_focused_host_view, mMetroCursorView);
    			lastFocusedView = v;
    		}else{
    			if(mViewMirrorMap.get(v)!=null){
    				mMetroCursorView.setUnFocusView(mViewMirrorMap.get(v).get());
    			}else{
        			mMetroCursorView.setUnFocusView(v);
    			}
    		}
    	}else{
        	if(hasFocus){
                lastFocusedView = v;        
    			bringChildToFront(v);
    			invalidate();
    			ObjectAnimator animX = ObjectAnimator.ofFloat(v, "ScaleX", 
            			new float[] { 1.0F, 1.1F }).setDuration(200);
    			ObjectAnimator animY = ObjectAnimator.ofFloat(v, "ScaleY", 
            			new float[] { 1.0F, 1.1F }).setDuration(200);
    			mScaleAnimator = new AnimatorSet();
    			mScaleAnimator.playTogether(new Animator[] { animX, animY });
    			mScaleAnimator.start();
    			
        		//v.setScaleX(1.1f);
    			//v.setScaleY(1.1f);
        	}else{    	   
        		v.setScaleX(1.0f);
    			v.setScaleY(1.0f);
        	} 
    	}
 	
    }
    
    public void setMetroCursorView(MetroCursorView v){
    	mMetroCursorView = v;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Handle automatic focus changes.
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int direction = 0;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_LEFT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_RIGHT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_UP;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_DOWN;
                    }
                    break;
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_FORWARD;
                    } else if (event.hasModifiers(KeyEvent.META_SHIFT_ON)) {
                        direction = View.FOCUS_BACKWARD;
                    }
                    break;
            }
            if (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP) {
                View focused = findFocus();
                if (focused != null) {
                    View v = focused.focusSearch(direction);
                    if (v == null) {
                        Utils.playKeySound(this, Utils.SOUND_ERROR_KEY);
                        mMetroCursorView.showIndicator();
                    }
                }
            }
        }
        boolean ret = super.dispatchKeyEvent(event);
        return ret;
    }

    public void focusMoveToLeft(){
        mLeftView.requestFocus();
    }

    public void focusMoveToRight(){
        mRightView.requestFocus();
    }

    public void focusMoveToPreFocused(){
        if(lastFocusedView!=null){
            lastFocusedView.requestFocus();
        }else {
            mLeftView.requestFocus();
        }
    }
}
