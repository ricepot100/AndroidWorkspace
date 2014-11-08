package com.github.ricepot100.smsservice.smsdatabase;

public interface SMSDB_COLUMN_INFO {
	public final static int INDEX__ID = 0;
	public final static String NAME__ID = "_id";
	
	public final static int INDEX_THREAD_ID = 1;
	public final static String NAME_THREAD_ID = "thread_id";
	
	public final static int INDEX_ADDRESS = 2;
	public final static String NAME_ADDRESS = "address";
	
	public final static int INDEX_PERSON = 3;
	public final static String NAME_PERSON = "person";
	
	public final static int INDEX_DATE = 4;
	public final static String NAME_DATE = "date";
	
	public final static int INDEX_DATE_SEND = 5;
	public final static String NAME_DATE_SEND = "date_send";
	
	public final static int INDEX_PROTOCOL = 6;
	public final static String NAME_PROTOCOL = "protocol";
	
	public final static int INDEX_READ = 7;
	public final static String NAME_READ = "read";
	
	public final static int INDEX_STATUS = 8;
	public final static String NAME_STATUS = "status";
	
	public final static int INDEX_TYPE = 9;
	public final static String NAME_TYPE = "type";
	
	
	public final static int INDEX_REPLY_PATH_PRESENT = 10;
	public final static String NAME_REPLY_PATH_PRESENT = "reply_path_present";
	
	public final static int INDEX_SUBJECT = 11;
	public final static String NAME_SUBJECT = "subject";
	
	public final static int INDEX_BODY = 12;
	public final static String NAME_BODY = "body";
	
	public final static int INDEX_SERVICE_CENTER = 13;
	public final static String NAME_SERVICE_CENTER = "service_center";
	
	public final static int INDEX_LOCKED = 14;
	public final static String NAME_LOCKED = "locked";
	
	public final static int INDEX_ERROR_CODE = 15;
	public final static String NAME_ERROR_CODE = "error_code";
	
	public final static int INDEX_SEEN = 16;
	public final static String NAME_SEEN = "seen";
}
