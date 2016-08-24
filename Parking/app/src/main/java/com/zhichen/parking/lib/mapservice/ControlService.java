package com.zhichen.parking.lib.mapservice;

import android.annotation.SuppressLint;
import android.content.Context;

import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

/**
 * 演示地图缩放，旋转，视角控制，截图
 */
public class ControlService extends BaseService {

	@SuppressLint("UseSparseArrays")
	public ControlService(Context context, MapView mapView) {
		super(context, mapView);
	}
	
	/**
	 * 处理缩放 sdk 缩放级别范围： [3.0, 20.0]
	 * 20-10m，19-20m，18-50m，17-100m，16-200m，15-500m，14-1km，13-2km，12-5km，
	 * 11-10km，10-10km，9-20km，8-25km，7-50km，6-100km，5-200km，4-500km，3-1000km
	 */
	public void perfomZoom(float zoomLevel) {
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
		mBaiduMap.animateMapStatus(u);
	}

	/**
	 * 处理旋转 旋转角范围： -180 ~ 180 , 单位：度 逆时针旋转
	 */
	public void perfomRotate(int rotateAngle) {
		MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(rotateAngle).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.animateMapStatus(u);
	}

	/**
	 * 处理俯视 俯角范围： -45 ~ 0 , 单位： 度
	 */
	public void perfomOverlook(int overlookAngle)
	{
		MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(overlookAngle).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.animateMapStatus(u);
	}
	
	/**
	 * 地图截屏
	 */
	public void snapshot(SnapshotReadyCallback callback)
	{
		mBaiduMap.snapshot(callback);
	}

	@Override
	protected void onPause() {
	}

	@Override
	protected void onResume() {
	}

	@Override
	protected void onDestroy() {
	}

}
