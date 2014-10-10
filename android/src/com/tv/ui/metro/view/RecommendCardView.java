package com.tv.ui.metro.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;
import com.tv.ui.metro.R;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.DisplayItem.UI;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.Image.Ani;

import java.util.ArrayList;
import java.util.List;

public class RecommendCardView extends RelativeLayout {

	private static String TAG = "RecommendCardView";
	protected CenterIconImage mBackView;
	protected CenterIconImage mFrontView;
	protected CenterIconImage mSubView;
	protected CenterIconImage mIconView;

	public ImageView mOperationView;
	public TextView mBannerTextView;
	public TextView mLabelTextView;

	public static int ITEM_NORMAL_SIZE = -1;
	public static int ITEM_H_WIDTH = -1;
	public static int ITEM_H_HEIGHT = -1;

	public static int ITEM_V_WIDTH = -1;
	public static int ITEM_V_HEIGHT = -1;
    public static int ANIMATION_OFFSET_Y = 0;
	private int base_res_id;
	private int default_background_id;

	private boolean showBannerText = false;

	private DisplayItem mItem;
	private AnimatorSet mAnimatorSet;
	private ImageLoader mImageLoader;

	public RecommendCardView bindData(DisplayItem _content) {
		mItem = _content;

		showBannerText = UI.METRO_CELL_BANNER.equals(mItem._ui.type);
		if (!showBannerText) {
			mLabelTextView.setText(mItem.name);
			mLabelTextView.setVisibility(VISIBLE);
		}

		bindImageLayer(mItem.images.icon(), mIconView);
		bindImageLayer(mItem.images.text(), mSubView);
		bindImageLayer(mItem.images.spirit(), mFrontView);
		bindBackground();

		return this;
	}

    public DisplayItem getContentData(){
        return  mItem;
    }

	public RecommendCardView(Context context, int viewType) {
		super(context);
		switch (viewType) {
		case MetroLayout.Vertical:
			base_res_id = R.layout.metro_vertical_item;
			default_background_id = R.drawable.icon_v_default;
			break;
		case MetroLayout.Horizontal:
			base_res_id = R.layout.metro_horizontal_item;
			default_background_id = R.drawable.icon_h_default;
			break;
		case MetroLayout.Normal:
			base_res_id = R.layout.base_metro_item;
			default_background_id = R.drawable.icon_normal_default;
			break;
		default:
			base_res_id = R.layout.base_metro_item;
			default_background_id = R.drawable.icon_normal_default;
			break;
		}

		init(context);
		if (mBackView != null) {
			mBackView.setOnImageChangedListener(mImageChangedListener);
		}
		mImageLoader = VolleyHelper.getInstance(getContext()).getImageLoader();
	}

	public RecommendCardView(Context context) {
		super(context);
		base_res_id = R.layout.base_metro_item;
		default_background_id = R.drawable.icon_normal_default;

		init(context);
		if (mBackView != null) {
			mBackView.setOnImageChangedListener(mImageChangedListener);
		}
	}

	private void bindAnimations() {
		mAnimatorSet = new AnimatorSet();
		if (mItem.images.spirit() != null && mItem.images.spirit().ani != null) {
			List<ValueAnimator> animators = getAnimators(mItem.images.spirit().ani, mFrontView);
			for (ValueAnimator animator : animators) {
				mAnimatorSet.play(animator);
			}
		}
		if (mItem.images.text() != null && mItem.images.text().ani != null) {
			List<ValueAnimator> animators = getAnimators(mItem.images.text().ani, mSubView);
			for (ValueAnimator animator : animators) {
				mAnimatorSet.play(animator);
			}
		}
	}

