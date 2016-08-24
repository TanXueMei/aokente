package com.zhichen.parking.fragment.homepage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.tools.GsonUtil;
import com.zhichen.parking.widget.GlobalTitleLayout;

import java.io.File;

/**
 * Created by xuemei on 2016-06-12.
 */
public class ParkDetailFragment extends BaseFragment implements View.OnClickListener {

    public static final String KEY_PARKINGLOT_DATA = "lot_data";
    private View mViewRoot;
    ParkingLot mParkingLot;

    private TextView lotName, lotDistance, lotAddress, lotTotle, lotEmpty;
    private TextView  toGo;
    private GlobalTitleLayout globalTitleLayout;
    private ImageView back;

    public ParkDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String lotData = args.getString(KEY_PARKINGLOT_DATA);
        mParkingLot = GsonUtil.createGson().fromJson(lotData, ParkingLot.class);
        mViewRoot = inflater.inflate(R.layout.fragment_parkdetail, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        lotName = (TextView) mViewRoot.findViewById(R.id.tv_parklotName);
        lotDistance = (TextView) mViewRoot.findViewById(R.id.tv_deatil_distance);
        lotAddress = (TextView) mViewRoot.findViewById(R.id.tv_lotadress);
        lotTotle = (TextView) mViewRoot.findViewById(R.id.tv_detail_totle);
        lotEmpty = (TextView) mViewRoot.findViewById(R.id.tv_detail_empty);


        lotName.setText(mParkingLot.getName());
        lotDistance.setText(String.valueOf(FindParkingLotFragment.formatDistance((long) UserManager.instance().getDistance(
                mParkingLot.getPosition()))));
        lotAddress.setText(mParkingLot.getAddress());
        lotTotle.setText(String.valueOf("总车位："+mParkingLot.getParkingSpaces()));
        lotEmpty.setText(String.valueOf("空车位："+mParkingLot.getParkingSpacesAvailable()));

//        导航
        toGo = (TextView) mViewRoot.findViewById(R.id.tv_detail_go);
        toGo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_detail_go:
                if(isInstallByread("com.baidu.BaiduMap")){
                    startBaiduAppNavi();
                }else {
                    showDialog();
                }
                break;
        }

    }

    /**
     * 导航
     */
    private void startBaiduAppNavi() {
        LatLng startPos = null;
        String startName = "";
        BDLocation location = UserManager.instance().getLocation();
        if (location != null) {
            startPos = new LatLng(location.getLatitude(), location.getLongitude());
            startName = location.getAddrStr();
        } else {
            startPos = UserManager.instance().getMapTargetPos();
        }
        LatLng endPos = mParkingLot.getPosition();
        String endName = mParkingLot.getName();
        Log.d("xuemei","endName==="+endName);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption().startPoint(startPos).endPoint(endPos).startName(startName)
                .endName(endName);
        BaiduMapNavigation.openBaiduMapNavi(para, getContext());

    }
    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                OpenClientUtil.getLatestBaiduMapApp(getContext());
                Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent, 100);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            startBaiduAppNavi();
        }
    }
}
