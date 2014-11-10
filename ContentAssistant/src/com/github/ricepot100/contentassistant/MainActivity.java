package com.github.ricepot100.contentassistant;

import java.util.Map;
import java.util.Vector;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		Button m_btnGetTableColumnName = null;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			m_btnGetTableColumnName = (Button)rootView.findViewById(R.id.id_btn_get_table_column_name);
			m_btnGetTableColumnName.setOnClickListener(new BtnListenerGetColumnName());
			//getActivity().getBaseContext();
			return rootView;
		}		
		
		private class BtnListenerGetColumnName  implements  OnClickListener {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(Assistant.TAG, "BtnListenerGetColumnName.onClick");
				Vector<String> vs = ContentHelper.getTableColumnName(
						PlaceholderFragment.this.getActivity().getBaseContext(), 
						ContactsContract.Contacts.CONTENT_URI);
				
				for (int i=0; i<vs.size();i++) {
					Log.d(Assistant.TAG, "column[" + i + "]" + "\tname: " + vs.get(i));
				}
				
				 Vector<Map<String, String>> vec_table = ContentHelper.getTableItem(
						 PlaceholderFragment.this.getActivity().getBaseContext(), 
						 ContactsContract.Contacts.CONTENT_URI);	
				 /*
				 for (int i=0; i<vec_table.size(); i++) {
					 Map<String, String> map_one_raw = vec_table.get(i);
					 for (int j=0; j<map_one_raw.size(); j++) {
						 for(String obj : map_one_raw.keySet()){
							 Log.d(Assistant.TAG, "obj: " + map_one_raw.get(obj));
						 }
					 }
				 }
				 */
				
			}
			
		}
		
	}
}
