package com.tv.ui.metro.view;

import java.util.ArrayList;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import com.tv.ui.metro.R;


public class ScaleAnimatorModel extends AnimatorModel {
	
	public ScaleAnimatorModel(View animatorView) {
		super(animatorView);

        if(MAX_PIVOT_X == -1) {
            MAX_PIVOT_X = animatorView.getResources().getDimensionPixelSize(R.dimen.MAX_PIVOT_X);
            MAX_PIVOT_Y = animatorView.getResources().getDimensionPixelSize(R.dimen.MAX_PIVOT_Y);
            mPivotY = MAX_PIVOT_Y;
        }
	}

	private static final int MIN_PIVOT_X_Y = 0;
	private static int MAX_PIVOT_X = -1;
	private static int MAX_PIVOT_Y = -1;
	
	private static final float DEFAULT_SCALE = 1.1f;
	private static final float MIN_SCALE = 0f;
	private static final float MAX_SCALE = 3f;
	
	private float mScale = DEFAULT_SCALE;
	
	private int mPivotX = MIN_PIVOT_X_Y;
	private int mPivotY = -1;
	
	public int getPivotX() {
		return mPivotX;
	}
	
	public int getPivotY() {
		return mPivotY;
	}
	
	public void setPivotY(int pivotY) {
		if (pivotY < MIN_PIVOT_X_Y || pivotY > MAX_PIVOT_Y) {
			return;
		}
		this.mPivotY = pivotY;
	}
	
	public void setPivotX(int pivotX) {
		if (pivotX < MIN_PIVOT_X_Y || pivotX > MAX_PIVOT_X) {
			return;
		}
		this.mPivotX = pivotX;
	}
	
	public float getScale() {
		return mScale;
	}

	public void setScale(float scale) {
		if (scale <= MIN_SCALE || scale > MAX_SCALE) {
			return;
		}
		this.mScale = scale;
	}

	@Override
	public List<ValueAnimator> toAnimators() {
		List<ValueAnimator> result = new ArrayList<ValueAnimator>();
		if (mScale == 1.0f) {
			return result;
		}
		getAnimatorView().setPivotX(mPivotX);
		getAnimatorView().setPivotY(mPivotY);
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(getAnimatorView(), "scaleX", mScale);
		assembleAnimator(scaleXAnimator);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(getAnimatorView(), "scaleY", mScale);
		assembleAnimator(scaleYAnimator);
		result.add(scaleXAnimator);
		result.add(scaleYAnimator);
		return result;
	}
}
