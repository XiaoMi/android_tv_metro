package com.tv.ui.metro.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.tv.ui.metro.R;

public class MetroCursorView extends View {
	private View mFocusView;
	private View mUnFocusView;
	private int[] mFocusLocation = new int[2];
	private int[] mLocation = new int[2];
	private Drawable mDrawableWhite;
	private Drawable mDrawableShadow;
	private float mScaleUp = 1.0f;
	private float mScaleDown = 1.1f;
	private Paint mPaint = new Paint();
	private Rect mRect = new Rect();
	private boolean mMirror = false; 


	ObjectAnimator anim = ObjectAnimator.ofFloat(this, "ScaleUp", 
			new float[] { 1.0F, 1.1F }).setDuration(getResources().getInteger(R.integer.scale_up_duration));
	ObjectAnimator anim1 = ObjectAnimator.ofFloat(this, "ScaleDown", 
			new float[] { 1.1F, 1.0F }).setDuration(getResources().getInteger(R.integer.scale_down_duration));
	
	ObjectAnimator animShime = ObjectAnimator.ofFloat(this, "ScaleUp",
	        new float[] { 1.1F, 1.15F, 1.1F }).setDuration(150);

    ObjectAnimator animUpdate = ObjectAnimator.ofFloat(this, "Update",
            new float[] { 1.0F, 1.0F }).setDuration(getResources().getInteger(R.integer.update_duration));
	        
	public MetroCursorView(Context context) {
		super(context);
		init(context);
	}
	
	public MetroCursorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	void init(Context context){
		mDrawableWhite = getResources().getDrawable(R.drawable.item_highlight);
		mDrawableShadow = getResources().getDrawable(R.drawable.item_shadow);
		mPaint.setColor(0xff000000);
		anim.setInterpolator(new DecelerateInterpolator());
		anim1.setInterpolator(new DecelerateInterpolator());
		animShime.setInterpolator(new AccelerateInterpolator());
		
	}

	@Override
    protected void onDraw(Canvas canvas) {
		drawCursorView(canvas,mFocusView,mScaleUp, true);
		//if(anim1.isRunning())
        {
			//drawCursorView(canvas,mUnFocusView,mScaleDown, false);
		}
    }
	
	public void drawCursorView(Canvas canvas, View view, float scale, boolean focus){
    	if(view!=null){
	    	canvas.save();

			if (null == mLocation) {
				mLocation = new int[2];
			}
			if (null == mFocusLocation) {
				mFocusLocation = new int[2];
			}
			getLocationInWindow(mLocation);		
			view.getLocationInWindow(mFocusLocation);
			
			int width = view.getWidth();
			int height = view.getHeight();
			if(view instanceof MirrorItemView ){
				height = ((MirrorItemView)view).getContentView().getHeight();
			}

			
			int left = (int)(mFocusLocation[0]-mLocation[0]-width*(scale-1)/2);
			int top = (int)(mFocusLocation[1]-mLocation[1]-height*(scale-1)/2);
			canvas.translate(left, top);
	    	canvas.scale(scale, scale);

	    	//view.draw(canvas);
			if(view instanceof MirrorItemView ){
				Bitmap bmp = ((MirrorItemView)view).getReflectBitmap();
				if(bmp != null)
				    canvas.drawBitmap(bmp, 0, height, null);
			}
	    	
	    	if(focus){
				Rect padding = new Rect();
				mDrawableShadow.getPadding(padding);
				mDrawableShadow.setBounds(-padding.left, -padding.top, width+padding.right, height+padding.bottom);
				mDrawableShadow.setAlpha((int)(255*(scale-1)*10));
		    	mDrawableShadow.draw(canvas); 
		    	mDrawableWhite.getPadding(padding);
		    	mDrawableWhite.setBounds(-padding.left-1, -padding.top-1, width+padding.right+1, height+padding.bottom+1);
		    	mDrawableWhite.setAlpha((int)(255*(scale-1)*10));
		    	mDrawableWhite.draw(canvas);

	    	}
            view.draw(canvas);
			canvas.restore();
    	}
	}
	
 
    public void setFocusView(View view){
    	if(mFocusView != view){
	    	mFocusView = view;	
	    	//mMirror = mirror;
	    	mScaleUp = 1.0f;
	    	//anim.setStartDelay(50);
	    	anim.start();
            animUpdate.start();
			if(view instanceof MirrorItemView ){
				((MirrorItemView)view).setReflection(false);
				view.invalidate();
			}
    	}
    }
    
    public void showIndicator(){
        animShime.start(); 
    }
    
    public void setUnFocusView(View view){
    	mFocusView = null;
		if(view instanceof MirrorItemView ){
			((MirrorItemView)view).setReflection(true);
			view.invalidate();
		}
    	if(mUnFocusView != view){
    		mUnFocusView = view;		
    		anim1.start();
    	}
    	invalidate();
    }
    
    /**
     * 该方法不能被混淆
     * @param scale
     */
    public void setScaleUp(float scale){
    	mScaleUp = scale;
    	invalidate();
    }

    
    public void setScaleDown(float scale){
    	mScaleDown = scale;
    	invalidate();
    }

    public void setUpdate(float scale){
        invalidate();
    }

}
