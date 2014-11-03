package com.github.ricepot100.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsReceiveBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// When a sms is coming, enter this broadcast
		Intent s_intent = new Intent(context, SmsSendListener.class);
		context.startService(s_intent);
		Object pdus[] = (Object[])intent.getExtras().get("pdus");
		Log.d(Assistant.TAG, "sms length=" + pdus.length);
		ThreadHandlePdu thread = new ThreadHandlePdu(pdus[0]);
		Thread threadHandlePdu = new Thread(thread);
		threadHandlePdu.start();
	}

}
