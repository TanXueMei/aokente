package com.zhichen.parking.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by xuemei on 2016-06-01.
 * 自定义 倒数计时Button
 */
public class CountDownButton extends Button {
    private CountDownTimer timer;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startCountDown(long millisInFuture,long countDownInterval){
        CountDownButton.this.setEnabled(false);
        timer=new CountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
              CountDownButton.this.setText((millisUntilFinished / 1000) + "秒后可重发");
            }

            @Override
            public void onFinish() {
                CountDownButton.this.setEnabled(true);
                CountDownButton.this.setText("获取验证码");
            }
        };
        timer.start();
    }


}
