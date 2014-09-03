package com.tv.ui.metro.view;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.tv.ui.metro.R;


public class MirrorItemView extends FrameLayout{
	private View mContentView;
	protected boolean mHasReflection = true;
	private static int REFHEIGHT = -1;
	public static Paint RefPaint=null;
	
	private Bitmap mReflectBitmap;
	private Canvas mReflectCanvas;
	public MirrorItemView(Context context) {
		super(context);

        if(REFHEIGHT == -1)
            REFHEIGHT = getResources().getDimensionPixelSize(R.dimen.mirror_ref_height);
		if(RefPaint==null){
	        RefPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	        RefPaint.setShader(new LinearGradient(0, 0, 
	                0, REFHEIGHT, 
	                new int[] {0x77000000, 0x66AAAAAA, 0x0500000,0x00000000},
	                new float[] {0.0f, 0.1f, 0.9f,1.0f}, Shader.TileMode.CLAMP));
	        RefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		}
		
		this.setClickable(true);
	}
	
	
	public void setContentView(View view, ViewGroup.LayoutParams lp){
		mContentView = view;
		//mContentView.setFocusable(false);
		//setFocusable(true);
		addView(view,lp);
	}
	
	public View getContentView(){
		return mContentView;
	}
	
	public void setReflection(boolean ref){
		mHasReflection = ref;
	}
	
	
	@Override
    public boolean performClick() {        
        return mContentView.performClick();
    }
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
        if(mHasReflection)
        {
            if(mReflectBitmap==null){
            	mReflectBitmap = Bitmap.createBitmap(mContentView.getWidth(), REFHEIGHT, Bitmap.Config.ARGB_8888);
            	mReflectCanvas = new Canvas(mReflectBitmap);
            }     
            drawReflection(mReflectCanvas,mContentView);
            /*
            mReflectCanvas.save();
            mReflectCanvas.scale(1, -1);
            mReflectCanvas.translate(0, -mContentView.getHeight());
            mContentView.draw(mReflectCanvas);
            mReflectCanvas.restore();         
            mReflectCanvas.drawRect(0, 0, mContentView.getWidth(), REFHEIGHT, RefPaint); */
            
            canvas.save();
            int dy = mContentView.getBottom();
            int dx = mContentView.getLeft();
            canvas.translate(dx, dy);
            canvas.drawBitmap(mReflectBitmap, 0, 0, null);
            canvas.restore();
        }
	}
	
	public Bitmap getReflectBitmap(){
		return mReflectBitmap;
	}
	
	public void drawReflection(Canvas canvas,View view){
		canvas.save();
		canvas.clipRect(0, 0, view.getWidth(), REFHEIGHT);
		canvas.save();
		canvas.scale(1, -1);
		canvas.translate(0, -view.getHeight());
		view.draw(canvas);
		canvas.restore();         
		canvas.drawRect(0, 0, view.getWidth(), REFHEIGHT, RefPaint); 
		canvas.restore();
	}
}
