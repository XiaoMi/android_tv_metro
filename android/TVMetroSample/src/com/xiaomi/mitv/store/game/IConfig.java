package com.xiaomi.mitv.store.game;


/**
 * 配置选项
 * @author mengshu
 *
 */
public interface IConfig {
	boolean DEBUG = true; //是否显示e调试信息
	
	boolean MI_TV = true;
	String GAME_CENTER_CID = "2050000";
	
	boolean CAN_NOT_SUPPORT_NEW_BLUETOOTH = DEBUG ? (!Client.isTV2()) : false;//蓝牙升级
	
	boolean LAUNCHER_LOG = true;//桌面更新的LOG包括定时任务以及退出UI
	
//	boolean STUB = true;//桩代码 /测试代码
	
	//是否是静默安装
	boolean SILENT_INSTALL = true;
	
	boolean CLIP_CHILDREN = true;//系统默认值为true限定不能超过区域
}
