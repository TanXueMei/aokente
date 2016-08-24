package com.zhichen.parking.tools;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProUitl {

	public static boolean isPhoneNumber(String phone)
	{
		if(phone == null || phone.trim().length() != 11){
			return false ;
		}
		return TextUtils.isDigitsOnly(phone) ;
	}
	
	public static String checkPasswordInfo(String password, String surePassword)
	{
		if(password == null || password.trim().isEmpty()){
			return "不能为空";
		}
		int len = password.trim().length();
		if(len < 6 || len > 16){
			return "位数不正确";
		}
//		String str="^(?=.*?[a-zA-Z])[a-zA-Z0-9]{6,16}$";//包含字母，只能是字母和数字
//		String str="^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";//必须包含数字和字母，而且是数字与字母的组合
//		String str="^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,16}$";//包含字母和数字即可
//		String str="^(?=.*?[a-zA-Z]).{6,16}$";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(password.trim());
//        boolean isMatch = m.matches();
		if( ! ToolRegexValidate.checkPsww(password.trim())){
			return "格式不对";
		}
		if( ! password.trim().equals(surePassword)){
			return "两次输入不一致";
		}
		return null ;
	}
	
	public static String checkPayPasswordInfo(String password, String surePassword)
	{
		if(password == null || password.trim().isEmpty()){
			return "不能为空";
		}
		int len = password.trim().length();
		if(len != 6){
			return "位数不正确";
		}
		String str="^[0-9]{6}$";
		Pattern p = Pattern.compile(str);     
		Matcher m = p.matcher(password.trim()); 
		boolean isMatch = m.matches();
		if( ! isMatch){
			return "格式不对";
		}
		if( ! password.trim().equals(surePassword)){
			return "两次输入不一致";
		}
		return null ;
	}
	
}
