package com.zhichen.parking.lib.messageservice;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public abstract class OnMessageListener {

	private List<Integer>	mSupportEventTypeList ;
	
	public OnMessageListener(){
		mSupportEventTypeList = new ArrayList<Integer>();
	}
	
	public void setSupportEvent(Integer eventType, boolean isSupport)
	{
		if(isSupport){
			if( ! mSupportEventTypeList.contains(eventType)){
				mSupportEventTypeList.add(eventType);
			}
		}
		else{
			mSupportEventTypeList.remove(eventType);
		}
	}
	
	public boolean isSupportEvent(Integer eventType){
		return mSupportEventTypeList.contains(eventType);
	}
	
	public abstract void onMessage(Integer eventType, Bundle arguments);
}
