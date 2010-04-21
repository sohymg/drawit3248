package com.drawIt;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends Activity implements OnClickListener {

	private TextView tvDomain;
	private TextView tvUsername;
	private EditText etCurrPwd;
	private EditText etNewPwd;
	private EditText etRetypedPwd;
//	private Button btnBack;
	private Button btnNext;
	private String domain;
	private String username;
	private String currPwd;
	private String newPwd;
	private String retypedPwd;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ps_management_change_password);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = this.getIntent().getExtras();
		domain = extras.getString("domain");
		username = extras.getString("username");
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		
		etCurrPwd = (EditText)findViewById(R.id.current_pwd);
		etNewPwd = (EditText)findViewById(R.id.new_pwd);
		etRetypedPwd = (EditText)findViewById(R.id.retyped_pwd);
		
	//	btnBack = (Button)findViewById(R.id.btnBack);
	//	btnBack.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnNext) {
			
			currPwd = etCurrPwd.getText().toString();
			newPwd = etNewPwd.getText().toString();
			retypedPwd = etRetypedPwd.getText().toString();
			
			//check if current password is correct from database
			//if exists, check if retyped password equals to the new password
			if(DatabaseManager.isLoginCorrect(drawIt.context, domain, username, currPwd) == true) {
				//now check if the new and confirmed passwords are equal
				if(newPwd.compareTo(retypedPwd) == 0) {
					DatabaseManager.updatePassword(drawIt.context, domain, username, newPwd);
					//clears the textboxes so there's some visual feedback that something has happened
					etCurrPwd.setText("");
					etNewPwd.setText("");
					etRetypedPwd.setText("");
				}
				else {
					Toast.makeText(this,"New and retyped passwords do not match",Toast.LENGTH_LONG).show();
				}
			}
			else {
				Toast.makeText(this,"Password is incorrect",Toast.LENGTH_LONG).show();
			}
			
		}
	/*	if(v == btnBack)
		{
			intent = new Intent(v.getContext(), SelectOption.class);
			intent.putExtra("domain", domain);
			intent.putExtra("username", username);
			startActivity(intent);
		}*/
	}
}
