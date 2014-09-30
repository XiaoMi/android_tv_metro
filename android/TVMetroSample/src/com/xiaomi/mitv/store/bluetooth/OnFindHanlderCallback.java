package com.xiaomi.mitv.store.bluetooth;

/**
 * 查找蓝牙设备
 * @author liubiqiang
 *
 */
public interface OnFindHanlderCallback {
	public void findNewHandlerDevice(String address);
	public void onFinishedFind();
}
