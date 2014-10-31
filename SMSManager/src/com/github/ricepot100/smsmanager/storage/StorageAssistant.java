package com.github.ricepot100.smsmanager.storage;

import java.io.File;

import com.github.ricepot100.smsmanager.Assistant;

import android.os.Environment;
import android.util.Log;

public class StorageAssistant {
	private static String s_ExtStorageAbsoluteDirectory = null;
	private static String s_RootStorageAbsoluteDirectory = null;
	public static boolean createInitStorage() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File f_extStorage = Environment.getExternalStorageDirectory();
			s_ExtStorageAbsoluteDirectory = f_extStorage.getAbsolutePath();
			
			File f_RootStorage = new File(s_ExtStorageAbsoluteDirectory + "/" +
					Assistant.RootDirectory);
			if (!f_RootStorage.exists()) {
				if (true == f_RootStorage.mkdirs()) {
					s_RootStorageAbsoluteDirectory = f_RootStorage.getAbsolutePath();
				} else {
					Log.d(Assistant.TAG, "Can not create direcotry for " + f_RootStorage.getAbsolutePath());
					return false;
				}
			} else if (f_RootStorage.isDirectory()) {
				s_RootStorageAbsoluteDirectory = f_RootStorage.getAbsolutePath();
			} else {
				Log.d(Assistant.TAG, "The file " + f_RootStorage.getAbsolutePath() + " exist, and not a directory");
				return false;
			}
		} else {
			Log.d(Assistant.TAG, "Have no external storage media");
			return false;
		}
		
		return true;
	}
	
	public static void WriteToRecord(String text) {
		if (StorageAssistant.createInitStorage()) {
			
		}
	}
}
