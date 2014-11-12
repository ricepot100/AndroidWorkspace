package com.github.ricepot100.smsservice.smsdatabase;

import com.github.ricepot100.smsservice.Assistant;
import com.github.ricepot100.smsservice.storage.StorageAssistant;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SMSDBService extends Service {
	
	private SMSDBInBoxContentObserver m_SmsDBInBoxObserver = null;
	private SMSDBSendBoxContentObserver m_SmsDBSendBoxObserver = null;
	private Handler m_handler = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Assistant.TAG, "SMSDBService--->onCreate");
		m_handler = new Handler();
		m_SmsDBInBoxObserver = new SMSDBInBoxContentObserver(this, m_handler);
		m_SmsDBInBoxObserver.registerToContext();
		m_SmsDBSendBoxObserver = new SMSDBSendBoxContentObserver(this, m_handler);
		m_SmsDBSendBoxObserver.registerToContext();
		MyThread myThread = new MyThread();
		Thread thread = new Thread(myThread);
		thread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		Log.d(Assistant.TAG, "SMSDBService--->onStartCommand");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if(null != m_SmsDBInBoxObserver) {
			m_SmsDBInBoxObserver.unregisterFromContext();
			m_SmsDBInBoxObserver=null;
		}
		if (null != m_SmsDBSendBoxObserver) {
			m_SmsDBSendBoxObserver.unregisterFromContext();
			m_SmsDBSendBoxObserver = null;
		}
		
		StorageAssistant.WriteDebugToRecord("SMSDBService--->onDestroy" + "\n");
	}
	
	private class MyThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			do {
				try {
					Thread.sleep(5000);
					Intent intent = new Intent();
					intent.setAction("com.github.ricepot100.callserviceproject.calllisten.CallingStatusService");
					SMSDBService.this.getApplicationContext().startService(intent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while(true);
		}
		
	}
}
