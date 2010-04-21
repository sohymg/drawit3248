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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectOption extends Activity implements OnItemClickListener{
	private TextView tvDomain;
	private TextView tvUsername;
	private ListView optionlist;
	private String options[]={
			"Change Password",
			"Change PassStroke",
			"Delete PassStroke"
	};
	
	private String domain;
	private String username;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_select_option);
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		username = extras.getString("username");
		
		optionlist=(ListView)findViewById(R.id.lvOptions);
		// By using setAdpater method in listview we an add string array in list.
		optionlist.setAdapter(new ArrayAdapter<String>(
				this,android.R.layout.simple_list_item_1 , options));
		optionlist.setOnItemClickListener(this);
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
	}

	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		try {
			Intent intent = null;
			
			switch(position)
			{
			case 0: //"Change Password"
				intent = new Intent(this, ChangePassword.class);
				intent.putExtra("domain", domain);
				intent.putExtra("username", username);
				
				startActivity(intent);
				break;
			case 1: //"Change PassStroke"
				intent = new Intent(this, ChangePassStroke.class);
				intent.putExtra("domain", domain);
				intent.putExtra("username", username);
				startActivity(intent);
				break;
			case 2: //"Delete PassStroke"
				DatabaseManager.deleteRow(drawIt.context, domain, username);
				finish();
				break;
			}
		}
		
		catch(Exception e) {
			Toast.makeText(this, "Error creating " + options[position] + 
					": " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
}
