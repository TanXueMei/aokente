package com.zhichen.parking.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtil {

	@SuppressLint("SimpleDateFormat") 
	public static String formatDispTime(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
	
	/**
	 * @param duration 单位为秒
	 */
	public static String formatDuration(int duration)
	{
		int hours = duration/60;
		int mins = (duration-hours*60)%60;
//		int seconds = duration ;

//		int seconds = duration%60 ;
//		int hours = duration/(60*60);
//		int mins = (duration-hours*60*60)/60;
		return hours + "小时" + mins + "分";
	}
	
}
