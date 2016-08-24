package com.zhichen.parking.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.tools.TextProUitl;
import com.zhichen.parking.widget.CountDownButton;
import com.zhichen.parking.widget.IconEditTextLayout;

/**
 * Created by xuemei on 2016-06-03.
 * 找回支付密码
 */
public class FindPayPswFragment extends BaseFragment implements View.OnClickListener {
    View mRootView;
    private String mAuthCode;
    private MyLogger logger=MyLogger.getLogger(Constants.USERNAME);
    public FindPayPswFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_fetch_paypsw, container, false);
        initUI();
        return mRootView;
    }

    CountDownButton countBtn;
    IconEditTextLayout et;
    View layout1;
    View layout2;
    IconEditTextLayout password_1_et;
    IconEditTextLayout password_2_et;

    private void initUI() {
        //第一步
        TextView tishi = (TextView) mRootView.findViewById(R.id.mi_ma_tishi);
//        tishi.setText(getContext().getString(R.string.pswLimit));
        countBtn = (CountDownButton) mRootView.findViewById(R.id.count_down_btn);
        countBtn.setOnClickListener(this);
        //第二步
        et = (IconEditTextLayout) mRootView.findViewById(R.id.fetch_authcode_edit);
        layout1 = mRootView.findViewById(R.id.fetch_pay_1_layout);
        layout2 = mRootView.findViewById(R.id.fetch_pay_2_layout);
        View nextbtn = mRootView.findViewById(R.id.fetch_pay_next_btn);
        nextbtn.setOnClickListener(this);
        //最后一步
        password_1_et = (IconEditTextLayout) mRootView.findViewById(R.id.fetch_pay_password_1_et);
        password_2_et = (IconEditTextLayout) mRootView.findViewById(R.id.fetch_pay_password_2_et);
        View submitBtn = mRootView.findViewById(R.id.fetch_pay_submit_btn);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_down_btn:
                getAuthCode();
                break;
            case R.id.fetch_pay_next_btn:
                nextBtn();
                break;
            case R.id.fetch_pay_submit_btn:
                finishFindPsw();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
        }
    }

    //获取验证码
    private void getAuthCode() {
        countBtn.startCountDown(1000 * 60, 1000);
        new Thread() {
            public void run() {
                UserControler.getAuthCode(UserManager.instance().getPhoneNumber());
            }
        }.start();
    }

    //下一步
    private void nextBtn() {
        final String code = et.getText();
        if (code == null || code.trim().isEmpty()) {
            Toast.makeText(getContext(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuthCode = code.trim();
        layout2.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);
    }

    //完成
    private void finishFindPsw() {
        String result = TextProUitl.checkPayPasswordInfo(password_1_et.getText(), password_2_et.getText());
        if (result != null) {
            Toast.makeText(getContext(), "密码" + result, Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();

            }
            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                logger.e("onFailure  statusCode==="+statusCode+"errorResponse==="+errorResponse);
                Toast.makeText(getContext(), "修改失败，请重试", Toast.LENGTH_SHORT).show();
            }
        };
        String phone = UserManager.instance().getPhoneNumber();
        logger.d("verification_code==="+mAuthCode);
        logger.d("password==="+ password_1_et.getText());
        UserControler.setPayPassword(password_1_et.getText(), mAuthCode, responseHandler);
    }

}
