package com.github.ricepot100.callmanager;

import android.text.format.Time;

import com.github.ricepot100.callmanager.storage.StorageAssistant;

public class ThreadHandleCalling implements Runnable {

	String m_str_filename = null;
	
	ThreadHandleCalling(String str_filename) {
		m_str_filename = str_filename;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Time time = new Time();
		time.setToNow();
		int hour = time.hour;
		int minute = time.minute;
		int sec = time.second;
		String audio_file_name = hour + "-" + minute + "-" + sec + "_" +  m_str_filename;
		StorageAssistant.StartRecordPhoneCalling(audio_file_name);
	}

}
