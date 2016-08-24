package com.zhichen.parking.image.cache;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 内存缓存的实现
 *
 * @author lin
 */
public class MemoryCache {

	public static final int DEFAULT_MAX_SIZE = (int) (Runtime.getRuntime().maxMemory() / 4);

	private LruCache<String, Bitmap> mLruCache;
	private HashMap<String, SoftReference<Bitmap>> mSoftCache = new HashMap<String, SoftReference<Bitmap>>();

	public MemoryCache() {
		this(DEFAULT_MAX_SIZE);
	}

	public MemoryCache(int maxSize) {
		mLruCache = new LruCache<String, Bitmap>(maxSize) {

			/**
			 * 当图片从LrucCache中 移除的时候，会回调这个方法
			 */
			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);

				// 当图片从一级缓存中移除的时候，将他放入到二级缓存中去
				SoftReference<Bitmap> soft = new SoftReference<Bitmap>(oldValue);
				mSoftCache.put(key, soft);
			}

			/**
			 * 必须要重写的方法，计算每一个缓存的对象的大小
			 */
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 4.0以后才有的方法
				// value.getByteCount()

				// 获取一个bitmap所占用的内存的大小
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public void put(String key, Bitmap value) {
		mLruCache.put(key, value);
	}

	public Bitmap get(String key) {
		Bitmap bmp = null;

		// 首先从一级缓存中取
		bmp = mLruCache.get(key);

		if (bmp == null) {
			// 一级缓存没有，就从二级缓存中取
			SoftReference<Bitmap> soft = mSoftCache.get(key);

			if (soft != null) {

				bmp = soft.get();

				// 二级缓存里面的内容已经被情况了，则移除二级缓存中key所对应的内容
				if (bmp == null) {
					mSoftCache.remove(soft);
				}
			}
		}

		return bmp;
	}
}
