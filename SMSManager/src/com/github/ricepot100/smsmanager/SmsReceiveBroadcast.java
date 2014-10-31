package com.github.ricepot100.smsmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiveBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object pdus[] = (Object[])intent.getExtras().get("pdus");
		Log.d(Assistant.TAG, "sms length=" + pdus.length);
		byte[] pdusmessage = (byte[])pdus[0];
		SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
		String phone_number = sms.getOriginatingAddress();
		String sms_content = sms.getMessageBody();
		Date sms_date = new Date(sms.getTimestampMillis());
		SimpleDateFormat sms_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sms_time = sms_date_format.format(sms_date);
		
		Log.d(Assistant.TAG, "You receive sms from: " + phone_number +
				"; at:" + sms_time );
		Log.d(Assistant.TAG, "You receive sms content: " + sms_content);
	}

}
