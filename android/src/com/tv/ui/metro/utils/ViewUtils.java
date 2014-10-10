package com.tv.ui.metro.utils;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public final class ViewUtils {
    //release all image resource from view
	public static void unbindDrawables(View view) {
		if (null == view) {
			return;
		}
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
            if(Build.VERSION.SDK_INT >= 16)
			    view.setBackground(null);
            else
                view.setBackgroundDrawable(null);
		}
		if(view instanceof ImageView){
			ImageView imageView = (ImageView)view;
			
			if(imageView.getDrawable() != null){
				imageView.getDrawable().setCallback(null);
				imageView.setImageDrawable(null);
			}
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			if (!(view instanceof AdapterView<?>)) {
				((ViewGroup) view).removeAllViews();
			}
		}
	}
	private ViewUtils() {
	}
}
