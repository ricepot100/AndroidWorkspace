package com.github.ricepot100.smsmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.telephony.SmsMessage;
import android.util.Log;

public class ThreadHandlePdu implements Runnable {

	private byte[] m_pdumessage;
	public ThreadHandlePdu(Object pdu) {
		m_pdumessage = (byte[])pdu;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SmsMessage sms = SmsMessage.createFromPdu(m_pdumessage);
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
