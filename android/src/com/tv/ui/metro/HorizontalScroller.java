package com.tv.ui.metro;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

public class HorizontalScroller extends OverScroller {

	private int mDuration =500;
	
	public HorizontalScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
		// TODO Auto-generated constructor stub
	}
	public int getTime(){
	    return mDuration;
	}
	
    public void setTime(int scrollerTime){
        mDuration=scrollerTime;
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead  
        
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }	
}
