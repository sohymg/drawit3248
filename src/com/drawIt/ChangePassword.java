package com.drawIt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	private Button btnBack;
	private Button btnNext;
	
	private String domain;
	private String username;
	private String password;
	private String passStroke;
	
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
		password = extras.getString("password");
		passStroke = extras.getString("passStroke");
		
		tvDomain = (TextView)findViewById(R.id.tvDomain);
		tvDomain.setText(domain);
		
		tvUsername = (TextView)findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		
		etCurrPwd = (EditText)findViewById(R.id.current_pwd);
		etCurrPwd.setText("");
		etNewPwd = (EditText)findViewById(R.id.new_pwd);
		etNewPwd.setText("");
		etRetypedPwd = (EditText)findViewById(R.id.retyped_pwd);
		etRetypedPwd.setText("");
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
	}

	public boolean updatePassword(Context ctx, String _domain, String _username, 
			String _password, String _passStroke)
	{
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		boolean ret = dbAdapt.updatePassStroke(_domain, _username, _password, _passStroke);
		dbAdapt.close();
		return ret;
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (v == btnNext) {
		  try{
			currPwd = etCurrPwd.getText().toString();
			newPwd = etNewPwd.getText().toString();
			retypedPwd = etRetypedPwd.getText().toString();
			
			if(currPwd.equals(password))
			{
				if(retypedPwd.equals(newPwd))
				{
					if(updatePassword(this,domain,username,newPwd, passStroke))
					{
						intent = new Intent(v.getContext(), SelectOption.class);
						intent.putExtra("domain", domain);
						intent.putExtra("username", username);
						intent.putExtra("password", password);
						intent.putExtra("passStroke", passStroke);
						startActivity(intent);
						
						Toast.makeText(this, "Password has been changed.", 
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						etCurrPwd.setText("");
						etNewPwd.setText("");
						etRetypedPwd.setText("");
						Toast.makeText(this, "Error updating the password.", 
								Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					etNewPwd.setText("");
					etRetypedPwd.setText("");
					Toast.makeText(this, "Re-typed password does not match the new password.", 
							Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				etCurrPwd.setText("");
				Toast.makeText(this, "Current password is incorrrect.", 
						Toast.LENGTH_SHORT).show();
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
			startActivity(intent);
		}
	}
}
