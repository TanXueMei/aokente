package com.zhichen.parking.fragment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.activity.MainActivity;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.fragment.FragmentCallback;
import com.zhichen.parking.lotmanager.ParkingLotManager;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.tools.NetworkUtils;
import com.zhichen.parking.tools.ToolRegexValidate;
import com.zhichen.parking.widget.IconEditTextLayout;

/**
 * Created by xuemei on 2016-06-02.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {


    private FragmentCallback mCallback;
    private View mRootView;
    private IconEditTextLayout mUserNameEdit;
    private IconEditTextLayout mPasswordEdit;
    private ImageView back;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (FragmentCallback) activity;
    }

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        initUI();
        return mRootView;
    }

    private void initUI() {
        back = (ImageView) mRootView.findViewById(R.id.back);
        View resister = mRootView.findViewById(R.id.tv_go_register);
        View forgetPsw = mRootView.findViewById(R.id.tv_forget_password);
        View login = mRootView.findViewById(R.id.btn_login_submit);
        back.setOnClickListener(this);
        resister.setOnClickListener(this);
        forgetPsw.setOnClickListener(this);
        login.setOnClickListener(this);

        mUserNameEdit = (IconEditTextLayout) mRootView.findViewById(R.id.login_username_edit);
        mPasswordEdit = (IconEditTextLayout) mRootView.findViewById(R.id.login_password_edit);
        UserManager userManager = UserManager.instance();
        mUserNameEdit.setText(userManager.getUserName());
        mPasswordEdit.setText(userManager.getPassWord());

    }

    @Override
    public void onClick(View v) {
        if (mCallback == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.tv_go_register://注册
                mCallback.onCallback(LoginFragment.this, LoginActivity.FREGMENT_REGISTER);
                break;
            case R.id.tv_forget_password://忘记密码
                mCallback.onCallback(LoginFragment.this, LoginActivity.FREGMENT_FORGET);
                break;
            case R.id.btn_login_submit://立即登录
                sumitLogin();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UserManager.instance().setUserName(mUserNameEdit.getText());
        UserManager.instance().setPassWord(mPasswordEdit.getText());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sumitLogin() {
        String error = checkLoginInfo();
        if (error != null) {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            return;
        }
        doLogin();
    }

    /**
     * @return error 若为null，则表示输入正确，否则为错误信息
     */
    private String checkLoginInfo() {
        String error = null;
        String name = mUserNameEdit.getText();
        if (name == null || name.trim().isEmpty()) {
            error = "用户名不能为空";
            return error;
        } else {
            if (!ToolRegexValidate.checkMobileNumber(name)) {
                error = "输入的号码有误！";
                return error;
            }
        }
        String password = mPasswordEdit.getText();
        if (password == null || password.trim().isEmpty()) {
            error = "密码不能为空";
            return error;
        } else {
            if (!ToolRegexValidate.checkPsww(password)) {
                error = "输入密码格式不对";
                return error;
            }
        }
        return null;
    }

    //    请求立即登录
    private void doLogin() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            LoginTask task = new LoginTask(mUserNameEdit.getText(), mPasswordEdit.getText());
            task.execute();
        } else {
            Toast.makeText(getActivity(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }


    private class LoginTask extends AsyncTask<Void, Integer, String> {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        private ProgressDialog mProgressDialog;
        private String name;
        private String password;

        public LoginTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = DialogHelper.showProgressDialog(getContext(), LoginTask.this, "登陆中，请稍后。。。");
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            logins(getContext(), name, password);
            User user = UserControler.getUserInfo(getContext(), name, password);
            if (user != null) {
                UserManager.instance().setUser(user);
                if (mCallback != null) {
                    mCallback.onCallback(LoginFragment.this, LoginActivity.FREGMENT_LOGIN_DONE);
                }
                result="sucess";
            }else {
                result=null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if(result==null){
                Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @return String 失败返回null
     */
    public static String login(Context context, String userName, String password) {
        String result = UserControler.login(context, userName, password);
        Log.e("xue", "登录返回==" + result);
        if (result != null) {
            UserManager.instance().setUserName(userName);
            UserManager.instance().setPassWord(password);
            ParkingLotManager.instance().updateParkingLotByAll();
            User user = UserControler.getUserInfo(context, userName, password);
            if (user != null) {
                UserManager.instance().setUser(user);
            } else {
                result = null;
            }
        }
        return result;
    }

    public void logins(final Context context, final String userName, final String password) {

        final MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                logger.d("login onSuccess===" + "statusCode:"+statusCode+"response="+response);
                UserManager.instance().setUserName(userName);
                UserManager.instance().setPassWord(password);
                ParkingLotManager.instance().updateParkingLotByAll();

            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                logger.e("login onFailure===" + "statusCode:"+statusCode+"errorResponse="+errorResponse);
                if(statusCode==400){
                    Toast.makeText(getContext(), "账号或者密码错误，请检查", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();
                }


            }
        };
        UserControler.login(context, userName, password, responseHandler);
    }
}
