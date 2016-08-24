package com.zhichen.parking.model;

import android.graphics.Bitmap;

/**
 * Created by xuemei on 2016-06-22.
 * 下载用户头像
 */
public class Avatar {
    private String kind;
    private Bitmap avatar;
    private String phone_number;
    private String nick_name;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "kind='" + kind + '\'' +
                ", avatar=" + avatar +
                ", phone_number='" + phone_number + '\'' +
                ", nick_name='" + nick_name + '\'' +
                '}';
    }
}
