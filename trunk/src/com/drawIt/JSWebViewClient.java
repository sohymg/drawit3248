package com.drawIt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JSWebViewClient extends WebViewClient {
	
	public void onPageFinished(WebView webview, String url)
	{
		drawIt.activity.setTitle(url);
		String domain = url.substring(url.indexOf(':') + 3);
		domain = domain.substring(0, domain.indexOf('/'));
	//	Util.pl(domain);

		String[] fields = DatabaseManager.hasPassStroke(drawIt.context,domain);
	//	String [] fields = null;

		if(fields != null) { //pass stroke exists, add pass stroke login			
			try {
				String loginJS = "";
				InputStream is = drawIt.context.getResources().openRawResource(R.raw.loginform);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				String str;
			    while ((str = br.readLine()) != null) {
			        loginJS += str;
			    }
			    br.close();
			    
			    loginJS = loginJS.replaceAll("%useridField%", fields[0]);
				loginJS = loginJS.replaceAll("%passwdField%", fields[1]);
				loginJS = loginJS.replaceAll("%domain%", domain);

				webview.loadUrl(loginJS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//inject code to detect form submit
		
		//this line is just for convenience purpose. if not u have to keep typing the login details to test
		//to be removed when code is ready
		//webview.loadUrl("javascript:document.getElementsByName('Email')[0].value = 'cs3248.g4@gmail.com'; document.getElementsByName('Passwd')[0].value = 'ilovecs3248';");
		//end
		
		
		try {
			String captureJS = "";
			InputStream is = drawIt.context.getResources().openRawResource(R.raw.captureform);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String str;
		    while ((str = br.readLine()) != null) {
		    	captureJS += str;
		    }
		    br.close();
		    
		    captureJS = captureJS.replaceAll("%domain%", domain);
		    
		    webview.loadUrl(captureJS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//webview.loadUrl("javascript:alert('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		
		/*String js = "javascript:document.getElementsByName('%useridField%')[0].value = "
			+ "'%userid%'; document.getElementsByName('%passwdField%')[0].value = '%passwd%'; alert(document.getElementsByName('%passwdField%')[0].value + ' ' + document.getElementsByName('%passwdField%').length); "
			;//+ "document.forms['%formName%'].submit();";
		
		js = js.replace("%useridField%", "LoginTextBox");
		js = js.replace("%userid%", "yuming1985@hotmail.com");
		js = js.replace("%passwdField%", "PasswordTextBox");
		js = js.replace("%passwd%", "mdmermWo09");
		js = js.replace("%formName%", "EmailPasswordForm");
		
		webview.loadUrl(js);*/
	}
}
