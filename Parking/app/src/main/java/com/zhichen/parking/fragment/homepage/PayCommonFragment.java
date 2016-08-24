package com.zhichen.parking.fragment.homepage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.activity.MainActivity;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.Billing;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.util.CalendarUtil;
import com.zhichen.parking.util.DateUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuemei on 2016-07-07.
 */
public class PayCommonFragment extends BaseFragment {
    MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
    protected View mRootView;
    protected RadioButton mMoneyPackRadio;
    private Billing mBilling;

    String startTime;
    String jieshuTime;
    /**
     * 账单信息
     */
    private TextView mParkingTypeTv;
    private TextView mParkStartTimeTv;
    private TextView mParkEndTimeTv;
    private TextView mParkDurationTv;
    private TextView mParkMoneyTv;
    private TextView mPayTotalTv;
    private Button mPaySubmitBtn;

    private TextView mCartPlot;
    private DecimalFormat df;
    private SimpleDateFormat format;

    protected void initMoney() {
        updateMoneyPack();
    }

    protected void initUI(View mRootView) {
        mMoneyPackRadio = (RadioButton) mRootView.findViewById(R.id.pay_common_layout).findViewById(R.id.pay_account_radio);
        // 设置余额
        setMoneyPack();
        mCartPlot = (TextView) mRootView.findViewById(R.id.layout_current_parklot).findViewById(R.id.pay_current_lot_name_tv);
        mParkingTypeTv = (TextView) mRootView.findViewById(R.id.pay_parking_type_tv);
        mParkStartTimeTv = (TextView) mRootView.findViewById(R.id.pay_park_start_time_tv);
        mParkEndTimeTv = (TextView) mRootView.findViewById(R.id.pay_park_end_time_tv);
        mParkDurationTv = (TextView) mRootView.findViewById(R.id.pay_park_time_len_tv);
        mParkMoneyTv = (TextView) mRootView.findViewById(R.id.pay_park_money_tv);
        mPayTotalTv = (TextView) mRootView.findViewById(R.id.pay_total_money_tv);
        mPaySubmitBtn = (Button) mRootView.findViewById(R.id.pay_submit_btn);
        //立即支付
        mPaySubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float accountBalance = UserManager.instance().getUser().getAccountBalance();
                int havabalance = (int) accountBalance;
                String paytotle1 = mPayTotalTv.getText().toString().trim();
                String paytotle2 = paytotle1.substring(0, paytotle1.indexOf("."));
                int payMoney = Integer.parseInt(paytotle2);
                int money = havabalance - payMoney;
                if (havabalance <= 0 && money <= 0) {
                    Toast.makeText(getContext(), "余额不足，请充值", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new RechargeFragment();
                    if (fragment != null) {
                        FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
                        helper.addToBackStack(fragment.getClass().getSimpleName());
                        activity.changeFragment(helper);
                    }

                } else {
                    PayTask task = new PayTask();
                    task.execute();
                }
            }
        });
//        充值
        View rechargeBtn = mRootView.findViewById(R.id.money_recharge_btn);
        rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RechargeFragment();
                if (fragment != null) {
                    FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
                    helper.addToBackStack(fragment.getClass().getSimpleName());
                    activity.changeFragment(helper);
                }
            }
        });
    }

    /**
     * 账户余额
     */
    private void setMoneyPack() {
        if (mMoneyPackRadio == null) {
            return;
        }
        logger.d("余额：===" + UserManager.instance().getUser().getAccountBalance());
        mMoneyPackRadio.setText("账户余额（余额：" + UserManager.instance().getUser().getAccountBalance() + "元）");
        mMoneyPackRadio.setEnabled(true);
    }

    //设置账单信息
    protected void setBilling(Billing billing) {
        df = new DecimalFormat("######0.00");
        mBilling = billing;
        mPaySubmitBtn.setEnabled(mBilling != null);
        if (mBilling == null) {
            mParkingTypeTv.setText("");
            mParkStartTimeTv.setText("");
            mParkEndTimeTv.setText("");
            mParkDurationTv.setText("");
            mParkMoneyTv.setText("");
            mPayTotalTv.setText("0.00");
        } else {
            mCartPlot.setText(mBilling.getParking_lot());
            mParkingTypeTv.setText("封闭式停车场");
            startTime = mBilling.getParking_time();
            if (mBilling != null && mBilling.getBilling_time() != null) {
                Log.d("cwf", "结束时间=====" + mBilling.getBilling_time());
                jieshuTime = mBilling.getBilling_time().replace("Z", " UTC");
                try {
                    if (jieshuTime.length() == 27) {
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                        Date jieshudate = format.parse(jieshuTime);
                        mParkEndTimeTv.setText(DateUtil.dateToString(jieshudate));
                    } else if (jieshuTime.length() == 23) {
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
                        Date jieshudate = format.parse(jieshuTime);
                        mParkEndTimeTv.setText(DateUtil.dateToString(jieshudate));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                mParkEndTimeTv.setText("");
            }
            mParkDurationTv.setText(CalendarUtil.formatDuration(mBilling.getCharged_duration()));
            mParkStartTimeTv.setText(mBilling.getParking_time());
            mParkDurationTv.setText(CalendarUtil.formatDuration(mBilling.getCharged_duration()));
            mParkMoneyTv.setText(df.format(Double.parseDouble(String.valueOf(mBilling.getAmount())) / 100));
            mPayTotalTv.setText(df.format(Double.parseDouble(String.valueOf(mBilling.getAmount())) / 100));
        }
    }

    /**
     * 支付异步任务
     */
    private class PayTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            UserManager um = UserManager.instance();
            String pay = PayControler.pay(getContext(), um.getUserName(), um.getPassWord(), mBilling.getOut_trade_no());
            return pay;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), (result == null) ? "支付失败，请重试" : "支付成功", Toast.LENGTH_SHORT).show();
            if (result != null) {
//                跳转到支付完成界面
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    //    支付成功之后刷新余额数据  重新请求
    private void updateMoneyPack() {
        String name = UserManager.instance().getUserName();
        String password = UserManager.instance().getPassWord();
        if (name != null && password != null) {
            UpdateMoneyTask task = new UpdateMoneyTask(name, password);
            task.execute();
        }
    }

    ProgressDialog mProgressDialog;
    private class UpdateMoneyTask extends AsyncTask<Void, Integer, User> {
        private String name;
        private String password;

        public UpdateMoneyTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "正在刷新余额，请稍后。。。");
        }

        @Override
        protected User doInBackground(Void... params) {
            User user = UserControler.getUserInfo(getContext(), name, password);
            if (user != null) {
                UserManager.instance().setUser(user);
                return user;
            }
            return null;
        }

        @Override
        protected void onPostExecute(User s) {
            super.onPostExecute(s);
//            if (mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
            setMoneyPack();
        }
    }
}
