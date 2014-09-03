package com.tv.ui.metro.view;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class AnimatorModel {
	public static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	
	private static final int DEFAULT_DURATION = 300;
	private static final int DEFAULT_START_DELAY = 0;
	
	private static final int MAX_DURATION = 10000;    //max duration 10s
	private static final int MIN_DURATION = 50;       //min duration 50ms
	
	private static final int MAX_START_DELAY = 10000; //max delay 10s
	private static final int MIN_START_DELAY = 0;     //min delay 0ms
	
	private Interpolator mInterpolator = DEFAULT_INTERPOLATOR;
	
	private int mDuration = DEFAULT_DURATION;
	private int mStartDelay = DEFAULT_START_DELAY;
	
	private View mAnimatorView;
	
	protected View getAnimatorView() {
		return mAnimatorView;
	}

	public AnimatorModel(View animatorView) {
		this.mAnimatorView = animatorView;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		if (duration < MIN_DURATION || duration > MAX_DURATION) {
			return;
		}
		this.mDuration = duration;
	}

	public Interpolator getInterpolator() {
		return mInterpolator;
	}
	
	public void setInterpolator(Interpolator interpolator) {
		this.mInterpolator = interpolator;
	}
	
	public int getStartDelay() {
		return mStartDelay;
	}

	public void setStartDelay(int startDelay) {
		if (startDelay < MIN_START_DELAY || startDelay > MAX_START_DELAY) {
			return;
		}
		this.mStartDelay = startDelay;
	}
	
	protected void assembleAnimator(ObjectAnimator oa) {
		oa.setDuration(getDuration());
		oa.setInterpolator(getInterpolator());
		if (getStartDelay() != 0) {
			oa.setStartDelay(getStartDelay());
		}
	}
	
	public abstract List<ValueAnimator> toAnimators();
}
