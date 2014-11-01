package com.github.ricepot100.callmanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallingListenerService extends Service {
	
	private TelephonyManager m_telephonyManager=null;

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
}

class PhoneStateListenerCus extends PhoneStateListener {
	@Override
	public void onCallStateChanged(int state,String inPhone){
		switch(state){
		case TelephonyManager.CALL_STATE_IDLE:
			Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_IDLE");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_RINGING");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.d(Assistant.TAG, "onCallStateChanged CALL_STATE_OFFHOOK");
			break;
		default:
			Log.d(Assistant.TAG, "onCallStateChanged default: state="+state);
		}
	}
}