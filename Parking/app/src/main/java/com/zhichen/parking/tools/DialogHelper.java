package com.zhichen.parking.tools;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zhichen.parking.R;


public class DialogHelper {

	/**
	 * 创建一个有按钮的对话框式
	 * @param context
	 * @param message
	 * @param listener
	 * @return
	 */
	public static Dialog createHintDialog(Context context , String message , DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
			.setTitle("温馨提醒") 
			.setPositiveButton("确定", listener);
		return builder.create() ;
	}
	
	public static Dialog createQueryDialog(Context context, String message, String negativeText, String positiveText, 
			DialogInterface.OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
		.setTitle("温馨提醒") 
		.setNegativeButton(negativeText, listener)
		.setPositiveButton(positiveText, listener);
		return builder.create() ;
	}
	
	public static Dialog createModifyDialog(Context context, View modifyView, String title, String negativeText, String positiveText, 
			DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(modifyView);
		builder.setTitle(title) 
		.setNegativeButton(negativeText, listener)
		.setPositiveButton(positiveText, listener);
		return builder.create() ;
	}
	
	public static void showModifyDialog(Context context, String title, String message, 
			String negativeText, String positiveText, final OnModifyCallback callback)
	{
		View modifyView = LayoutInflater.from(context).inflate(R.layout.dialog_edittext, null);
		final EditText et = (EditText) modifyView.findViewById(R.id.dialog_modify_et);
		et.setText(message);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == Dialog.BUTTON_POSITIVE){
					String result = et.getText().toString();
					callback.onResult(result);
				}
			}
		};
		Dialog dialog = DialogHelper.createModifyDialog(context, modifyView, title, negativeText, positiveText, listener);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public interface OnModifyCallback {
		void onResult(String result);
	}
	
	public static ProgressDialog showProgressDialog(Context context, String hint, boolean cancelable)
	{
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(hint);
		dialog.setCancelable(cancelable);
		dialog.show();
		return dialog ;
	}
	
	public static ProgressDialog showProgressDialog(Context context, final AsyncTask task, String hint)
	{
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(hint);
		dialog.setCancelable(true);//设置进度条是否可以按退回键取消
		dialog.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				task.cancel(true);
			}
		});
		dialog.show();
		return dialog ;
	}
	
}
