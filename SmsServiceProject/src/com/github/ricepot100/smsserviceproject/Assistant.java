package com.github.ricepot100.smsserviceproject;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;


public class Assistant {
	public static final String TAG = "ricepot100";
	public static final String RootDirectory = "SMSServiceProject";		// /sdcard/SMSManagerProject
	public static final String SMSRecordFile = "SMSRecord.txt";		// /sdcard/SMSManager/SMSRecord.txt
	public static final String DebugFile = "SMSDebug.txt";
	
	public static final String CallintStatusServiceClassName = "com.github.ricepot100.callserviceproject.calllisten.CallingStatusService";
	public static final String CallingStatusServiceActionStart = "intent.action.start.CallingStatusService";
	
	
	public static boolean isServiceRunning(Context context, String service_class_name) {
		ActivityManager activeManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> services_info = activeManager.getRunningServices(250);
		Iterator<ActivityManager.RunningServiceInfo> iterator_service = services_info.iterator();
		while (iterator_service.hasNext()) {
			ActivityManager.RunningServiceInfo service_info_entry = iterator_service.next();
			if (service_info_entry.service.getClassName().equals(service_class_name))
				return true;
		}
		return false;
	}
}
