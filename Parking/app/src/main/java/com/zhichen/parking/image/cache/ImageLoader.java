package com.zhichen.parking.image.cache;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    /**
     * 解决图片重复下载
     */
    private HashMap<String, ImageView> mLoadingBitmap = new HashMap<String, ImageView>();

    public ImageLoader() {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ImageView img = (ImageView) msg.obj;
            String path = (String) img.getTag();
            String key = getFileName(path);

            Bitmap bmp = mMemoryCache.get(key);
            img.setImageBitmap(bmp);
        }
    };

    public void load(final ImageView img, final String path, final int resId) {
        if (!"".equals(path) || null != path) {

            final String key = getFileName(path);

            img.setTag(path);
            img.setImageBitmap(null);

            // 先从内存缓存中取
            Bitmap bmp = mMemoryCache.get(key);

            if (bmp == null) {

                // 每次将要去下载图片的时候，先判断当前url的图片是否有正在下载
                boolean isLoading = mLoadingBitmap.containsKey(path);

                if (!isLoading) {

                    mLoadingBitmap.put(path, img);

                    new Thread() {
                        @Override
                        public void run() {

                            // 内存没有，再从sdcard中取
                            Bitmap bmp = mDiskCache.get(key);

                            if (bmp == null) {
                                try {
                                    URL url = new URL(path);

                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setConnectTimeout(25000);
                                    conn.setReadTimeout(25000);

                                    int code = conn.getResponseCode();

                                    if (code == 200) {
                                        InputStream is = conn.getInputStream();
                                        mDiskCache.put(key, is);
                                        is.close();

                                        bmp = mDiskCache.get(key);
                                    }else img.setImageResource(resId);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (bmp != null) {
                                mMemoryCache.put(key, bmp);
                            }

                            ImageView iamgeView = mLoadingBitmap.get(path);

                            if (path.equals(iamgeView.getTag())) {
                                Message.obtain(handler, 1, iamgeView).sendToTarget();
                            }

                            mLoadingBitmap.remove(path);
                        }
                    }.start();
                } else {
                    mLoadingBitmap.put(path, img);
                }
            } else {
                img.setImageBitmap(bmp);
            }
        } else img.setImageResource(resId);
    }

    public static String getFileName(String url) {
        return "img_" + url.hashCode() + ".tmp";
    }
}
