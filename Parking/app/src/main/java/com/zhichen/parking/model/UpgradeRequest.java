package com.zhichen.parking.model;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.tools.UpgradeTool;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UpgradeRequest {		
	
	private Context context;
		
	private UpgradeBean upgradeBean;
	private String apkPath;

	public UpgradeRequest(Context context){
		this.context = context;
	}

	public void checkUpgrade(String xmlUrl){
		new CheckUpgradeTask().execute(xmlUrl);
	}

	public boolean needUpgrade(UpgradeBean upgradeBean)
	{
		this.upgradeBean = upgradeBean ;
		if(upgradeBean.getVersionCode() != null){
			try{
				int code = Integer.parseInt(upgradeBean.getVersionCode());
				if(UpgradeTool.getVersionCode(context) < code){
					showUpgradeDialog();
					return true ;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		return false ;
	}

	private class CheckUpgradeTask extends AsyncTask<String, Void, UpgradeBean>{

		@Override
		protected UpgradeBean doInBackground(String... arg0){
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(arg0[0]);

			try {
				HttpResponse response = httpClient.execute(httpGet);
				Log.d("cwf"," UpgradeRequest  CheckUpgradeTask  response"+response);
				if(response.getStatusLine().getStatusCode() == 200){
					upgradeBean = UpgradeTool.parseXML(response.getEntity().getContent());
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return upgradeBean;
		}

		@Override
		protected void onPostExecute(UpgradeBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(null != result){
				needUpgrade(result);
			}
		}
	}

	private class DownloadApkTask extends AsyncTask<String, Integer, Void>{
		
		private ProgressDialog progressDialog;
		
		public DownloadApkTask(){
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("正在下载更新");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
		}
		
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(arg0[0]);
			InputStream is = null;
			OutputStream os = null;
			try {
				os = new FileOutputStream(apkPath);
				HttpResponse response = httpClient.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == 200){
					is = response.getEntity().getContent();
					//获得文件长度
					long fileSize = response.getEntity().getContentLength();
					int count = 0;
					int len = 0;
					byte[] buf = new byte[1024];
					while((len = is.read(buf)) != -1){
						count += len;
						int progress = (int)(count * 100 / fileSize);
						publishProgress(progress);
						os.write(buf, 0, len);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(null != is)
						is.close();
					if(null != os)
						os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			UpgradeTool.installApk(context, apkPath);
		}
		
	}

	private void showUpgradeDialog(){
		Builder builer = new Builder(context);
		builer.setTitle("检查到新版本，是否升级？");
		builer.setMessage(upgradeBean.getNestupdatepromt());
		 //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
		builer.setPositiveButton("马上升级", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					String filename = "upgrade.apk";
					apkPath = AppConstants.TEMP_DIR + "/" + filename;
					File file = new File(filename);
					if(!file.exists()){
						file.mkdirs();
					}
					new DownloadApkTask().execute(upgradeBean.getUri());
				}else{
					Toast.makeText(context, "没有存储卡", Toast.LENGTH_SHORT).show();
				}			
			}
		});
		builer.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

}
