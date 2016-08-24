package com.zhichen.parking.fragment.homepage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhichen.parking.R;
import com.zhichen.parking.fragment.homepage.BaseRechargeFragment;
import com.zhichen.parking.fragment.homepage.PayFragment;
import com.zhichen.parking.lib.pay.PayResultListener;
import com.zhichen.parking.lib.pay.yilian.YinlianPayTask;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.YinlianPay;
import com.zhichen.parking.model.ZhifubaoPay;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.widget.GlobalTitleLayout;
import com.zhichen.parking.wxapi.WXPayEntryActivity;

/**
 * Created by xuemei on 2016-05-30.
 * 账户充值
 */
public class RechargeFragment extends BaseRechargeFragment implements View.OnClickListener, PayResultListener {
    private RelativeLayout rlAlipay, rlWechat, rlBank;

    private EditText rechargeNumber;


    public RechargeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recharge, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        rechargeNumber = (EditText) view.findViewById(R.id.recharge_custom_money_et);
        rlAlipay = (RelativeLayout) view.findViewById(R.id.rl_alipay);
        rlWechat = (RelativeLayout) view.findViewById(R.id.rl_wechat);
        rlBank = (RelativeLayout) view.findViewById(R.id.rl_bank);



        rlAlipay.setOnClickListener(this);
        rlWechat.setOnClickListener(this);
        rlBank.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        int money;
        String trim;
        switch (v.getId()) {
            case R.id.rl_alipay:
                trim = rechargeNumber.getText().toString().trim();
                if(trim.equals("")) {
                    Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                }else {
                    money = (int) (Float.valueOf(trim) * 100);
                    try {
                        if (money > 0) {
                            payZhifubao(money);
                        }
                    } catch (NumberFormatException e) {
//                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.rl_wechat:
                trim = rechargeNumber.getText().toString().trim();
                if(!trim.equals("")) {
                    money = (int) (Float.valueOf(trim) * 100);
                    try {
                        if (money > 0) {
                            payWeixin(money);
                        }
                    } catch (NumberFormatException e) {
//                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_bank:
                trim = rechargeNumber.getText().toString().trim();
                if(!trim.equals("")) {
                    money = (int) (Float.valueOf(trim) * 100);
                    try {
                        if (money > 0) {
                            payYinlian(money);
                        }
                    } catch (NumberFormatException e) {
//                        Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }


}
