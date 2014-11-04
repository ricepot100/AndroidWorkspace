package com.github.ricepot100.contentassistant;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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

}
