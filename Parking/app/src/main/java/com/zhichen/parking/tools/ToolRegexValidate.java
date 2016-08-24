package com.zhichen.parking.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证账号，密码等工具类
 */

public final class ToolRegexValidate {
    private static final String MOBIEL_PATTERN = "^(13|14|15|17|18)\\d{9}$";
    private static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{_2,}$";
    private static final String INPUT_PSWORD = "^[A-Za-z0-_9]{6,16}$";
    private static final String INPUT_CODE = "^[A-Za-z0-_9]{4}$";
    private static final String INPUT_NICKNAME = "[\\u4E00-\\u9FA5\\\\w]+";
    private static final String PSW="^(?=.*?[a-zA-Z]).{6,16}$";
//    private static final String INPUT_NICKNAME="[A-Za-z0-9_-]";
//    private static final String INPUT_NICKNAME="[A-Za-z0-9_-]|\\d|[\u4E00-\u9FA5]";

    private ToolRegexValidate() {
    }

    /**
     * 验证邮箱
     *
     * @param email 要验证的邮箱
     * @return
     */
    public static boolean checkEmail(String email) {
        return matches(email, EMAIL_PATTERN);
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return true 正确  false 错误
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        return matches(mobileNumber, MOBIEL_PATTERN);
    }

    private static boolean matches(String text, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);
        return matcher.matches();

    }

    /**
     * 校验输入昵称
     */
    public static boolean checkNickname(String nickname) {
        return matches(nickname, INPUT_NICKNAME);
    }

    public static boolean checkPsw(String psw) {
        return matches(psw, INPUT_PSWORD);
    }
    public static boolean checkPsww(String psw) {
        return matches(psw, PSW);
    }
    /**
     * 校验验证码
     */
    public static boolean checkCode(String code) {
        return matches(code, INPUT_CODE);
    }

    /**
     * 校验身份证号码是否符合规则
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * @param string
     * @return ｔｒｕｅ　标识是汉字，ｄａｌｓｅ表示不是汉字
     */
    public static boolean isChinese(String string) {
        boolean result = false;
        try {
            Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
            Matcher matcher = pattern.matcher(string);
            result = matcher.matches();
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * 判断字符是否是英文或数字，或-、_
     *
     * @return
     */

    public static boolean isEngOrNumOr(String string) {
        boolean result = false;
        try {
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]+$");
            Matcher matcher = pattern.matcher(string);
            result = matcher.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    /**
     * 中英文，数字，或-、_
     */
    public static boolean isEngOrCh(String string) {
        boolean result = false;
        try {
            Pattern pattern = Pattern.compile("^[_\\-A-Za-z0-9\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
            Matcher matcher = pattern.matcher(string);
            result = matcher.matches();
        } catch (Exception e) {
            result = false;
        }

        return result;
    }
}
