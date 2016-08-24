package com.zhichen.parking.fragment.homepage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.zhichen.parking.model.ParkingLot;
import com.zhichen.parking.servercontoler.ParkingControler;


import java.util.ArrayList;
import java.util.List;

public class ParkingLotTask extends AsyncTask<Void, Integer, String>{

	private ProgressDialog mProgressDialog;
	private Context mContext ;
	private boolean mShowProgress = true ;
	
	private List<String> idList ; 
	private OnResultListener mOnResultListener ;
	private List<ParkingLot> lotList ;
	List<String> failedIdList ;
	
	public ParkingLotTask(Context context)
	{
		mContext = context ;
		idList =  new ArrayList<>();
	}
	
	public void addParkingLot(long id)
	{
		idList.add(String.valueOf(id));
	}
	
	public void addParkingLots(List<String> ids)
	{
		idList.addAll(ids);
	}
	
	public boolean isShowProgress() {
		return mShowProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.mShowProgress = showProgress;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!mShowProgress){
			return ; 
		}
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
		}
		mProgressDialog.setMessage("正努力获取数据中，请稍等。。。");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		for(String id : idList){
			ParkingLot lot = ParkingControler.getParkingLotById(id);
			if(lot == null){
				if(failedIdList == null){
					failedIdList = new ArrayList<>();
				}
				failedIdList.add(id);
				continue ;
			}
			if(lotList == null){
				lotList = new ArrayList<>();
			}
			lotList.add(lot);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		if(mOnResultListener != null){
			mOnResultListener.onResult(lotList, failedIdList);
		}
	}
	
	public void setOnResultListener(OnResultListener listener)
	{
		this.mOnResultListener = listener ;
	}

	public interface OnResultListener
	{
		public void onResult(List<ParkingLot> lotList, List<String> failedIdList);
	}
}
