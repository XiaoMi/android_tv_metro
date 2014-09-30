package com.tv.ui.metro.view;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tv.ui.metro.R;
import com.tv.ui.metro.loader.ProgressNotifiable;

public class EmptyLoadingView extends LinearLayout implements ProgressNotifiable {
	private LinearLayout mProgressLayout;
	private ImageView mProgressView;
	private LinearLayout mEmptyView;
	private TextView mTextView;

	private View mDataLoadingView;
	private Animation mRotateAnim;

	public EmptyLoadingView(Context context) {
		super(context);
		init(context);
	}

    @SuppressWarnings("deprecation")
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.empty_loading_layout, this);
		mProgressLayout = (LinearLayout) this.findViewById(R.id.progress_layout);
		mProgressView = (ImageView) this.findViewById(R.id.empty_progress_view);

		mEmptyView = (LinearLayout) this.findViewById(R.id.empty_layout);
        if(Build.VERSION.SDK_INT >= 16){
            mEmptyView.setBackground(null);
        }else {
            mEmptyView.setBackgroundDrawable(null);
        }
		mTextView = (TextView) this.findViewById(R.id.empty_textview);
		mTextView.getPaint().setFakeBoldText(true);

	}

	public void setDataLoadingView(View dataLoadingView) {
		mDataLoadingView = dataLoadingView;
	}

    @SuppressWarnings("deprecation")
	private void updateStyle(boolean hasData) {
		if (mDataLoadingView == null) {
			if (hasData) {
				getLayoutParams().height = LayoutParams.WRAP_CONTENT;
				//setBackgroundResource(R.drawable.loading_view_bg);//放在最后一项时有用
				// TODO
			} else {
				getLayoutParams().height = LayoutParams.MATCH_PARENT;
                if(Build.VERSION.SDK_INT >= 16){
                    setBackground(null);
                }else {
                    setBackgroundDrawable(null);
                }
			}
		}
	}

	@Override
	public void startLoading(boolean hasData) {
		updateStyle(hasData);
		mProgressLayout.setVisibility(VISIBLE);
		mEmptyView.setVisibility(GONE);

		if (mDataLoadingView != null && hasData) {
			showView(mDataLoadingView);
			hideView(this);
		} else {
			showView(this);
			hideView(mDataLoadingView);
		}
	}

	@Override
	public void stopLoading(boolean hasData, boolean hasNext) {
		if (!hasNext) {
			updateStyle(hasData);
			hideView(mDataLoadingView);
			if (hasData) {
				hideView(this);
			} else {
				showView(this);
				mProgressLayout.setVisibility(GONE);
				viewStopANimation(mProgressView);

				mEmptyView.setVisibility(VISIBLE);
			}
		}
	}

	private void showView(View view) {
		if (view == null) {
			return;
		}
		if (view.getVisibility() == GONE) {
			view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.appear));
			view.setVisibility(VISIBLE);
		}

		if (null == mRotateAnim) {
			viewStartAnimation(mProgressView);
		}
	}

	private void hideView(View view) {
		if (view == null) {
			return;
		}
		if (view.getVisibility() == VISIBLE) {
			if (view.isShown()) {
				view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.disappear));
			}
			view.setVisibility(GONE);
		}
		if (mRotateAnim != null) {
			viewStopANimation(mProgressView);
		}
	}

	@Override
	public void init(boolean hasData, boolean isLoading) {
		updateStyle(hasData);
		if (isLoading) {
			if (mDataLoadingView != null && hasData) {
				setVisibility(GONE);
				mDataLoadingView.setVisibility(VISIBLE);
			} else {
				setVisibility(VISIBLE);
				mProgressLayout.setVisibility(VISIBLE);
				viewStartAnimation(mProgressView);
				mEmptyView.setVisibility(GONE);
			}
		} else {
			if (mDataLoadingView != null) {
				mDataLoadingView.setVisibility(GONE);
			}
			if (hasData) {
				setVisibility(GONE);
			} else {
				setVisibility(VISIBLE);
				mProgressLayout.setVisibility(GONE);
				viewStopANimation(mProgressView);

				mEmptyView.setVisibility(VISIBLE);
			}
		}
	}

	public void setEmptyText(int emptyRes) {
		mTextView.setText(emptyRes);
	}

	public void setEmptyText(CharSequence text) {
		mTextView.setText(text);
	}

	public void viewStartAnimation(ImageView view) {
		mProgressLayout.setVisibility(View.VISIBLE);
		mRotateAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnim.setInterpolator(new LinearInterpolator());
		mRotateAnim.setRepeatCount(Animation.INFINITE);
		mRotateAnim.setDuration(800);
		// mRotateAnim.setFillAfter(false);
		view.startAnimation(mRotateAnim);
	}

	public void viewStopANimation(ImageView view) {
		if (mRotateAnim != null) {
			view.clearAnimation();
			mRotateAnim = null;
			mProgressLayout.setVisibility(View.GONE);
		}
	}
}