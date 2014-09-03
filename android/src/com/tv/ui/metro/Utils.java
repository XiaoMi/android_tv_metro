package com.tv.ui.metro;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.webkit.MimeTypeMap;

public class Utils {
	
	public static boolean DEBUG = true;
	public static final int SOUND_KEYSTONE_KEY = 1;
	public static final int SOUND_ERROR_KEY = 0;
	public static final int LARGE_NUMBER_BASE = 100000;
	
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = (elements.length * 110) / 100 + 5;
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }
    
    public static boolean isConnected(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    
    public static long readSystemAvailableSize() {
    	String path = "/data"; 
    	StatFs sf = new StatFs(path);
    	long blockSize = sf.getBlockSize();
    	Log.d("block size", "block size: " + blockSize);
    	//long blockCount = sf.getBlockCount();
    	//Log.d("available count", "available count: " + sf.getAvailableBlocks());
    	long availCount = sf.getAvailableBlocks();
    	Log.d("available count", "available count: " + availCount);
    	return blockSize * availCount/1024;
    }
    
    public static void playKeySound(View view, int soundKey) {
    	if (null != view) {
    		if (soundKey == SOUND_KEYSTONE_KEY) {
    			view.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
    		} else if (soundKey == SOUND_ERROR_KEY) {
    			view.playSoundEffect(5);
    		}
		}
	}

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	// TODO: maybe long type
	public static float getMSizeFromK(int kSize) {
		return kSize / 1024f;
	}

	public static String longToDate(long timeMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		return sdf.format(timeMillis);
	}
	
	public static byte[] readStreamToByteArray(InputStream inStream) throws Exception{        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();        
        byte[] buffer = new byte[512 * 1024];        
        int len = 0;        
        while( (len=inStream.read(buffer)) != -1){    
//        	Log.e("@@@@@@@@@@@@@@: ", len + "");
            outStream.write(buffer, 0, len);        
        }        
        outStream.close();        
        inStream.close();        
        return outStream.toByteArray();        
    }  
	
	public static void saveInputstreamToFile(InputStream is, File targetFile) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(targetFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8 * 1024];
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	public static String getStringMD5(String key){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(key.getBytes());
		//important: use Base64.URL_SAFE flag to avoid "+" and "/"
		return new String(Base64.encode(md5.digest(), Base64.URL_SAFE));
	}
	
	public static String getCacheFolder(Context context) {
		File cacheFolder = new File(context.getCacheDir().getAbsolutePath() + File.separator + "app_icons");
		if (!cacheFolder.exists()) {
			cacheFolder.mkdir();
		}
		return cacheFolder.getAbsolutePath();
	}
	
	public static int getMemoryClass(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		//128m for mibox
//		Log.e(TAG, "Memory class: " + memoryClass);
		return memoryClass;
	}
    
	public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data);
		return byte2HexStr(rawHmac);
	}
	
    public static String byte2HexStr(byte[] b)  
    {  
        String stmp="";  
        StringBuilder sb = new StringBuilder("");  
        for (int n=0;n<b.length;n++)  
        {  
            stmp = Integer.toHexString(b[n] & 0xFF);  
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
        }  
        return sb.toString().toLowerCase().trim();  
    }
    
	public static Integer getKeyByValue(Map<Integer, String> map, Object value) {
		Integer key = -1;
		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, String> entry = (Entry<Integer, String>) it.next();
			String obj = entry.getValue();
			if (obj != null && obj.equals(value)) {
				// break as find the first key, assuming key and value are one-to-one
				key = (Integer) entry.getKey();
				break;
			}
		}
		return key;
	}
	
	public static String largeNumberPattern(int largeNumber) {
		String patternedString = "0";
		if (largeNumber > 0) {
			String unit = null;
			if (largeNumber >= LARGE_NUMBER_BASE) {
				largeNumber /= 10000;
				unit = "万";
			}
			DecimalFormat df = new DecimalFormat("#,###");
			patternedString = df.format(largeNumber);
			if (null != unit) {
				patternedString += unit;
			}
		}
		return patternedString;
	}
	
	public static String getFileNameExtension(String fileName) {
		if (!TextUtils.isEmpty(fileName)) {
			int end = fileName.lastIndexOf(".");
			if (end >= 0) {
				return fileName.substring(end + 1);
			}
		}
		return null;
	}
	
	public static String covertStreamToString(InputStream is, String encode) throws IOException {
		if (null != is) {
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			int n;
			while ((n = is.read(b)) != -1) {
				if(encode == null){
					out.append(new String(b, 0, n));
				}else{
					out.append(new String(b, 0, n, encode));
				}
			}
			return out.toString();
		}
		return null;
	}
	
	public static void changeFileMod(String path, String mod) {
		if (!TextUtils.isEmpty(path)) {
			try {
				File file = new File(path);
				if (file.exists()) {
					String command = mod + " " + file.getAbsolutePath();
					Log.d("changeFileMod", "path = " + path + "command = " + command);
					int count = 0;
					while(count < 5) {
						Log.d("changeFileMod", "chmod count = " + count);
						Process p = Runtime.getRuntime().exec(command);
						int status = p.waitFor();    
						if (status == 0) { 
							Log.d("changeFileMod", "change mod success.");
						    return;    
						}
						count++;
					}
				}
			} catch (Exception e) {
				Log.e("changeFileMod", "Error changeFileMod()" + e.toString());
				e.printStackTrace();
			}
		}
	}
	
	public static String getFilePath(String url) {
		if (!TextUtils.isEmpty(url)) {
			int end = url.lastIndexOf("/");
			if (end >= 0) {
				return url.substring(0, end + 1);
			}
		}
		return null;
	}
	
	public static String getFileName(String url) {
		if (!TextUtils.isEmpty(url)) {
			int end = url.lastIndexOf("/");
			if (end >= 0) {
				return url.substring(end + 1);
			}
		}
		return null;
	}
	
	public static boolean containNonEnglishChar(String str) {
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] > 255 || charArray[i] < 0) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkFileValid(String fullPath) {
		if (!TextUtils.isEmpty(fullPath)) {
			File file = new File(fullPath);
			if (file.exists() && file.isFile()) {
				return true;
			}
		}
		return false;
	}
	
	public static int px2sp(Context context, float pxValue) {  
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);  
	}
	
	public static void covertStreamToFile(InputStream is, File outFile) throws IOException{
		outFile.delete();
		if (!outFile.exists()) {
			OutputStream os = null;
			outFile.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(outFile));
			int count = 0;
			byte[] buffer = new byte[8192];
			while ((count = is.read(buffer)) > 0) {
				os.write(buffer, 0, count);
			}
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}
	
