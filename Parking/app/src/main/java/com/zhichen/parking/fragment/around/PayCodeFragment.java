package com.zhichen.parking.fragment.around;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;

/**
 * Created by xuemei on 2016-06-04.
 * 付款码
 */
public class PayCodeFragment extends BaseFragment {
    View mViewRoot;

    public PayCodeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mViewRoot=inflater.inflate(R.layout.fragment_paycode,container,false);
        return mViewRoot;
    }


}
