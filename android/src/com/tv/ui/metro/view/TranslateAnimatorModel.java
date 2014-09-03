package com.tv.ui.metro.view;

import java.util.ArrayList;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

public class TranslateAnimatorModel extends AnimatorModel {
	
	private static final int DEFAULT_DELTA = 0;
	private static final int MAX_X_DELTA = 606;
	private static final int MAX_Y_DELTA = 300;
	private int mXDelta = DEFAULT_DELTA; // x delta : + to right, - to left
	private int mYDelta = DEFAULT_DELTA; // y delta : + to up, - to down
	
	public TranslateAnimatorModel(View animatorView) {
		super(animatorView);
	}
	
	public int getXDelta() {
		return mXDelta;
	}
	
	public void setXDelta(int delta) {
		if (delta < -MAX_X_DELTA || delta > MAX_X_DELTA) {
			return;
		}
		this.mXDelta = delta;
	}
	
	public int getYDelta() {
		return mYDelta;
	}

	public void setYDelta(int delta) {
		if (delta < -MAX_Y_DELTA || delta > MAX_Y_DELTA) {
			return;
		}
		this.mYDelta = delta;
	}

	@Override
	public List<ValueAnimator> toAnimators() {
		List<ValueAnimator> result = new ArrayList<ValueAnimator>();
		if (mXDelta == 0 && mYDelta == 0) {
			return result;
		}
		if (mXDelta != 0) {
			float currentX = getAnimatorView().getX();
			ObjectAnimator oa = ObjectAnimator.ofFloat(getAnimatorView(), "x", currentX + mXDelta);
			assembleAnimator(oa);
			result.add(oa);
		}
		if (mYDelta != 0) {
			float currentY = getAnimatorView().getY();
			ObjectAnimator oa = ObjectAnimator.ofFloat(getAnimatorView(), "y", currentY + mYDelta);
			assembleAnimator(oa);
			result.add(oa);
		}
		return result;
	}

}
