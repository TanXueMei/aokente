package com.zhichen.parking.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.zhichen.parking.R;
import com.zhichen.parking.util.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuemei on 2016-06-01.
 */
public class TimeProcessBar extends View {

    private int BOTTOM_PADDING = 3 ;
    private int mPaddingLeft = 5;
    private int mPaddingRight = 5;
    private int mTextSize = 16;
    private int mBarHeight = 3;
    private int mTotal = 15 * 60;
    private int mPos = 0;

    private Paint	mPaint ;
    private Path 	mPath ;

    private Timer	mTimer ;

    public TimeProcessBar(Context context) {
        super(context);
        init();
    }

    public TimeProcessBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeProcessBar);

        final int size = a.getIndexCount();
        for (int i = 0; i < size; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TimeProcessBar_total: {
                    mTotal = a.getInt(attr, mTotal);
                }
                break;
                case R.styleable.TimeProcessBar_pos: {
                    mPos = a.getInt(attr, mPos);
                }
                break;
                case R.styleable.TimeProcessBar_barHeight: {
                    mBarHeight = a.getDimensionPixelSize(attr, mBarHeight);
                }
                break;
                case R.styleable.TimeProcessBar_textSize: {
                    mTextSize = a.getDimensionPixelSize(attr, mTextSize);
                }
                break;
                case R.styleable.TimeProcessBar_paddingLeft: {
                    mPaddingLeft = a.getDimensionPixelSize(attr, mPaddingLeft);
                }
                break;
                case R.styleable.TimeProcessBar_paddingRight: {
                    mPaddingRight = a.getDimensionPixelSize(attr, mPaddingRight);
                }
                break;
            }
        }
        a.recycle();

        init();
    }

    private void init()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
        BOTTOM_PADDING = DensityUtil.dip2px(getContext(), 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        //灰条
        mPaint.setStrokeWidth(mBarHeight);
        mPaint.setColor(Color.parseColor("#8F8F8F"));
        float startX = mPaddingLeft ;
        float stopX = width-mPaddingRight ;
        float startY = height-mBarHeight - BOTTOM_PADDING ;
        float stopY = height-mBarHeight - BOTTOM_PADDING ;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        //蓝条
        mPaint.setColor(Color.parseColor("#1C86EE"));
        stopX = (float)(width-mPaddingRight-mPaddingLeft)/mTotal*mPos + mPaddingLeft ;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        //三角突出的高度
        final int triangleExtrude = (int) (BOTTOM_PADDING*2);
        //文本
        mPaint.setStyle(Style.FILL_AND_STROKE);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(1);
        int min = mPos/60 ;
        int sec = mPos%60 ;
        String text = "" + min + "分" + sec + "秒";
        float w = mPaint.measureText(text);
        float textX = stopX - w/2;
        if(textX < mPaddingLeft){
            textX = 0 ;
        }
        else if((stopX + w/2) > (width - mPaddingRight)){
            textX = (width - mPaddingRight) - w;
        }
        canvas.drawText(text, textX, (stopY - triangleExtrude -mTextSize/4), mPaint);

        //三角
        canvas.save();
        canvas.translate(stopX, stopY - triangleExtrude);
        mPaint.setColor(Color.parseColor("#FFA54F"));
        mPaint.setStyle(Style.FILL);
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(-BOTTOM_PADDING*2, mBarHeight+triangleExtrude*2);
        mPath.lineTo(BOTTOM_PADDING*2, mBarHeight+triangleExtrude*2);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    public void startTimeCount()
    {
        if(mTimer != null){
            mTimer.cancel();
        }
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++mPos ;
                if(mPos >= mTotal){
                    mTimer.cancel();
                }
                postInvalidate();
            }
        };
        mTimer.schedule(task, 0, 1000);
    }
}
