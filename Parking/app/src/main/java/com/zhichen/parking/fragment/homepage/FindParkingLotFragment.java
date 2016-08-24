package com.zhichen.parking.fragment.homepage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.lib.mapservice.LocationService;
import com.zhichen.parking.lib.mapservice.MapConstants;
import com.zhichen.parking.lib.mapservice.MapServiceManager;
import com.zhichen.parking.lib.mapservice.OverlayService;
import com.zhichen.parking.lotmanager.MarkerManager;
import com.zhichen.parking.lotmanager.ParkingLotManager;
import com.zhichen.parking.lotmanager.ParkingLotMap;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.GsonUtil;
import com.zhichen.parking.widget.GlobalTitleLayout;
import com.zhichen.parking.widget.MarqueeTextView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-06-04.
 * 寻找停车
 */
public class FindParkingLotFragment extends BaseFragment implements View.OnClickListener {
    View mViewRoot;

    private ImageView search, list;
    MapServiceManager mMapServiceManager;
    ParkingLotMap mParkingLotMap;
    MarkerManager mMarkerManager;

    PopupWindow mParkingPopupWin;
    private List<String> permissions;

    private GlobalTitleLayout globalTitleLayout;
    private ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_find_parklot, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        globalTitleLayout = (GlobalTitleLayout) mViewRoot.findViewById(R.id.finLot_title);
        search = (ImageView) globalTitleLayout.findViewById(R.id.search);
        list = (ImageView) globalTitleLayout.findViewById(R.id.list);
        search.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        search.setOnClickListener(this);
        list.setOnClickListener(this);

        addPermision();
        MPermissions.requestPermissions(FindParkingLotFragment.this, 4, permissions.toArray(new String[permissions.size()]));
//        百度地图部分
//        MapView mapView = (MapView) mViewRoot.findViewById(R.id.bmapView);
//        mMapServiceManager = new MapServiceManager(getContext(), mapView);
//        mMarkerManager = new MarkerManager(getContext(), mMapServiceManager);
//        mParkingLotMap = new ParkingLotMap(getContext(), mapView, mMarkerManager);
//
//        //        开始定位
//        startLocation();
////        在地图上放置图标
//        playParkingLot();
    }

    private void addPermision() {
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_SETTINGS);
    }

    //    	加入运行时权限回调
    @PermissionGrant(4)
    public void requestContactSuccess() {
    }

    @PermissionDenied(4)
    public void requestContactFailed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    FragmentChangeHelper helper;

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {

            case R.id.search:
                fragment = new SearchFragment();
                break;
            case R.id.list:
                fragment = new ParkLotListFragment();
                break;
        }
        if (fragment != null) {
            helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapServiceManager != null) {
            mMapServiceManager.onResume();
        }
        MapView mapView = (MapView) mViewRoot.findViewById(R.id.bmapView);
        mMapServiceManager = new MapServiceManager(getContext(), mapView);
        mMarkerManager = new MarkerManager(getContext(), mMapServiceManager);
        mParkingLotMap = new ParkingLotMap(getContext(), mapView, mMarkerManager);
        //        开始定位
        startLocation();
//        在地图上放置图标
        playParkingLot();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapServiceManager != null) {
            mMapServiceManager.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapServiceManager != null) {
            mMapServiceManager.onDestroy();
            mMapServiceManager = null;
        }
        mViewRoot = null;
    }

    private void startLocation() {
        final int distance = 3 * 1000;
        LocationService locationService = mMapServiceManager.getLocationService();
        locationService.start();
        locationService.setUserLocationListener(new LocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                UserManager.instance().setLocation(location);
                mParkingLotMap.show(location.getCityCode());
//                ParkingLotManager.instance().updateParkingLotByDistance(location.getLatitude(),
//                        location.getLongitude(), distance, distance / 3 * 2);
            }
        });
        mMapServiceManager.getControlService().perfomZoom(MapConstants.ZOOM_LOCATION);
    }

    private void playParkingLot() {
        final OverlayService.CategoryMarkerClickListener listener = new OverlayService.CategoryMarkerClickListener("parking_lot") {
            @Override
            public boolean onMarkerClick(Marker marker, Bundle info) {
                showParkingPopup(marker.getPosition(), info.getLong(MarkerManager.KEY_MARKER_LOT_ID));
                return true;
            }
        };
        mMarkerManager.setCategoryMarkerClickListener(listener);
        mMapServiceManager.getMapView().getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.e("cwf", "***onMapLoaded***");
                mParkingLotMap.init();
            }
        });
    }

    private void showParkingPopup(final LatLng position, final long lotId) {
        if (mParkingPopupWin == null) {
            mParkingPopupWin = createParkingPopup();
        }
        updatePopupContent(lotId);
        if (!mParkingPopupWin.isShowing()) {
            mParkingPopupWin.showAtLocation(mViewRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
            mParkingPopupWin.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View contentView = mParkingPopupWin.getContentView();
                    Object tag = contentView.getTag();
                    if (tag == null) {
                        return;
                    }
                    ParkingLot lot = (ParkingLot) tag;
                    Bundle bundle = new Bundle();
                    bundle.putString(ParkDetailFragment.KEY_PARKINGLOT_DATA, GsonUtil.createGson().toJson(lot));
                    Fragment fragment = new ParkDetailFragment();
                    if (fragment != null) {
                        FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
                        helper.addToBackStack(fragment.getClass().getSimpleName());
                        helper.setArguments(bundle);
                        activity.changeFragment(helper);
                    }
                    mParkingPopupWin.dismiss();
                }
            });
        }
    }

    private void updatePopupContent(final long lotId) {
        final View contentView = mParkingPopupWin.getContentView();
        contentView.setTag(null);
        ParkingLotTask task = new ParkingLotTask(getContext());
        task.setShowProgress(false);
        task.addParkingLot(lotId);
        task.setOnResultListener(new ParkingLotTask.OnResultListener() {
            @Override
            public void onResult(List<ParkingLot> lotList, List<String> failedIdList) {
                if (lotList == null || lotList.isEmpty()) {
                    return;
                }
                ParkingLot parkingLot = lotList.get(0);
                contentView.setTag(parkingLot);
                MarqueeTextView nameTv = (MarqueeTextView) contentView.findViewById(R.id.parking_name_tv);
                nameTv.setText(parkingLot.getName());
                TextView availableTv = (TextView) contentView.findViewById(R.id.parking_available_num_tv);
                availableTv.setText(String.valueOf(parkingLot.getParkingSpacesAvailable()));
                TextView totalTv = (TextView) contentView.findViewById(R.id.parking_total_num_tv);
                totalTv.setText(String.valueOf(parkingLot.getParkingSpaces()));
                TextView distanceTv = (TextView) contentView.findViewById(R.id.parking_distance_tv);
                LatLng p2 = new LatLng(parkingLot.getLatitude(), parkingLot.getLongitude());
                double distance = UserManager.instance().getDistance(p2);
                distanceTv.setText(formatDistance((long) distance));
            }
        });
        task.execute();
    }

    private PopupWindow createParkingPopup() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.item_parking_popup_menu, null);
        PopupWindow pw = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        return pw;
    }
    public static String formatDistance(long distance) {
        long k = distance / 1000;
        long m = distance % 1000;
        return (k > 0) ? (k + "千米" + m + "米") : (m + "米");
    }

}
