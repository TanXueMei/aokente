package com.zhichen.parking.fragment.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;

/**
 * Created by xuemei on 2016-06-13.
 */
public class PayOtherFragment extends BaseFragment {
    private View mViewRoot;
    public PayOtherFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot=inflater.inflate(R.layout.fragment_pay_other,container,false);
        return mViewRoot;
    }
}
