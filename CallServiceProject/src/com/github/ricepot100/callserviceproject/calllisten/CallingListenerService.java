package com.github.ricepot100.callserviceproject.calllisten;


import com.github.ricepot100.callserviceproject.Assistant;
import com.github.ricepot100.callserviceproject.storage.StorageAssistant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallingListenerService extends Service {
	
	private TelephonyManager m_telephonyManager=null;
	private PhoneStateListener m_phoneStateListener = new PhoneStateListenerCus();
	
	private String m_outphone_number = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(Assistant.TAG, "CallingListenerServer--->onCreate");	
		m_telephonyManager = (TelephonyManager)super.getSystemService(Context.TELEPHONY_SERVICE);
		m_telephonyManager.listen(m_phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) { 
		super.onStartCommand(intent, flags, startId);
		Log.d(Assistant.TAG, "CallingListenerService--->onStartCommand");
		/*
		synchronized(m_telephonyManager) {
			m_outphone_number = intent.getStringExtra(Assistant.OutPhoneNumberExtra);
			if (null != m_outphone_number) {
				m_telephonyManager.notify();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}*/
		if (null != intent.getStringExtra(Assistant.OutPhoneNumberExtra)) {
			m_outphone_number = intent.getStringExtra(Assistant.OutPhoneNumberExtra);
		}
		return START_STICKY;
	}
	
	@Override
	public void onDestroy(){
		Log.d(Assistant.TAG, "CallingListenerService--->onDestroy");
		StorageAssistant.WriteDebugToRecord("CallingListenerService--->onDestroy");
		m_telephonyManager.listen(m_phoneStateListener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}
	
	
	private class PhoneStateListenerCus extends PhoneStateListener {
		private String m_inPhone = null;
		private String m_outPhone = null;
		
		@Override
		public void onCallStateChanged(int state,String inPhone){
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				Log.d(Assistant.TAG, "PhoneStateListenerCus CALL_STATE_IDLE");
				m_inPhone = null;
				StorageAssistant.StopRecordPhoneCalling();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.d(Assistant.TAG, "PhoneStateListenerCus CALL_STATE_RINGING");
				StorageAssistant.WriteCMLogToRecord("PhoneStateListenerCus CALL_STATE_RINGING, call from: " + inPhone + "\n");
				m_inPhone = inPhone;			
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.d(Assistant.TAG, "PhoneStateListenerCus CALL_STATE_OFFHOOK");
				String str_promotion = null;
				if (null!=m_inPhone){
					StorageAssistant.WriteCMLogToRecord("PhoneStateListenerCus CALL_STATE_OFFHOOK: call from: " + m_inPhone + "\n");
					str_promotion = "from_" + m_inPhone + ".3gp";
					m_inPhone = null;
				}else {
					/*
					synchronized(m_telephonyManager) {
						try {
							m_telephonyManager.wait(1000*2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}*/
					m_outPhone = CallingListenerService.this.m_outphone_number;
					StorageAssistant.WriteCMLogToRecord("PhoneStateListenerCus CALL_STATE_OFFHOOK: call to: " + m_outPhone + "\n");
					str_promotion = "to_" + m_outPhone + ".3gp";
					m_outPhone = CallingListenerService.this.m_outphone_number = null;
				}
				ThreadHandleCalling thread_calling = new ThreadHandleCalling(str_promotion);
				Thread threadHandleCalling_calling = new Thread(thread_calling);
				threadHandleCalling_calling.start();
				break;
			default:
				Log.d(Assistant.TAG, "PhoneStateListenerCus default: state="+state);
				StorageAssistant.WriteCMLogToRecord("PhoneStateListenerCus default: state="+ state + "\n");
			}
		}
	}
}
