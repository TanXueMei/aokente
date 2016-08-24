package com.zhichen.parking.model;

/**
 * Created by xuemei on 2016-05-26.
 * 停车记录实体类
 */
public class Record {
    private String title;
    private String enterTime;
    private String outTime;
    private String money;
    private String dataSource;

    public Record(){}

    public Record(String title, String enterTime, String outTime, String money, String dataSource) {
        this.title = title;
        this.enterTime = enterTime;
        this.outTime = outTime;
        this.money = money;
        this.dataSource = dataSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
