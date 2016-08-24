package com.zhichen.parking.model;

/**
 * Created by xuemei on 2016-08-11.
 * 充值记录
 */
public class RechargeRecord {
    private String payment_time;
    private String payment_channel;
    private String amount;

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getPayment_channel() {
        return payment_channel;
    }

    public void setPayment_channel(String payment_channel) {
        this.payment_channel = payment_channel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RechargeRecord{" +
                "payment_time='" + payment_time + '\'' +
                ", payment_channel='" + payment_channel + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
