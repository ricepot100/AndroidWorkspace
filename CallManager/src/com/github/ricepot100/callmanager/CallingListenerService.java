package com.github.ricepot100.callmanager;

import com.github.ricepot100.callmanager.storage.StorageAssistant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallingListenerService extends Service {
	
	private TelephonyManager m_telephonyManager=null;
	
	private String m_outphone_number = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(Assistant.TAG, "CallingListenerServer onCreate");
		m_telephonyManager = (TelephonyManager)super.getSystemService(Context.TELEPHONY_SERVICE);
		m_telephonyManager.listen(new PhoneStateListenerCus(), PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent,int startId){ 
		m_outphone_number = intent.getStringExtra(Assistant.OutPhoneNumberExtra);
		super.onStart(intent, startId);
	}
	
	private class PhoneStateListenerCus extends PhoneStateListener {
		private String m_inPhone = null;
		private String m_outPhone = null;
		
		@Override
		public void onCallStateChanged(int state,String inPhone){
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_IDLE");
				m_inPhone = null;
				StorageAssistant.WriteCMLogToRecord("onCallStateChanged CALL_STATE_IDLE" + "\n");
				StorageAssistant.StopRecordPhoneCalling();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_RINGING");
				StorageAssistant.WriteCMLogToRecord("onCallStateChanged CALL_STATE_RINGING, call from: " + inPhone + "\n");
				m_inPhone = inPhone;			
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_OFFHOOK");
				String str_promotion = null;
				if (null!=m_inPhone){
					StorageAssistant.WriteCMLogToRecord("onCallStateChanged CALL_STATE_OFFHOOK: call from: " + m_inPhone + "\n");
					str_promotion = "from_" + m_inPhone;
					m_inPhone = null;
				}else {
					StorageAssistant.WriteCMLogToRecord("onCallStateChanged CALL_STATE_OFFHOOK: call to: " + CallingListenerService.this.m_outphone_number + "\n");
					str_promotion = "to_" + CallingListenerService.this.m_outphone_number;
				}
				ThreadHandleCalling thread_calling = new ThreadHandleCalling(str_promotion);
				Thread threadHandleCalling_calling = new Thread(thread_calling);
				threadHandleCalling_calling.start();
				break;
			default:
				Log.d(Assistant.TAG, "onCallStateChanged default: state="+state);
				StorageAssistant.WriteCMLogToRecord("onCallStateChanged default: state="+ state + "\n");
			}
		}
	}
}
