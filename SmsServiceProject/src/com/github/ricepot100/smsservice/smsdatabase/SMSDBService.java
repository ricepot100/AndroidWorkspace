package com.github.ricepot100.smsservice.smsdatabase;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

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
		m_handler = new Handler();
		m_SmsDBInBoxObserver = new SMSDBInBoxContentObserver(this, m_handler);
		m_SmsDBInBoxObserver.registerToContext();
		m_SmsDBSendBoxObserver = new SMSDBSendBoxContentObserver(this, m_handler);
		m_SmsDBSendBoxObserver.registerToContext();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
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
	}
}
