package com.zhichen.parking.lib.mapservice;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class LocationService extends BaseService {

	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private boolean isFirstLoc = true;
	
	private OnLocationListener mUserLocationListener ;

	public LocationService(Context context, MapView mapView) {
		super(context, mapView);
//		mCurrentMode = LocationMode.FOLLOWING;
		mCurrentMode = LocationMode.NORMAL;
	}
	
	@Override
	protected void onPause() {
	}

	@Override
	protected void onResume() {
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if(mLocClient != null){
			mLocClient.stop();
		}
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
	}

	//coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
	public void start() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		//option.setIsNeedLocationDescribe(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		setLocationConfigeration(mCurrentMode);
	}

	/**
	 * 无效的资源则会切换到默认图标
	 */
	public void setMarker(int resId) {
		mCurrentMarker = BitmapDescriptorFactory.fromResource(resId);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
	}

	public void setLocationConfigeration(LocationMode mode) {
		switch (mode) {
		case FOLLOWING:
			mCurrentMode = LocationMode.FOLLOWING;
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
			break;
		case NORMAL:
			mCurrentMode = LocationMode.NORMAL;
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
			break;
		case COMPASS:
			mCurrentMode = LocationMode.COMPASS;
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
			break;
		}
	}
	
	public MyLocationData getMyLocationData()
	{
		return mBaiduMap.getLocationData() ;
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			Log.d("map","location----radius=" + location.getRadius() + " ---latitude=" + location.getLatitude()
					+ "---Longitude=" + location.getLongitude());

			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}

			if(mUserLocationListener != null){
				mUserLocationListener.onReceiveLocation(location);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	public void setUserLocationListener(OnLocationListener listener)
	{
		mUserLocationListener = listener ;
	}

	public interface OnLocationListener
	{
		public void onReceiveLocation(BDLocation location);
	}
}