	private void bindImageLayer(Image image, ImageView view) {
		if (image == null || TextUtils.isEmpty(image.url)) {
            if(view == null){
                Log.d(TAG, "why here");
            }
			view.setVisibility(View.GONE);
			return;
		}

		view.setVisibility(View.VISIBLE);

		if (image.pos != null) {
			ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
			layoutParams.leftMargin = image.pos.x;
			layoutParams.topMargin = getHeight()-image.pos.y-ANIMATION_OFFSET_Y;
			view.setLayoutParams(layoutParams);
		}

		mImageLoader.get(image.url, ImageLoader.getImageListener(view, 0, 0));
	}

	/**
	 * 设置焦点状态 引发动画
	 */
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			// 获得焦点动画
			startAnimation();
		} else {
			// 失去焦点动画 恢复正常状态动画
			reverseAnimation();
		}
	}

	private void startAnimation() {
		if (mAnimatorSet == null) {
			bindAnimations();
		} else {
			mAnimatorSet.cancel();
		}
		mAnimatorSet.start();
	}

	private void reverseAnimation() {
		if (mAnimatorSet == null) {
			return;
		}

		mAnimatorSet.cancel();
		for (Animator animator : mAnimatorSet.getChildAnimations()) {
			ValueAnimator valueAnimator = (ValueAnimator) animator;
			valueAnimator.reverse();
		}
	}

	private Interpolator getInterpolator(int type) {
		Interpolator result = AnimatorModel.DEFAULT_INTERPOLATOR;
		switch (type) {
		case Image.Ani.INTERPOLATOR_ACCELERATEDECELERATE:
			result = new AccelerateDecelerateInterpolator();
			break;
		case Image.Ani.INTERPOLATOR_ACCELERATE:
			result = new AccelerateInterpolator();
			break;
		case Image.Ani.INTERPOLATOR_DECELERATE:
			result = new DecelerateInterpolator();
			break;
		case Image.Ani.INTERPOLATOR_LINEAR:
			result = new LinearInterpolator();
			break;
		case Image.Ani.INTERPOLATOR_BOUNCE:
			result = new BounceInterpolator();
			break;
		default:
			break;
		}
		return result;
	}

	private List<ValueAnimator> getAnimators(Ani ani, View view) {
		List<ValueAnimator> animators = new ArrayList<ValueAnimator>();

		if (ani.scale != null) {
			ScaleAnimatorModel scaleAnimatorModel = new ScaleAnimatorModel(view);
			scaleAnimatorModel.setInterpolator(getInterpolator(ani.scale.interpolator));
			scaleAnimatorModel.setDuration(ani.scale.duration);
			scaleAnimatorModel.setStartDelay(ani.scale.startDelay);
			scaleAnimatorModel.setPivotX(ani.scale.pivotX);
			scaleAnimatorModel.setPivotY(ani.scale.pivotY);
			scaleAnimatorModel.setScale(ani.scale.scale_size);
			return scaleAnimatorModel.toAnimators();
		} else if (ani.translate != null) {
			TranslateAnimatorModel translateAnimatorModel = new TranslateAnimatorModel(view);
			translateAnimatorModel.setInterpolator(getInterpolator(ani.translate.interpolator));
			translateAnimatorModel.setDuration(ani.translate.duration);
			translateAnimatorModel.setStartDelay(ani.translate.startDelay);
			translateAnimatorModel.setXDelta(ani.translate.x_delta);
			translateAnimatorModel.setYDelta(ani.translate.y_delta);
			return translateAnimatorModel.toAnimators();
		} else {
			return animators;
		}
	}

	public boolean isSelected() {
		return super.isSelected();
	}

	private void bindBackground() {
		if (useBgColor() || useBgImage()) {
			return;
		} else {
			mBackView.setBackgroundResource(default_background_id);
		}
	}

	private boolean useBgColor() {
		Image back = mItem.images.back();
		if (back == null) {
			return false;
		}

		return false;

		// if (back.bgcolor <= 0) {
		// return false;
		// }
		//
		// mBackView.setBackgroundColor(back.bgcolor);
		// return true;
	}

	private boolean useBgImage() {
		Image back = mItem.images.back();
		if (back == null) {
			return false;
		}

		if (TextUtils.isEmpty(back.url)) {
			return false;
		}

		mImageLoader.get(back.url, ImageLoader.getCommonViewImageListener(mBackView,
				default_background_id, default_background_id));
		return true;
	}

	// 游戏操控类型
	protected void bindOperateView(int type) {
		mOperationView.setVisibility(View.GONE);
	}

	// 倒影
	protected ImageChangedListener mImageChangedListener = new ImageChangedListener() {

		@Override
		public void onImageChanged(ImageView view) {
			if (null == view) {
				return;
			}
		}
	};

	public int cardWidth() {
		initDimens();

		int width = 0;
		if (mItem._ui.layout.w == 2) {
			width = ITEM_H_WIDTH;
		} else {
			if (mItem._ui.layout.h == 2) {
				width = ITEM_V_WIDTH;
			} else {
				width = ITEM_NORMAL_SIZE;
			}
		}

		return width;
	}

	protected void init(Context context) {
		initDimens();
		this.setClipChildren(true);
		View view = LayoutInflater.from(context).inflate(base_res_id, this);

		mOperationView = (ImageView) view.findViewById(R.id.handler_image_view);
		mBannerTextView = (TextView) view.findViewById(R.id.recommend_textview);
		mLabelTextView = (TextView) view.findViewById(R.id.labelTextView);



        mBackView = (CenterIconImage) view.findViewById(R.id.back_ground_imageview);
		mBannerTextView.setVisibility(View.INVISIBLE);
        OnClickListener mClick = RecommendCardViewClickListenerFactory.getInstance().getRecommendCardViewClickListener();
		this.setOnClickListener(mClick==null?clickListener:mClick);

		mFrontView = (CenterIconImage) view.findViewById(R.id.front_ground_imageview);
		mSubView = (CenterIconImage) view.findViewById(R.id.sub_ground_imageview);
		mIconView = (CenterIconImage) view.findViewById(R.id.icon_imageview);
        if(mIconView == null){
            Log.d(TAG, "why come here");
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
       /// mBannerTextView.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
       // mLabelTextView.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("micontent://" + mItem.ns + "/" + mItem.type + "?rid="
						+ mItem.id));
				intent.putExtra("item", mItem);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				v.getContext().startActivity(intent);

				// TODO
				// ReportManager.getInstance().send(Report.createReport(ReportType.STATISTICS,StaticsType.PAGE_RECOMMEND,
				// null, null, mRecommendInfo.modActionUrl, null, null, null));
			} catch (Exception ne) {
				ne.printStackTrace();
			}
		}
	};

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			if (showBannerText) {
				mBannerTextView.setText(mItem.name);
				mBannerTextView.setVisibility(View.VISIBLE);
                mLabelTextView.setScaleX(1.1f);
                mLabelTextView.setScaleY(1.1f);
			}
			setSelected(true);
		} else {
			mBannerTextView.setVisibility(View.INVISIBLE);
			setSelected(false);
            mLabelTextView.setScaleX(1.0f);
            mLabelTextView.setScaleY(1.0f);

		}
	}

	private void initDimens() {
		if (ITEM_NORMAL_SIZE == -1) {

			ITEM_NORMAL_SIZE = getResources().getDimensionPixelSize(R.dimen.ITEM_NORMAL_SIZE);

			ITEM_H_WIDTH = getResources().getDimensionPixelSize(R.dimen.ITEM_H_WIDTH);
			ITEM_H_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_H_HEIGHT);

			ITEM_V_WIDTH = getResources().getDimensionPixelSize(R.dimen.ITEM_V_WIDTH);
			ITEM_V_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_V_HEIGHT);

            ANIMATION_OFFSET_Y = getResources().getDimensionPixelSize(R.dimen.animation_offset_y);
		}
	}
}
