package com.zhichen.parking.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;

/**
 * Created by xuemei on 2016-06-02.
 */
public class AgreementFragment extends BaseFragment {

    public AgreementFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agreement,container,false);
    }
}
