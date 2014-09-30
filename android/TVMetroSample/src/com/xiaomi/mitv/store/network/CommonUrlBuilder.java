package com.xiaomi.mitv.store.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

public class CommonUrlBuilder {

	private String mBaseUrl;
	private StringBuilder mStringBuilder;
	private Map<String, String> mParamsMap;
	private boolean mIsFirstAdd = true;

	private static final String QUESTION_MARK = "?";
	private static final String EQUAL = "=";
	private static final String AMPERSAND = "&";
	private static final String UTF_8 = "UTF-8";

	public CommonUrlBuilder(String baseUrl) {
		mBaseUrl = baseUrl;
		mParamsMap = new LinkedHashMap<String, String>();
	}

	public void put(String name, String value) {
		mParamsMap.put(name, value);
	}

	public void remove(String name) {
		mParamsMap.remove(name);
	}

	public String toUrl() {
		if (TextUtils.isEmpty(mBaseUrl) || mParamsMap.size() == 0) {
			return mBaseUrl;
		}
		this.mStringBuilder = new StringBuilder(mBaseUrl);
		mIsFirstAdd = true;

		Iterator<String> it = mParamsMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = mParamsMap.get(key);
			if (mIsFirstAdd) {
				mIsFirstAdd = false;
				if (mStringBuilder.indexOf(QUESTION_MARK) < 1) {
					// does not contain "?"
					mStringBuilder.append(QUESTION_MARK);
					encode(key, value);
				} else {
					// contains "?"
					mStringBuilder.append(AMPERSAND);
					encode(key, value);
				}
			} else {
				mStringBuilder.append(AMPERSAND);
				encode(key, value);
			}
		}

		Log.d("xxx", "toUrl:" + mStringBuilder.toString());

		return mStringBuilder.toString();
	}

	private void encode(String name, String value) {
		try {
			mStringBuilder.append(URLEncoder.encode(name, UTF_8));
			mStringBuilder.append(EQUAL);
			mStringBuilder.append(URLEncoder.encode(value, UTF_8));
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("VM does not support UTF-8 encoding");
		}
	}
}
