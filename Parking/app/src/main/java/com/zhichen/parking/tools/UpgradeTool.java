package com.zhichen.parking.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.zhichen.parking.model.UpgradeBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class UpgradeTool {
	
	private static final String TAG = UpgradeTool.class.getSimpleName();

	public static UpgradeBean parseXML(InputStream in){
		UpgradeBean upgradeBean = new UpgradeBean();
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(in, null);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
//					String name = parser.getName();
//					System.out.println(name + "--"+parser.nextText());
					if("version".equals(parser.getName())){
						upgradeBean.setVersion(parser.nextText());
					}else if("versionCode".equals(parser.getName())){
						upgradeBean.setVersionCode(parser.nextText());
					}else if("uri".equals(parser.getName())){
						upgradeBean.setUri(parser.nextText());
					}else if("md5".equals(parser.getName())){
						upgradeBean.setMd5(parser.nextText());
					}else if("nestupdatepromt".equals(parser.getName())){
						upgradeBean.setNestupdatepromt(parser.nextText());
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return upgradeBean;
	}
	
	public static String getVersionName(Context context) {
		String string = null;
		try {
			string = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	public static int getVersionCode(Context context) {
		int tmp = 0;
		try {
			tmp = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	// 安装apk
	public static void installApk(Context context, String path) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
		
	}

	public static boolean delApk(String fileString) {
		if (!fileString.startsWith("file")) {

		}
		File file = new File(fileString);
		return file.delete();
	}

}
