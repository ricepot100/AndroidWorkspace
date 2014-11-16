package com.github.ricepot100.contentassistant;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
				
				Vector<Map<String, String>> table_items = ContentHelper.getTableItem(
						 PlaceholderFragment.this.getActivity().getBaseContext(), 
						 ContactsContract.Contacts.CONTENT_URI);	
				
				for (int rowIndex=0; rowIndex<table_items.size(); rowIndex++) {
					Map<String, String> one_row_values = table_items.get(rowIndex);
					Set<Map.Entry<String, String>> entrys = one_row_values.entrySet();
					Iterator<Map.Entry<String, String>> iterator =  entrys.iterator();
					while (iterator.hasNext()) {
						Map.Entry<String, String> entry = iterator.next();
						if (entry.getValue()!=null) {
							Log.d(Assistant.TAG, "key(" + entry.getKey() + "):" + "value(" + entry.getValue() + ")");
						}
					}
				}
				
			}
			
		}
		
	}
}
