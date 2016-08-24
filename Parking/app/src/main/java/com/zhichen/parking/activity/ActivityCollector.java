package com.zhichen.parking.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-13.
 * 管理Activity
 */
public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
