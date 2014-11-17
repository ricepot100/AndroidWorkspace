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
	
	public static String getOneItemValueFromColumn(Cursor cr, int columnIndex) {
		String value=null;
		int columnType = cr.getType(columnIndex);
		switch(columnType) {
		case Cursor.FIELD_TYPE_NULL:
			value=null;
			break;
		case Cursor.FIELD_TYPE_INTEGER:
			value = String.valueOf(cr.getInt(columnIndex));
			break;
		case Cursor.FIELD_TYPE_FLOAT:
			value = String.valueOf(cr.getFloat(columnIndex));
			break;
		case Cursor.FIELD_TYPE_STRING:
			value = cr.getString(columnIndex);
			break;
		case Cursor.FIELD_TYPE_BLOB:
			value = "No support for blob";
			break;
		default:
			value = "known type";	
			break;
		}
		return value;
	}
	
	public static Vector<Map<String, String>> getTableItem(Context context, Uri uri) {
		Vector<Map<String, String>> vec_table_items = new Vector<Map<String, String>>();
		Cursor cr = context.getContentResolver().query(uri, null, null, null, null);
		if (null != cr) {
			int itemCount = cr.getCount();
			int columnCount = cr.getColumnCount();
			while(cr.moveToNext()) { //对于每一行
				Log.d(Assistant.TAG, "For the row");
				Map<String, String> items = new HashMap<String, String>();
				for (int columnIndex=0; columnIndex<columnCount; columnIndex++) { //对于每一列
					String columnName = cr.getColumnName(columnIndex);
					String columnValue = getOneItemValueFromColumn(cr, columnIndex);
					items.put(columnName, columnValue);
				}
				vec_table_items.add(items);
			}
		}
		return vec_table_items;
	}

	public static Vector<Map<String, String>> getRowItemsFromAColumnValue(Context context, Uri uri, String columnName, String columnValue) {
		Vector<Map<String, String>> vec_rows_items = new Vector<Map<String, String>>();
		return vec_rows_items;
	}
}
