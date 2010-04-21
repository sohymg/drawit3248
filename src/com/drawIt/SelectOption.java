package com.drawIt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectOption extends Activity implements OnItemClickListener, OnClickListener{
	private TextView tvDomain;
	private TextView tvUsername;
	private ListView lvOptions;
	private String optionlist[]={
			"Change Password",
			"Change PassStroke",
			"Delete PassStroke"
	};
	private Button btnBack;
	
	private String domain;
	private String username;
	private String password;
	private String passStroke;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_select_option);
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		username = extras.getString("username");
		password = extras.getString("password");
		passStroke = extras.getString("passStroke");
		
		lvOptions=(ListView)findViewById(R.id.lvOptions);
		// By using setAdpater method in listview we an add string array in list.
		lvOptions.setAdapter(new ArrayAdapter<String>(
				this,android.R.layout.simple_list_item_1 , optionlist));
		lvOptions.setOnItemClickListener(this);
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}
	
	public boolean deletePassStroke(Context ctx, String _domain, String _username)
	{
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		boolean ret = dbAdapt.deletePassStroke(_domain, _username);
		dbAdapt.close();
		return ret;
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
				intent.putExtra("password", password);
				intent.putExtra("passStroke", passStroke);
				startActivity(intent);
				break;
			case 1: //"Change PassStroke"
				intent = new Intent(this, ChangePassStroke.class);
				intent.putExtra("domain", domain);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("passStroke", passStroke);
				startActivity(intent);
				break;
			case 2: //"Delete PassStroke"
				if(deletePassStroke(this, domain, username))
				{
					//go back to select domain page
					intent = new Intent(this, SelectDomain.class);
					startActivity(intent);
					//display message
					Toast.makeText(this, "PassStroke for "+username+" has been deleted.", 
							Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(this, "Error deleting passStroke.", 
							Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
		
		catch(Exception e) {
			Toast.makeText(this, "Error creating " + optionlist[position] + 
					": " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btnBack)
		{
			Intent intent = new Intent(v.getContext(), SelectDomain.class);
			startActivity(intent);
		}
	}
}
