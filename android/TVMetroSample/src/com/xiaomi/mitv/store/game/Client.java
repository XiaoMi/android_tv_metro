package com.xiaomi.mitv.store.game;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.xiaomi.mitv.store.utils.MiTVSystem;

import java.util.Locale;

public class Client
{
	public static String IMEI;
	public static String MacWifi;
	public static String SYSTEM_VERSION;
	public static int SDK_VERSION;
	public static boolean IS_BACKGROUND_INSTALL;
	public static int GAMECENTER_VERSION;
	public static String GAMECENTER_VERSION_NAME;
	public static boolean SYSTEM_APP;//该客户端是否是系统APP
	public static String DEVICE;//产品型号
	/**
	 * 设备augustrush(八月迷情):盒子一代 
	 * casablanca(卡萨布兰卡):盒子二代;
	 * 都是影片的名字;
	 * 1s : augustrush_plus_cntv 
	 */
	public static String PRODUCT;//产品代码

	private Client(){
		
	}

	public static void init( Context context )
	{

		SYSTEM_VERSION = Build.DISPLAY;
		SDK_VERSION = Build.VERSION.SDK_INT;
		IS_BACKGROUND_INSTALL = isBackgroundInstall( context );
		GAMECENTER_VERSION = getGameCenterVersion( context );
		GAMECENTER_VERSION_NAME = getGameCenterName( context );
		
		//是系统APP
		SYSTEM_APP = ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) > 0 )
				? true : false;
		
		DEVICE = "mitv";//Build.PRODUCT
		PRODUCT = Build.PRODUCT;
		if(!TextUtils.isEmpty(PRODUCT)){
			PRODUCT = PRODUCT.toLowerCase(Locale.getDefault());
		}
	}
	
	/**
	 * 判断是否是TV2
	 * @return
	 */
	public static final boolean isTV2(){
		if(!TextUtils.isEmpty(PRODUCT)){
			if(PRODUCT.startsWith("entrapment")){
				return true;
			}
		}
		return false;
	}

	private static String getGameCenterName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo pkgInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return pkgInfo.versionName;
		} catch (NameNotFoundException e) {
			if (IConfig.DEBUG) e.printStackTrace();
			return "";
		}
	}

	private static int getGameCenterVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo pkgInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return pkgInfo.versionCode;
		} catch (NameNotFoundException e) {
			if(IConfig.DEBUG) e.printStackTrace();
			return 0;
		}
	}

	private static boolean isBackgroundInstall(Context context) {
		try {
			context.enforceCallingOrSelfPermission(
					android.Manifest.permission.INSTALL_PACKAGES, null);
			return true;
		} catch (SecurityException e) {
			return false;
		}
	}


	public static boolean isLaterThanHoneycombMR2()
	{
		return SDK_VERSION >= Build.VERSION_CODES.HONEYCOMB_MR2;
	}

	public static boolean isLaterThanHoneycomb()
	{
		return SDK_VERSION >= Build.VERSION_CODES.HONEYCOMB;
	}
}
