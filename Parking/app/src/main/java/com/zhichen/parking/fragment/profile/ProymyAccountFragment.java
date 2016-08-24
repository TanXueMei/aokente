package com.zhichen.parking.fragment.profile;

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
 * Created by xuemei on 2016-05-30.
 * 我的---我的账户
 */
public class ProymyAccountFragment extends BaseFragment implements View.OnClickListener {
    RelativeLayout rlChangeLogPsw;
    RelativeLayout rlChangePayPsw;
    RelativeLayout rlChangeFindPayPsw;

    public ProymyAccountFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myaccount,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        rlChangeLogPsw= (RelativeLayout) view.findViewById(R.id.change_logpsw);
        rlChangePayPsw= (RelativeLayout) view.findViewById(R.id.change_paypsw);
        rlChangeFindPayPsw= (RelativeLayout) view.findViewById(R.id.change_findpaypsw);

        rlChangeLogPsw.setOnClickListener(this);
        rlChangePayPsw.setOnClickListener(this);
        rlChangeFindPayPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!UserManager.instance().isLogined()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }
        Fragment fragment=null;
        switch (v.getId()){
            case R.id.change_logpsw:
                fragment=new ModifLoginpswFragment();
                break;
            case R.id.change_paypsw:
                fragment=new ModifPaypswFragment();
                break;
            case R.id.change_findpaypsw:
                fragment=new FindPayPswFragment();
                break;
        }
        if(fragment!=null){
            FragmentChangeHelper helper=new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }

    }
}
