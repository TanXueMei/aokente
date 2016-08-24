package com.zhichen.parking.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	public static Gson createGson()
	{
		//"2015-11-13T12:14:26.742Z"   "yyyy-MM-dd'T'HH:mm:ss.SSS"      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
	}
}
