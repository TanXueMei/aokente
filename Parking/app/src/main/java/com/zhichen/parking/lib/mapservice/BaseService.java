package com.zhichen.parking.lib.mapservice;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public abstract class BaseService {

	protected Context mContext ;
	protected MapView mMapView;
	protected BaiduMap mBaiduMap;
	
	public BaseService(Context context, MapView mapView)
	{
		init(context, mapView);
	}
	
	protected void init(Context context, MapView mapView)
	{
		mContext = context ;
		mMapView = mapView ;
		mBaiduMap = mMapView.getMap();
	}
	
	protected abstract void onPause();
	
	protected abstract void onResume();

	protected abstract void onDestroy();

}
