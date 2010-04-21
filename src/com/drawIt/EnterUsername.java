package com.drawIt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterUsername extends Activity implements OnClickListener {

	private TextView tvDomain;
	private EditText etUsername;
	private Button btnBack;
	private Button btnNext;
	private String domain;
	private String username;
	private String[] loginData;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_enter_username);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		
		etUsername = (EditText)findViewById(R.id.tvUsername);
		etUsername.setText("");
		etUsername.setOnClickListener(this);
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		
	}

	public boolean checkUsername(Context ctx, String _username,String _domain)
	{
		//get the login details associated with this domain and pass stroke
		//if not found, return null
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor c = dbAdapt.getPassStroke(_domain);
		dbAdapt.close();
		if(c.getCount()>0)
		{
			//search for username
			do
			{
				if(c.getString(1).equals(_username))
				{
					loginData = new String[]{
						c.getString(0), //domain
						c.getString(1), //username
						c.getString(5), //password
						c.getString(6)  //passStroke
					};

					return true;
				}
			}
			while(c.moveToNext());
		}
		
		return false;
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if(v == etUsername)
		{
			//do something
		}
		if (v == btnNext) {
			try{

				username = etUsername.getText().toString();

				//check if username exist in database
				//if exists, go to SelectOption
				if(checkUsername(v.getContext(),username,domain))
				{
					intent = new Intent(v.getContext(), SelectOption.class);
					intent.putExtra("domain", loginData[0]);
					intent.putExtra("username",loginData[1]);
					intent.putExtra("password", loginData[2]);
					intent.putExtra("passStroke",loginData[3]);
					startActivity(intent);
				}
		
				//else, display message
				else
				{
					etUsername.setText("");
					Toast.makeText(this, "Username is not found!", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e) {
				Toast.makeText(this, username + " not found in the database", Toast.LENGTH_LONG).show();
			}
		}
		if(v == btnBack)
		{
			intent = new Intent(v.getContext(), SelectDomain.class);
			startActivity(intent);
		}
	}
}
