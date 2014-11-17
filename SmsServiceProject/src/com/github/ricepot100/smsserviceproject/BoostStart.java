package com.github.ricepot100.smsserviceproject;

import com.github.ricepot100.smsservice.smsdatabase.SMSDBService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BoostStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent s_intent = new Intent(context, SMSDBService.class);
		context.getApplicationContext().startService(s_intent);
	}

}
