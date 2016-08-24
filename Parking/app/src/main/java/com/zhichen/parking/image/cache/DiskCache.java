package com.zhichen.parking.image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DiskCache {

	public static final int FREE_SIZE = 10 * 1024 * 1024;

	private int mWidth = 250;
	private int mHeight = 250;

	/**
	 * 应用程序缓存的路径
	 */
	private String mAppCacheDir;

	public DiskCache() {
		mAppCacheDir = "/data/data/com.example.testcache.cache/cache/";
	}

	public DiskCache(Context ctx) {
		mAppCacheDir = ctx.getCacheDir().getAbsolutePath();
	}

	public DiskCache(Context ctx, int width, int height) {
		mAppCacheDir = ctx.getCacheDir().getAbsolutePath();
		mWidth = width;
		mHeight = height;
	}

	public void setBitmapSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public static void clearCache() {
		File dir = new File(Environment.getExternalStorageDirectory(), "TestCache");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * 下载的时候，可以直接将流写入到文件中
	 *
	 * @param key
	 * @param is
	 */
	public void put(String key, InputStream is) {
		File file = getCacheFile(key);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			int len = 0;
			byte[] tmp = new byte[1024];

			while ((len = is.read(tmp)) != -1) {
				fos.write(tmp, 0, len);
			}

			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图片保存到sdcard的缓存
	 *
	 * @param key
	 * @param bmp
	 */
	public void put(String key, Bitmap bmp) {
		File file = getCacheFile(key);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从缓存中取对应key的图片
	 *
	 * @param key
	 * @return
	 */
	public Bitmap get(String key) {
		File file = getCacheFile(key);
		Bitmap bmp = ImageUtils.decodeFile(file.getAbsolutePath(), mWidth, mHeight);
		return bmp;
	}

	/**
	 * 获取缓存的路径
	 *
	 * @param fileName
	 * @return
	 */
	private File getCacheFile(String fileName) {
		if (isSdcardEnable() && getSdcardFreeSize() > FREE_SIZE) {
			File dir = new File(Environment.getExternalStorageDirectory(), "TestCache");
			File file = new File(dir, fileName);
			file.getParentFile().mkdirs();
			return file;
		} else {
			File file = new File(mAppCacheDir, fileName);
			file.getParentFile().mkdirs();
			return file;
		}
	}

	public static boolean isSdcardEnable() {
		// return
		// Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		return true;
	}

	/**
	 * sdcard的剩余空间
	 *
	 * @return
	 */
	public static long getSdcardFreeSize() {
		File file = Environment.getDataDirectory();
		StatFs fs = new StatFs(file.getAbsolutePath());
		long blockSize = fs.getBlockSize();
		long availableBlocks = fs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
}
