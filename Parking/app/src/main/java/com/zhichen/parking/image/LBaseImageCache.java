package com.zhichen.parking.image;

import android.graphics.Bitmap;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/***
 * 图片缓存处理类
 * 
 */
public abstract class LBaseImageCache implements LImageLoaderCache {
	// 缓存图片的集合
	public HashMap<String, Reference<Bitmap>> bitmaps = new HashMap<String, Reference<Bitmap>>();

	/***
	 * 将key存入键值 图片存入缓存集合
	 */
	@Override
	public boolean put(String key, Bitmap bm) {
		bitmaps.put(getFileName(key), createReference(bm));
		return true;
	}

	/***
	 * 根据图片key值获取缓存中的图片信息
	 */
	@Override
	public Bitmap get(String key) {
		Bitmap result = null;
		Reference<Bitmap> res = bitmaps.get(getFileName(key));
		if (null != res) {
			result = res.get();
		}
		return result;
	}

	/***
	 * 将所有的key值转换集合返回
	 */
	@Override
	public Collection<String> getUrls() {
		synchronized (bitmaps) {
			return new HashSet<String>(bitmaps.keySet());
		}
	}

	/***
	 * 移除缓存中该key对应的图片信息
	 */
	@Override
	public Bitmap remove(String key) {
		Bitmap result = null;
		if (bitmaps.containsKey(getFileName(key))) {
			result = bitmaps.remove(key).get();
		}
		return result;
	}

	/***
	 *  移除缓存中所有的缓存图片信息
	 */
	@Override
	public boolean clear() {
		bitmaps.clear();
		return bitmaps.isEmpty();
	}

	/***
	 * 为图片bitmap创建一个缓存对象
	 * 
	 * @param value
	 * @return
	 */
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}

	/**
	 * 将url中的"/"符号和":"清除以作为文件名
	 * 
	 * @param key
	 * @return
	 */
	private String getFileName(String key) {
		String result = "";
		key = key.substring(0, key.lastIndexOf("."));
		result = key.replace("/", "").replace(":", "").replace(".", "")
				.replace("-", "");
		return result;
	}

}
