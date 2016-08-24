package com.zhichen.parking.fragment.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.tools.FragmentChangeHelper;

/**
 * Created by xuemei on 2016-07-11.
 * 记录
 */
public class RecordFragment extends BaseFragment implements View.OnClickListener {
    private View mViewRoot;
    private RelativeLayout parkRecord, payRecord,rechargeRecord;

    public RecordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_record, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        parkRecord = (RelativeLayout) mViewRoot.findViewById(R.id.rl_parkRecord);
        payRecord = (RelativeLayout) mViewRoot.findViewById(R.id.rl_payRecord);
        rechargeRecord= (RelativeLayout) mViewRoot.findViewById(R.id.rl_rechargeRecord);
        parkRecord.setOnClickListener(this);
        payRecord.setOnClickListener(this);
        rechargeRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!UserManager.instance().isLogined()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra("frag","RecordFragment");
            startActivity(intent);
            return;
        }
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.rl_parkRecord:
                fragment = new ParkRecordFragment();
                break;
            case R.id.rl_payRecord:
                fragment = new PayRecordFragment();
                break;
            case R.id.rl_rechargeRecord:
                fragment=new RechargeRecordFragment();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }
}
