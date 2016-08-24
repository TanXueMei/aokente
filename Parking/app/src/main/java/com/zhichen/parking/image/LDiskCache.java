package com.zhichen.parking.image;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.InputStream;

/**
 * SD卡图片文件的操作
 *
 */
public interface LDiskCache {
     String PATH1 = Environment.getExternalStorageDirectory()
			.getPath() + "/police";
	public String PATH = PATH1 + "/cache/";

	/**
	 * 根据图片key(即图片名称)获取保存的图片文件
	 *
	 * @param key
	 * @return
	 */
	File get(String key);

	/***
	 * 将图片信息保存到SD卡上（以流的方式）
	 *
	 * @param key
	 * @param is
	 * @return
	 */
	boolean save(String key, InputStream is);

	/***
	 * 将图片信息保存到SD卡上 (bitmap)
	 *
	 * @param key
	 * @param bm
	 * @return
	 */
	boolean save(String key, Bitmap bm);

	/***
	 *删除SD卡上某一个指定的图片文件
	 *
	 * @param key
	 * @return
	 */
	boolean remove(String key);

	/***
	 * 清除文件夹下的所有图片信息
	 */
	boolean clear();

}
