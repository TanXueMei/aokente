package com.zhichen.parking.fragment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.fragment.FragmentCallback;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.tools.NetworkUtils;
import com.zhichen.parking.tools.TextProUitl;
import com.zhichen.parking.tools.ToolRegexValidate;
import com.zhichen.parking.widget.CountDownButton;
import com.zhichen.parking.widget.IconEditTextLayout;
import com.zhichen.parking.widget.SlidingUnderline;

/**
 * Created by xuemei on 2016-06-02.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private ImageView back;
    private FragmentCallback mCallback;
    private View mRootView;
    private SlidingUnderline mSlidingUnderline;
    private IconEditTextLayout mPhoneEdit;//输入手机号码
    private IconEditTextLayout mCarNumberEdit;//输入车牌号
    private IconEditTextLayout mAuthCodeEdit;//注册第二部中的输入验证码
    private CountDownButton countBtn;//获取验证码按钮

    public static final String AUTO_LOGIN = "true";
    boolean mFirstRun = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (FragmentCallback) activity;
    }

    public RegisterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_register, container, false);
        initUI();
        return mRootView;
    }

    private void initUI() {
        back = (ImageView) mRootView.findViewById(R.id.back);

        mSlidingUnderline = (SlidingUnderline) mRootView.findViewById(R.id.sliding_underline);
        mPhoneEdit = (IconEditTextLayout) mRootView.findViewById(R.id.register_phone_et);
        mCarNumberEdit = (IconEditTextLayout) mRootView.findViewById(R.id.register_carno_et);
        mAuthCodeEdit = (IconEditTextLayout) mRootView.findViewById(R.id.check_no_et);
        //获取验证码按钮
        View toFetchAuthBtn = mRootView.findViewById(R.id.register_fetchauth_btn);
        toFetchAuthBtn.setOnClickListener(this);

        //服务协议按钮
        View agreement = mRootView.findViewById(R.id.register_agreement_tv);
        agreement.setOnClickListener(this);
        //注册第二步，获取验证码按钮
        countBtn = (CountDownButton) mRootView.findViewById(R.id.count_down_btn);
        countBtn.setOnClickListener(this);
        //注册第二步中的“下一步”
        View toSetPassWordBtn = mRootView.findViewById(R.id.register_authcode_submit);
        toSetPassWordBtn.setOnClickListener(this);
        //注册第三步 "完成"
        View registerBtn = mRootView.findViewById(R.id.register_submit_btn);
        registerBtn.setOnClickListener(this);

        back.setOnClickListener(this);
    }

    private void showFrament(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_fragment_layput, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.back:
                LoginFragment fra = new LoginFragment();
                Bundle args = new Bundle();
                args.putBoolean(AUTO_LOGIN, mFirstRun);
                mFirstRun = false;
                fra.setArguments(args);
                showFrament(fra);
                break;
            case R.id.register_fetchauth_btn://进入注册第二步入口
                registerOne();
                break;
            case R.id.register_agreement_tv:
                fragment = new AgreementFragment();
                break;
            case R.id.count_down_btn://获取验证码
                countBtn.startCountDown(1000 * 60, 1000);
                new Thread() {
                    @Override
                    public void run() {
                        UserControler.getAuthCode(mPhoneEdit.getText());
                    }
                }.start();
                break;
            case R.id.register_authcode_submit:
                String authCode = mAuthCodeEdit.getText();
                if (authCode == null || authCode.trim().isEmpty()) {
                    Toast.makeText(getContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                showPart(2);
                break;
            case R.id.register_submit_btn:
                SubmitRegister();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }

    private void registerOne() {
        CheckBox check = (CheckBox) mRootView.findViewById(R.id.register_agree_check);
        if (!check.isChecked()) {
            Toast.makeText(getContext(), "你尚未同意“哒哒停车用户服务协议”", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = mPhoneEdit.getText();
//        phone.trim().length() != 11
        if (phone == null || !ToolRegexValidate.checkMobileNumber(phone)) {
            Toast.makeText(getContext(), "请正确填写手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String carNo = mCarNumberEdit.getText();
        if (carNo == null || carNo.trim().isEmpty()) {
            Toast.makeText(getContext(), "请填写车牌号", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView phoneTv = (TextView) mRootView.findViewById(R.id.phone_show_tv);
        phoneTv.setText(phone);
        showPart(1);
    }

    private void showPart(int pos) {
        View layout1 = mRootView.findViewById(R.id.fragment_register_1);
        View layout2 = mRootView.findViewById(R.id.fragment_register_2);
        View layout3 = mRootView.findViewById(R.id.fragment_register_3);
        switch (pos) {
            case 0:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
                break;
            case 1:
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                break;
            case 2:
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                break;
        }
        mSlidingUnderline.setPos(pos);
    }

    /**
     * 注册最后一步
     */
    private void SubmitRegister() {
        IconEditTextLayout passwordEdit = (IconEditTextLayout) mRootView.findViewById(R.id.fragment_register_3).findViewById(R.id.register_password_1_et);
        IconEditTextLayout surePasswordEdit = (IconEditTextLayout) mRootView.findViewById(R.id.fragment_register_3).findViewById(R.id.register_password_2_et);
        String result = TextProUitl.checkPasswordInfo(passwordEdit.getText(), surePasswordEdit.getText());
        if (result != null) {
            Toast.makeText(getContext(), "登录密码" + result, Toast.LENGTH_SHORT).show();
            return;
        }
        IconEditTextLayout payPasswordEdit = (IconEditTextLayout) mRootView
                .findViewById(R.id.fragment_register_3).findViewById(R.id.register_paypw_1_et);
        IconEditTextLayout paySurePasswordEdit = (IconEditTextLayout) mRootView
                .findViewById(R.id.fragment_register_3).findViewById(R.id.register_paypw_2_et);
        result = TextProUitl.checkPayPasswordInfo(payPasswordEdit.getText(),
                paySurePasswordEdit.getText());
        if (result != null) {
            Toast.makeText(getContext(), "支付密码" + result, Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.isNetworkAvailable(getContext())) {
            RegisterTask task = new RegisterTask(mPhoneEdit.getText(), mCarNumberEdit.getText(), passwordEdit
                    .getText(), payPasswordEdit.getText(), mAuthCodeEdit.getText());
            task.execute();
        } else {
            Toast.makeText(getContext(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    //注册
    private void Register(final String phone, final String carNumber, final String password, final String payPassword, final String authCode) {
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                logger.d("注册===成功"+"statusCode==="+statusCode+"response==="+response);
                UserManager.instance().setUserName(phone);
                UserManager.instance().setPassWord(password);
                setLoginPsw(phone,carNumber,password,payPassword,authCode);
            }
            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                logger.d("注册===失败"+"statusCode==="+statusCode+"response==="+errorResponse);
                if (errorResponse.contains("404")) {
                    Toast.makeText(getContext(), "此号码已经注册，请换其他号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (statusCode == 400) {
                    if (errorResponse.contains("406")) {
                        Toast.makeText(getContext(), "验证码无效或车牌号已被注册，请重新注册", Toast.LENGTH_SHORT).show();
                        showPart(0);
                    }
                    return;
                }
                Toast.makeText(getContext(), "注册失败，请重试", Toast.LENGTH_SHORT).show();
            }
        };
        UserControler.register(phone, carNumber, authCode, responseHandler);
    }

    MyLogger logger=MyLogger.getLogger(Constants.USERNAME);
    //设置登录密码
    private void setLoginPsw(final String phone, final String carNumber, final String password, final String payPassword, final String authCode) {
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                logger.d("注册时设置登录密码===成功"+"statusCode==="+statusCode+"response==="+response);
                User user = UserControler.getUserInfo(getContext(), phone, password);
                UserManager.instance().setUser(user);
                Toast.makeText(getContext(), "恭喜您，注册成功！", Toast.LENGTH_SHORT).show();
                mCallback.onCallback(RegisterFragment.this, LoginActivity.FLAG_HOMEPAGE);
            }
            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                Toast.makeText(getContext(), "设置登录密码失败", Toast.LENGTH_SHORT).show();
                logger.d("注册时设置登录密码===失败"+"statusCode==="+statusCode+"response==="+errorResponse);
            }
        };
        UserControler.setAndResetLoginPsw(phone, password, authCode, responseHandler);
    }

    private class RegisterTask extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog mProgressDialog;
        private String phone;
        private String carNumber;
        private String password;
        private String payPassword;
        private String authCode;

        public RegisterTask(String phone, String carNumber, String password, String payPassword, String authCode) {
            this.phone = phone;
            this.carNumber = carNumber;
            this.password = password;
            this.payPassword = payPassword;
            this.authCode = authCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), RegisterTask.this, "努力连接中，请稍后。。。");
        }

        /**
         * 支付密码提交？？？
         */
        @Override
        protected Void doInBackground(Void... params) {
            Register(phone, carNumber, password, payPassword, authCode);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }


}
