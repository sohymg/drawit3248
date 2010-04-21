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
	//private Button btnBack;
	private Button btnNext;
	
	private String domain;
	private String username;
//	private String password;
//	private String passStroke;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_change_passstroke);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		username = extras.getString("username");
	//	password = extras.getString("password");
	//	passStroke = extras.getString("passStroke");
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		
		etPassword = (EditText)findViewById(R.id.password);
		etPassword.setText("");
		
	//	btnBack = (Button)findViewById(R.id.btnBack);
	//	btnBack.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnNext) {
				entPwd = etPassword.getText().toString();
				if(DatabaseManager.isLoginCorrect(drawIt.context, domain, username, entPwd) == true)
				{
					Intent intent = new Intent(this, DrawScreen.class);
			    	intent.putExtra("mode", DrawScreen.DRAW_TO_PM_SAVE);
			    	intent.putExtra("domain", domain);
					startActivityForResult(intent, DrawScreen.DRAW_TO_PM_SAVE);
				}
				else
				{
					etPassword.setText("");
					Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
				}
		}
		/*if(v == btnBack)
		{
			intent = new Intent(v.getContext(), SelectOption.class);
			intent.putExtra("domain", domain);
			intent.putExtra("username", username);
			intent.putExtra("password", password);
			intent.putExtra("passStroke", passStroke);
			startActivityForResult(intent, 0);
		}*/
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == DrawScreen.DRAW_TO_PM_SAVE && resultCode == RESULT_OK) {
			//update database
			Bundle extras = data.getExtras();
			String passStroke = extras.getString("passStroke");
			etPassword.setText("");
			
			DatabaseManager.updatePassStroke(drawIt.context, domain, username, passStroke);
			
		}
	}
}