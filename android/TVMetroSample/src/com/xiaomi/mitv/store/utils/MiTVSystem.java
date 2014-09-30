
package com.xiaomi.mitv.store.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;


public class MiTVSystem {
    private static String TAG = "MiTVSystem";

	private volatile static String sDeviceMac;
	private volatile static String sDeviceId;
	
	private volatile static String sEthernetMac;
	private volatile static String sWifiMac;
	
	public static String getDeviceID(Context con) {
		String deviceId = sDeviceId;
		if(deviceId == null || deviceId.length() == 0){
			deviceId = getMD5(getDeviceMac(con));
			if(deviceId != null && deviceId.length() > 0){
				sDeviceId = deviceId;
			}
		}
		return deviceId;
	}
	
	public static String getDeviceMac(Context con){
		String mac = sDeviceMac;
		if(mac == null || mac.length() == 0){
		    if(MiTVBuild.getProductCode() == MiTVBuild.M7_PRODUCT) {
		        mac = getProperty(con, "ro.boot.btmac");
		    } else if(MiTVBuild.getProductCode() == MiTVBuild.M8_PRODUCT){
				mac = readFile("/sys/class/efuse/mac_bt");
			}else{
				mac = readFile("/sys/class/net/eth0/address");
			}
			if(mac != null && mac.length() > 0){
				mac = mac.trim();
				sDeviceMac = mac;
			}else{
			    sDeviceMac = getEthernetAddress(con);
			}
		}
		return mac;
	}
	
	public static String readFile(String path){
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        String content = "";
        try {
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(path);
            final int blockSize = 8192;
            byte[] buffer = new byte[blockSize];
            int count = 0;
            while ((count = fis.read(buffer, 0, blockSize)) > 0) {
                bos.write(buffer, 0, count);
            }
            content = new String(bos.toByteArray());
        } catch (Exception e) {
            Log.i(TAG, "read file: " + path + ", exception: ", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }
	
	public static String getProperty(Context con, String key){         

        //reflect call system properties
        Class osSystem = null;
        try {
            osSystem = Class.forName("android.os.SystemProperties");
            Method getDeviceIDMethod = osSystem.getMethod("get", new Class[]{String.class});
            String tv2deviceid = (String) getDeviceIDMethod.invoke(osSystem, new Object[]{key});
            if(tv2deviceid != null && tv2deviceid.length() > 0) {
                
                Log.d("DeviceHelper", key + " = "+tv2deviceid);
                return tv2deviceid;
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return  "";
    }
	
	
	 public static String getEthernetAddress(Context con){	       

	        //reflect call system properties
	        Class osSystem = null;
	        try {
	            osSystem = Class.forName("android.os.SystemProperties");
	            Method getDeviceIDMethod = osSystem.getMethod("get", new Class[]{String.class});
	            String tv2deviceid = (String) getDeviceIDMethod.invoke(osSystem, new Object[]{"bootenv.var.eth_macaddr"});
	            if(tv2deviceid != null && tv2deviceid.length() > 0) {
	                
	                Log.d("DeviceHelper", "2 find eth address = "+tv2deviceid);
	                return tv2deviceid;
	            }

	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }

	        return  "";
	    }

	public static int getPlatform(){
		int product = MiTVBuild.getProductCode();
	    if(product == MiTVBuild.TV_PRODUCT){
	        return 601;
	    }else if(product == MiTVBuild.TV2_PRODUCT){
	        return 602;
	    }else if(product == MiTVBuild.M6_PRODUCT){
	        return 205;
	    }else if(product == MiTVBuild.M8_PRODUCT){
	        return 206;
	    }else if(product == MiTVBuild.M7_PRODUCT){
            return 207;
        }else if(product == MiTVBuild.TV2_XP_PRODUCT){
            return 603;
        }
	    return 204;
	}

	public static String getEthernetMacAddress() {
		if (sEthernetMac == null || sEthernetMac.length() == 0) {
			if(MiTVBuild.getProductCode() == MiTVBuild.M8_PRODUCT){
				sEthernetMac = getMacAddress("usbnet");
			}else{
				sEthernetMac = getMacAddress("eth");
			}
		}
		return sEthernetMac;
	}

	public static String getWifiMacAddress() {
		if (sWifiMac == null || sWifiMac.length() == 0) {
			sWifiMac = getMacAddress("wlan");
		}
		return sWifiMac;
	}
	
	private static String getMacAddress(String name) {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().toLowerCase().contains(name)) {
					byte[] ha = intf.getHardwareAddress();
					if (ha != null) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < ha.length; ++i) {
							if (i > 0) {
								sb.append(":");
							}
							sb.append(String.format("%1$02x", ha[i]));
						}
						return sb.toString();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private static String getMD5(String message) {
		if (message == null || message.length() == 0){
			return "";
		}
		return getMD5(message.getBytes());
	}

	private static String getMD5(byte[] bytes) {
		if (bytes == null){
			return "";
		}
		String digest = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			digest = toHexString(algorithm.digest());
		} catch (Exception e) {
		}
		return digest;
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String str = Integer.toHexString(0xFF & b);
			while (str.length() < 2) {
				str = "0" + str;
			}
			hexString.append(str);
		}
		return hexString.toString();
	}
}
