package com.zhichen.parking.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhichen.parking.R;
import com.zhichen.parking.activity.LoginActivity;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.library.bgabanner.BGABanner;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.tools.FragmentChangeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuemei on 2016-05-25.
 * 首页Fragment
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    private View mViewRoot;
    private BGABanner mBGABanner;
    private TextView findPark, toPark;
    private Button toPay;
    List<String> list;

    public HomePageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return mViewRoot;
    }

    private void initUI() {
        mBGABanner = (BGABanner) mViewRoot.findViewById(R.id.banner);
        findPark = (TextView) mViewRoot.findViewById(R.id.tv_findPark);
        toPark = (TextView) mViewRoot.findViewById(R.id.tv_toPark);
        toPay = (Button) mViewRoot.findViewById(R.id.bt_toPay);

        List<View> views = new ArrayList<>();
        views.add(getPageView(R.mipmap.banner_1));
        views.add(getPageView(R.mipmap.banner_2));
        views.add(getPageView(R.mipmap.banner_3));
        mBGABanner.setViews(views);


//        mBGABanner.setOnPageClickListener(new FlashViewListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(getContext(),"你点击了"+position,Toast.LENGTH_SHORT).show();
//            }
//        });

        //网络图片
//        initData();
//        List<View> pageView = getPageView(list);
//        mBGABanner.setViews(pageView);

        findPark.setOnClickListener(this);
        toPark.setOnClickListener(this);
        toPay.setOnClickListener(this);
    }

    //加载默认图片
    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

//    //加载网络图片
//    private List<View> getPageView(List<String> imageUrls) {
//        List<View> imageViewList = new ArrayList<>();
//        ImageView imageView;
//        for (int i = 0; i < imageUrls.size(); i++) {
//            imageView = new ImageView(getContext());
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            PWApplication.mImageLoader.clear();
//            PWApplication.mImageLoader.display(imageUrls.get(i), imageView, R.mipmap.ic_launcher, imageView.getWidth(), imageView.getHeight());
//            imageViewList.add(imageView);
//        }
//        return imageViewList;
//    }

    private void initData() {
        list = new ArrayList<>();
        list.add("http://7xt7l1.com1.z0.glb.clouddn.com/banner_1%20-%20%E5%89%AF%E6%9C%AC.png");
        list.add("http://7xt7l1.com1.z0.glb.clouddn.com/banner_2%20-%20%E5%89%AF%E6%9C%AC.png");
        list.add("http://7xt7l1.com1.z0.glb.clouddn.com/banner_3%20-%20%E5%89%AF%E6%9C%AC.png");
    }

    @Override
    public void onClick(View v) {
        if (!UserManager.instance().isLogined()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra("frag","HomePageFragment");
            startActivity(intent);
            getActivity().finish();
            return;
        }
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.tv_findPark:
                fragment = new FindParkingLotFragment();
                break;
            case R.id.tv_toPark:
                fragment = new OnRoadFragment();
                break;
            case R.id.bt_toPay:
                fragment = new PayFragment();
                break;
        }
        if (fragment != null) {
            FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
            helper.addToBackStack(fragment.getClass().getSimpleName());
            activity.changeFragment(helper);
        }
    }
}
