package com.zhichen.parking.lib.mapservice;

import android.content.Context;

import com.baidu.mapapi.map.MapView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapServiceManager {
	
	Context mContext ;
	MapView mMapView ;
	
	Map<String, BaseService> mServiceMap ;
	
	public MapServiceManager(Context context, MapView mapView) {
		mContext = context ;
		mMapView = mapView ;
		mServiceMap = new HashMap<>();
	}
	
	public MapView getMapView() {
		return mMapView ;
	}
	
	private BaseService getService(Class<? extends BaseService> cls) {
		String key = cls.getSimpleName();
		BaseService service = mServiceMap.get(key);
		if(service == null){
			try {
				Constructor<? extends BaseService> constructor = cls.getConstructor(Context.class, MapView.class);
				service = constructor.newInstance(mContext, mMapView);
				mServiceMap.put(key, service);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return service ;
	}
	public LocationService getLocationService() {
		BaseService service = getService(LocationService.class);
		return (LocationService)service ;
	}
	
	public OverlayService getOverlayService() {
		BaseService service = getService(OverlayService.class);
		return (OverlayService)service ;
	}
	
	public ControlService getControlService() {
		BaseService service = getService(ControlService.class);
		return (ControlService)service ;
	}
	
	public HeatMapService getHeatMapService() {
		BaseService service = getService(HeatMapService.class);
		return (HeatMapService)service ;
	}

	public void onPause() {
		mMapView.onPause();
	}

	public void onResume() {
		mMapView.onResume();
	}

	public void onDestroy() {
		Collection<BaseService> serviceList =  mServiceMap.values();
		for(BaseService service : serviceList){
			service.onDestroy() ;
		}
		mServiceMap.clear();
		mMapView.onDestroy();
	}
}
