package com.tv.ui.metro.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import com.tv.ui.metro.R;
import com.tv.ui.metro.Utils;

public class SmoothHorizontalScrollView extends HorizontalScrollView{
    final String TAG = "SmoothHorizontalScrollView";
    public SmoothHorizontalScrollView(Context context) {
        this(context, null, 0);
    }
    public SmoothHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SmoothHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


    @Override
    public void scrollBy(int dx, int dy) {
    	super.scrollBy(dx, dy);
    }

    @Override
    public void computeScroll(){
        super.computeScroll();
        
    }

    
    private int _index = -1;
    public void setTabIndex(int index){
        _index = index;
    }
    
    private int _tabcount = -1;
    public void setTabCount(int _count){
        _tabcount = _count;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View currentFocused = findFocus();
        if (currentFocused == this) currentFocused = null;
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN){
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, View.FOCUS_LEFT);
            if(nextFocused == null && _index == 0){
                Object obj = currentFocused.getTag(R.integer.tag_view_focused_host_view);

                if(MetroCursorView.class.isInstance(obj)){
                    Utils.playKeySound((MetroCursorView)obj, Utils.SOUND_ERROR_KEY);
                    ((MetroCursorView)obj).showIndicator();
                }
                return true;
            }
        /*}
        else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN){
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, View.FOCUS_DOWN);
            if(nextFocused == null){
                Object obj = currentFocused.getTag(R.integer.tag_view_focused_host_view);
                if(MetroCursorView.class.isInstance(obj)){
                    Utils.playKeySound((MetroCursorView)obj, Utils.SOUND_ERROR_KEY);
                    ((MetroCursorView)obj).showIndicator();
                }
                Log.d(TAG, "no move to down");
            }   */
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN){
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, View.FOCUS_RIGHT);
            if(nextFocused == null){
                Object obj = currentFocused.getTag(R.integer.tag_view_focused_host_view);
                if(MetroCursorView.class.isInstance(obj)){
                    if(_tabcount != -1 && _index == _tabcount-1){
                        Utils.playKeySound((MetroCursorView)obj, Utils.SOUND_ERROR_KEY);
                        ((MetroCursorView)obj).showIndicator();
                        
                        return true;
                    }
                }
                Log.d(TAG, "no move to right");
            }
        }

        boolean ret = super.dispatchKeyEvent(event);
        if(ret == true && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN ||
                           event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN)){
            Utils.playKeySound(this, Utils.SOUND_KEYSTONE_KEY);
        }
        return ret;
    }
    
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;

        int fadingEdge = this.getResources().getDimensionPixelSize(R.dimen.fading_edge) ;

        // leave room for left fading edge as long as rect isn't at very left
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }

        // leave room for right fading edge as long as rect isn't at very right
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }

        int scrollXDelta = 0;

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move right to get it in view: move right just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += (rect.left - screenLeft);
            } else {
                // get entire rect at right of screen
                scrollXDelta += (rect.right - screenRight);
            }

            // make sure we aren't scrolling beyond the end of our content
            int right = getChildAt(0).getRight();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move right to get it in view: move right just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= (screenRight - rect.right);
            } else {
                // entire rect at left
                scrollXDelta -= (screenLeft - rect.left);
            }

            // make sure we aren't scrolling any further than the left our content
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }
    
    @Override
    protected boolean onRequestFocusInDescendants(int direction,
            Rect previouslyFocusedRect) {

        // convert from forward / backward notation to up / down / left / right
        // (ugh).
        
        if(previouslyFocusedRect != null){
            if (direction == View.FOCUS_FORWARD) {
                direction = View.FOCUS_RIGHT;
            } else if (direction == View.FOCUS_BACKWARD) {
                direction = View.FOCUS_LEFT;
            }
        	View nextFocus = FocusFinder.getInstance().findNextFocusFromRect(this,
                    previouslyFocusedRect, direction);
            if (nextFocus == null) {
                return false;
            }
            return nextFocus.requestFocus(direction, previouslyFocusedRect);
        }else{
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
                if (child.getVisibility()==View.VISIBLE) {
                    if (child.requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
