package com.tv.ui.metro.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 *
 */
public class CenterIconImage extends ImageView {

	private Object mSyncObj = new Object();
	private ArrayList<ImageChangedListener> mImageChangedListeners 
		= new ArrayList<ImageChangedListener>(4);
	
	public CenterIconImage(Context context) {
	    this(context, null, 0);
	}
	
	public CenterIconImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CenterIconImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnImageChangedListener(ImageChangedListener listener){
	    addImageChangedListener(listener);
	}
	
	public void addImageChangedListener(ImageChangedListener listener){
	    synchronized(mSyncObj){
	        if(!mImageChangedListeners.contains(listener)){
	            mImageChangedListeners.add(listener);
	        }
	    }
	}

    @Override
    public void setImageBitmap(Bitmap resId) {
        super.setImageBitmap(resId);
        notifyListener();
    }

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		//notifyListener();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		//notifyListener();
	}
	
	//因为该函数方法最终也是调用到setImageDrawable
//	@Override
//	public void setImageBitmap(Bitmap bm)

	private void notifyListener() {
	    if(mSyncObj != null){
            synchronized (mSyncObj) {
                for(ImageChangedListener listener : mImageChangedListeners){
                    if(listener != null){
                        listener.onImageChanged(this);
                    }
                }
            }
	    }
    }
}
