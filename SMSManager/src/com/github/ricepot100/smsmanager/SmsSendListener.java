package com.github.ricepot100.smsmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.ricepot100.smsmanager.storage.StorageAssistant;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SmsSendListener extends Service {
	
	private SMSSendBoxContentObserver m_smsDBSendBoxObserver = null;
	private static int s_max_id=-1;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		Log.d(Assistant.TAG, "SmsSendListener onCreate");
		m_smsDBSendBoxObserver = new SMSSendBoxContentObserver(new Handler());
		this.getContentResolver().registerContentObserver(
				Uri.parse(SMSDB_URI.SMS_URI_ALL), 
				true, m_smsDBSendBoxObserver);
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(Assistant.TAG, "SmsSendListener onStart");
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy(){
		Log.d(Assistant.TAG, "SmsSendListener onDestroy");
		if(null!=m_smsDBSendBoxObserver){
			this.getContentResolver().unregisterContentObserver(m_smsDBSendBoxObserver);
			m_smsDBSendBoxObserver = null;
		}
		super.onDestroy();
	}
	
	private class SMSSendBoxContentObserver extends ContentObserver {
		public SMSSendBoxContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			Cursor cr = SmsSendListener.this.getContentResolver().query(
					Uri.parse(SMSDB_URI.SMS_URI_SEND), 
					null, 
					null, 
					null, 
					SMSDB_COLUMN_INFO.NAME__ID + " asc");
			if (null != cr) {
				if (true == cr.moveToLast()){
					SmsSendListener.s_max_id = cr.getInt(SMSDB_COLUMN_INFO.INDEX__ID);
					cr.close();
				}
			}
			Log.d(Assistant.TAG, "s_max_id=" + s_max_id);
		}
		
		private void recordSendSms(Cursor cr){
			String sendAddress = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_ADDRESS));
			int sendPerson = cr.getInt(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_PERSON));
			String sendBody = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_BODY));
			Long l_sendDate = cr.getLong(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_DATE));
			Date d_sendDate = new Date(l_sendDate);
			SimpleDateFormat sdf_sendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			String sendDate = sdf_sendDate.format(d_sendDate);
			
			String sendMsm = "Send to: " + sendAddress + "(" + sendPerson + ")" + 
					" at: " + sendDate + "\n" + 
					sendBody;
			Log.d(Assistant.TAG, "sendMsm: " + sendMsm);
			StorageAssistant.WriteSmsToRecord(sendMsm);
		}
		
		@Override

		public void onChange(boolean selfChange) {
			Log.d(Assistant.TAG, "SMSSendBoxContentObserver onChange");
			int current_max_id = -1;
			Cursor cr = SmsSendListener.this.getContentResolver().query(
					Uri.parse(SMSDB_URI.SMS_URI_SEND), 
					null,										// all columns 
					null,									 	//
					null, 
					SMSDB_COLUMN_INFO.NAME__ID + " asc");		// sorted by id
			if (null != cr) {
				if (cr.moveToLast()) {
					current_max_id = cr.getInt(SMSDB_COLUMN_INFO.INDEX__ID);
				}
				
				if (current_max_id > SmsSendListener.s_max_id) {
					Log.d(Assistant.TAG, "You have send a message");
					recordSendSms(cr);
				}
				cr.close();
			}
			
			SmsSendListener.s_max_id = current_max_id;
			Log.d(Assistant.TAG, "onChange s_max_id=" + s_max_id);
		}
		
		
	}
}
