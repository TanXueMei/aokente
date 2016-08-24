package com.zhichen.parking.model;

import java.io.Serializable;

/**
 * Created by xuemei on 2016-06-16.
 */
public class ParkHistoryOut implements Serializable {
    private Long id;
    private String plate_number;
    private String parking_card_number;
    private String out_time;
    private String parking_lot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return "ParkHistoryOut{" +
                "id=" + id +
                ", plate_number='" + plate_number + '\'' +
                ", parking_card_number='" + parking_card_number + '\'' +
                ", out_time=" + out_time +
                ", parking_lot='" + parking_lot + '\'' +
                '}';
    }
}
