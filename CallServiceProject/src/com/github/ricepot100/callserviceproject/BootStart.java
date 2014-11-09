package com.github.ricepot100.callserviceproject;

import com.github.ricepot100.callserviceproject.calllisten.CallingStatusService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent s_intent = new Intent(context, CallingStatusService.class);
		context.startService(s_intent);
	}

}
