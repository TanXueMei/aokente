package com.zhichen.parking.fragment.profile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.fragment.homepage.PayCommonFragment;
import com.zhichen.parking.fragment.homepage.RechargeFragment;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.User;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;

/**
 * Created by xuemei on 2016-05-30.
 * 我的--我的钱包
 */
public class PromyWalletFragment extends PayCommonFragment implements View.OnClickListener {
   private TextView tv_topUp;//充值
    private TextView mMoneyWallet;
    public PromyWalletFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mywallet,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();

        mMoneyWallet = (TextView)view.findViewById(R.id.mywallet);
        mMoneyWallet.setText(String.valueOf(UserManager.instance().getUser().getAccountBalance()));

        tv_topUp= (TextView) view.findViewById(R.id.tv_topUp);
        tv_topUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment=null;
        switch (v.getId()){
            case R.id.tv_topUp:
               fragment=new RechargeFragment();
                break;
        }
        if(fragment!=null){
            FragmentChangeHelper helper=new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateMoneyPack();
    }

    //    支付成功之后刷新余额数据  重新请求
    private void updateMoneyPack() {
        String name = UserManager.instance().getUserName();
        String password = UserManager.instance().getPassWord();
        if (name != null && password != null) {
            UpdateMoneyTask task = new UpdateMoneyTask(name, password);
            task.execute();
        }
    }

    ProgressDialog mProgressDialog;
    private class UpdateMoneyTask extends AsyncTask<Void, Integer, User> {
        private String name;
        private String password;

        public UpdateMoneyTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = DialogHelper.showProgressDialog(getContext(), this, "正在刷新余额，请稍后。。。");
        }

        @Override
        protected User doInBackground(Void... params) {
            User user = UserControler.getUserInfo(getContext(), name, password);
            if (user != null) {
                UserManager.instance().setUser(user);
                return user;
            }
            return null;
        }

        @Override
        protected void onPostExecute(User s) {
            super.onPostExecute(s);
//            if (mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
            mMoneyWallet.setText(String.valueOf(UserManager.instance().getUser().getAccountBalance()));

        }
    }

}
