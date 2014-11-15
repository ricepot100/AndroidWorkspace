package com.foxconn.otaupgradeproject.upgrade;

public interface OTA_DOWNLOAD_STATE {
	public static final int OTA_DOWNLOAD_STATE_INIT = -1;
	
	public static final int OTA_CONNECT_SERVER_FAIL = 0;
	public static final int OTA_CONNECT_SERVER_SUCCESS = 1;
	
	public static final int OTA_GET_FW_INFO_FAIL = 2;
	public static final int OTA_GET_FW_INFO_SUCCESS = 3;
	
	public static final int OTA_DOWNLOAD_START = 4;
	public static final int OTA_DOWNLOAD_DOING = 5;
	public static final int OTA_DOWNLOAD_DONE = 6;
}
