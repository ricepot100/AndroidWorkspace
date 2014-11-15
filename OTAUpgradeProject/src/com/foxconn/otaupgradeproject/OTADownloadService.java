package com.foxconn.otaupgradeproject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.foxconn.otaupgradeproject.upgrade.DefaultOTAUpgradeLogical;
import com.foxconn.otaupgradeproject.upgrade.IOTAUpgradeLogical;
import com.foxconn.otaupgradeproject.upgrade.IOTAUpgradeNotify;
import com.foxconn.otaupgradeproject.upgrade.OTA_DOWNLOAD_STATE;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class OTADownloadService extends Service {

	private static final String CONNECT_URL = "http://192.168.1.2:8080/PanHub.RDS.Lib.new/";
	private static final String GET_FW_INFO_URL = "http://192.168.1.2:8080/PanHub.RDS.Lib.new/GetSWUpgradeInfo";
	private static final String DOWNLOAD_FW_URL = "http://192.168.1.2:8080/PanHub.RDS.Lib.new/Download/";
	
	private IOTAUpgradeLogical m_ota_upgrade_logical = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		IntentFilter intent_filter = new IntentFilter();
		intent_filter.addAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_STATE);
		intent_filter.addAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_SPEED);
		intent_filter.addAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_PERCENT);
		registerReceiver(m_download_receiver, intent_filter);
		
		IOTAUpgradeNotify notify = new AndroidDefaultOTAUpgradeNotify(this);
		m_ota_upgrade_logical = new DefaultOTAUpgradeLogical(notify);
		m_ota_upgrade_logical.connectServer(CONNECT_URL);
		Map<String,String> map_fw_info = m_ota_upgrade_logical.getFWinfoFromOTAServer(GET_FW_INFO_URL);
		Set<Map.Entry<String, String>> set_entrys = map_fw_info.entrySet();
		Iterator<Map.Entry<String, String>> iterator = set_entrys.iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			Log.d(MainActivity.TAG, "key(" + entry.getKey() + "):" + "value(" + entry.getValue() + ")");
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return startId;
	}
	
	@Override
	public void onDestroy(){
		unregisterReceiver(m_download_receiver);
	}
	
	private BroadcastReceiver m_download_receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(MainActivity.TAG, "action=" + action);
			if (action.equals(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_STATE)) {
				int state = intent.getIntExtra(
						OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_STATE_EXT_KEY,
						OTA_DOWNLOAD_STATE.OTA_DOWNLOAD_STATE_INIT);
				Log.d(MainActivity.TAG, "state=" + state);
			} else if (action.equals(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_SPEED)) {
				int speed = intent.getIntExtra(
						OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_SPEED_EXT_KEY, 
						0);
				Log.d(MainActivity.TAG, "speed=" + speed);
			} else if (action.equals(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_PERCENT)) {
				int percent = intent.getIntExtra(
						OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_PERCENT_EXT_KEY, 
						0);
				Log.d(MainActivity.TAG, "percent=" + percent);
			}						
		}
		
	};

}
