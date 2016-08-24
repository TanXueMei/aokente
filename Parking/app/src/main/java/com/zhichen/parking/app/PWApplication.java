package com.zhichen.parking.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.image.LImageLoader;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.tools.NetworkUtils;
import com.zhichen.parking.util.FileUtil;


import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-05-25.
 */
public class PWApplication extends Application {
    private static PWApplication instance;
    /* 存放所有的activity*/
    public static List<Activity> activityList = new ArrayList<Activity>();
    /**
     * 图片加载类
     **/
    public static LImageLoader mImageLoader;
    /**
     * 屏幕管理器
     **/
    public static DisplayMetrics metrics = null;
    /**
     * 窗口管理器
     **/
    private WindowManager mManager = null;


    public PWApplication() {
    }

    // 确定当前的唯一
    public synchronized static PWApplication getInstance() {
        if (null == instance) {
            instance = new PWApplication();
        }
        return instance;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (NetworkUtils.isNetworkAvailable(this)) {

        } else {
            Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }
        instance = this;
        x.Ext.init(this);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        UserManager.instance().loadConfig(this);
//        加载图片
        mManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(metrics);
        mImageLoader = new LImageLoader(this);
        initImageLoader(getApplicationContext());
        /**
         * 建立目录
         */
        initAppFolders();
        /**
         * 错误日志
         */
        initCrashHandler();


    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public void setAppDirByName(String appName) {
        AppConstants.APP_FOLDER = appName;
        AppConstants.APP_DIR = AppConstants.ZCHX_DIR + "/" + AppConstants.APP_FOLDER;
    }

    public String getAppDir() {
        return AppConstants.APP_DIR;
    }

    public void initAppFolders() {
        FileUtil.createFolder(AppConstants.APP_DIR);
        FileUtil.createFolder(AppConstants.DOCUMNETS_DIR);
        FileUtil.createFolder(AppConstants.LOG_DIR);
        FileUtil.createFolder(AppConstants.RESOURCE_DIR);
        FileUtil.createFolder(AppConstants.LOCAL_DIR);
        FileUtil.createFolder(AppConstants.TEMP_DIR);
    }

    public void initCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
