package com.zhichen.parking.lib.mapservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示覆盖物的用法 点、线、多边形、圆、文本、图标
 */
public class OverlayService extends BaseService {

	private static final String KEY_CATEGORY = "category";

	Map<Integer, BitmapDescriptor> mBitmapDescMap;
	Map<String, CategoryMarkerClickListener> mMarkerListenerMap;
	
	@SuppressLint("UseSparseArrays")
	public OverlayService(Context context, MapView mapView) {
		super(context, mapView);
		mBitmapDescMap = new HashMap<Integer, BitmapDescriptor>();
		mMarkerListenerMap = new HashMap<String, CategoryMarkerClickListener>();

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.getExtraInfo() == null){
					return false ;
				}
				String key = marker.getExtraInfo().getString(KEY_CATEGORY);
				CategoryMarkerClickListener listener = mMarkerListenerMap.get(key);
				if (listener != null) {
					listener.onMarkerClick(marker, marker.getExtraInfo());
				}
				return false;
			}
		});
	}

	public BitmapDescriptor getBitmapDescriptor(int resId) {
		BitmapDescriptor bd = mBitmapDescMap.get(resId);
		if (bd == null) {
			bd = BitmapDescriptorFactory.fromResource(resId);
			mBitmapDescMap.put(resId, bd);
		}
		return bd;
	}

	public MarkerOptions createMarkerOptions(int resId, LatLng position, int layer) {
		BitmapDescriptor bd = getBitmapDescriptor(resId);
		MarkerOptions options = new MarkerOptions().position(position).icon(bd).zIndex(layer);
		return options;
	}

	public MarkerOptions createMarkerOptions4Anim(List<Integer> resIdList, LatLng position, int layer, int period) {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		BitmapDescriptor bd = null;
		for (Integer id : resIdList) {
			bd = getBitmapDescriptor(id);
			giflist.add(bd);
		}
		MarkerOptions options = new MarkerOptions().position(position).icons(giflist).zIndex(layer).period(period);
		return options;
	}

	public GroundOverlayOptions createGroundOverlayOptions(int resId, LatLng southwest, LatLng northeast, float transparency) {
		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();
		BitmapDescriptor bd = getBitmapDescriptor(resId);
		GroundOverlayOptions options = new GroundOverlayOptions().positionFromBounds(bounds).image(bd).transparency(transparency);
		return options;
	}

	public TextOptions createTextOptions(String text, int fontSize, int fontColor, int bgColor, LatLng position) {
		TextOptions ooText = new TextOptions().bgColor(bgColor).fontSize(fontSize).fontColor(fontColor).text(text)
				.position(position);
		return ooText;
	}

	public CircleOptions createCircleOptions(LatLng center, int radius, int strokeWidth, int strokeColor, int fillColor) {
		CircleOptions ooCircle = new CircleOptions().center(center).radius(radius).stroke(new Stroke(strokeWidth, strokeColor))
				.fillColor(fillColor);
		return ooCircle;
	}

	public Marker addMarker(MarkerOptions options, CategoryMarkerClickListener listener, Bundle info) {
		Marker mar = (Marker) (mBaiduMap.addOverlay(options));
		if(info == null){
			info = new Bundle();
		}
		info.putString(KEY_CATEGORY, listener.category());
		mar.setExtraInfo(info);
		mMarkerListenerMap.put(listener.category(), listener);
		return mar ;
	}

	@Override
	protected void onPause() {
	}

	@Override
	protected void onResume() {
	}

	@Override
	protected void onDestroy() {

		Collection<BitmapDescriptor> bdList = mBitmapDescMap.values();
		for (BitmapDescriptor bd : bdList) {
			bd.recycle();
		}
		mBitmapDescMap.clear();
		mMarkerListenerMap.clear();
	}

	public static abstract class CategoryMarkerClickListener {
		private String mCategory;

		public CategoryMarkerClickListener(String category) {
			mCategory = category;
		}

		public String category() {
			return mCategory;
		}

		public abstract boolean onMarkerClick(final Marker marker, final Bundle info);
	}
}
