package com.zhichen.parking.model;

import java.io.Serializable;

/**
 * Created by xuemei on 2016-06-16.
 */
public class PayRecord implements Serializable {
    private String parking_lot;//停车场--支付地点
    private String plate_number;//车牌号
    private String payment_time;//缴费时间
    private String id;//缴费金额
    private String amount;
    private String payment_type;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getParking_lot() {
        return parking_lot;
    }

    public void setParking_lot(String parking_lot) {
        this.parking_lot = parking_lot;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PayRecord{" +
                "parking_lot='" + parking_lot + '\'' +
                ", plate_number='" + plate_number + '\'' +
                ", payment_time='" + payment_time + '\'' +
                ", id='" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", payment_type='" + payment_type + '\'' +
                '}';
    }
}
