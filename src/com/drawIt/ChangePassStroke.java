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

public class ChangePassStroke extends Activity implements OnClickListener {

	private TextView tvDomain;
	private TextView tvUsername;
	private EditText etPassword;
	private String entPwd;
	private Button btnBack;
	private Button btnNext;
	
	private String domain;
	private String username;
	private String password;
	private String passStroke;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_change_passstroke);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		username = extras.getString("username");
		password = extras.getString("password");
		passStroke = extras.getString("passStroke");
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		
		etPassword = (EditText)findViewById(R.id.password);
		etPassword.setText("");
		
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
				entPwd = etPassword.getText().toString();
				if(entPwd.equals(password))
				{
					//go to draw screen
				}
				else
				{
					etPassword.setText("");
					Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e) {
				Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
			}
		}
		if(v == btnBack)
		{
			intent = new Intent(v.getContext(), SelectOption.class);
			intent.putExtra("domain", domain);
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			intent.putExtra("passStroke", passStroke);
			startActivityForResult(intent, 0);
		}
	}
}