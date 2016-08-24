package com.zhichen.parking.servercontoler;

import android.util.Base64;

import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.response.Response;

import java.util.LinkedHashMap;

public class ServerManger {

	private static ServerManger ins = new ServerManger();

	private LiteHttp mLiteHttp;
	
	public static ServerManger intance() {
		return ins;
	}
	
	private ServerManger(){
	}
	
	public LiteHttp getLiteHttp()
	{
		if(mLiteHttp != null){
			return mLiteHttp ;
		}
		mLiteHttp = LiteHttp.newApacheHttpClient(null);
		return mLiteHttp ;
	}
	
	public LinkedHashMap<String, String> createAuthHeader(String userName, String password)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		String author = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.DEFAULT);
		map.put("Authorization", author);
		return map ;
	}
	
	public static abstract class AsyncResponseHandler extends HttpListener<String>
	{
		@Override
		public void onSuccess(String data, Response<String> response) {
			super.onSuccess(data, response);
			onSuccess(200, data);
		}
		
		@Override
		public void onFailure(HttpException e, Response<String> response) {
			super.onFailure(e, response);
			onFailure(400, e.toString(), e.getCause());
		}
		
		public abstract void onSuccess(int statusCode, String response);
	    public abstract void onFailure(int statusCode, String errorResponse, Throwable e);
	}
}
