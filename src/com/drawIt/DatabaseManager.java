/* 
 * this is a dummy class that should interface with the real database class.
 * all the fields should be stored in the database instead of as variables.
 */

package com.drawIt;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.drawIt.DBAdapter;

public class DatabaseManager {
	static String domain = "", formName = "", useridField = "", passwdField = "", userid = "", passwd = "", 
				passStroke = "";
	public static void addPassStroke(Context ctx, String domain, String formName, String userIDField, String userID, 
									String passwdField, String password, String passStroke) {
		long result;
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		
		//add a new pass stroke into the database
		result = dbAdapt.insertPassStroke(domain, userID, formName, userIDField, passwdField, password, passStroke);
		if(result == -1)
			Toast.makeText(ctx,"Error saving to database",Toast.LENGTH_LONG);
		else
			Toast.makeText(ctx,"PassStroke Saved",Toast.LENGTH_LONG);
		
		dbAdapt.close();
		/*
		DatabaseManager.domain = domain;
		DatabaseManager.formName = formName;
		DatabaseManager.useridField = useridField;
		DatabaseManager.userid = userid;
		DatabaseManager.passwdField = passwdField;
		DatabaseManager.passwd = passwd;
		DatabaseManager.passStroke = passStroke;*/
	}
	
	public static String[] getLogin(Context ctx, String domain, String passStroke) {
		
		//get the login details associated with this domain and pass stroke
		//if not found, return null
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor c = dbAdapt.getPassStroke(domain,passStroke);
		dbAdapt.close();
		if(c.getCount() > 0) {
			//string array will contain formName, userIDField, userID, passwordField, password
			return new String[] {c.getString(2), c.getString(3), c.getString(1), c.getString(4), c.getString(5)};
		}
		else {
			return null;
		}
	}
	
	public static String[] hasPassStroke(Context ctx, String domain) {
		
		//get the userid and passwd field names
		//used by JSWebViewClient to check if a page has previously saved pass stroke.
		//if yes, JSWebViewClient will inject listeners to these fields to show pass stroke login
		//else, return null for no saved pass stroke
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor c = dbAdapt.getFields(domain);
		dbAdapt.close();
		
		if(c.getCount() > 0) {
			return new String[] {c.getString(0), c.getString(1)};
		}
		else {
			return null;
		}
	}
	
	public static boolean hasPassStroke(Context ctx, String domain, String userid, String passwd) {
		
		//used by drawit to check if this set of password has already been saved. if saved, don prompt to save again
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		boolean exists = dbAdapt.checkExist(domain,userid,passwd);
		dbAdapt.close();
		
		if(exists)
			return true;
		else
			return false;
	}
}
