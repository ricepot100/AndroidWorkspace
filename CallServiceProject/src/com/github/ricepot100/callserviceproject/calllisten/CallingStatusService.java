package com.github.ricepot100.callserviceproject.calllisten;


import com.github.ricepot100.callserviceproject.Assistant;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class CallingStatusService extends Service {
	
	private PhoneStatusReceiver m_phoneStatusReceiver=null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Assistant.TAG, "CallingStatusService--->onCreate");
		m_phoneStatusReceiver = new PhoneStatusReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(m_phoneStatusReceiver, filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(Assistant.TAG, "CallingStatusService--->onStartCommand");
		MyThread myThread = new MyThread();
		Thread thread = new Thread(myThread);
		thread.start();
		return START_STICKY;
	}
	
	
	private class PhoneStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Intent phone_intent = new Intent(context, CallingListenerService.class);
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				String outPhone_number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				phone_intent.putExtra(Assistant.OutPhoneNumberExtra, outPhone_number);
			}
			context.startService(phone_intent);
		}
		
	}


	private class MyThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
				Intent intent = new Intent();
				intent.setAction("com.github.ricepot100.smsservice.smsdatabase");
				CallingStatusService.this.getApplicationContext().startService(intent);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		
	}
}
