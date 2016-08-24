package com.zhichen.parking.lotmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLngBounds;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.lib.mapservice.MapServiceManager;
import com.zhichen.parking.lib.mapservice.OverlayService;
import com.zhichen.parking.lib.mapservice.OverlayService.CategoryMarkerClickListener;
import com.zhichen.parking.R;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MarkerManager {

	public static final String KEY_MARKER_LOT_ID = "lot_id";
	
	Context mContext ;
	MapServiceManager mMapServiceManager ;
	BaiduMap mBaiduMap;

	private CategoryMarkerClickListener mMarkerClickListener ;
	private Bitmap mSmartBitmap;//智慧停车图标  app图标灰
	private Bitmap mRoadBitmap;//路边停车图标  黄
	private Bitmap mTrdditionBitmap;//传统停车图标  蓝

	private Bitmap mRedBitmap;//红
	private Bitmap mBlueBitmap;//蓝
	private Bitmap mGreenBitmap;//绿
	private Bitmap mGrayBitmap;//灰

	private List<MarkerBundle> mMarkerList ;
	
	public MarkerManager(Context context, MapServiceManager mapServiceManager) {
		mContext = context ;
		mMapServiceManager = mapServiceManager ;
		mBaiduMap = mMapServiceManager.getMapView().getMap();
		mSmartBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_park_smart);
		mRoadBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_park_road);
		mTrdditionBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_park_trddition);

		mRedBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_red);
		mBlueBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_blue);
		mGreenBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_green);
		mGrayBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_gray);


		mMarkerList = new ArrayList<MarkerBundle>();
	}
	
	public void showMarker(Set<? extends Cluster<MiniParkingLot>> clusterList) {
		Log.e("cwf", "----showMarker-----size=" + clusterList.size());
		final LatLngBounds mapBound = mBaiduMap.getMapStatus().bound;
		for(MarkerBundle markerBundle : mMarkerList){
			Log.e("xue","markerBundle.marker.getPosition()==="+markerBundle.marker.getPosition());
			if(mapBound.contains(markerBundle.marker.getPosition())){
				markerBundle.isShowing = true ;
			} else{
				markerBundle.isShowing = false ;
			}
		}
		MarkerBundle markerBundle = null ;
		for(Cluster<MiniParkingLot> cluster : clusterList){
			if(mapBound.contains(cluster.getPosition())){
				Iterator<?> iterator = cluster.getItems().iterator();
				if(iterator.hasNext()){
					markerBundle = nextMarkerBundle((MiniParkingLot)iterator.next());
					if(markerBundle != null){
						markerBundle.isShowing = true ;
						markerBundle.marker.setPosition(cluster.getPosition());
					}
				}
			}
		}
	}
	
	private MarkerBundle nextMarkerBundle(MiniParkingLot lot) {
		for(MarkerBundle bundle : mMarkerList){
			if(bundle.lotId == lot.getParkingLotId()){
				return bundle ;
			}
		}
		if(mMarkerList.size() < 200){
			OverlayService overlayService = mMapServiceManager.getOverlayService();
			Bundle info = new Bundle();
			info.putLong(KEY_MARKER_LOT_ID, lot.getParkingLotId());
			int resId = getMarkerIconRes(lot.getType());
			MarkerOptions options = overlayService.createMarkerOptions(resId, lot.getPosition(), mMarkerList.size()+1);
			Marker addMarker = overlayService.addMarker(options, mMarkerClickListener, info);
			MarkerBundle bundle = new MarkerBundle(addMarker, true, lot.getParkingLotId());
			mMarkerList.add(bundle);
			return bundle ;
		}
		for(MarkerBundle bundle : mMarkerList){
			if( ! bundle.isShowing){
				Bundle info = bundle.marker.getExtraInfo();
				info.putLong(KEY_MARKER_LOT_ID, lot.getParkingLotId());
				bundle.isShowing = true ;
				bundle.lotId = lot.getParkingLotId();
				return bundle ;
			}
		}
		return null ;
	}
	
	public Bitmap getMarkerIcon(int lotType) {
//		if (lotType == AppConstants.PARKINGLOT_TYPE_ROAD) {
//			return mRoadBitmap;
//		}
//		if (lotType == AppConstants.PARKINGLOT_TYPE_TRDDITION) {
//			return mTrdditionBitmap;
//		}
		if(lotType==AppConstants.TYPE_ICON_RED){
			return mRedBitmap;
		}
		if(lotType==AppConstants.TYPE_ICON_BLUE){
			return mBlueBitmap;
		}
		if(lotType==AppConstants.TYPE_ICON_GREEN){
			return mGrayBitmap;
		}
		if(lotType==AppConstants.TYPE_ICON_GRAY){
			return mGrayBitmap;
		}
		return mSmartBitmap;
	}

	public int getMarkerIconRes(int lotType) {
//		if (lotType == AppConstants.PARKINGLOT_TYPE_ROAD) {
//			return R.mipmap.icon_park_road;
//		}
//		if (lotType == AppConstants.PARKINGLOT_TYPE_TRDDITION) {
//			return R.mipmap.icon_park_trddition;
//		}
		if(lotType==AppConstants.TYPE_ICON_RED){
			return R.mipmap.icon_red;
		}
		if(lotType==AppConstants.TYPE_ICON_BLUE){
			return R.mipmap.icon_blue;
		}
		if(lotType==AppConstants.TYPE_ICON_GREEN){
			return R.mipmap.icon_green;
		}
		if(lotType==AppConstants.TYPE_ICON_GRAY){
			return R.mipmap.icon_gray;
		}
		return R.mipmap.icon_park_smart;
	}
	
	public void setCategoryMarkerClickListener(CategoryMarkerClickListener listener)
	{
		mMarkerClickListener = listener ;
	}
	
	class MarkerBundle {
		public Marker marker ;
		public boolean isShowing ;
		public long lotId;
		
		public MarkerBundle(Marker marker, boolean isShowing, long lotId)
		{
			this.marker = marker ;
			this.isShowing = isShowing ;
			this.lotId = lotId ;
		}
	}

}
