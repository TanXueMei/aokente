package com.zhichen.parking.image.cache;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ImageUtils {

	public static Bitmap decodeFile(String path, int reqWidth, int reqHeight) {
		Bitmap bmp = null;

		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;

		bmp = BitmapFactory.decodeFile(path, options);

		return bmp;
	}

	/**
	 * 图片压缩的方式叫做二次采样
	 *
	 * @param res
	 * @param id
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeResources(Resources res, int id, int reqWidth, int reqHeight) {

		Options options = new Options();
		// 让BitmapFactory解析图片的时候，只解析边框（其实就是他的分辨率）
		// 当设置为true的时候，BitmapFactory不会去解析图片生成Bitmap，只有为false才会生成bitmap
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(res, id, options);

		int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// 指定压缩图片的倍数
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;

		Bitmap bmp = BitmapFactory.decodeResource(res, id, options);

		return bmp;
	}

	/**
	 * @param options
	 * @param reqWidth
	 *            你希望将图片压缩到 多少x多少 分辨率的像素
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
