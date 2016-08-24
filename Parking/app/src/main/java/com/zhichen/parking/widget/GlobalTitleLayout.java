package com.zhichen.parking.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhichen.parking.R;

/**
 * Created by xuemei on 2016-05-25.
 * 自定义通用标题栏
 */
public class GlobalTitleLayout extends RelativeLayout {

    private ImageView back;
    private TextView titleTv;
    private ImageView search;
    private ImageView parkList;

    public GlobalTitleLayout(Context context) {
        super(context);
    }

    public GlobalTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //初始化控件
    private void init(final Context context, final AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.global_title, this);
        back = (ImageView) view.findViewById(R.id.back);
        titleTv = (TextView) view.findViewById(R.id.tv_title);
        search = (ImageView) view.findViewById(R.id.search);
        parkList = (ImageView) view.findViewById(R.id.list);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlobalTitleLayout);
        String titleText = null;
        boolean isShowBack = true;

        if (typedArray != null) {
            titleText = typedArray.getString(R.styleable.GlobalTitleLayout_global_title);
            isShowBack = typedArray.getBoolean(R.styleable.GlobalTitleLayout_global_back, false);

            if (view != null) {
                if (titleText != null) {
                    setTitle(titleText);
                }
                setBackButtonVisibility(isShowBack);
                back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof Activity) {
                            Activity activity = (Activity) context;
                            activity.onBackPressed();
                            activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                        }
                    }
                });

                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
        }

    }
    /**
     * 设置是否显示标题栏后退按钮，默认为显示
     *
     * @param visible 是否显示标题栏后退按钮
     */
    public void setBackButtonVisibility(boolean visible) {
        if (visible) {
            back.setVisibility(VISIBLE);
        } else {
            back.setVisibility(GONE);
        }
    }
    /**
     * 设置标题文字
     *
     * @param title 要显示的文字
     */
    public void setTitle(CharSequence title) {
        if (title != null) {
            titleTv.setText(title);
        }
    }

    /**
     * 设置搜索按钮可见并设置点击事件，同时自动显示
     *
     * @param searchOnClickListener
     */
    public void setSearch(OnClickListener searchOnClickListener) {
        if (searchOnClickListener != null) {
            search.setVisibility(VISIBLE);
            search.setOnClickListener(searchOnClickListener);
        }

    }

    /**
     * 设置停车场列表按钮可见并设置点击事件
     *
     * @param listOnClickListener
     */
    public void setParkList(OnClickListener listOnClickListener) {
        if (listOnClickListener != null) {
            parkList.setVisibility(VISIBLE);
            parkList.setOnClickListener(listOnClickListener);
        }
    }
}
