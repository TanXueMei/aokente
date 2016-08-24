package com.zhichen.parking.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;


import com.zhichen.parking.R;
import com.zhichen.parking.app.PWApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/***
 *@author linqi
 * 图片加载类
 */
public class LImageLoader {
	/** 缓存图片 **/
	private LLimitImageCache cache;
	/** 记录要加载图片的imageView **/
	private HashMap<String, ImageView> currentLoading = new HashMap<String, ImageView>();
	private Handler mHandler;
	/** 加载图片成功(普通) **/
	private final int LOADING_IMAGE = 1;
	/** 加载图片失败(普通) **/
	private final int LOADING_FAIL = 0;
	/** 加载图片成功(banner) **/
	private final int LOADING_BANNER = 10086;
	/** 加载图片失败(banner) **/
	private final int LOADING_BANNER_FAIL = 10010;
	/** 如果缓存和SD卡上都未找到图片,是否加载网络图片?默认加载网络图片 **/
	private boolean loadImage = true;
	private Context context;

	@SuppressLint("HandlerLeak")
	public LImageLoader(Context context) {
		this.context = context;
		cache = new LLimitImageCache();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				if (what == LOADING_IMAGE) {
					// 新图片加载完毕
					String key = (String) msg.obj;
					// 普通--加载图片完毕
					synchronized (currentLoading) {
						if (currentLoading.containsKey(key)) {
							// 查询currentLoading集合，核对对应的ImageView并设置图片
							if (key.equals( currentLoading.get(key).getTag())) {
								currentLoading.get(key).setImageBitmap(cache.get(key));
								currentLoading.get(key).invalidate();
							}
						}
					}
				} else if (LOADING_FAIL == what) {
					// 新图片加载完毕
					String key = (String) msg.obj;
					// 普通--加载图片完毕
					synchronized (currentLoading) {
						if (currentLoading.containsKey(key)) {
							//  查询currentLoading集合，核对对应的ImageView并设置图片
							if (key.equals((String) currentLoading.get(key).getTag())) {
								currentLoading.get(key).setImageResource(R.mipmap.ic_launcher);
								currentLoading.get(key).invalidate();
							}
						}
					}

				} else if (LOADING_BANNER == what) {
					//  加载banner成功
					ImageTag tag = (ImageTag) msg.obj;
					String key = tag.getImageUri();
					if (currentLoading.containsKey(key)) {
						// 查询currentLoading集合，核对对应的ImageView并设置图片
						if (key.equals(((ImageTag) currentLoading.get(key).getTag()).getImageUri())) {
							currentLoading.get(key).setImageBitmap(cache.get(key));
							currentLoading.get(key).invalidate();
						}
					}
				} else if (LOADING_BANNER_FAIL == what) {
					//加载banner成功
					ImageTag tag = (ImageTag) msg.obj;
					String key = tag.getImageUri();
					synchronized (currentLoading) {
						if (currentLoading.containsKey(key)) {
							// 查询currentLoading集合，核对对应的ImageView并设置图片
							if (key.equals(((ImageTag) currentLoading.get(key).getTag()).getImageUri())) {
								currentLoading.get(key).setImageResource(R.mipmap.ic_launcher);
								currentLoading.get(key).invalidate();
							}
						}
					}
				}
			}
		};
	}

	/***
	 * _1,从缓存找图。_2,中内存找图 。_3,网络下载新图
	 *
	 * @param url
	 * @param image
	 *            linqi
	 */
	public void display(final String url, ImageView image, int resId, final int width, final int height) {
		if ("".equals(url) || null == url) {
			image.setImageDrawable((context.getResources().getDrawable(resId)));
			return;
		}

		synchronized (currentLoading) {

			image.setTag(url);
			currentLoading.put(url, image);
		}
		Bitmap has = cache.get(url);
		if (null == has) {//缓存中不存在该图片,SD卡中也不存在
			if (loadImage) {
				// 下载新图片
				new Thread() {
					@Override
					public void run() {
						try {
							Log.i("donwload","bm");
							Bitmap bm = getImageStream(url, width, height);// 下载网络图片

							Log.i("donwload","bm="+bm.toString());
							Message msg = mHandler.obtainMessage();
							if (null != bm) {
								cache.save(url, bm);// 将图片保存
								// 下载成功
								// 保存下载的图片
								cache.put(url, bm);
								msg.what = LOADING_IMAGE;
							} else {
								Log.i("donwload","下载失败");
								msg.what = LOADING_FAIL;
							}
							// 发送通知֪
							msg.obj = url;
							mHandler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			} else {
				// 不下载新图----设置默认图片
				image.setImageDrawable((context.getResources().getDrawable(resId)));

			}
		} else {// 找到目标图片
			image.setImageBitmap(has);
			image.invalidate();
		}
	}

	/***
	 * 加载banner图
	 *
	 * @param tag
	 * @param image
	 */
	public void displayBanner(final ImageTag tag, ImageView image, int resId, final int width, final int height) {
		final String url = tag.getImageUri();
		if ("".equals(url) || null == url) {
			image.setImageDrawable((context.getResources().getDrawable(resId)));
			return;
		}
		synchronized (currentLoading) {
			image.setTag(tag);
			currentLoading.put(url, image);
		}
		Bitmap has = cache.get(url);
		if (null == has) {// 缓存中不存在该图片,SD卡中也不存在
			if (loadImage) {
				// 下载新图片
				new Thread() {
					@Override
					public void run() {
						try {
							Bitmap bm = getImageStream(url,width,height);// 下载网络图片
							cache.save(url, bm);// 将图片保存
							Message msg = mHandler.obtainMessage();
							if (null != bm) {
								// 下载成功
								// 保存下载的图片
								cache.put(url, bm);
								msg.what = LOADING_BANNER;
							} else {
								msg.what = LOADING_BANNER_FAIL;
							}
							// 发送通知
							msg.obj = tag;
							mHandler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		} else {// 找到目标图片
			image.setImageBitmap(has);
			image.invalidate();
		}

	}

	/**
	 * @param loadImage
	 *            the loadImage to set
	 */
	public void setLoadImage(boolean loadImage) {
		this.loadImage = loadImage;
	}

	/***
	 * 下载图片
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Bitmap getImageStream(String path, int width, int height) throws Exception {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(path);
		HttpResponse response = client.execute(get);
		Bitmap bm = null;
		Log.i("donwload","getStatusCode()"+response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			byte[] by = EntityUtils.toByteArray(entity);
//			bm = getBitmap(by, width, height);
			 bm=compressBitmap(by);
		}
		return bm;
	}

	public Bitmap getBitmap(byte[] data, int width, int height) {
		Options opts = new Options();// 进入图片设置选择的功能
		opts.inJustDecodeBounds = true;// 设置图片的状态为可设置
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);// 获取原始图片
		int xScale = opts.outWidth / width;
		int yScale = opts.outHeight / height;
		opts.inSampleSize = xScale > yScale ? xScale : yScale;
		if (opts.inSampleSize < 1) {
			opts.inSampleSize = 1;
		}
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	/****
	 * 质量压缩
	 * 
	 * @param image
	 */
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}



	/***
	 * 大小压缩
	 * 
	 * @param data
	 */
	public Bitmap compressBitmap(byte[] data) {
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机分辨率，所以高和宽我们设置为
		float hh = PWApplication.metrics.heightPixels;// 这里设置高度
		float ww = PWApplication.metrics.widthPixels;// 这里设置宽度
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}


	/***
	 * 清除缓存
	 * 
	 */
	public boolean clear() {
		return cache.clear();
	}

	/***
	 * 保存图片
	 * 
	 * @param key
	 * @param bm
	 */
	public void put(String key, Bitmap bm) {
		cache.put(key, bm);
	}

	/***
	 * 获取图片
	 * 
	 * @param key
	 */
	public Bitmap get(String key) {
		return cache.get(key);
	}
}
