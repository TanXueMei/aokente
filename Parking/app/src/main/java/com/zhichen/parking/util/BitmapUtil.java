package com.zhichen.parking.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BitmapUtil {

	public static Bitmap scale(Bitmap srcBitmap, int width, int height,
			boolean IgnoreAspectRatio) 
	{
		Rect srcRect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
		return scale(srcBitmap, srcRect, width, height, IgnoreAspectRatio);
	}
	
	public static Bitmap scale(Bitmap srcBitmap, Rect srcRect, int width, int height,
			boolean IgnoreAspectRatio) 
	{
		float hFactor = (float) width / srcBitmap.getWidth();
		float vFactor = (float) height / srcBitmap.getHeight();
		Matrix matrix = new Matrix();
		if (IgnoreAspectRatio) {
			matrix.postScale(hFactor, vFactor);
		} else {
			float factor = Math.min(vFactor, hFactor);
			matrix.postScale(factor, factor);
		}
		Bitmap resizeBmp = Bitmap.createBitmap(srcBitmap, srcRect.left, srcRect.top, srcRect.width(), srcRect.height(), matrix, true);
		return resizeBmp;
	}
	
	public static boolean saveAsFile(Bitmap bitmap, String path)
	{
		if(path == null || bitmap == null){
			return false ;
		}
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		try {
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
			return true ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false ;
	}
	
	public static Bitmap createBitmap(int width, int height, Config config)
	{
    	Bitmap bitmap = null ;
    	try{
    		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    	}
    	catch(OutOfMemoryError e){
    		e.printStackTrace();
    	}
        return bitmap ;
    }

	/*欧阳健....*/
	public static Bitmap decodeImage(String srcPath , int width ,int height) {   
        BitmapFactory.Options newOpts = new BitmapFactory.Options();   
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了   
        newOpts.inJustDecodeBounds = true;   
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空   
           
        newOpts.inJustDecodeBounds = false;   
//       
        
        float sw = (float)width/newOpts.outWidth ;
        float sh = (float)height/newOpts.outHeight ;
        int be = (int)(1/Math.min(sw, sh));
	      if (be <= 0)   
	    	  	be = 1;   	
        
        newOpts.inSampleSize = be;//设置缩放比例   
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了   
        try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (Exception e) {
			Log.e("otcyan",e.getMessage()) ;
			e.printStackTrace();
		} 
        Log.e("otcyan","图片压缩比："+be) ;
        return bitmap;//压缩好比例大小后再进行质量压缩   
    } 

	public static Bitmap decodeImage(String srcPath , int width ){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();   
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了   
        newOpts.inJustDecodeBounds = true;   
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空   
           
        newOpts.inJustDecodeBounds = false;   
//      
        float sw = (float)width/newOpts.outWidth ;
        
        int be = (int)(1/sw);
	      if (be <= 0)   
	    	  	be = 1;   	
        
        newOpts.inSampleSize = be;//设置缩放比例   
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了   
        try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (Exception e) {
			Log.e("otcyan",e.getMessage()) ;
			e.printStackTrace();
		} 
        return bitmap;//压缩好比例大小后再进行质量压缩   
	}
	
	/**
     * 转换图片成圆形
     * 
     * @param bitmap
     *            传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                    roundPx = width / 2;
                    top = 0;
                    bottom = width;
                    left = 0;
                    right = width;
                    height = width;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
            } else {
                    roundPx = height / 2;
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
            }

            Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right,
                            (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top,
                            (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
    }
    
    /**
     * 转换图片成圆形
     * 
     * @param bitmap 需要
     * @return
     */
    public static Bitmap toLoveBitmap(Context context , Bitmap bitmap  , Bitmap maskBitmap , Bitmap maskBitmap2) {
    	
        int maskWidth = maskBitmap.getWidth();
        int maskHheight = maskBitmap.getHeight();
        int bmWidth = bitmap.getWidth() ;
        int bmHeight = bitmap.getHeight() ;
        
        
      
        Bitmap output = Bitmap.createBitmap(maskWidth, maskHheight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawBitmap(maskBitmap, 0, 0, paint);
        
        final int left = (bmWidth > maskWidth) ? (bmWidth-maskWidth)/2 : 0;
        final int top = (bmHeight > maskHheight) ? (bmHeight-maskHheight)/2 : 0;
         int right = left + maskWidth ;
        right = (right > bmWidth) ? bmWidth : right ;
         int bottom = top + maskHheight ;
        bottom = (bottom > bmHeight) ? bmHeight : bottom ;
        final Rect src = new Rect( left , (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect( 0,  0,maskWidth,  maskHheight);
        
        Log.v("snamon", paint.getXfermode()+"====") ;
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap,src, dst, paint);
        
        paint.setXfermode(null);
        canvas.drawBitmap(maskBitmap2, 0, 0, paint);
        
        return output;
}

}
