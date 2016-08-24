package com.zhichen.parking.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhichen.parking.BuildConfig;
import com.zhichen.parking.R;

/**
 * Created by xuemei on 2016-05-30.
 * 关于我们
 */
public class AboutFragment extends Fragment {

    private TextView version;
    private View mViewRoot;

    public AboutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mViewRoot=inflater.inflate(R.layout.fragment_about,container,false);
        version= (TextView) mViewRoot.findViewById(R.id.about_version_tv);
        version.setText(String.valueOf("V" + BuildConfig.VERSION_NAME));
        return mViewRoot;

    }


}
