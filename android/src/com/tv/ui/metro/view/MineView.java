package com.tv.ui.metro.view;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;
import com.tv.ui.metro.R;

/**
 * Created by tv metro on 9/2/14.
 */
public class MineView extends RelativeLayout implements ImageChangedListener {

	private RelativeLayout mInfoBgView;
	protected CenterIconImage mActionView;
	protected TextView mInfoPointView;
	private TextView mPlusView;
	private TextView mItemTitleView;
	private TextView mItemSummaryView;

	private BluetoothDevice mBluetoothDevice;

	public MineView(Context context) {
		this(context, null, 0);
	}

	public MineView(Context context, AttributeSet as) {
		this(context, as, 0);
	}

	public MineView(Context context, AttributeSet as, int uiStyle) {
		super(context, as, uiStyle);

		init(getContext());
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		init(getContext());
	}

    @SuppressWarnings("deprecation")
	public void setBackground(Drawable background) {
        if(Build.VERSION.SDK_INT >= 16){
            setBackground(background);
        }else {
            this.setBackgroundDrawable(background);
        }
	}

	public void setBluetoothDevice(BluetoothDevice device) {
		mBluetoothDevice = device;
	}

	public BluetoothDevice blueDevice() {
		return mBluetoothDevice;
	}

	public void setInfoBg(int resid) {
		mInfoBgView.setBackgroundResource(resid);
	}

    @SuppressWarnings("deprecation")
	public void setInfoBg(Drawable d) {
        if(Build.VERSION.SDK_INT >= 16){
            mInfoBgView.setBackground(d);
        }else {
            mInfoBgView.setBackgroundDrawable(d);
        }
	}

	// 设置默认头像
	public void setLogoPhoto(int resid) {
		mInfoPointView.setVisibility(View.GONE);
		mActionView.setVisibility(View.VISIBLE);
		mActionView.setImageResource(resid);
	}

	// 绑定头像
	public void bindLogoPhoto(String url) {
		mInfoPointView.setVisibility(View.GONE);
		mActionView.setVisibility(View.VISIBLE);

		if (!TextUtils.isEmpty(url)) {
			VolleyHelper
					.getInstance(getContext())
					.getImageLoader()
					.get(url,
							ImageLoader.getImageListener(mActionView,
									R.drawable.account_photo_normal,
									R.drawable.account_photo_normal));
		}
	}

	public void setLogoPhotoChangerListener(ImageChangedListener listener) {
		mActionView.addImageChangedListener(listener);
	}

	public void setItemTitle(CharSequence text) {
		mItemTitleView.setText(text);
	}

	public void setItemTitle(int resid) {
		mItemTitleView.setText(resid);
	}

	public void setItemSummary(int resid) {
		mItemSummaryView.setText(resid);
	}

	public void setItemSummary(CharSequence text) {
		mItemSummaryView.setText(text);
	}

	protected int accountItemSize() {
		return getResources().getDimensionPixelSize(R.dimen.ACCOUNT_ITEM_SIZE);
	}

	protected int plusSmallTextSize() {
		return getResources().getInteger(R.integer.text_font_size_two_digit);
	}

	private void init(Context context) {
		this.removeAllViews();

		View viewRoot = LayoutInflater.from(context).inflate(R.layout.mine_layout, null);
		RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(viewRoot, lp);

		mInfoBgView = (RelativeLayout) viewRoot.findViewById(R.id.account_info_bg);
		mActionView = (CenterIconImage) viewRoot.findViewById(R.id.mine_action_view);

		mInfoPointView = (TextView) viewRoot.findViewById(R.id.mine_info_pointer_view);
		mPlusView = (TextView) viewRoot.findViewById(R.id.mine_plus_view);
		mPlusView.setText("+");

		mItemTitleView = (TextView) viewRoot.findViewById(R.id.mine_title_view);
		mItemSummaryView = (TextView) viewRoot.findViewById(R.id.mine_small_view);
	}

	@Override
	public void onImageChanged(ImageView view) {
		if (view != null && mActionView != null) {
			mActionView.setImageDrawable(view.getDrawable());
		}
	}

	public void onStart(Context context) {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public void onResume() {
	}

	public void onCreateView(Activity activity) {
	}
}