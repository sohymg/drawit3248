package com.drawIt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.*;

//yiling here just testing
public class drawIt extends Activity {
	final Context myApp = this;
	WebView webview;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		webview = (WebView) findViewById(R.id.appView);
		webview.setWebViewClient(new HelloWebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		
		webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		webview.loadUrl("http://www.google.com/");
		
	}
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url)
	    {
	        /* This call inject JavaScript into the page which just finished loading. */
			webview.loadUrl("javascript:document.getElementsByName('q')[0].onfocus = " + "function() { window.HTMLOUT.showHTML('textfield focused'); };");
			//webview.loadUrl("javascript:window.HTMLOUT.showHTML(document.getElementsByName('q')[0]);");
	    }
	}
	

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
	    @SuppressWarnings("unused")
	    public void showHTML(String html)
	    {
	        new AlertDialog.Builder(myApp)
	            .setTitle("HTML")
	            .setMessage(html)
	            .setPositiveButton(android.R.string.ok, null)
	        .setCancelable(false)
	        .create()
	        .show();
	    }
	}
}