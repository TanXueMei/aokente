package com.zhichen.parking.widget;

/**
 * Created by xuemei on 2016-06-01.
 * 自定义带有图片的EditText布局
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhichen.parking.R;

public class IconEditTextLayout extends RelativeLayout
{
    private ImageView mIconIv;
    private EditText mInputEt;
    private int mTextSize;
    int num = 0;

    public IconEditTextLayout(Context context) {
        super(context);
        init(context, null);
    }

    public IconEditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        View rootView = LayoutInflater.from(context).inflate(R.layout.icon_edittext_layout, this);
        mIconIv = (ImageView) rootView.findViewById(R.id.icon_iv);
        mInputEt = (EditText) rootView.findViewById(R.id.input_et);
        //监听软键盘的删除键
        mInputEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    num++;
                    //在这里加判断的原因是点击一次软键盘的删除键,会触发两次回调事件
                    if (num % 2 != 0) {
                        String s = mInputEt.getText().toString();
                        if (!TextUtils.isEmpty(s)) {
                            mInputEt.setText("" + s.substring(0, s.length() - 1));
                            //将光标移到最后
                            mInputEt.setSelection(mInputEt.getText().length());
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconEditText);
        final int size = a.getIndexCount();
        for (int i = 0; i < size; ++i) {
            int attr = a.getIndex(i);
            switch(attr){
                case R.styleable.IconEditText_icon:{
                    mIconIv.setImageDrawable(a.getDrawable(attr));
                }
                break;
                case R.styleable.IconEditText_text:{
                    mInputEt.setText(a.getString(attr));
                }
                case R.styleable.IconEditText_hint:{
                    mInputEt.setHint(a.getString(attr));
                }
                case R.styleable.IconEditText_password:{
                    boolean password = a.getBoolean(attr, false);
                    if(password){
                        mInputEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
                break;
                case R.styleable.IconEditText_textSize:{
                    mTextSize = a.getDimensionPixelSize(attr, (int) mInputEt.getTextSize());
                    mInputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                }
                break;
            }
        }
        a.recycle();
    }

    public String getText()
    {
        return mInputEt.getText().toString();
    }

    public void setText(String text)
    {
        mInputEt.setText(text);
    }
}
