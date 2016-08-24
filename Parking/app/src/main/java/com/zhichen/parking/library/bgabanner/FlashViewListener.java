package com.zhichen.parking.library.bgabanner;

/**
 * Created by xuemei on 2016-08-10.
 * 功能：向外提供点击事件的接口，其中position代表的是图片的索引，即第几张图片
 */
public interface FlashViewListener {
    void onClick(int position);
}
