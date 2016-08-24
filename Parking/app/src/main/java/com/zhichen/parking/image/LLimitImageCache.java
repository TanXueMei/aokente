package com.zhichen.parking.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 
 * 设计该缓存集合所能允许的缓存大小
 */
public class LLimitImageCache extends LBaseImageCache {
	/** 最大缓存16M **/
	public static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;

	/** 允许缓存大小 **/
	private int limitSize = 10 * 1024 * 1024;
	/**
	 * J2SE _5.0提供了一组atomic
	 * class来帮助我们简化同步处理。基本工作原理是使用了同步synchronized的方法实现了对一个long, integer,
	 * 对象的增、减、赋值（更新）操作. 比如对于++运算符AtomicInteger可以将它持有的integer 能够atomic
	 * 地递增。在需要访问两个或两个以上 atomic变量的程序代码（或者是对单一的atomic变量执行两个或两个以上的操作）
	 * 通常都需要被synchronize以便两者的操作能够被当作是一个atomic的单元。
	 **/
	private AtomicInteger cacheSize;
	/***
	 * 有先后顺序的bitmap图片集合
	 */
	private List<Bitmap> hardCache = new LinkedList<Bitmap>();
	/***
	 * 记录缓存图片的使用频度
	 */
	private HashMap<String, Integer> hit = new HashMap<String, Integer>();
	/***
	 * SD卡文件操作
	 */
	private LLimitDiskCache mDiskCache;

	public LLimitImageCache() {
		mDiskCache = new LLimitDiskCache();
		cacheSize = new AtomicInteger();
		if (this.limitSize > MAX_CACHE_SIZE) {
			Log.i("ming", "缓存阀值大于" + MAX_CACHE_SIZE);
		}
	}

	public boolean save(String key, Bitmap bm) {
		return mDiskCache.save(key, bm);
	}

	public File getFile(String key) {
		return mDiskCache.get(key);
	}

	@Override
	public boolean put(String key, Bitmap bm) {
		boolean success = false;// 未存入缓存的状态
		int size = getSize(bm);// 目标图片的大小
		int limitsize = getLimitSize();// 缓存允许的大小
		int currentCacheSize = cacheSize.get();// 当前缓存的大小
		if (size < limitsize) {// 图片的大小处于缓存所允许的大小范围内
			/***
			 * 判断将要存入缓存的目标图片的大小和当前集合的总缓存大小 与缓存限制之间的大小
			 */
			while (size + currentCacheSize > limitsize) {
				// 新图片放入缓存后，缓存的总大小大于缓存限制---此刻需移除一些图片以保证缓存大小在16M之内,剩下的容量不足以存放新图片则一直移除旧图片
				Bitmap needmove = removeNext();
				if (hardCache.remove(needmove)) {
					currentCacheSize = cacheSize.getAndAdd(-getSize(needmove));// 缓存大小执行--操作
					// 将移除的图片保存到SD卡上。
					mDiskCache.save(key, needmove);
				}
				hardCache.add(bm);// 将新图片放入缓存
				cacheSize.addAndGet(size);// 将新图片的大小计数在缓存容量中
				success = true;// 存入缓存成功
			}
			super.put(key, bm);
		}
		return success;
	}

	@Override
	public Bitmap remove(String key) {
		Bitmap needmove = super.remove(key);
		if (hardCache.contains(needmove)) {
			hardCache.remove(needmove);// 将图片移除缓存
			cacheSize.addAndGet(-getSize(needmove));// 缓存大小执行--操作
		}
		return needmove;
	}

	@Override
	public Bitmap get(String key) {
		Bitmap result = super.get(key);
		if (null != result && hardCache.contains(result)) {// 缓存中含有该图片---更改该图片的使用频率+_1
			synchronized (hit) {
				int h = hit.get(key);
				h++;
				hit.put(key, h);// 更新使用频率
			}
		} else if (null == result) {// 缓存中不存在该图片的任何信息
			// 去SD卡中搜寻痕迹
			File f = mDiskCache.get(key);
			// _1，在SD卡中找到了该图片，取出该图片
			if (null != f) {
				try {
					result = BitmapFactory.decodeFile(f.getAbsolutePath());
					// _2，存入缓存（注意同步集合安全synchronized）并设置初始使用频率为1
					put(key, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public boolean clear() {
		hardCache.clear();// 清除图片集合
		cacheSize.set(0);// 设置当前的缓存大小为0
		// 清除SD上的缓存文件
		return super.clear()&&mDiskCache.clear();
	}

	/***
	 * 获取该图片的大小(MB)
	 *
	 * @param bm
	 * @return
	 */
	public int getSize(Bitmap bm) {
		if (null == bm) {
			return 0;
		} else {
			return bm.getRowBytes() * bm.getHeight();
		}
	}

	protected int getLimitSize() {
		return this.limitSize;
	}

	/***
	 * 移除一个使用次数最少的图片对象
	 *
	 * @return
	 */
	public Bitmap removeNext() {
		synchronized (hit) {
			Iterator<Entry<String, Integer>> inter = hit.entrySet().iterator();
			Entry<String, Integer> entry = inter.next();
			String needmovekey = entry.getKey();
			int count = entry.getValue();
			while (inter.hasNext()) {
				Entry<String, Integer> e = inter.next();
				if (e.getValue() < count) {
					count = e.getValue();
					needmovekey = e.getKey();
				}
			}

			return remove(needmovekey);
		}
	};
}
