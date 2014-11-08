package com.github.ricepot100.smsservice.smsdatabase;

public interface SMSDB_URI {
	public final static String SMS_URI_ALL = "content://sms/";
	public final static String SMS_URI_INBOX = "content://sms/inbox";
	public final static String SMS_URI_SEND = "content://sms/sent";
	public final static String SMS_URI_DRAFT = "content://sms/draft";
	public final static String SMS_URI_OUTBOX = "content://sms/outbox";
	public final static String SMS_URI_FAILED = "content://sms/failed";
	public final static String SMS_URI_QUEUED = "content://sms/queued";
}
