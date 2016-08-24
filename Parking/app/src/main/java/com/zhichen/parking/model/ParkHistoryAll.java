package com.zhichen.parking.model;

import java.io.Serializable;

/**
 * Created by xuemei on 2016-06-17.
 */
public class ParkHistoryAll implements Serializable {
    private String id;
    private String plate_number;
    private String parking_card_number;
    private String in_time;
    private String out_time;
    private String parking_lot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getParking_card_number() {
        return parking_card_number;
    }

    public void setParking_card_number(String parking_card_number) {
        this.parking_card_number = parking_card_number;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getParking_lot() {
        return parking_lot;
    }

    public void setParking_lot(String parking_lot) {
        this.parking_lot = parking_lot;
    }

    @Override
    public String toString() {
        return "ParkHistoryAll{" +
                "id='" + id + '\'' +
                ", plate_number='" + plate_number + '\'' +
                ", parking_card_number='" + parking_card_number + '\'' +
                ", in_time='" + in_time + '\'' +
                ", out_time='" + out_time + '\'' +
                ", parking_lot='" + parking_lot + '\'' +
                '}';
    }
}
