package com.zhichen.parking.fragment.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.BuildConfig;
import com.zhichen.parking.R;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.google.gson.Gson;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.lib.messageservice.MessageService;
import com.zhichen.parking.lib.messageservice.OnMessageListener;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.SoftwareVersion;
import com.zhichen.parking.model.UpgradeBean;
import com.zhichen.parking.model.UpgradeRequest;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.DialogHelper;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.MyLogger;
import com.zhichen.parking.widget.CircleImage;
import com.zhichen.parking.widget.CircularImage;

/**
 * Created by xuemei on 2016-05-25.
 * 我的Fragment
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
    private RelativeLayout rlUserInfo;
    private RelativeLayout rlVersion;
    private TextView myAcount, myWallet, cartManage, feedBack, aboutMine, logout;
    private Dialog dialog;
    private TextView phone, yuge, money, yuan, cutVersion;
    private ImageView ivIn;
    private ImageView header;

    private OnMessageListener onMessageListener;
    private View mViewRoot;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        rlUserInfo = (RelativeLayout) mViewRoot.findViewById(R.id.rl_userInfo);
        rlVersion = (RelativeLayout) mViewRoot.findViewById(R.id.rl_Version);
        myAcount = (TextView) mViewRoot.findViewById(R.id.tv_myAcount);
        myWallet = (TextView) mViewRoot.findViewById(R.id.tv_myWallet);
        cartManage = (TextView) mViewRoot.findViewById(R.id.tv_cartmanage);
        feedBack = (TextView) mViewRoot.findViewById(R.id.tv_feedBack);
        aboutMine = (TextView) mViewRoot.findViewById(R.id.tv_aboutMine);
        logout = (TextView) mViewRoot.findViewById(R.id.tv_logout);

        phone = (TextView) mViewRoot.findViewById(R.id.tv_phone);
        yuge = (TextView) mViewRoot.findViewById(R.id.yu);
        money = (TextView) mViewRoot.findViewById(R.id.money);
        yuan = (TextView) mViewRoot.findViewById(R.id.tv_yuan);
        ivIn = (ImageView) mViewRoot.findViewById(R.id.iv_in);
        header = (CircularImage) mViewRoot.findViewById(R.id.user_header);

        cutVersion = (TextView) mViewRoot.findViewById(R.id.tv_CurrentVersion);
//        APP当前版本
        cutVersion.setText(String.valueOf("V" + BuildConfig.VERSION_NAME));
        if (!UserManager.instance().isLogined()) {
            phone.setText("您还未登录");
            phone.setTextColor(getResources().getColor(R.color.money_red));
            yuge.setText("请登录，查看更多信息");
            money.setVisibility(View.GONE);
            yuan.setVisibility(View.GONE);
            ivIn.setVisibility(View.GONE);
            header.setImageResource(R.mipmap.user_head_bg);
        } else {
            String name = UserManager.instance().getNickName();
            if (name != null) {
                phone.setText(name);
            }
            yuge.setText("余额");
            money.setVisibility(View.VISIBLE);
            money.setText(String.valueOf(UserManager.instance().getUser().getAccountBalance()));
            yuan.setVisibility(View.VISIBLE);
            ivIn.setVisibility(View.GONE);//暂时先隐藏
            header.setImageResource(R.mipmap.backheader);
//            updateAvatar();
        }

        onMessageListener = new OnMessageListener() {
            @Override
            public void onMessage(Integer eventType, Bundle arguments) {
                if (eventType == AppConstants.TYPE_AVATAR_CHANGE) {
                    updateAvatar();
                } else if (eventType == AppConstants.TYPE_NICKNAME_CHANGE) {
                    phone.setText(UserManager.instance().getNickName());
                }
            }
        };
        onMessageListener.setSupportEvent(AppConstants.TYPE_AVATAR_CHANGE, true);
        onMessageListener.setSupportEvent(AppConstants.TYPE_NICKNAME_CHANGE, true);
        MessageService.instance().addMessageListener(onMessageListener);


        rlUserInfo.setOnClickListener(this);
        rlVersion.setOnClickListener(this);
        myAcount.setOnClickListener(this);
        myWallet.setOnClickListener(this);
        cartManage.setOnClickListener(this);
        feedBack.setOnClickListener(this);
        aboutMine.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    //   获取应用code和name
    public void getASVersionName() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
    }

    private void updateAvatar() {
        Bitmap avatar = BitmapFactory.decodeFile(UserManager.instance().getAvatarPath());
        if (avatar != null) {
            header.setImageBitmap(avatar);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.rl_userInfo:
                if (!UserManager.instance().isLogined()) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("frag", "ProfileFragment");
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
//                fragment = new UserInfoFragment();
                break;
            case R.id.rl_Version:
                updateVersion();
                break;
            case R.id.tv_myAcount:
                fragment = new ProymyAccountFragment();
                break;
            case R.id.tv_myWallet:
                if (!UserManager.instance().isLogined()) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("frag", "ProfileFragment");
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                fragment = new PromyWalletFragment();
                break;
            case R.id.tv_cartmanage:
                if (!UserManager.instance().isLogined()) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("frag", "ProfileFragment");
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                fragment = new CarManageFragment();
                break;
            case R.id.tv_feedBack:
                if (!UserManager.instance().isLogined()) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("frag", "ProfileFragment");
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                fragment = new FeedBackFragment();
                break;
            case R.id.tv_aboutMine:
                fragment = new AboutFragment();
                break;
            case R.id.tv_logout:
                if (!UserManager.instance().isLogined()) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("frag", "ProfileFragment");
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                dialogLogout();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }

    //版本更新
    private void updateVersion() {
        final ProgressDialog pd = DialogHelper.showProgressDialog(getContext(), "检查更新中，请稍后。。。", true);
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                pd.dismiss();
                Log.d("cwf", "version enterUpgrade response====" + response);
                SoftwareVersion version = new Gson().fromJson(response, SoftwareVersion.class);
                UpgradeBean upgradeBean = new UpgradeBean();
                String url = Constants.VERSION_LOAD;
                upgradeBean.setUri(url);
                upgradeBean.setVersionCode(String.valueOf(version.getVersion_code()));
                UpgradeRequest upgradeRequest = new UpgradeRequest(getContext());
                if (!upgradeRequest.needUpgrade(upgradeBean)) {
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    };
                    Dialog dialog = DialogHelper.createHintDialog(getContext(), "恭喜您，软件已经是最新版本", listener);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                pd.dismiss();
                Log.d("cwf", "version enterUpgrade errorResponse====" + errorResponse);
            }
        };
        UserControler.checkUpgrade(getContext(), responseHandler);
    }

    //是否确定退出登录
    private void dialogLogout() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_dialog_logout, null);
        final TextView cancle = (TextView) view.findViewById(R.id.tv_cancle);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView title = (TextView) view.findViewById(R.id.dilog_title);
        title.setText("是否确定退出当前账号？");
        //取消按钮
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定按钮
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = UserManager.instance().getUserName();
                String passWord = UserManager.instance().getPassWord();
                Log.e("xue", "name===" + userName);
                Log.e("xue", "passWord===" + passWord);
                new LogoutTask(userName, passWord).execute();
            }
        });
        //新建对话框
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();//获取屏幕宽和高
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();//获取对话框当前的参数值
        params.height = (int) (display.getHeight() * 0.21);
        params.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setContentView(view);
    }

    private class LogoutTask extends AsyncTask<Void, Integer, String> {
        private String name;
        private String password;

        public LogoutTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = UserControler.logout(getContext(), name, password);
            Log.e("xue", "result===" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.contains("Successfully logged out")) {
                UserManager.instance().setUser(null);
                UserManager.instance().setPassWord(null);
                UserManager.instance().saveConfig(getContext());
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.putExtra("logout", "logout");
                getContext().startActivity(intent);
                MessageService.instance().notifyMessage(AppConstants.TYPE_EVENT_ACCOUNT_QUIT, null);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "退出失败", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
