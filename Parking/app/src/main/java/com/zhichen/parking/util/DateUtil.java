package com.zhichen.parking.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuemei on 2016-06-04.
 */
public class DateUtil {
    /**
     * 日期转字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        try {
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateformat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String differDate(Date prev, Date next) {
        final long DAY = 24 * 60 * 60 * 1000;
        final long HOUR = 60 * 60 * 1000;
        final long MINUTE = 60 * 1000;
        final long SECOND = 1000;

        long prevMsec = prev.getTime();
        long nextMsec = next.getTime();
        long differ = prevMsec - nextMsec;
        String result = differ >= 0 ? "+" : "-";
        differ = Math.abs(differ);
        long day = (differ - differ % DAY) / DAY;
        long hour = ((differ - day * DAY) - ((differ - day * DAY) % HOUR)) / HOUR;
        long minute = ((differ - day * DAY - hour * HOUR) - ((differ - day * DAY - hour * HOUR) % MINUTE)) / MINUTE;
        long second = ((differ - day * DAY - hour * HOUR - minute * MINUTE) - ((differ - day * DAY - hour * HOUR - minute * MINUTE) % SECOND)) / SECOND;
        long milliscond = differ - day * DAY - hour * HOUR - minute * MINUTE - second * SECOND;
//		return result + day + "天" + hour + "时" + minute + "分" + second + "秒" + milliscond + "毫秒";
        return hour + "时" + minute + "分" + second + "秒";
    }
}
