package com.drawIt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class drawIt extends Activity{

	static Context context;
	static Activity activity;
	WebView webview;	
	private EditText urlText;
	private Button goButton;
	private LinearLayout addressBar;

	private static final int MENU_ADDRESSBAR = 0;
	private static final int MENU_PSMANAGEMENT = 1;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		activity = this;
		
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main);
		setProgressBarVisibility(true);
		
		webview = (WebView) findViewById(R.id.appView);
		webview.setWebViewClient(new JSWebViewClient());
		webview.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {  
				System.out.println(message);		  
		        return false;  
		    }; 
		    
		    public void onProgressChanged(WebView view, int progress) {System.out.println(progress);
		        activity.setProgress(progress * 1000);
		      }
		});
		
		
		webview.getSettings().setSavePassword(false); //disable webview default password saving
		webview.getSettings().setJavaScriptEnabled(true);
		
		webview.addJavascriptInterface(new JSCallback(this), "JSCALLBACK");
		webview.loadUrl("http://www.hotmail.com/");
		
		// Get a handle to all user interface elements
		urlText = (EditText) findViewById(R.id.url_field);
		goButton = (Button) findViewById(R.id.go_button);
		addressBar = (LinearLayout)findViewById(R.id.addressBar);
		// Setup event handlers
		goButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
					addressBar.setVisibility(View.GONE);
					openBrowser();
				}
		});
		
		urlText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
						addressBar.setVisibility(View.GONE);
						openBrowser();
						return true;
				}
				return false;
			}
		});
		
		//clicking on the webview will hide the address bar
		//false is returned so that events are still handled properly by the webview
		webview.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				addressBar.setVisibility(View.GONE);
				return false;
			}
		});


		//System.out.println("Lev dist: " + Util.LevenshteinDistance("saturday", "sunday"));
		//Util.showMsg(this,Util.LevenshteinDistance("1234", "4321") +  " " + Util.DTWDistance("1234", "4321"));
		
		/*Intent intent = new Intent(this, DrawScreen.class);
    	intent.putExtra("mode", DrawScreen.DRAW_TO_SAVE);
    	intent.putExtra("domain", "d");
    	intent.putExtra("formName", "formName");
    	intent.putExtra("useridField", "useridField");
    	intent.putExtra("userid", "userid");
    	intent.putExtra("passwdField", "passwdField");
    	intent.putExtra("passwd", "passwd");
		
		startActivityForResult(intent, DrawScreen.DRAW_TO_SAVE);*/
	}
	
	/** Open a browser on the URL specified in the text box */
	private void openBrowser() {
		webview.getSettings().setJavaScriptEnabled(true);
		String url = urlText.getText().toString();
		if(url.contains("http://") == false) {
			url = "http://" + url;
		}
		if(url.endsWith("/")==false)
			url += "/";
		urlText.setText(url);
		webview.loadUrl(url);
	}

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_ADDRESSBAR, 0, "Go To Address");
	    menu.add(0, MENU_PSMANAGEMENT, 0, "PassStroke Management");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	//shows the address bar
	    	case MENU_ADDRESSBAR:
	    		addressBar.setVisibility(View.VISIBLE);
	    		urlText.setText(webview.getUrl());
	    		urlText.selectAll();
	    		urlText.requestFocus();
	    		return true;
	    		
	    	case MENU_PSMANAGEMENT:
	    		Intent newScreen = new Intent (this, SelectDomain.class);
	    		startActivity(newScreen);
	    		return true;
	    }
	    return false;
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == DrawScreen.DRAW_TO_LOGIN && resultCode == RESULT_OK) {
			doLogin(data);
		}
		else if(requestCode == DrawScreen.DRAW_TO_SAVE && resultCode == RESULT_OK) {
			doSave(data);
		}
	}
	
	private void doLogin(Intent data) {
		//called when user has drawn the correct pass stroke.
		//auto fill in the form and submit
		
		Bundle extras = data.getExtras();
		String domain = extras.getString("domain");
		String passStroke = extras.getString("passStroke");
		String[] fields = DatabaseManager.getLogin(context, domain, passStroke);
		
		//debug printout
	//	Util.pl(fields[3] + " "+ fields[4] + " " + fields[0]);
	//	Util.showMsg(this, fields[3] + " "+ fields[4] + " " + fields[0]);
		
		String js = "javascript:document.getElementsByName('%useridField%')[0].value = "
			+ "'%userid%'; document.getElementsByName('%passwdField%')[0].value = '%passwd%';"// alert(document.getElementsByName('%passwdField%')[0].value + ' ' + document.getElementsByName('%passwdField%').length); "
			;//+ "document.forms['%formName%'].submit();";
		
		js = js.replace("%useridField%", fields[1]);
		js = js.replace("%userid%", fields[2]);
		js = js.replace("%passwdField%", fields[3]);
		js = js.replace("%passwd%", fields[4]);
		js = js.replace("%formName%", fields[0]);
		
		Toast.makeText(this, "Login details entered", Toast.LENGTH_LONG).show();
		webview.loadUrl(js);
	}
	
	private void doSave(Intent data) {
		//called when the user has drawn the pass stroke twice (first draw and confirmation draw) already
		//save pass stroke to database
		
		Bundle extras = data.getExtras();
		String domain = extras.getString("domain");
		String formName = extras.getString("formName");
		String useridField = extras.getString("useridField");
		String userid = extras.getString("userid");
		String passwdField = extras.getString("passwdField");
		String passwd = extras.getString("passwd");
		String passStroke = extras.getString("passStroke");
		
		//debug printout
		Util.pl(domain + " " + formName + " " + useridField + " " + userid + " " + passwdField + " " + passwd + " " + passStroke);
		DatabaseManager.addPassStroke(context,domain, formName, useridField, userid, passwdField, passwd, passStroke);
	}	
	
	public void showPSLogin(String domain) {
		//called when we detect the user taps on the userid field on a form
		//show the draw pass stroke login interface
		
		Intent intent = new Intent(this, DrawScreen.class);
    	intent.putExtra("mode", DrawScreen.DRAW_TO_LOGIN);
    	intent.putExtra("domain", domain);
		
		startActivityForResult(intent, DrawScreen.DRAW_TO_LOGIN);
	}
	
	public void showPSSave(String domain, String formName, String useridField, String userid, String passwdField, String passwd) {
		//called when the user manually logins for the first to a domain
		//show the draw pass stroke interface which will save the pass stroke
		
		Util.pl(formName + " " + useridField + " " + userid + " " + passwdField + " " + passwd);
		
		if(DatabaseManager.hasPassStroke(context, domain, userid, passwd) == false) {
			Intent intent = new Intent(this, DrawScreen.class);
	    	intent.putExtra("mode", DrawScreen.DRAW_TO_SAVE);
	    	intent.putExtra("domain", domain);
	    	intent.putExtra("formName", formName);
	    	intent.putExtra("useridField", useridField);
	    	intent.putExtra("userid", userid);
	    	intent.putExtra("passwdField", passwdField);
	    	intent.putExtra("passwd", passwd);
			
			startActivityForResult(intent, DrawScreen.DRAW_TO_SAVE);
		}
	}
	
	//captures back button press and use it to go back to the last visited page
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//if back button is pressed and address bar is showing, just hide the address bar
		if(keyCode == KeyEvent.KEYCODE_BACK && addressBar.getVisibility() == View.VISIBLE) {
			addressBar.setVisibility(View.GONE);
			return true;
		}
		//or else go back to the previous page shown
		else if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	        webview.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}