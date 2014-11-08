package com.github.ricepot100.smsservice.smsdatabase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SMSDBService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}
}
