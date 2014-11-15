package com.foxconn.otaupgradeproject.upgrade;

abstract public interface IOTAUpgradeNotify {
	abstract int notifyDownloadSpeed(int speed);
	abstract int notifyDownloadPercent(int percent);
	abstract int notifyDownloadState(int state);
}