/*	public static void resetViewLayoutParamsForM8(View view, int topMargin, int leftMargin) {
		if ((null != view) && (AppStoreClient.PRODUCT_CODE == mitv.os.Build.M8_PRODUCT)) {
			ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			if (topMargin >= 0) {
				lp.topMargin = topMargin;
			}
			if (leftMargin >= 0) {
				lp.leftMargin = leftMargin;
			}
			view.setLayoutParams(lp);
		}
	}
	
	public static void resetViewTextSizeForM8(TextView view, float size) {
		if ((null != view) && (AppStoreClient.PRODUCT_CODE == mitv.os.Build.M8_PRODUCT)) {
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
	}*/
	
	public static void testForZmon(Context context, String url) {
		BufferedWriter out = null;
		try {
			String path = context.getFilesDir().getAbsolutePath() + "/desktop";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir.getAbsolutePath(), "test.txt");
			if (!file.exists()) {
				file.createNewFile();
			} else {
				if ( file.length() >= 1024 * 10) {
					file.delete();
					file.createNewFile();
				}
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true))); 
			Date now = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance();
			String time = dateFormat.format(now);
			String outString = time + "-------" + url + "\n";
			out.write(outString);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testForUpdate(Context context, String url) {
		BufferedWriter out = null;
		try {
			String path = context.getFilesDir().getAbsolutePath() + "/desktop";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir.getAbsolutePath(), "test_status.txt");
			if (!file.exists()) {
				file.createNewFile();
			} else {
				if ( file.length() >= 1024 * 10) {
					file.delete();
					file.createNewFile();
				}
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true))); 
			Date now = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance();
			String time = dateFormat.format(now);
			String outString = time + "-------" + url + "\n";
			out.write(outString);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static boolean isEmpty(ArrayList list) {
        return list != null && list.size() > 0;
    }   
}
