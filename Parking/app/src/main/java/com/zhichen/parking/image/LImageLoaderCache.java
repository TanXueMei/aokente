package com.zhichen.parking.image;

import android.graphics.Bitmap;

import java.util.Collection;

public interface LImageLoaderCache {
	/****
	 * 将图片存入缓存,key对应图片Bitmap的url
	 * 
	 * @param key
	 * @param bm
	 * @return
	 */
	boolean put(String key, Bitmap bm);

	/***
	 * 根据图片的url获取缓存中的图片信息
	 * 
	 * @param key
	 * @return
	 */
	Bitmap get(String key);

	/***
	 * 获取缓存中所有的图片的url
	 * 
	 * @return
	 */
	Collection<String> getUrls();

	/***
	 * 移除缓存中的该url对应的图片
	 * 
	 * @param key
	 * @return
	 */
	Bitmap remove(String key);

	/***
	 * 移除所有的图片缓存信息
	 */
	boolean clear();
}
