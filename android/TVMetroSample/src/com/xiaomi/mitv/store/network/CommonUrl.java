package com.xiaomi.mitv.store.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xiaomi.mitv.store.network.Constants;
import com.xiaomi.mitv.store.utils.MiTVBuild;
import com.xiaomi.mitv.store.utils.MiTVSystem;
import com.xiaomi.mitv.store.utils.Utils;

public class CommonUrl {
	private Context mAppContext;

	public CommonUrl(Context appContext) {
		mAppContext = appContext.getApplicationContext();
	}

	public String addCommonParams(String url) {
		CommonUrlBuilder urlBuilder = new CommonUrlBuilder(url);

		urlBuilder.put("locale", getLocale());
		urlBuilder.put("res", getResolution());
		urlBuilder.put("ptf", getProduct());
		urlBuilder.put("device_id", getDeviceId(mAppContext));
		urlBuilder.put("key", Constants.KEY);

		String tmpUrl = urlBuilder.toUrl();
		String path;
		try {
			path = new URL(tmpUrl).getPath();
		} catch (MalformedURLException e) {
			return tmpUrl;
		}

		int indexOfPath = tmpUrl.indexOf(path);
		String strForSign = tmpUrl.substring(indexOfPath);
		Log.e("xxx", "strForSign " + strForSign);
		String sign = genSignature(strForSign);
		Log.e("xxx", "sign " + sign);

		urlBuilder.put("opaque", sign);

		return urlBuilder.toUrl();
	}

	private String getLocale() {
		Locale locale = mAppContext.getResources().getConfiguration().locale;
		return locale.getLanguage() + "_" + locale.getCountry();
	}

	private String getResolution() {
		DisplayMetrics displaymetrics = mAppContext.getResources().getDisplayMetrics();
		if (displaymetrics.heightPixels == 720) {
			return "hd720";
		} else if (displaymetrics.heightPixels == 1080) {
			return "hd1080";
		} else if (displaymetrics.heightPixels == 2160) {
			return "hd2160";
		} else {
			return displaymetrics.widthPixels + "x" + displaymetrics.heightPixels;
		}
	}

	private String getProduct() {
		int productCode = MiTVBuild.getProductCode();
		switch (productCode) {
		case MiTVBuild.M3_PRODUCT:
			return "204";

		case MiTVBuild.M6_PRODUCT:
			return "205";

		case MiTVBuild.M8_PRODUCT:
			return "206";

		case MiTVBuild.TV_PRODUCT:
			return "601";

		case MiTVBuild.TV2_PRODUCT:
			return "602";

		default:
			return "601";
		}
	}

	private static String sDeviceId;

	private static String getDeviceId(Context con) {
		if (sDeviceId == null) {
			sDeviceId = MiTVSystem.getDeviceID(con);
			if ((null == sDeviceId) || sDeviceId.isEmpty()) {
				sDeviceId = "81816e93dd774fa3a422825976434adf";
			}
		}
		return sDeviceId;
	}

	private String genSignature(String str) {
		String tmpUrlStr;
		tmpUrlStr = str + "&token=" + Constants.TOKEN;
		Log.d("xxx", tmpUrlStr);

		String opaque = null;
		try {
			opaque = Utils.getSignature(tmpUrlStr.getBytes(), Constants.SSEC.getBytes());
		} catch (InvalidKeyException e) {
			Log.e("InvalidKeyException", "InvalidKeyException");
		} catch (NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException", "NoSuchAlgorithmException");
		}

		return opaque;
	}
}
