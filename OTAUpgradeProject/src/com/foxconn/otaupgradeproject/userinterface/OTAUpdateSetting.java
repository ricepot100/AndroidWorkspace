package com.foxconn.otaupgradeproject.userinterface;

import android.os.SystemProperties;

public interface OTAUpdateSetting {
	public static String CONNECT_URL = SystemProperties.get("ro.product.otaupdateurl", "http://192.168.1.2:8080/PanHub.RDS.Lib.new/");
	public static String GET_FW_URL = CONNECT_URL + "Download/";
	public static String DOWNLOAD_PATH = "/data/data/com.foxconn.otaupgradeproject/";
	
	public static String TAG_PAYLOAD = "payload";
	
}
