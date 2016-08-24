package com.zhichen.parking.fragment.around;

import android.app.Activity;
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
import com.zbar.lib.CaptureActivity;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.MyLogger;

/**
 * Created by xuemei on 2016-05-27.
 * 我的二维码Fragment
 */
public class AroundScanFragment extends BaseFragment implements View.OnClickListener {
    private MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
    View mViewRoot;
    private RelativeLayout rlScanScan;
    private RelativeLayout rlPayCode;
    public static final int SCANSCAN_CODE = 100;

    public AroundScanFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_aoundscan, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        rlScanScan = (RelativeLayout) mViewRoot.findViewById(R.id.rl_ScanScan);
        rlPayCode = (RelativeLayout) mViewRoot.findViewById(R.id.rl_payCode);

        rlScanScan.setOnClickListener(this);
        rlPayCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.rl_ScanScan:
                Intent intent = new Intent(getContext(), CaptureActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, SCANSCAN_CODE);
                break;
            case R.id.rl_payCode:
                fragment = new PayCodeFragment();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }

    //    二维码扫描回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if(requestCode==SCANSCAN_CODE){
          if(resultCode== Activity.RESULT_OK){
              Bundle bundle=data.getExtras();
              String result=bundle.getString("result");
              logger.d("scanscan  response=="+result);
              Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
          }
      }
    }
}
