package com.drawIt;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (v == btnNext) {
			try{
			
			username = etUsername.getText().toString();
			//check if username exist in database
			//if exists, go to SelectOption
			intent = new Intent(v.getContext(), SelectOption.class);
			intent.putExtra("domain", domain);
			intent.putExtra("username",username);
			startActivity(intent);
			//else, display message
			}
			catch(Exception e) {
				Toast.makeText(this, "Error finding "+ username + e.toString(), Toast.LENGTH_LONG).show();
			}
		}
		if(v == btnBack)
		{
			intent = new Intent(v.getContext(), SelectDomain.class);
			startActivity(intent);
		}
	}
}
