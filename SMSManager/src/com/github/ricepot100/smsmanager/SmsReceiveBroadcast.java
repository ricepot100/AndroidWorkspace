package com.github.ricepot100.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsReceiveBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object pdus[] = (Object[])intent.getExtras().get("pdus");
		Log.d(Assistant.TAG, "sms length=" + pdus.length);
	}

}
