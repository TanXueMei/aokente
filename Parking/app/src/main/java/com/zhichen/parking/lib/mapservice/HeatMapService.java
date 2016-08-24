package com.zhichen.parking.lib.mapservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.zhichen.parking.R;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HeatMapService  extends BaseService {

	@SuppressLint("UseSparseArrays")
	public HeatMapService(Context context, MapView mapView) {
		super(context, mapView);
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
	
	public void addHeatMap(long cityCode) {
		HeatMapTask task = new HeatMapTask();
		task.execute();
	}

	private List<LatLng> getLocations() {
		List<LatLng> list = new ArrayList<LatLng>();
		InputStream inputStream = mContext.getResources().openRawResource(R.raw.locations);
		String json = new Scanner(inputStream).useDelimiter("\\A").next();
		JSONArray array;
		try {
			array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				double lat = object.getDouble("lat");
				double lng = object.getDouble("lng");
				//list.add(new LatLng(lat, lng));
			}
			
			list.add(new LatLng(22.57567 + 0.003, 113.956659 - 0.005));
			list.add(new LatLng(22.57567 + 0.01, 113.956659 + 0.01));
			list.add(new LatLng(22.57567 - 0.01, 113.956659 - 0.01));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private class HeatMapTask extends AsyncTask<Void, Integer, String>
	{
		private HeatMap heatmap ;
		
		private HeatMapTask(){
		}
		
		@Override
		protected String doInBackground(Void... params) {
			List<LatLng> data = getLocations();
			heatmap = new HeatMap.Builder().data(data).build();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			mBaiduMap.addHeatMap(heatmap);
		}
	}
}
