package com.foxconn.otaupgradeproject.upgrade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.foxconn.otaupgradeproject.userinterface.OTAUpdateSetting;

import android.util.Xml;

interface THREAD_DOWNLOAD_STATE {
	public static final int THREAD_DOWNLOAD_INIT = 0;
	public static final int THREAD_DOWNLOAD_RESET = 1;
	public static final int THREAD_DOWNLOAD_NETWORK_CHECKING = 2;
	public static final int THREAD_DOWNLOAD_START = 3;
	public static final int THREAD_DOWNLOAD_DONE = 4;
}

public class DefaultOTAUpgradeLogical implements IOTAUpgradeLogical {

	private IOTAUpgradeNotify m_notify = null;
	
	public DefaultOTAUpgradeLogical(IOTAUpgradeNotify notify) {
		m_notify = notify;
	}
	
	@Override
	public boolean connectServer(String url) {
		// TODO Auto-generated method stub
		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_CONNECT_SERVER_SUCCESS);
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_CONNECT_SERVER_FAIL);
		return false;
	}

	private InputStream getInputStreamFromURI(String url) {
		InputStream inputStream = null;
		HttpGet httpRequest = new HttpGet(url);
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				inputStream = httpResponse.getEntity().getContent();
				return inputStream;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Map<String,String> getFWinfoFromOTAServer(String url) {
		InputStream input_stream = getInputStreamFromURI(url);
		if (null == input_stream) {	
			m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_GET_FW_INFO_FAIL);
			return null;
		}
		XmlPullParser xml_parser = Xml.newPullParser();
		Map<String,String> map_pay_load = null;
		try {
			xml_parser.setInput(input_stream, "UTF-8");
			int event = xml_parser.getEventType();
			while(event!=XmlPullParser.END_DOCUMENT) {
				switch(event) {
				case XmlPullParser.START_DOCUMENT:
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.START_TAG:
					if (OTAUpdateSetting.TAG_PAYLOAD.equals(xml_parser.getName())) {
						map_pay_load = new HashMap<String,String>();
						int attr_number = xml_parser.getAttributeCount();
						for (int attr_index=0; attr_index<attr_number; attr_index++) {
							String attr_key = xml_parser.getAttributeName(attr_index);
							String attr_value = xml_parser.getAttributeValue(attr_index);
							map_pay_load.put(attr_key, attr_value);
						}
					}
					m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_GET_FW_INFO_SUCCESS);
					return map_pay_load;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_GET_FW_INFO_FAIL);
		return null;	
	}

	@Override
	public boolean downloadFWFromOTAServer(String url, String save_path, String save_name) {
		MultiThreadDownload download_multthread_mgr = new MultiThreadDownload(url, save_path, save_name);
		(new Thread(download_multthread_mgr)).start();;
		return true;
	}

	@Override
	public boolean upgradeFW() {
		return false;
	}
	
	private class MultiThreadDownload implements Runnable {
		
		private String m_download_url = null;
		private String m_save_path = null;
		private String m_save_name = null;
		
		private int m_download_file_size = 0;
		private static final int GATE_SIZE = 1024*1024*1;	// 当下载文件小于该值时，不调整下载线程的个数
		private int m_threads_num = 5;						// 默认的下载线程个数
		private int m_threads_download_size = 0;			// 每个线程应该下载的大小
		private static final int ACTIVE_THREAD_NUM = 3;		// 每次最多只允许ACTIVE_THREAD_NUM个下载线程同时工作
		private int m_threads_has_started = 0;
		private int m_threads_has_done = 0;
		
		private ThreadDownload[] m_threads_download = null;	// 下载线程对象 
		private File[] m_threads_file = null;				// 每个下载线程对象对应一个下载文件块
		
		private int m_download_speed_KB = 0;
		private int m_download_percent = 0;
		private boolean m_download_finish = false;
		
		private static final int BUFFER_SIZE = 1024;
		

		public MultiThreadDownload(String url, String save_path, String save_name) {
			// TODO Auto-generated constructor stub
			m_download_url = url;
			m_save_path = save_path;
			m_save_name = save_name;
		}
		@Override
		public void run() {
			try {
				URL url = new URL(m_download_url);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				m_download_file_size = conn.getContentLength();
				if (m_download_file_size < 0) {
					// Get download file error
					return;
				}
				if (m_download_file_size > GATE_SIZE) {
					// 当下载文件大于GATE_SIZE时，调整下载线程的个数
					m_threads_num = m_threads_num + (m_download_file_size-1)/GATE_SIZE + 1;
				}
				// 最后一个下载线程可能需要下载大于m_thread_download_size
				m_threads_download_size = m_download_file_size/m_threads_num;
				
				m_threads_download = new ThreadDownload[m_threads_num];
				m_threads_file = new File[m_threads_num];
				
				conn.disconnect();
				Thread.sleep(1000*2);
				
				for (int i=0; i<ACTIVE_THREAD_NUM; i++) {
					StartNewDownloadThread(url, i);
				}
				m_threads_has_started = ACTIVE_THREAD_NUM;
				m_threads_has_done = 0;
				m_threads_download_size = 0;
				
				boolean all_threads_done = false;
				long last_download_size = 0;
				/*
				 * while 循环用于分块下载服务器上的升级包
				 * */
				while (!all_threads_done /*All thread has finished*/) {
					all_threads_done = true;
					long current_download_size = 0;
					long current_download_start_time = System.currentTimeMillis();
					for (int thread_index=0; thread_index < m_threads_has_started; thread_index++) {
						current_download_size += m_threads_download[thread_index].getDownloadSize();
						if (m_threads_download[thread_index].isThreadFinished() && 
								m_threads_download[thread_index].isThreadDownloadFinished()) {
							/*
							 * 该下载线程完成，并且下载成功，可以开启一个新的下载线程
							 * */
							m_threads_has_done++;
							if (m_threads_has_done < m_threads_num) {
								StartNewDownloadThread(url, ++m_threads_has_started);
								all_threads_done=false;
							}
						} else if (m_threads_download[thread_index].isThreadFinished()) {
							/*
							 * 该下载线程完成，但是并没有下载成功，重新打开该线程
							 * */
							current_download_size -= m_threads_download[thread_index].getDownloadSize();
							StartNewDownloadThread(url, thread_index);
							all_threads_done = false;
						} else {
							/*
							 * 该下载线程还没有完成
							 * */
							all_threads_done = false;
						}
					}
					long current_download_end_time = System.currentTimeMillis();
					long current_download_spend_time = (current_download_end_time-current_download_start_time)/1000;
					if (0==current_download_spend_time)current_download_spend_time=1;
					m_download_speed_KB =  (int)((current_download_size-last_download_size)/1024/current_download_spend_time);
					m_download_percent = (int)(current_download_size*100/m_download_file_size);
					last_download_size = current_download_size;
					m_notify.notifyDownloadPercent(m_download_percent);
					m_notify.notifyDownloadSpeed(m_download_speed_KB);
				} //所有线程下载完成
				
				/*
				 * 将所有下载的分块整合
				 * */
				if (m_threads_has_done != m_threads_num) {
					return;
				}
				Thread.sleep(1000);
				File file_update = new File(m_save_path+m_save_name);
				if (file_update.exists()) {
					file_update.delete();
				}
				RandomAccessFile rand_file_update = new RandomAccessFile(file_update, "rw");
				byte[] read_store_bytes = new byte[BUFFER_SIZE];
				InputStream input_stream = null;
				int byte_read = 0;
				for (int thread_index=0; thread_index<m_threads_num; thread_index++) {
					input_stream = new FileInputStream(m_threads_file[thread_index]);
					while ((byte_read = input_stream.read(read_store_bytes)) != -1) {
						rand_file_update.write(read_store_bytes, 0, byte_read);
					}
					input_stream.close();
					m_threads_file[thread_index].delete();
				}
				rand_file_update.close();
				m_download_finish = true;
				m_notify.notifyDownloadState(OTA_DOWNLOAD_STATE.OTA_DOWNLOAD_DONE);
			} catch (MalformedURLException e) {
				// URL url = new URL(m_download_url) exception
				e.printStackTrace();
			} catch (InterruptedException e) {
				// Thread.sleep(1000*2) exception
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// new RandomAccessFile exception
				e.printStackTrace();
			} catch (IOException e) {
				// url.openConnection()/input_stream.read() exception
				e.printStackTrace();
			} 
		}

		private void StartNewDownloadThread(URL url, int thread_index) {
			m_threads_file[thread_index] = new File(m_save_path + m_save_name + ".part" + String.valueOf(thread_index));
			m_threads_download[thread_index] = new ThreadDownload(url, 
					m_threads_file[thread_index], 
					thread_index*m_threads_download_size, 
					(thread_index+1)!=m_threads_num ? (thread_index+1)*m_threads_download_size : m_download_file_size);
		}
		private class ThreadDownload implements Runnable {
			private URL m_url;
			private File m_file;
			private int m_start_position;
			private int m_current_position;
			private int m_end_position;
			private int m_download_size = 0;
			private boolean m_thread_finish = false;			// 表明该线程的run过程是否完成,并不一定指下载完成
			private boolean m_thread_download_finish = false;	// 表明该线程是否下载完成
			private int m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_INIT;

			public ThreadDownload(URL url, File file, int startPosition, int endPosition){
				m_url = url;
				m_file = file;
				m_start_position = startPosition;
				m_current_position = startPosition;
				m_end_position = endPosition;
			}
			@Override
			public void run() {
				do {
					if (THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_DONE == taskStateMachine()) 
						break;
				} while(true);
				m_thread_finish = true;
			}
			
			private int taskStateMachine() {
				switch (m_state) {
					case THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_INIT:
						if((m_file.length()+m_start_position-1 == m_end_position) && isThreadDownloadFinished()) {
							m_download_size = m_end_position - m_start_position + 1;
							m_thread_finish = true;
							m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_DONE;
						} else {
							if (m_file.length()>0) {
								String fileName =m_file.getAbsolutePath();
								m_file.delete();
								m_file = new File(fileName);
							}
							m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_START;
						}
						break;
					case THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_START:
						 if (doDownloading()){
							 m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_DONE;
						 } else {
							 m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_RESET;
						 }
						 break;
					case THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_RESET:
						try {
							Thread.sleep(1000*60);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						m_state = THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_START;
						break;
					case THREAD_DOWNLOAD_STATE.THREAD_DOWNLOAD_DONE:
						break;
				}
				
				return m_state;
			}
			
			private boolean doDownloading() {
				m_thread_finish = false;
				m_thread_download_finish = false;
				
				BufferedInputStream buf_input_bits = null;
				RandomAccessFile rand_access_file = null;
				byte[] buf_bytes = new byte[BUFFER_SIZE];
				HttpURLConnection con = null;
				try {
					con =(HttpURLConnection)m_url.openConnection();
					con.setAllowUserInteraction(true);
					
					con.setRequestProperty("Range", "bytes="+(m_start_position+m_file.length()) + "-" + m_end_position);
					int response_code = con.getResponseCode();
					if(response_code>400) {
						m_thread_download_finish = false;
						return false;
					}
					rand_access_file = new RandomAccessFile(m_file, "rw");
					rand_access_file.seek(m_file.length());			
					buf_input_bits=new BufferedInputStream(con.getInputStream());
					
					while(m_current_position < m_end_position) {
						int len = buf_input_bits.read(buf_bytes, 0, BUFFER_SIZE);
						if(-1==len)break;
						rand_access_file.write(buf_bytes, 0, len);
						m_current_position += len;
						if (m_current_position > m_end_position) {
							m_download_size += len - (m_current_position-m_end_position) + 1;
						} else {
							m_download_size += len;
						}
					}
					buf_input_bits.close();
					rand_access_file.close();
					con.disconnect();
					m_thread_download_finish = true;
					return true;
				} catch (IOException e) {
					// m_url.openConnection() exception
					e.printStackTrace();
					if (null!=con) {
						con.disconnect();
					}
					m_thread_download_finish = false;
				}
				return false;
			}
			
			public boolean isThreadFinished() {
				return m_thread_finish;
			}
			
			public boolean isThreadDownloadFinished() {
				return m_thread_download_finish;
			}
			
			public int getDownloadSize() {
				return m_download_size;
			}
		}
	
		public int getDownloadSpeed(){
			return m_download_speed_KB;
		}
		
		public int getDownloadPercent() {
			return m_download_percent;
		}
	
		public boolean isDownloadFinish() {
			return m_download_finish;
		}
	}

}
