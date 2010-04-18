package com.drawIt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PSManagement extends Activity {

	private ListView lvDomains;
	private String domains[]={"www.gmail.com",
			"www.hotmail.com",
			"www.yahoo.com",
			"www.nus.edu.sg"};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_main);
		
		lvDomains=(ListView)findViewById(R.id.domainlist);
		// By using setAdpater method in listview we an add string array in list.
		lvDomains.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , domains));
	}
	
	

}
