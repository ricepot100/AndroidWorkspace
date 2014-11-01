package com.github.ricepot100.callmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallingReceivBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(Assistant.TAG, "onReceiver: action: " + intent.getAction());
		context.startService(new Intent(context, CallingListenerService.class));
	}

}
