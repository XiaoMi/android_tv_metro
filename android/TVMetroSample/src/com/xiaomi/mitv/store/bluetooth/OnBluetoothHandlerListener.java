package com.xiaomi.mitv.store.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface OnBluetoothHandlerListener {
	public void connected(BluetoothDevice device);
	public void disconnected(BluetoothDevice device);
}
