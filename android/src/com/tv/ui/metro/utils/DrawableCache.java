package com.tv.ui.metro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 图片 避免多次加载,经常用到的图片
 * 将其缓存到内存中
 * @author dream
 * 
 * XXX 注意:
 * 如果一个页面有两个控件同时使用同一张图片的话，尤其是文本或者编辑框的背景图，而且其控件上的文字还不一样的话
 * 会造成意想不到的错误;因此，遇到此类问题就不能使用本类来加载图片
 *
 * COMMENTET OUT BY ROBIN.LIU 2011.04.14
 */
public class DrawableCache {
	private static final boolean DEBUG = false;
    private LruCache<Integer, Drawable> mBuff;
    private static DrawableCache sInstance;
    private Context mContext;
    private Object mSyncObject = new Object();
    private final int CACHE_SIZE = 8388608;//8MB
    
    public static void init(Context context){
    	sInstance = new DrawableCache(context);
    }
    
    public static DrawableCache getInstance(){
        return sInstance;
    }
    
    private DrawableCache(Context context){
    	mContext = context;
//    	CACHE_SIZE = Math.round(0.15f * Runtime.getRuntime().maxMemory());
//    	Log.e("DrawableCache", "cachesize=" + CACHE_SIZE);//大概57MB
    	mBuff = new LruCache<Integer, Drawable>(CACHE_SIZE){
			@Override
			protected int sizeOf(Integer key, Drawable value) {
				if(value instanceof BitmapDrawable){
					Bitmap bitmap = ((BitmapDrawable)value).getBitmap();
					if(bitmap != null){
						return bitmap.getByteCount();
					}
				}
				return 1024;
			}
    	};
    }
    
	public Drawable loadByResId(int resID) {
		synchronized (mSyncObject) {
			Drawable d = mBuff.get(resID);
			if (null == d) {
				try {
//					Log.e("DrawableCache", "cachesize=" + CACHE_SIZE);
					d = mContext.getResources().getDrawable(resID);
				} catch (OutOfMemoryError e) {
					System.gc();
					Runtime.getRuntime().gc();
					e.printStackTrace();
				}
				if(DEBUG) Log.d("DrawableCache", "save:" + resID);
				mBuff.put(resID, d);
				if(DEBUG) Log.d("DrawableCache", "total size:" + mBuff.size());
			}else{
				if(DEBUG) Log.d("DrawableCache", "load cache:" + resID);
			}
			return d;
		}
	}

	/**
	 * 
	 * @param resID
	 */
	public void unload_res(int resID) {
		synchronized (mSyncObject) {
			if (null == mBuff){
				return;
			}
			Drawable d = mBuff.get(resID);
			if (null == d){
				return;
			}else{
				mBuff.remove(resID);
				if(DEBUG) Log.d("DrawableCache", "unload cache:" + resID);
			}
		}
	}

	public void release() {
		synchronized (mSyncObject) {
			mBuff.evictAll();
		}
	}
}
