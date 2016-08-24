package com.zhichen.parking.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.fragment.around.AroundFragment;
import com.zhichen.parking.fragment.homepage.HomePageFragment;
import com.zhichen.parking.fragment.profile.LoginFragment;
import com.zhichen.parking.fragment.profile.ProfileFragment;
import com.zhichen.parking.fragment.record.ParkRecordFragment;
import com.zhichen.parking.fragment.record.RecordFragment;
import com.zhichen.parking.lib.messageservice.MessageService;
import com.zhichen.parking.lib.messageservice.OnMessageListener;
import com.zhichen.parking.lotmanager.ParkingLotManager;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.servercontoler.UserControler;
import com.zhichen.parking.tools.FragmentChangeHelper;
import com.zhichen.parking.tools.NetworkUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private HomePageFragment homePageFragment;
    private RecordFragment recordFragment;
    private AroundFragment aroundFragment;
    private ProfileFragment profileFragment;
    private RadioGroup radioGroup;
    FragmentTransaction tran;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCollector.addActivity(this);
        addPermision();
        MPermissions.requestPermissions(this, 4, permissions.toArray(new String[permissions.size()]));
        supportFragmentManager = getSupportFragmentManager();
        autoLogin();
        loadAvatar();
        init();
        initMessageListener();
        UserManager.instance().setUserName(UserManager.instance().getUserName());
        UserManager.instance().setPassWord("");

        if(getIntent()!=null){
            type=getIntent().getStringExtra("login");
            if(type!=null){
                if(type.equals("HomePageFragment")){
                    transitionFragment(R.id.homePage);
                    radioGroup.check(R.id.homePage);
                }else if(type.equals("RecordFragment")){
                    transitionFragment(R.id.record);
                    radioGroup.check(R.id.record);
                }else if(type.equals("AroundFragment")){
                    transitionFragment(R.id.around);
                    radioGroup.check(R.id.around);
                }else if(type.equals("ProfileFragment")){
                    transitionFragment(R.id.profile);
                    radioGroup.check(R.id.profile);
                }
            }else {
                init();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.instance().clear();
        MessageService.instance().removeMessageListener(mOnMessageListener);
        ParkingLotManager.instance().clear();
    }

    private List<String> permissions;

    private void addPermision() {
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_SETTINGS);
    }

    //    	加入运行时权限回调
    @PermissionGrant(4)
    public void requestContactSuccess() {

    }

    @PermissionDenied(4)
    public void requestContactFailed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void init() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.homePage);
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        String name = UserManager.instance().getUserName();
        String password = UserManager.instance().getPassWord();
        if (name != null && password != null) {
            AutoLoginTask task = new AutoLoginTask(name, password);
            task.execute();
        }

    }

    private class AutoLoginTask extends AsyncTask<Void, Integer, String> {
        private String name;
        private String password;

        public AutoLoginTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = LoginFragment.login(MainActivity.this, name, password);
            return result;
        }
    }

    /**
     * 加载用户头像
     */
    private void loadAvatar() {
        AsyncResponseHandler responseHandler = new AsyncResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                Log.d("tanxuemei","loadAvatar   onSuccess==="+response);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse, Throwable e) {
                Log.d("tanxuemei","loadAvatar  onFailure==="+errorResponse);
            }
        };
        String url = Constants.BASE_PARK_URL + "/user/avatar/?filename=avatar.jpg";
        UserControler.downLoadFile(this, UserManager.instance().getAvatarPath(), url, responseHandler);
    }

    /**
     * 初始化信息监听
     */
    private OnMessageListener mOnMessageListener;

    private void initMessageListener() {
        mOnMessageListener = new OnMessageListener() {
            @Override
            public void onMessage(Integer eventType, Bundle arguments) {
                MainActivity.this.finish();
            }
        };
        mOnMessageListener.setSupportEvent(AppConstants.TYPE_EVENT_ACCOUNT_QUIT, true);
        MessageService.instance().addMessageListener(mOnMessageListener);
    }


    /**
     * TAB切换事件
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id = group.getId();
        if (id == R.id.radiogroup) {
            transitionFragment(checkedId);
        }
    }
    /**
     * 切换tab主Fragment
     */
    Fragment fragment = null;
    private void transitionFragment(int checkID) {
        switch (checkID) {
            case -1:
                // TODO 断网的的Fragment
                break;
            case R.id.homePage:
//                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
//                }
                fragment = homePageFragment;
                break;
            case R.id.record:
//                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
//                }
                fragment = recordFragment;
                break;

            case R.id.around:
//                if (aroundFragment == null) {
                    aroundFragment = new AroundFragment();
//                }
                fragment = aroundFragment;
                break;
            case R.id.profile:
//                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
//                }
                fragment = profileFragment;
                break;
            default:
                break;
        }
        // 切换
        FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
        helper.clearAllBackStack(true);
        changeFragment(helper);
    }

    /**
     * 按两次返回键退出程序的方法
     */
    private long lastPressedTime; //最后一次按下的时间

    @Override
    public void onBackPressed() {
        long firstPressedTime = System.currentTimeMillis();
        // 如果fragment回退栈中已经没了fragment，则处理点击两次返回
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (firstPressedTime - lastPressedTime <= 2000) {
                ActivityCollector.finishAll();

            } else {
                Toast.makeText(this, "再按一下退出", Toast.LENGTH_SHORT).show();
                lastPressedTime = firstPressedTime;
            }
        } else { // 如果fragment回退栈中还有fragment，则返回上一个fragment
            super.onBackPressed();
        }
    }

    // activty 统一管理Fragment会退栈
    private FragmentManager supportFragmentManager;
    private String targetFragmentTag;

    /**
     * 这个方法由各Fragment 中的Activity 实例调用，通过
     * 传入的FragmentChangeHelper 对象管理具体方法
     *
     * @param helper 包含Fragment 切换操作信息助手类
     */
    public void changeFragment(FragmentChangeHelper helper) {
        if (helper != null) {
            tran = supportFragmentManager.beginTransaction();
            Fragment targetFragment = helper.getTargetFragment();
            if (targetFragment != null) {

                //检查是否附带参数
                Bundle args = helper.getArgs();
                if (args != null) {
                    targetFragment.setArguments(args);
                }

                //检查是否添加到回退栈
                targetFragmentTag = helper.getTargetFragmentTag();
                if (targetFragmentTag != null) {
                    tran.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                    tran.addToBackStack(targetFragmentTag);
                }

                //检查是否要删除回退栈里对应的Fragment
                String[] removeFragmentTag = helper.getRemoveFragmentTag();
                if (removeFragmentTag != null) {
                    for (String s : removeFragmentTag) {
                        tran.remove(supportFragmentManager.findFragmentByTag(s));
                    }
                }

                //检查是否清空回退栈
                if (helper.isClearAllBackStack()) {
                    supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                    );
                }

                tran.replace(R.id.fragmeLayout, targetFragment);

                tran.commit();

            } else { //如果没有目标Fragment，代表需要返回到栈中的指定Fragment
                String backToFragmentTag = helper.getBackToFragmentTag();

                if (backToFragmentTag != null) {
                    supportFragmentManager.popBackStackImmediate(
                            backToFragmentTag,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                    );
                }
            }
        }
    }




}
