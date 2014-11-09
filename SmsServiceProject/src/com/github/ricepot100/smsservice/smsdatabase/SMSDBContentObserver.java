package com.github.ricepot100.smsservice.smsdatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.ricepot100.smsservice.Assistant;
import com.github.ricepot100.smsservice.storage.StorageAssistant;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public abstract class SMSDBContentObserver extends ContentObserver {
	protected Context m_context = null;
	protected Handler m_handler = null;
	protected Uri	  m_uriObserver = null;
	protected int	  m_maxId = -1;

	public SMSDBContentObserver(Context context, Handler handler, Uri uriObserver) {
		super(handler);
		// TODO Auto-generated constructor stub
		m_context = context;
		m_handler = handler;
		m_uriObserver = uriObserver;
		
		Cursor cr = m_context.getContentResolver().query(m_uriObserver, 
				null,				// All column 
				null, 
				null, 
				SMSDB_COLUMN_INFO.NAME__ID + " asc");	// Sorted by _id
		if (null != cr) {
			if (cr.moveToLast()) {
				m_maxId = cr.getInt(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME__ID));
			}
			cr.close();
		}
	}
	
	protected abstract void doOnChangeAction(Cursor cr);
	
	public void registerToContext() {
		m_context.getContentResolver().registerContentObserver(Uri.parse(SMSDB_URI.SMS_URI_ALL), true, this);
	}
	
	public void unregisterFromContext() {
		m_context.getContentResolver().unregisterContentObserver(this);
	}
	
	@Override
	public void onChange(boolean selfChange){
		int currentMaxId = -1;
		Cursor cr = m_context.getContentResolver().query(m_uriObserver,
				null,										// all columns 
				null,									 	//
				null, 
				SMSDB_COLUMN_INFO.NAME__ID + " asc");		// sorted by id
		if(null != cr) {
			if(cr.moveToLast()) {
				currentMaxId = cr.getInt(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME__ID));
			}
			if (currentMaxId > m_maxId){
				doOnChangeAction(cr);
			}
			cr.close();
			m_maxId = currentMaxId;
		}
	}

}

class SMSDBInBoxContentObserver extends SMSDBContentObserver {
	
	private final static Uri s_f_uriSMSDBInBox = Uri.parse(SMSDB_URI.SMS_URI_INBOX);

	public SMSDBInBoxContentObserver(Context context, Handler handler) {
		super(context, handler, s_f_uriSMSDBInBox);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doOnChangeAction(Cursor cr) {
		// TODO Auto-generated method stub
		Log.d(Assistant.TAG, "Receive a SMS");
		String strReceiveAddress = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_ADDRESS));
		String strPerson = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_PERSON));
		
		Long longReceiveDate = cr.getLong(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_DATE)); 
		Date dateReceiveDate = new Date(longReceiveDate);
		SimpleDateFormat sdfReceiveDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String strReceiveDate = sdfReceiveDate.format(dateReceiveDate);
		
		String strReceiveBody = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_BODY));
		
		String strSmsTitle = "Receive from: " + strReceiveAddress + 
				"(" + strPerson + ")" +
				"; at:" + strReceiveDate + "\n";
		String strSmsContent = "Receive content: " + strReceiveBody + "\n"; 
		
		Log.d(Assistant.TAG, strSmsTitle);
		Log.d(Assistant.TAG, strSmsContent);
		
		StorageAssistant.WriteSmsToRecord(strSmsTitle + strSmsContent);
	}
	
}

class SMSDBSendBoxContentObserver extends SMSDBContentObserver {
	
	private final static Uri s_f_uriSMSDBSendBox = Uri.parse(SMSDB_URI.SMS_URI_SEND);

	public SMSDBSendBoxContentObserver(Context context, Handler handler) {
		super(context, handler, s_f_uriSMSDBSendBox);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doOnChangeAction(Cursor cr) {
		// TODO Auto-generated method stub
		String strSendAddress = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_ADDRESS));
		String strPerson = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_PERSON));
		
		Long longSendDate = cr.getLong(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_DATE)); 
		Date dateSendDate = new Date(longSendDate);
		SimpleDateFormat sdfSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String strSendDate = sdfSendDate.format(dateSendDate);
		
		String strSendBody = cr.getString(cr.getColumnIndex(SMSDB_COLUMN_INFO.NAME_BODY));
		
		String strSmsTitle = "Send to: " + strSendAddress + 
				"(" + strPerson + ")" + 
				"; at:" + strSendDate + "\n";
		String strSmsContent = "Send content: " + strSendBody + "\n";
		
		Log.d(Assistant.TAG, strSmsTitle);
		Log.d(Assistant.TAG, strSmsContent);
		
		StorageAssistant.WriteSmsToRecord(strSmsTitle + strSmsContent);
	}
	
}
