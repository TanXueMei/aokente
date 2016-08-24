package com.zhichen.parking.tools;

import android.text.TextUtils;

/**
 * Created by xuemei on 2016-08-10.
 * 验证工具
 */
public class VerifyTools {
    /**
     * 验证是否是车牌号
     * 车牌号格式：汉字 + A-Z + 5位A-Z或0-9
     （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
     当然这个正则表达式有局限性，比如第一位只限定是汉字，没限定只有34个省汉字缩写；
     车牌号不存在字母I和O，防止和1、0混淆；部分车牌无法分辨等等。
     */
    public static boolean isCarnumberNO(String carnumber) {
            String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
            if (TextUtils.isEmpty(carnumber)) return false;
              else return carnumber.matches(carnumRegex);
          }


}
