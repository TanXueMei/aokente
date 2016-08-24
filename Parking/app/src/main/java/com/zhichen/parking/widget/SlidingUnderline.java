package com.zhichen.parking.widget;

/**
 * Created by xuemei on 2016-06-02.
 * 自定义文字下方的指示线条
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhichen.parking.R;

public class SlidingUnderline extends View {

    private int	mTotal = 1 ;
    private int	mPos = 0 ;

    private Paint	mPaint ;

    public SlidingUnderline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingUnderline);
        final int size = a.getIndexCount();
        for (int i = 0; i < size; ++i) {
            int attr = a.getIndex(i);
            switch(attr){
                case R.styleable.SlidingUnderline_pos:{
                    mPos = a.getInt(attr, mPos);
                }
                break;
                case R.styleable.SlidingUnderline_total:{
                    mTotal = a.getInt(attr, mTotal);
                }
            }
        }
        a.recycle();
    }

    private void init()
    {
        mPaint = new Paint();
        mPaint.setColor(getContext().getResources().getColor(R.color.titleColor));
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        int w = getWidth();
        int linew = w/mTotal ;
        mPaint.setStrokeWidth(w);
        canvas.drawLine(linew*mPos, 0, linew*(mPos+1), 0, mPaint);
    }

    public void setPos(int pos)
    {
        mPos = pos ;
        invalidate();
    }

}

