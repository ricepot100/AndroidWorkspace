package com.github.ricepot100.callserviceproject.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



import com.github.ricepot100.callserviceproject.Assistant;

import android.media.MediaRecorder;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public class StorageAssistant {
	private static String s_ExtStorageAbsoluteDirectory = null;
	private static String s_RootStorageAbsoluteDirectory = null;
	private static String s_RootRecordFile = null;
	
	private static String s_RootCallingAudioAbsoluteDirectory = null;
	
	private static MediaRecorder s_recorder = null;
	
	private static boolean createInitStorage() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File f_extStorage = Environment.getExternalStorageDirectory();
			s_ExtStorageAbsoluteDirectory = f_extStorage.getAbsolutePath();
			
			File f_RootStorage = new File(s_ExtStorageAbsoluteDirectory + "/" +
					Assistant.RootDirectory);
			if (!f_RootStorage.exists()) {
				if (f_RootStorage.mkdirs()) {
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
	
	public static void WriteCMLogToRecord(String text) {
		if (StorageAssistant.createInitStorage()) {
			s_RootRecordFile = s_RootStorageAbsoluteDirectory + "/" + Assistant.LogFile;
			try {
				FileWriter fw_record = new FileWriter(s_RootRecordFile, true);
				fw_record.write(text);
				fw_record.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static boolean createCallingStorage() {
		if (!StorageAssistant.createInitStorage()) {
			return false;
		}
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month + 1;
		int day = time.monthDay;
		String directory_name = year + "-" + month + "-" + day;
		String directory_absolute_name = s_RootStorageAbsoluteDirectory + "/" + directory_name;
		
		File f_dir_today = new File(directory_absolute_name);
		if (!f_dir_today.exists()) {
			if (f_dir_today.mkdirs()) {
				s_RootCallingAudioAbsoluteDirectory = f_dir_today.getAbsolutePath();
			} else {
				Log.d(Assistant.TAG, "Can not create directory: " + f_dir_today.getAbsolutePath());
				return false;
			}
		} else if (f_dir_today.isDirectory()) {
			s_RootCallingAudioAbsoluteDirectory = f_dir_today.getAbsolutePath();
		} else {
			return false;
		}
		return true;
	}

	public static void StartRecordPhoneCalling(String file_name) {
		if (!StorageAssistant.createCallingStorage()) {
			return;
		}
		
		s_recorder = new MediaRecorder();
		s_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		s_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		s_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		s_recorder.setOutputFile(s_RootCallingAudioAbsoluteDirectory + "/" + file_name);
		Log.d(Assistant.TAG, "Audio Description: " + s_recorder.toString());
		
		try {
			s_recorder.prepare();
			s_recorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	public static void StopRecordPhoneCalling() {
		if (null != s_recorder) {
			Log.d(Assistant.TAG, "Stop record audio");
			Log.d(Assistant.TAG, "Audio Description: " + s_recorder.toString());
			s_recorder.stop();
			s_recorder.release();
			s_recorder = null;
		}
	}

}
