package com.zhichen.parking.fragment.around;

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
import com.zhichen.parking.fragment.homepage.OnRoadFragment;
import com.zhichen.parking.fragment.homepage.FindParkingLotFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.tools.FragmentChangeHelper;

/**
 * Created by xuemei on 2016-05-25.
 * 周边Fragment
 */
public class AroundFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_myScan;
    private RelativeLayout rl_parkRent;
    private RelativeLayout rl_liftService;
    private RelativeLayout rl_findPark;
    private RelativeLayout rl_mypark;
    private Intent intent;


    public AroundFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_around, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        rl_myScan= (RelativeLayout) view.findViewById(R.id.myScan);
        rl_parkRent= (RelativeLayout) view.findViewById(R.id.parkRent);
        rl_liftService= (RelativeLayout) view.findViewById(R.id.liftService);
        rl_findPark= (RelativeLayout) view.findViewById(R.id.findPark);
        rl_mypark= (RelativeLayout) view.findViewById(R.id.myPark);

        rl_myScan.setOnClickListener(this);
        rl_parkRent.setOnClickListener(this);
        rl_liftService.setOnClickListener(this);
        rl_findPark.setOnClickListener(this);
        rl_mypark.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!UserManager.instance().isLogined()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra("frag","AroundFragment");
            startActivity(intent);
            getActivity().finish();
            return;
        }
        Fragment fragment=null;
        switch (v.getId()){
            case R.id.myScan:
               fragment=new AroundScanFragment();
                break;
            case R.id.parkRent:
                Toast.makeText(getActivity(),"暂未开放，敬请期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.liftService:
                Toast.makeText(getActivity(),"暂未开放，敬请期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.findPark:
                fragment=new FindParkingLotFragment();
                break;
            case R.id.myPark:
                fragment=new OnRoadFragment();
                break;
        }
        if(fragment!=null){
            FragmentChangeHelper helper=new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }
}
