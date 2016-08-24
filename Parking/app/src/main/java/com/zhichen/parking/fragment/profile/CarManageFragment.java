package com.zhichen.parking.fragment.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.model.User.Vehicle;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.litesuits.http.response.Response;
import com.zhichen.parking.library.damajia.swipe.SwipeLayout;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.util.DensityUtil;

import java.util.List;

/**
 * Created by xuemei on 2016-05-30.
 * 车辆管理
 */
public class CarManageFragment extends Fragment implements View.OnClickListener {
    View mRootView;
    LinearLayout mCarManagerLayout;
    View newCar;

    public CarManageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_car_manage, container, false);
        initUI();
        return mRootView;
    }

    private void initUI() {
        mCarManagerLayout = (LinearLayout) mRootView.findViewById(R.id.car_no_manager_layout);
        List<Vehicle> carList = UserManager.instance().getVehicleList();
        if (carList != null) {
            for (Vehicle vehicle : carList) {
                //添加新的车牌号
                addNewCarLayout(vehicle);
            }
        }
        newCar = mRootView.findViewById(R.id.car_no_add_layout);
//        mCarManagerLayout.setOnClickListener(this);
        newCar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_no_add_layout:
                Log.e("tan","添加");
                View modifyView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext, null);
                final EditText et = (EditText) modifyView.findViewById(R.id.dialog_modify_et);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Dialog.BUTTON_POSITIVE) {
                            String newCar = et.getText().toString();
                            doAddCarNumber(newCar);
                        }
                    }
                };
                Dialog dialog = DialogHelper.createModifyDialog(getContext(), modifyView, "新增车牌号", "取消", "确定", listener);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }
    /**
     * 请求添加车牌号列表
     */
    private void doAddCarNumber(final String newCar) {
        if (newCar == null || newCar.trim().isEmpty()) {
            return;
        }
        if (UserManager.instance().hadCarNumber(newCar)) {
            Toast.makeText(getContext(), "列表已存在此号码，不能重复添加", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.d("cwf", "doAddCarNumber  response" + response);
                Vehicle vehicle = UserManager.instance().addNewCarNumber(newCar);
                addNewCarLayout(vehicle);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                Toast.makeText(getContext(), "添加失败，请重试", Toast.LENGTH_SHORT).show();
            }
        };
        UserControler.addCarNumber(getContext(), newCar, responseHandler);
    }

    /**
     * 请求添加新的车牌号
     */
    private void addNewCarLayout(Vehicle vehicle) {
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 100));
        final View carlayout = LayoutInflater.from(getContext()).inflate(R.layout.item_car_number, null);
        SwipeLayout swipeLayout = (SwipeLayout) carlayout.findViewById(R.id.car_swipelayout);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//		swipeLayout.addDrag(SwipeLayout.DragEdge.Left, carlayout.findViewById(R.id.bottom_wrapper));

        carlayout.findViewById(R.id.car_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                删除车牌号
                doRemoveCarNumber(carlayout);
//                deleteCarNumber(carlayout);
            }
        });

        final TextView tv = (TextView) carlayout.findViewById(R.id.car_no_show_tv);
        tv.setText(vehicle.getPlate_number());
        //修改车牌号
        carlayout.setOnClickListener(createModifyListener(carlayout));
        carlayout.setTag(vehicle);
        mCarManagerLayout.addView(carlayout, mCarManagerLayout.getChildCount() - 1, params);
    }

    private View.OnClickListener createModifyListener(final View carLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tv = (TextView) carLayout.findViewById(R.id.car_no_show_tv);
                View modifyView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edittext, null);
                final EditText et = (EditText) modifyView.findViewById(R.id.dialog_modify_et);
                et.setText(tv.getText());
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Dialog.BUTTON_POSITIVE) {
                            final Vehicle vehicle = (Vehicle) carLayout.getTag();
                            final String newCar = et.getText().toString();
                            if (newCar == null || newCar.trim().isEmpty()) {
                                doRemoveCarNumber(carLayout);
//                                deleteCarNumber(carLayout);
                            } else {
                                final AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, String response) {
                                        Vehicle newVehicle = UserManager.instance().addNewCarNumber(newCar);
                                        UserManager.instance().removeCar(vehicle);
                                        carLayout.setTag(newVehicle);
                                        tv.setText(newCar);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, String errorResponse, Throwable e) {
                                        Toast.makeText(getContext(), "修改失败，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                };
                                UserControler.modifyCarNumber(vehicle.getPlate_number(), newCar, responseHandler);
                            }
                        }
                    }
                };
                Dialog dialog = DialogHelper.createModifyDialog(getContext(), modifyView, "车牌号修改", "取消", "确定", listener);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        };
    }

    /**
     * 请求删除车牌号
     */
    private void doRemoveCarNumber(final View carLayout) {
        if (mCarManagerLayout.getChildCount() == 2) {
            Toast.makeText(getContext(), "请至少保留一个号码，不能删除", Toast.LENGTH_SHORT).show();
            return;
        }
        final Vehicle vehicle = (Vehicle) carLayout.getTag();
        final AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                mCarManagerLayout.removeView(carLayout);
                UserManager.instance().removeCar(vehicle);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                Toast.makeText(getContext(), "删除失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnd(Response<String> response) {
                super.onEnd(response);
            }

        };
        UserControler.deleteCarNumber(getContext(), vehicle.getPlate_number(), responseHandler);
    }
}
