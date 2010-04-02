package com.drawIt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.*;

//yiling here just testing
public class drawIt extends Activity {
	Handler handler = new Handler();
	final Context myApp = this;
	WebView webview;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		webview = (WebView) findViewById(R.id.appView);
		webview.setWebViewClient(new HelloWebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		
		webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		webview.loadUrl("http://www.gmail.com/");
		
	}
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url)
	    {
	        /* This call inject JavaScript into the page which just finished loading. */
			
			
			webview.loadUrl("javascript:(function() { document.getElementsByName('Email')[0].onfocus = " 
					+ "function() { window.HTMLOUT.showHTML('textfield focused'); }; })()");
			
			//webview.loadUrl("javascript:function wave() {document.getElementsByName('Email')[0].value = 'cs3248.g4@gmail.com'; }");
			
			
			//uncomment this line to autofill the login details when pressing submit
			//webview.loadUrl("javascript:document.forms['gaia_loginform'].onsubmit = function() {document.getElementsByName('Email')[0].value = 'cs3248.g4@gmail.com'; document.getElementsByName('Passwd')[0].value = 'ilovecs3248';};");
	    }
	}
	

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
	    @SuppressWarnings("unused")
	    public void showHTML(String html)
	    {
	        /*new AlertDialog.Builder(myApp)
	            .setTitle("HTML")
	            .setMessage(html)
	            .setPositiveButton(android.R.string.ok, null)
	        .setCancelable(false)
	        .create()
	        .show();*/
	        
	        handler.post(new Runnable() {
                public void run() {
                	webview.loadUrl("javascript:document.getElementsByName('Email')[0].value = 'cs3248.g4@gmail.com';");
                	webview.loadUrl("javascript:document.getElementsByName('Passwd')[0].value = 'ilovecs3248';");
                	
                	//uncomment this line to auto-submit
                	//webview.loadUrl("javascript:document.forms['gaia_loginform'].submit();");
                }
            });
	    }
	}
}