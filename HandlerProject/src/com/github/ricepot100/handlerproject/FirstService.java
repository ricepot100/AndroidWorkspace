package com.github.ricepot100.handlerproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class FirstService extends Service {
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(Assistant.TAG, "FirstService--->onCreate");
		Message msg = new Message();
		msg.what = Assistant.MSG_FIRST_SERVER_01;
		m_handler.sendMessageDelayed(msg, 1000*10);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Assistant.TAG, "FirstService--->onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d(Assistant.TAG, "FirstService--->onDestroy");
		super.onDestroy();
	}
	
	private Handler m_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Assistant.MSG_FIRST_SERVER_01:
				Log.d(Assistant.TAG, "FirstService--->Handler--->MSG_FIRST_SERVER_01");
				break;
			case Assistant.MSG_FIRST_SERVER_02:
				Log.d(Assistant.TAG, "FirstService--->Handler--->MSG_FIRST_SERVER_02");
				break;
			default:
				Log.d(Assistant.TAG, "FirstService--->Handler--->Default");
				break;
			}
		}
	};

}
