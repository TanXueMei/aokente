package com.zhichen.parking.fragment.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.fragment.FragmentCallback;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.ServerManger;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.NetworkUtils;
import com.zhichen.parking.tools.TextProUitl;
import com.zhichen.parking.widget.CountDownButton;
import com.zhichen.parking.widget.IconEditTextLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuemei on 2016-06-02.
 */
public class ForgetpasswordFragment extends BaseFragment implements View.OnClickListener {
    private FragmentCallback mCallback;
    private ImageView back;
    private View mRootView;
    private CountDownButton mCountDownButton;
    private IconEditTextLayout mFetchPhoneEdit;
    private IconEditTextLayout mAuthCodeEdit;
    private IconEditTextLayout mPasswordEdit;
    private IconEditTextLayout mPasswordSureEdit;
    private View mFetchAuthLayout;
    private View mResetLayout;

    public static final String AUTO_LOGIN = "true";
    boolean mFirstRun = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentCallback) context;
    }

    public ForgetpasswordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_forgetpsw, container, false);
        initUI();
        return mRootView;
    }

    private void initUI() {
        back = (ImageView) mRootView.findViewById(R.id.back);
        //忘记密码，操作的第一个界面
        mFetchAuthLayout = mRootView.findViewById(R.id.fetch_auth_layout);
        mCountDownButton = (CountDownButton) mRootView.findViewById(R.id.fetch_authcode_btn);
        mFetchPhoneEdit = (IconEditTextLayout) mRootView.findViewById(R.id.fetch_phone_edit);
        mAuthCodeEdit = (IconEditTextLayout) mRootView.findViewById(R.id.fetch_authcode_edit);
        View nextBtn = mRootView.findViewById(R.id.fetch_auth_submit);
        //忘记密码，操作的第二个界面
        mResetLayout = mRootView.findViewById(R.id.fetch_reset_layout);
        mPasswordEdit = (IconEditTextLayout) mRootView.findViewById(R.id.register_password_1_et);
        mPasswordSureEdit = (IconEditTextLayout) mRootView.findViewById(R.id.register_password_2_et);
        View sumitBtn = mRootView.findViewById(R.id.register_submit_btn);

        back.setOnClickListener(this);
        mCountDownButton.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        sumitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                LoginFragment fra = new LoginFragment();
                Bundle args = new Bundle();
                args.putBoolean(AUTO_LOGIN, mFirstRun);
                mFirstRun = false;
                fra.setArguments(args);
                showFrament(fra);
                break;
            case R.id.fetch_authcode_btn://获取验证码
                if (NetworkUtils.isNetworkAvailable(getContext())) {
                    fetchAuthCode();
                } else {
                    Toast.makeText(getContext(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fetch_auth_submit://下一步
                nextBtn();
                break;
            case R.id.register_submit_btn://完成
                resetPsw();
                break;
        }
    }

    private void showFrament(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_fragment_layput, fragment);
        transaction.commit();
    }

    /**
     * 获取验证码
     */
    private void fetchAuthCode() {
        final String phone = mFetchPhoneEdit.getText();
        if (phone == null || phone.trim().length() != 11) {
            Toast.makeText(getContext(), "请输入正确的号码", Toast.LENGTH_SHORT).show();
            return;
        }
        mCountDownButton.startCountDown(1000 * 60, 1000);
        new Thread() {
            public void run() {
                UserControler.getAuthCode(phone.trim());
            }
        }.start();
    }

    /**
     * 下一步
     */
    private void nextBtn() {
        String authCode = mAuthCodeEdit.getText();
        if (authCode == null || authCode.trim().isEmpty()) {
            Toast.makeText(getContext(), "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        mResetLayout.setVisibility(View.VISIBLE);
        mFetchAuthLayout.setVisibility(View.GONE);
    }

    /**
     * 重置密码
     */
    private void resetPsw() {
        String error = TextProUitl.checkPasswordInfo(mPasswordEdit.getText(), mPasswordSureEdit.getText());
        if (error != null) {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        } else {
            setLoginPsw(mFetchPhoneEdit.getText(), mPasswordEdit.getText(), mAuthCodeEdit.getText());

        }
    }

    //设置登录密码
    private void setLoginPsw(final String phone, final String password, final String authCode) {
        ServerManger.AsyncResponseHandler responseHandler = new ServerManger.AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Toast.makeText(getContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
                UserManager.instance().setUserName(phone);
                UserManager.instance().setPassWord(null);
                mCallback.onCallback(ForgetpasswordFragment.this, LoginActivity.FREGMENT_FORGET_DONE);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                Log.e("xuemei","eeror==="+errorResponse);
                Log.e("xuemei","statusCode==="+errorResponse);
                if(errorResponse.contains("404")){
                    Toast.makeText(getContext(), "您还未注册，请先注册", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "重置密码失败", Toast.LENGTH_SHORT).show();
                }

            }
        };
        UserControler.setAndResetLoginPsw(phone, password, authCode, responseHandler);
    }


}
