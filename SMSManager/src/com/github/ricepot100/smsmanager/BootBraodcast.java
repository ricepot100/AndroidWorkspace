package com.github.ricepot100.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBraodcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent s_intent = new Intent(context, SmsSendListener.class);
		context.startService(s_intent);
	}

}
