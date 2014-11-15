package com.github.ricepot100.handlerproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SecondService extends Service {
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(Assistant.TAG, "SecondService--->onCreate");
		Message msg = new Message();
		msg.what = Assistant.MSG_SECOND_SERVER_01;
		m_handler.sendMessageDelayed(msg, 1000*10);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Assistant.TAG, "SecondService--->onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d(Assistant.TAG, "SecondService--->onDestroy");
		super.onDestroy();
	}
	
	private Handler m_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Assistant.MSG_SECOND_SERVER_01:
				Log.d(Assistant.TAG, "SecondService--->Handler--->MSG_FIRST_SERVER_01");
				break;
			case Assistant.MSG_SECOND_SERVER_02:
				Log.d(Assistant.TAG, "SecondService--->Handler--->MSG_FIRST_SERVER_02");
				break;
			case Assistant.MSG_SECOND_SERVER_03:
				Log.d(Assistant.TAG, "SecondService--->Handler--->MSG_FIRST_SERVER_03");
				break;
			default:
				Log.d(Assistant.TAG, "SecondService--->Handler--->Default");
				break;
			}
		}
	};

}
