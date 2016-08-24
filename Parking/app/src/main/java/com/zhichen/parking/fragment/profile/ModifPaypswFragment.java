package com.zhichen.parking.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.tools.TextProUitl;
import com.zhichen.parking.widget.IconEditTextLayout;

/**
 * Created by xuemei on 2016-06-03.
 * 修改支付密码
 */
public class ModifPaypswFragment extends BaseFragment {
    View mRootView;
    MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
    public ModifPaypswFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_modify_pay_psw, container, false);
        initUI();
        return mRootView;
    }

    private void initUI() {
        final IconEditTextLayout old_password_et = (IconEditTextLayout) mRootView.findViewById(R.id.modify_pay_password_1_et);
        final IconEditTextLayout password_1_et = (IconEditTextLayout) mRootView.findViewById(R.id.modify_pay_password_2_et);
        final IconEditTextLayout password_2_et = (IconEditTextLayout) mRootView.findViewById(R.id.modify_pay_password_3_et);

        View submitBtn = mRootView.findViewById(R.id.modify_pay_submit_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                    }
                    @Override
                    public void onFailure(int statusCode, String errorResponse, Throwable e) {
                        logger.e("onFailure  statusCode==="+statusCode+"errorResponse==="+errorResponse);

                        if(statusCode==400){
                            Toast.makeText(getContext(), "原支付密码不对，请重试", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "修改失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                UserControler.modifyPayPassword(old_password_et.getText(), password_1_et.getText(), responseHandler);
            }
        });
    }

}
