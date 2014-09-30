package com.xiaomi.mitv.store.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver {
	
	private OnBluetoothHandlerListener mOnBluetoothHandlerListener;
	
	public BluetoothReceiver(OnBluetoothHandlerListener listener){
		super();
		mOnBluetoothHandlerListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == intent){
			return;
		}
		String action = intent.getAction();
		BluetoothDevice device = null;

		if("android.bluetooth.input.profile.action.HID_INFO".equals(action)){
			device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(null == device){
				return;
			}
			if (isXiaomiBluetoothHandle(device)){
				if(mOnBluetoothHandlerListener != null){
					mOnBluetoothHandlerListener.connected(device);
				}
			}else{
				int pid = intent.getIntExtra("android.bluetooth.BluetoothInputDevice.extra.EXTRA_DEVICE_PID", -1);
				int vid = intent.getIntExtra("android.bluetooth.BluetoothInputDevice.extra.EXTRA_DEVICE_VID", -1);
				if(isXiaomiBluetoothHandleByHidPid(vid,pid)){
					if(mOnBluetoothHandlerListener != null){
						mOnBluetoothHandlerListener.connected(device);
					}
				}
			}
		}else if ("android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED".equals(action)) {
			device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(device != null){
				int newState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE,0);
				if(newState == BluetoothProfile.STATE_DISCONNECTED){
					if(mOnBluetoothHandlerListener != null){
						mOnBluetoothHandlerListener.disconnected(device);
					}
				}
			}
		}
	}

    private static final int XIAOMI_BT_PID = 10007;
    private static final int XIAOMI_HANDLE_VID_HIGH_ADDRESS = 1;//遥控器

    public static boolean isXiaomiBluetoothHandleByHidPid(int vid, int pid) {
        if(vid == XIAOMI_BT_PID){
            if((((pid >> 8) & 0x0F)) == XIAOMI_HANDLE_VID_HIGH_ADDRESS){
                return true;
            }
        }
        return false;
    }


    private static final String XIAOMI_HANDLER = new String("\u5C0F\u7C73\u624B\u67C4");//小米手柄
    private static final String XIAOMI_BLUETOOTH_HANDLER = new String("\u5C0F\u7C73\u84DD\u7259\u624B\u67C4");//小米蓝牙手柄
    private static final String XIAOMI_BLUETOOTH_GAME_HANDLER = new String("\u5C0F\u7C73\u84DD\u7259\u6E38\u620F\u624B\u67C4");//小米蓝牙游戏手柄
    public static boolean isXiaomiBluetoothHandle(BluetoothDevice device){
        if(null == device){
            return false;
        }
        String name = device.getName();
        if(XIAOMI_BLUETOOTH_HANDLER.equals(name)
                || XIAOMI_BLUETOOTH_GAME_HANDLER.equals(name)
                || XIAOMI_HANDLER.equals(name)){
            return true;
        }

        return false;
    }
}
