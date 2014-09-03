package com.tv.ui.metro.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Debug;
import android.util.Log;

import java.text.DecimalFormat;

public class MemoryManager {

	private static final String TAG = "MemoryManager";
	private static long mPeakHeapUsed;//栈使用的峰值
	
	public static int[] mallocInts(int size) {
		if (!isCanMallocHeap(size * 4)) {
			return null;
		}
		int val[] = new int[size];

		updatePeakHeapMem();
		return val;
	}
	
	public static byte[] mallocBytes(int size) {
		if (!isCanMallocHeap(size)) {
			return null;
		}
		byte val[] = new byte[size];

		updatePeakHeapMem();
		return val;
	}
	
	public static Bitmap mallocBitmap(int width, int height, Config config) {
		Bitmap bmp = null;
		int size = width * height;
		if (config == Config.ARGB_8888) {
			size *= 4;
		} else if (config == Config.RGB_565) {
			size *= 2;
		}
		if (!isCanMallocBmp(size)) {
			return null;
		}
		bmp = Bitmap.createBitmap(width, height, config);
		return bmp;
	}
	
	private static boolean updatePeakHeapMem() {
		long total = Runtime.getRuntime().totalMemory();
		long freeMem = Runtime.getRuntime().freeMemory();
		long used = total - freeMem;
		if (used > mPeakHeapUsed) {
			mPeakHeapUsed = used;
		}
		return true;
	}
	
	
	/**
	 * 申请heap时，剩余不足2M就会挂掉
	 * 
	 * @param size
	 * @return
	 */
	private static boolean isCanMallocHeap(long size) {
		return isCanMalloc((int) (size + 1024 * 1024 * 1.5));
	}

	private static boolean isCanMallocBmp(long size) {
		if (isCanMalloc(size) && isCanMallocBmpAtPeak(size)) {
			return true;
		}
		return false;
	}
	
	private static boolean isCanMallocBmpAtPeak(long size) {
		double total = ((double) Runtime.getRuntime().totalMemory());
		double freeMem = ((double) Runtime.getRuntime().freeMemory());
		double maxMem = ((double) Runtime.getRuntime().maxMemory());

		double exernalMem = (double) Debug.getNativeHeapAllocatedSize();
		if (mPeakHeapUsed + exernalMem + size >= maxMem) {
			Log.d("MEM", "isCanMallocBmpAtPeak used=" + (total - freeMem)
					+ " exernal=" + exernalMem + " size=" + size + " maxMem="
					+ maxMem + " exceed="
					+ (total - freeMem + exernalMem + size));

			return false;
		}
		return true;
	}
	
	/**
	* 是否可以申请指定的内存
	* @param size 字节数
	* @return
	*/
	public static boolean isCanMalloc(long size) {
		double total = ((double) Runtime.getRuntime().totalMemory());
		double freeMem = ((double) Runtime.getRuntime().freeMemory());
		double maxMem = ((double) Runtime.getRuntime().maxMemory());

		double exernalMem = (double) Debug.getNativeHeapAllocatedSize();
		if (total - freeMem + exernalMem + size >= maxMem) {
			Log.e(TAG, "used=" + (total - freeMem) + " exernal=" + exernalMem
					+ " size=" + size + " maxMem=" + maxMem + " exceed="
					+ (total - freeMem + exernalMem + size));

			return false;
		}
		return true;
	}
	
	/**
	 * 获取内存使用
	 * 
	 */
	public static void memeryUsed(String title) {
		// usedMem为byte Byte等类型占用空间
		// exernalMem 是Bitmap类型占用空间
		double total = ((double) Runtime.getRuntime().totalMemory() / (1024 * 1024));
		double freeMem = ((double) Runtime.getRuntime().freeMemory() / (1024 * 1024));
		double maxMem = ((double) Runtime.getRuntime().maxMemory() / (1024 * 1024));

		double exernalMem = (double) Debug.getNativeHeapAllocatedSize() / 1024 / 1024;
		DecimalFormat df = new DecimalFormat("0.000");// 格式话浮点数

		if(true){
		Log.e(TAG,"[" + title + "][Memery Test] [used]="
						+ df.format(total - freeMem) + "[exernalMem]="
						+ df.format(exernalMem) + " [totalUsed]="
						+ df.format(total - freeMem + exernalMem)
						+ " [maxMem]=" + df.format(maxMem));
		}
	}
	
}
