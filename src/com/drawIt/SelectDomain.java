package com.drawIt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectDomain extends Activity implements OnItemClickListener{
	
	private ListView domainlist;
	private String domains[]={
			"www.gmail.com",
			"www.hotmail.com",
			"www.nus.edu.sg",
			"www.yahoo.com",
			"www.163.com",
			"www.facebook.com",
			"www.renren.com",
			"ivle.nus.edu.sg"
	};
	
	private static final int MENU_CLEARDB = 0;
	//private TextView inst;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_select_domain);
		
		domainlist=(ListView)findViewById(R.id.lvDomains);
		domains = DatabaseManager.getDomains(drawIt.context);
		
		if(domains != null) {
			// By using setAdpater method in listview we an add string array in list.
			domainlist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , domains));
			domainlist.setOnItemClickListener(this);
		}
		
		//inst = (TextView)findViewById(R.id.ps_mgt_instruction);
	}

	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		try {
			//inst.setText("Domain selected: " + domains[position]);
			Intent intent = new Intent(this, EnterUsername.class);
			intent.putExtra("domain",domains[position]);
			startActivity(intent);
		}
		
		catch(Exception e) {
			Toast.makeText(this, "Error creating " + domains[position] + ": " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_CLEARDB, 0, "Clear All PassStrokes");
	    return true;
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	//deletes all passStrokes from the database
	    	case MENU_CLEARDB:
	    		DBAdapter dbAdapt = new DBAdapter(this);
	    		dbAdapt.open();
	    		dbAdapt.deleteAllPassStroke();
	    		dbAdapt.close();
	    		Toast.makeText(this, "Database cleared", Toast.LENGTH_LONG).show();
	    		return true;
	    }
	    return false;
	}
}
