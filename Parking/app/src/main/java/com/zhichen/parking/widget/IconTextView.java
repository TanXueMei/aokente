package com.zhichen.parking.widget;

/**
 * Created by xuemei on 2016-06-01.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhichen.parking.R;
import com.zhichen.parking.util.DensityUtil;

public class IconTextView extends LinearLayout {

    private ImageView mIconIv;
    private TextView mTitleTv;
    private int mTextSize;

    public IconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        createViews(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);
        final int size = a.getIndexCount();
        for (int i = 0; i < size; ++i) {
            int attr = a.getIndex(i);
            switch(attr){
                case R.styleable.IconTextView_icon:{
                    mIconIv.setImageDrawable(a.getDrawable(attr));
                }
                break;
                case R.styleable.IconTextView_text:{
                    mTitleTv.setText(a.getString(attr));
                }
                break;
                case R.styleable.IconTextView_textSize:{
                    mTextSize = a.getDimensionPixelSize(attr, (int) mTitleTv.getTextSize());
                    mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                }
                break;
            }
        }
        a.recycle();
    }

    private void createViews(Context context, AttributeSet attrs)
    {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.bottomMargin = DensityUtil.dip2px(context, 10);
        mIconIv = new ImageView(context);
        addView(mIconIv, params);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitleTv = new TextView(context, attrs);
        mTitleTv.setGravity(Gravity.CENTER);
        addView(mTitleTv, params);
    }
}
