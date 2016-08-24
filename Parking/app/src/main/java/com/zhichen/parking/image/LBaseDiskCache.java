package com.zhichen.parking.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/***
 * SD卡图片文件的操作类
 *
 */
public class LBaseDiskCache implements LDiskCache {
	/** SD 卡状态 **/
	private static String sdState = Environment.getExternalStorageState();

	@Override
	public File get(String key) {
		File result = null;
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {// 查找内存卡中的key对应的图片信息
			File file = new File(PATH + getFileName(key) + ".jpg");
			if (file.exists()) {
				result = file;
			}
		}
		return result;
	}

	@Override
	public boolean save(String key, InputStream is) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(PATH);
			if (!file.isDirectory()) {
				file.mkdirs();// 保存图片的文件夹不存在---创建
			}
			// 保存图片文件爱你的文件夹存在
			File needsave = new File(PATH + getFileName(key) + ".jpg");
			if (file.exists()) {// 文件已经存在---删除---换新图
				needsave.delete();// 删除已经存在的图片文件
			}
			// 创建新文件---并保存
			File f = new File(PATH + getFileName(key) + ".jpg");
			try {
				FileOutputStream os = new FileOutputStream(f);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
					os.flush();
				}
				is.close();
				os.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean save(String key, Bitmap bm) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(PATH);
			if (!file.isDirectory()) {//  保存图片文件爱你的文件夹不存在
				file.mkdirs();// 保存图片的文件夹不存在---创建
			}
			File needsave = new File(PATH + getFileName(key) + ".jpg");
			if (file.exists()) {// 文件已经存在---删除---换新图
				needsave.delete();// 删除已经存在的图片文件
			}
			// 创建新文件---并保存
			File f = new File(PATH + getFileName(key) + ".jpg");
			try {
				FileOutputStream os = new FileOutputStream(f);
				bm.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean remove(String key) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File f = new File(PATH + getFileName(key) + ".jpg");
			if (f.exists()) {
				f.delete();// 删除该图片信息
				return true;
			} else {
				return false;
			}
		} else {
			return false;

		}
	}

	@Override
	public boolean clear() {
		boolean success = false;
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(PATH);
			if (file.isDirectory()) {
				File[] list = file.listFiles();
				for (int i = 0; i < list.length; i++) {
					File move = list[i];// 循环删除文件夹下的所有文件
					if (move.exists()) {
						move.delete();
					}
				}
				file.delete();
				if (!file.exists() || list.length == 0) {
					success = true;
				}
			}
		}
		return success;
	}

	/**
	 * 将url中的"/"符号和":"清除以作为文件名
	 *
	 * @param key
	 * @return
	 */
	public String getFileName(String key) {
		String result = "";
		key = key.substring(0, key.lastIndexOf("."));
		result = key.replace("/", "");
		result = result.replace(":", "");
		result = result.replace(".", "");
		result = result.replace("-", "");
		return result;
	}

}
