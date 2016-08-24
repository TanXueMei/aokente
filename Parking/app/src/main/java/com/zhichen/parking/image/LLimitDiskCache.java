package com.zhichen.parking.image;

/**
 * @author linqi
 */

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

public class LLimitDiskCache extends LBaseDiskCache {

	/** SD卡存储图片文件阀值,默认10M **/
	private int MAX_DISK_SIZE = 10 * 1024 * 1024;
	/** SD卡当前大小 **/
	private AtomicLong currentSize;
	/** 图片文件的标记日期 **/
	private HashMap<String, Long> useDate = new HashMap<String, Long>();

	public LLimitDiskCache() {
		currentSize = new AtomicLong();
		// 获取保存图片的文件夹的容量
		//
		currentSize.addAndGet(0);// 标记当前SD卡上该目录下所有文件的总大小
		// useDate.put(all[i], all[i].lastModified());// 取出文件的最后修改使用时间

	}

	@Override
	public boolean save(String key, Bitmap bm) {
		long bmSize = bm.getRowBytes() * bm.getHeight();
		long curSize = currentSize.get();
		if (bmSize > MAX_DISK_SIZE) {
			// 将要存储的图片大于阀值
			Log.i("ming", "您存储的文件过大--" + bmSize);
			return false;
		} else {
			while (bmSize + curSize > MAX_DISK_SIZE) {
				// 将要存储的文件和目前的文件夹的容量之和大于总阀值 ---删除标记时间比较久的文件为新文件腾出位置
				synchronized (useDate) {
					Iterator<Entry<String, Long>> iter = useDate.entrySet().iterator();
					Entry<String, Long> e = iter.next();
					String needMoveFilepath = e.getKey();
					File needMove = null;
					long modified = e.getValue();
					while (iter.hasNext()) {
						Entry<String, Long> next = iter.next();
						long curmodified = next.getValue();
						if (curmodified < modified) {
							// 标记时间比第一个小--比较早
							needMoveFilepath = next.getKey();// 需要删除的文件
						}
					}
					long length = new File(PATH
							+ getFileName(needMoveFilepath) + ".jpg").length();
					remove(needMoveFilepath);// 删除
					useDate.remove(needMove);// 移除使用标记
					curSize = currentSize.addAndGet(-length);// 更新当前文件夹大小
				}
			}
			// 添加新文件
			boolean success = super.save(key, bm);
			hitFile(key);// 标记该图片
			currentSize.addAndGet(bm.getRowBytes() * bm.getHeight());// 更新当前文件夹大小
			return success;
		}
	}

	@Override
	public boolean save(String key, InputStream is) {
		try {
			long lenght = is.available();
			long curSize = currentSize.get();
			if (lenght > MAX_DISK_SIZE) {
				// 将要存储的图片大于阀值
				Log.i("ming", "您存储的文件过大--" + lenght);
				return false;
			} else {
				while (lenght + curSize > MAX_DISK_SIZE) {
					// 将要存储的文件和目前的文件夹的容量之和大于总阀值 ---删除标记时间比较久的文件为新文件腾出位置
					synchronized (useDate) {
						Iterator<Entry<String, Long>> iter = useDate.entrySet()
								.iterator();
						Entry<String, Long> e = iter.next();
						String needMovefilepath = e.getKey();
						long modified = e.getValue();
						while (iter.hasNext()) {
							Entry<String, Long> next = iter.next();
							long curmodified = next.getValue();
							if (curmodified < modified) {
								// 标记时间比第一个小--比较早
								needMovefilepath = next.getKey();// 需要删除的文件
							}
						}
						long length = new File(PATH + getFileName(needMovefilepath) + ".jpg").length();
						remove(needMovefilepath);// 删除
						useDate.remove(needMovefilepath);// 移除使用标记
						curSize = currentSize.addAndGet(-length);// 更新当前文件夹大小
					}
				}
				// 添加新文件
				boolean success = super.save(key, is);
				hitFile(key);// 标记该图片
				currentSize.addAndGet(lenght);// 更新当前文件夹大小
				return success;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public File get(String key) {
		File result = super.get(key);// 向SD卡中查询
		if (null != result) {// SD卡中含有该图片
			hitFile(key);// 取出该图片并使用--更新其使用标记日期
		}
		return result;

	}

	/***
	 * 标记文件的最后使用日期
	 * 
	 * @param key
	 */
	private void hitFile(String key) {
		File f = new File(PATH + super.getFileName(key) + ".jpg");
		if (f.exists()) {// 文件存在
			long currentTime = System.currentTimeMillis();
			f.setLastModified(currentTime);// 设置标记日期
			synchronized (useDate) {
				useDate.put(key, currentTime);// 更新标记
			}
		}
	}
}
