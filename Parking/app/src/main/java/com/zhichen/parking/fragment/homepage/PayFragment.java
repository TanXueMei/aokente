package com.zhichen.parking.fragment.homepage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.library.dialogplus.DialogPlus;
import com.zhichen.parking.library.dialogplus.ViewHolder;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.Billing;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.GsonUtil;
import com.zhichen.parking.widget.GlobalTitleLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xuemei on 2016-06-01.
 * 停车缴费
 */
public class PayFragment extends PayCommonFragment implements View.OnClickListener {

    View mViewRoot;
    private GlobalTitleLayout globalTitleLayout;
    private Button bt_change;
    //当前车辆
    private TextView mCarNumber;


    public PayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("gggggg", "onCreateView");
        mViewRoot = inflater.inflate(R.layout.fragment_stop_topay, container, false);
        initUI();
        return mViewRoot;
    }


    @Override
    public void onResume() {
        super.onResume();
        initMoney();

    }

    protected void initMoney() {
        super.initMoney();
    }

    protected void initUI() {
        super.initUI(mViewRoot);
        Button btnRechrge = (Button) mViewRoot.findViewById(R.id.money_recharge_btn);//充值
        globalTitleLayout = (GlobalTitleLayout) mViewRoot.findViewById(R.id.pay_title);

        TextView payother = (TextView) globalTitleLayout.findViewById(R.id.tv_payother);//代缴
        View change_carunmber_layout = mViewRoot.findViewById(R.id.pay_fragment_1_layout);
        bt_change = (Button) change_carunmber_layout.findViewById(R.id.pay_car_change_btn);//修改
        //当前车辆
        mCarNumber = (TextView) mViewRoot.findViewById(R.id.pay_fragment_1_layout).findViewById(R.id.pay_car_no_tv);
        List<User.Vehicle> vehicleList = UserManager.instance().getVehicleList();
        if (vehicleList != null && !vehicleList.isEmpty()) {
            mCarNumber.setText(vehicleList.get(0).getPlate_number());
        }
        //刷新账单信息
        updateBilling(mCarNumber.getText().toString());

        payother.setVisibility(View.INVISIBLE);
        payother.setOnClickListener(this);
        btnRechrge.setOnClickListener(this);
        bt_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.money_recharge_btn://充值
                fragment = new RechargeFragment();
                break;
            case R.id.tv_payother://代缴
                fragment = new PayOtherFragment();
                break;
            case R.id.pay_car_change_btn://修改
                initSpinner();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }

    //修改
    private void initSpinner() {
        Button btn = (Button) mViewRoot.findViewById(R.id.pay_fragment_1_layout).findViewById(R.id.pay_car_change_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager um = UserManager.instance();
                final int size = um.getVehicleList() == null ? 0 : um.getVehicleList().size();
                final String[] mItems = new String[size];
                for (int i = 0; i < size; i++) {
                    mItems[i] = um.getVehicleList().get(i).getPlate_number();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, mItems);
                ListView lv = new ListView(getContext());
                lv.setAdapter(adapter);
                ViewHolder holder = new ViewHolder(lv);
                final DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setContentHolder(holder)
                        .setGravity(Gravity.CENTER)
                        .setExpanded(false)
                        .setCancelable(true)
                        .setBackgroundColorResourceId(R.color.white)
                        .create();
                dialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        mCarNumber.setText(mItems[position]);
                        updateBilling(mItems[position]);
                    }
                });
            }
        });
    }

    //请求获取账单信息
    private void updateBilling(String carNumber) {
        BillingTask task = new BillingTask(carNumber);
        task.execute();
    }

    private class BillingTask extends AsyncTask<Void, Integer, String> {
        private String carNumber;
        private Billing billing;
        ProgressDialog mProgressDialog;

        public BillingTask(String carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "获取缴费单中，请稍后。。。");
        }

        @Override
        protected String doInBackground(Void... params) {
            UserManager um = UserManager.instance();
            String billingResult = PayControler.getBillingResult(getContext(), um.getUserName(), um.getPassWord(), carNumber);
            if (billingResult != null) {
                try {
                    JSONObject json = new JSONObject(billingResult);
                    if (!json.isNull("detail")) {
                        String content = json.getString("detail");
                        if (content.contains("Bill has been paid")) {
                            return "此车牌账单已支付！";
                        }
                    } else {
                        billing = GsonUtil.createGson().fromJson(billingResult, Billing.class);
                        if (billing == null) {
                            return "获取账单失败，请再次尝试";
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (billingResult == null) {
                return "没有此账单";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
            //显示账单信息
            setBilling(billing);
        }
    }

}
