package com.zhichen.parking.lotmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;


import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.zhichen.parking.manager.UserManager;

import java.util.List;
import java.util.Set;


public class ParkingLotMap implements OnMapStatusChangeListener {

	private Context mContext;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private Canvas mCanvas;
	private Bitmap mSpotBitmap;
	private Marker mParkingMarker;
	private String mCityCode;

	MarkerManager mMarkerManager ;
	private ClusterManager<MiniParkingLot> mClusterManager ;

	public ParkingLotMap(Context context, MapView mapView, MarkerManager markerManager) {
		mContext = context;
		mMapView = mapView;
		mMarkerManager = markerManager ;
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapStatusChangeListener(this);
		mClusterManager = new ClusterManager<>(context, mBaiduMap);
		UserManager.instance().setMapTargetPos(mBaiduMap.getMapStatus().target);
	}

	public void init() {
		if (mSpotBitmap != null) {
			return;
		}
		mSpotBitmap = Bitmap.createBitmap(mMapView.getWidth(), mMapView.getHeight(), Bitmap.Config.ARGB_8888);
		mSpotBitmap.eraseColor(Color.TRANSPARENT);
		mCanvas = new Canvas(mSpotBitmap);
		BitmapDescriptor fromBitmap = BitmapDescriptorFactory.fromBitmap(mSpotBitmap);
		LatLng centerLatLng = new LatLng(mBaiduMap.getMapStatus().target.longitude, mBaiduMap.getMapStatus().target.longitude);
		MarkerOptions ooB = new MarkerOptions().position(centerLatLng).icon(fromBitmap).zIndex(13).anchor(0.5f, 0.5f);
		mParkingMarker = (com.baidu.mapapi.map.Marker) mBaiduMap.addOverlay(ooB);
	}

	public void show(String cityCode) {
		if (cityCode == null) {
			return;
		}
		if (cityCode.equals(mCityCode)) {
			return;
		}
		mCityCode = cityCode;
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		Log.e("cwf", "-------onMapStatusChangeFinish------");
		updateParkingLot();
		UserManager.instance().setMapTargetPos(mBaiduMap.getMapStatus().target);
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
	}

	private void updateParkingLot() {
		List<MiniParkingLot> lotList = ParkingLotManager.instance().getMiniParkingLotList();
		if (lotList == null) {
			return;
		}
		//drawHeatMap();
		
		long start = System.currentTimeMillis();
		
		mClusterManager.updateItems(lotList);
		Set<? extends Cluster<MiniParkingLot>> clusters = mClusterManager.cluster();

		mMarkerManager.showMarker(clusters);
		
		Log.d("cwf", "----showMarker-----time=" + (System.currentTimeMillis()-start));
	}
	
	private void drawHeatMap() {
		if (mParkingMarker == null) {
			return;
		}
		Projection projection = mBaiduMap.getProjection();
		if (projection == null) {
			return;
		}
		List<MiniParkingLot> lotList = ParkingLotManager.instance().getMiniParkingLotList();
		if (lotList == null) {
			return;
		}
		mSpotBitmap.eraseColor(Color.parseColor("#12ff0000"));
		Point screenLoc = null;
		Bitmap drawBitmap = null;
		long start = System.currentTimeMillis();
		float left = 0;
		float top = 0;
		final LatLngBounds bound = mBaiduMap.getMapStatus().bound;
		for (MiniParkingLot lot : lotList) {
			if(!bound.contains(lot.getPosition())){
				continue ;
			}
			screenLoc = projection.toScreenLocation(lot.getPosition());
			drawBitmap = mMarkerManager.getMarkerIcon(lot.getType());
			left = screenLoc.x - drawBitmap.getWidth() / 2;
			top = screenLoc.y - drawBitmap.getHeight();
			mCanvas.drawBitmap(drawBitmap, left, top, null);
		}
		Log.d("cwf", "time=" + (System.currentTimeMillis() - start));
		BitmapDescriptor fromBitmap = BitmapDescriptorFactory.fromBitmap(mSpotBitmap);
		mParkingMarker.setIcon(fromBitmap);
		mParkingMarker.setPosition(mBaiduMap.getMapStatus().target);
	}
}
