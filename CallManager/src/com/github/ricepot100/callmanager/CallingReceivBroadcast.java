package com.github.ricepot100.callmanager;

import com.github.ricepot100.callmanager.storage.StorageAssistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallingReceivBroadcast extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(Assistant.TAG, "onReceiver: action: " + intent.getAction());
		StorageAssistant.WriteCMLogToRecord("onReceiver: action: " + intent.getAction() + "\n");
		Intent phone_intent = new Intent(context, CallingListenerService.class);
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String outPhone_number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);  
			phone_intent.putExtra(Assistant.OutPhoneNumberExtra, outPhone_number);
			StorageAssistant.WriteCMLogToRecord("Call out phone_number:  " + outPhone_number + "\n");
		}	
		context.startService(phone_intent);
	}
}
