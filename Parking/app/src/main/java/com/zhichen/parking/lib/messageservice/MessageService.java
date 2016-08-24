package com.zhichen.parking.lib.messageservice;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

	private static MessageService ins = new MessageService();

	private List<OnMessageListener> mMsgListenerList;

	private MessageService() {
		mMsgListenerList = new ArrayList<OnMessageListener>();
	}

	public static MessageService instance() {
		return ins;
	}

	public void addMessageListener(OnMessageListener listener)
	{
		synchronized(this)
		{
			if (mMsgListenerList.contains(listener)) {
				return;
			}
			mMsgListenerList.add(listener);
		}
	}

	public void removeMessageListener(OnMessageListener listener)
	{
		synchronized(this)
		{
			if (!mMsgListenerList.contains(listener)) {
				return;
			}
			mMsgListenerList.remove(listener);
		}
	}

	public void notifyMessage(Integer eventType, Bundle arguments)
	{
		for (OnMessageListener listener : mMsgListenerList) {
			if(listener.isSupportEvent(eventType)){
				listener.onMessage(eventType, arguments);
			}
		}
	}
}
