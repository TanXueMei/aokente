package com.zhichen.parking.common;

import android.os.Environment;

public class AppConstants {
	/**
	 * SD卡路径
	 */
	public static String PATH = Environment.getExternalStorageDirectory()
			.getPath() + "/smartParting";
	/**
	 * APK路径
	 */
	public static final String APK_PATH = PATH + "/apk";

	// APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wxfb1cf19da66e305c";

	/**
	 * 软件运行目录
	 */
	public static final String ZCHX_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aokente";
	public static String APP_FOLDER = "Parking";
	public static String APP_DIR = ZCHX_DIR + "/" + APP_FOLDER;
	public static String DOCUMNETS_DIR = APP_DIR + "/Documents";// 用户文档目录
	public static String LOG_DIR = APP_DIR + "/Logcat";
	public static String RESOURCE_DIR = APP_DIR + "/Resource";// 公共资源
	public static String LOCAL_DIR = APP_DIR + "/Local";// 单次运行的持久数据
	public static String TEMP_DIR = APP_DIR + "/Temp";// 临时数据
	
	/**
	 * 消息定义 
	 */
	public static final int TYPE_EVENT_ACCOUNT_QUIT = 1;
	public static final int TYPE_AVATAR_CHANGE 		= 2;
	public static final int TYPE_NICKNAME_CHANGE 	= 3;
	public static final int TYPE_PARKINGLOT_LOADED 	= 4;
	
	/**
	 * 停车场类型定义 
	 */
	public static final int PARKINGLOT_TYPE_TRDDITION = 0;
	public static final int PARKINGLOT_TYPE_ROAD = 1;
	public static final int PARKINGLOT_TYPE_SMART = 2;


	/**
	 * 空车位数目不同图标显示不同
	 */
	public static final int TYPE_ICON_RED = 5;
	public static final int TYPE_ICON_BLUE	= 6;
	public static final int TYPE_ICON_GREEN = 7;
	public static final int TYPE_ICON_GRAY	= 8;
	
}
