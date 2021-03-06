package com.github.ricepot100.callserviceproject;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

public class Assistant {
	public static final String TAG = "ricepot100";
	public static final String RootDirectory = "CallSericeProject";		// /sdcard/SMSManage
	public static final String LogFile = "cmlog.txt";
	public static final String DebugFile = "debug.txt";
	
	public static final String OutPhoneNumberExtra = "outphone_number";
	
	public static final String SMSDBServiceClassName = "com.github.ricepot100.smsservice.smsdatabase.SMSDBService";
	public static final String SMSDBServiceActionStart = "intent.action.start.smsdatabase";
	
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
