package com.zhichen.parking.fragment.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.library.gridpasswordview.GridPasswordView;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.widget.TimeProcessBar;

/**
 * Created by xuemei on 2016-06-01.
 * 我要停车
 */
public class OnRoadFragment extends BaseFragment implements View.OnClickListener {
    View mViewRoot;
    GridPasswordView mGridPasswordView;//泊位号
    TimeProcessBar mTimeProcessBar;//免费计时进度条
    TextView mMoneyPackTv;//账户余额显示数目
    TextView mPartTimeTv;//停车总时长
    TextView mPartMoneyTv;//停车应收费用
    Button partBtn;//开始停车按钮

    public OnRoadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_stop_park, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        mGridPasswordView = (GridPasswordView) mViewRoot.findViewById(R.id.onroad_berth_pswView);
        mGridPasswordView.togglePasswordVisibility();
        mTimeProcessBar = (TimeProcessBar) mViewRoot.findViewById(R.id.onroad_time_free_timecount);
        mMoneyPackTv = (TextView) mViewRoot.findViewById(R.id.onroad_account_num_tv);
        mMoneyPackTv.setText(String.valueOf(UserManager.instance().getUser().getAccountBalance()));
        partBtn = (Button) mViewRoot.findViewById(R.id.onroad_berth_ok_btn);

        mPartTimeTv= (TextView) mViewRoot.findViewById(R.id.onroad_time_hour_tv);
        mPartMoneyTv= (TextView) mViewRoot.findViewById(R.id.onroad_money_num_tv);

        partBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onroad_berth_ok_btn:
                mTimeProcessBar.startTimeCount();
                partBtn.setText("停车计时中");
                partBtn.setEnabled(false);
                break;
        }
    }
}
