package com.foxconn.otaupgradeproject;

import android.content.Context;
import android.content.Intent;

import com.foxconn.otaupgradeproject.upgrade.IOTAUpgradeNotify;

interface OTA_ANDROID_BC_ACTION_TO_USER {
	public static final String ACT_DOWNLOAD_STATE = "intent.user.download_fw_percent";
	public static final String ACT_DOWNLOAD_STATE_EXT_KEY = "DownloadState";
	
	public static final String ACT_DOWNLOAD_FW_PERCENT = "intent.user.download_fw_percent";
	public static final String ACT_DOWNLOAD_FW_PERCENT_EXT_KEY = "DownloadPercent";
	
	public static final String ACT_DOWNLOAD_FW_SPEED = "intent.user.download_fw_speed";
	public static final String ACT_DOWNLOAD_FW_SPEED_EXT_KEY = "DownloadSpeed";
}


public class AndroidDefaultOTAUpgradeNotify implements IOTAUpgradeNotify {
	
	private Context m_context;
	
	public AndroidDefaultOTAUpgradeNotify(Context context) {
		m_context = context;
	}

	@Override
	public int notifyDownloadSpeed(int speed) {
		Intent speed_intent = new Intent();
		speed_intent.setAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_SPEED);
		speed_intent.putExtra(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_SPEED_EXT_KEY, speed);
		m_context.sendBroadcast(speed_intent);
		return speed;
	}

	@Override
	public int notifyDownloadPercent(int percent) {
		Intent percent_intent = new Intent();
		percent_intent.setAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_PERCENT);
		percent_intent.putExtra(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_FW_PERCENT_EXT_KEY, percent);
		m_context.sendBroadcast(percent_intent);
		return 0;
	}

	@Override
	public int notifyDownloadState(int state) {
		Intent state_intent = new Intent();
		state_intent.setAction(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_STATE);
		state_intent.putExtra(OTA_ANDROID_BC_ACTION_TO_USER.ACT_DOWNLOAD_STATE_EXT_KEY, state);
		return 0;
	}

}
