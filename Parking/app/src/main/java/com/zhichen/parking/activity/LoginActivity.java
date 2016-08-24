package com.zhichen.parking.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.FragmentCallback;
import com.zhichen.parking.fragment.profile.ForgetpasswordFragment;
import com.zhichen.parking.fragment.profile.LoginFragment;
import com.zhichen.parking.fragment.profile.RegisterFragment;
import com.zhichen.parking.fragment.record.RecordFragment;
import com.zhichen.parking.tools.NetworkUtils;

public class LoginActivity extends FragmentActivity implements FragmentCallback {
    public static final String FRAGMENT_ARGS = "fragment_args";
    public static final String AUTO_LOGIN = "true";

    public static final int FREGMENT_CANCEL = 1;//Fragment取消
    public static final int FREGMENT_REGISTER = 2;//Fragment注册
    public static final int FREGMENT_FORGET = 3;//Fragment忘记密码
    public static final int FREGMENT_REGISTER_DONE = 4;
    public static final int FREGMENT_FORGET_DONE = 5;
    public static final int FREGMENT_LOGIN_DONE = 6;

    public static final int FLAG_HOMEPAGE = 7;
    public static final int FLAG_RECORD = 8;
    public static final int FLAG_AROUND = 9;
    public static final int FLAG_PROFILE = 10;

    boolean mFirstRun = true;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        flag=getIntent().getStringExtra("frag");

        toLoginFragment();
    }
    /**
     * 登录
     */
    private void toLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AUTO_LOGIN, mFirstRun);
        loginFragment.setArguments(bundle);
        showFragment(loginFragment);
    }

    /**
     * 注册
     */
    private void toRegisterFragment() {
        showFragment(new RegisterFragment());
    }

    /**
     * 取回密码
     */
    private void toFetchPswFragment() {
        showFragment(new ForgetpasswordFragment());
    }

    /**
     * 记录
     */
    private void toRecordFragment() {
        showFragment(new RecordFragment());
    }

    /**
     * 周边
     */
    private void toAroundFragment() {
        showFragment(new RecordFragment());
    }

    /**
     * 我的
     */
    private void toProfileFragment() {
        showFragment(new RecordFragment());
    }

    /**
     * 首页
     */
    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login",flag);
        startActivity(intent);
        LoginActivity.this.finish();

    }

    /**
     * 注册成功后回首页

     */
    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login","HomePageFragment");
        startActivity(intent);
        LoginActivity.this.finish();

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_fragment_layput, fragment);
        transaction.commit();
    }

    /**
     * Fragment回调函数
     *
     * @param fragment
     * @param status
     */
    @Override
    public void onCallback(Fragment fragment, int status) {
        switch (status) {
            case FREGMENT_CANCEL://登录
                toLoginFragment();
                break;
            case FREGMENT_REGISTER://注册
                toRegisterFragment();
                break;
            case FREGMENT_FORGET://忘记密码
                toFetchPswFragment();
                break;
            case FREGMENT_REGISTER_DONE://回首页
                toMainActivity();
                break;
            case FREGMENT_FORGET_DONE://去登录
                toLoginFragment();
                break;
            case FREGMENT_LOGIN_DONE://登录成功后回首页
                toMainActivity();
                break;
            case FLAG_HOMEPAGE://注册成功后回到首页
                goMainActivity();
                break;
            case FLAG_RECORD://回记录
                toRecordFragment();
                break;
            case FLAG_AROUND://回周边
                toAroundFragment();
                break;
            case FLAG_PROFILE://回我的
                toProfileFragment();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        toMainActivity();
        this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        return false;
    }
}
