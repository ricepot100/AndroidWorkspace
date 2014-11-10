package com.github.ricepot100.contentassistant;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ContentHelper {
	public static Vector<String> getTableColumnName(Context context, Uri uri) {
		Vector<String> vs = new Vector<String>();
		Cursor cr = context.getContentResolver().query(uri, null, null, null, null);
		if (null != cr) {
			for (int i=0; i<cr.getColumnCount(); i++) {
				String columnName = cr.getColumnName(i);
				vs.add(columnName);
			}
		}
		return vs;
	}
	
	public static Vector<Map<String, String>> getTableItem(Context context, Uri uri) {
		Vector<Map<String, String>> vec_table = new Vector<Map<String, String>>();
		Cursor cr = context.getContentResolver().query(uri, null, null, null, null);
		if (null != cr) {
			int itemCount = cr.getCount();
			int columnCount = cr.getColumnCount();
			for (int itemIndex=0; itemIndex<itemCount; itemIndex++) {
				Map<String, String> map_one_raw = new HashMap<String, String>();
				for (int columnIndex=0; columnIndex<columnCount; columnIndex++) {
					Log.d(Assistant.TAG, "For the column");
					String columnName = cr.getColumnName(columnIndex);
					int columnType = cr.getType(columnIndex);
					//String itemValue ="" + cr.getString(columnIndex);
					Log.d(Assistant.TAG, columnName + ":" + columnType);
					//map_one_raw.put(columnName, itemValue);
				}
				vec_table.add(map_one_raw);
			}
		}
		
		return vec_table;
	}

}
