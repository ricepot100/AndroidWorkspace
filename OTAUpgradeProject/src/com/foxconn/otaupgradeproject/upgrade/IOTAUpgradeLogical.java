package com.foxconn.otaupgradeproject.upgrade;

import java.util.Map;
import java.util.Vector;

abstract public interface IOTAUpgradeLogical {
	abstract boolean connectServer(String url);
	abstract Map<String,String> getFWinfoFromOTAServer(String url);
	abstract boolean downloadFWFromOTAServer(String url, String save_path, String save_name);
	abstract boolean upgradeFW();
}


