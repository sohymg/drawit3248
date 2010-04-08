package com.drawIt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class drawIt extends Activity {

	static Context context;
	static Activity activity;
	WebView webview;	
	
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
		webview.loadUrl("http://www.gmail.com/");
		
		//System.out.println("Lev dist: " + Util.LevenshteinDistance("saturday", "sunday"));
		//Util.showMsg(this,Util.LevenshteinDistance("1234", "4321") +  " " + Util.DTWDistance("1234", "4321"));
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
		String[] fields = DatabaseManager.getLogin(domain, passStroke);
		
		//debug printout
		//Util.pl(domain + " "+ passStroke);
		
		String js = "javascript:document.getElementsByName('%useridField%')[0].value = "
			+ "'%userid%'; document.getElementsByName('%passwdField%')[0].value = '%passwd%'; "
			+ "document.forms['%formName%'].submit();";
		
		js = js.replace("%useridField%", fields[1]);
		js = js.replace("%userid%", fields[2]);
		js = js.replace("%passwdField%", fields[3]);
		js = js.replace("%passwd%", fields[4]);
		js = js.replace("%formName%", fields[0]);
		
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
		//Util.pl(domain + " " + formName + " " + useridField + " " + userid + " " + passwdField + " " + passwd + " " + passStroke);
		
		DatabaseManager.addPassStroke(domain, formName, useridField, userid, passwdField, passwd, passStroke);
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
		
		if(DatabaseManager.hasPassStroke(domain, userid, passwd) == false) {
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
}