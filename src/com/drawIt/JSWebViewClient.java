package com.drawIt;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JSWebViewClient extends WebViewClient {
	public void onPageFinished(WebView webview, String url)
    {
		String domain = "www.gmail.com"; //todo: supposed to extract domain from url
		
		String[] fields = DatabaseManager.hasPassStroke(domain);
		
		if(fields != null) { //pass stroke exists, add pass stroke login
			String js = "javascript:document.getElementsByName('%useridField%')[0].onfocus = " 
			+ "function() { window.JSCALLBACK.showPSLogin('%domain%'); };";
			
			js = js.replace("%useridField%", fields[0]);
			js = js.replace("%domain%", domain);
			
			webview.loadUrl(js);
		}
		else { //pass stroke does not exist, inject code to detect form submit
			//todo: supposed to find out the form, userid and passwd field names from each url
			
			String formName = "gaia_loginform";
			String useridField = "Email";
			String passwdField = "Passwd";
			
			//this line is just for convenience purpose. if not u have to keep typing the login details to test
			//to be removed when code is ready
			webview.loadUrl("javascript:document.getElementsByName('Email')[0].value = 'cs3248.g4@gmail.com'; document.getElementsByName('Passwd')[0].value = 'ilovecs3248';");
			//end
			
			String js = "javascript:document.forms['%formName%'].onsubmit = "
				+ "function() { window.JSCALLBACK.showPSSave('%domain%', '%formName%', "
				+ "'%useridField%', document.getElementsByName('%useridField%')[0].value, "  
				+ "'%passwdField%', document.getElementsByName('%passwdField%')[0].value); }";
			
			js = js.replace("%domain%", domain);
			js = js.replace("%formName%", formName);
			js = js.replaceAll("%useridField%", useridField);
			js = js.replaceAll("%passwdField%", passwdField);
			
			webview.loadUrl(js);
		}
    }
}
