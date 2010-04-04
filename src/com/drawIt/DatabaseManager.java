/* 
 * this is a dummy class that should interface with the real database class.
 * all the fields should be stored in the database instead of as variables.
 */

package com.drawIt;

public class DatabaseManager {
	static String domain = "", formName = "", useridField = "", passwdField = "", userid = "", passwd = "", 
				passStroke = "";
	
	public static void addPassStroke(String domain, String formName, String useridField, String userid, 
									String passwdField, String passwd, String passStroke) {
		
		//add a new pass stroke into the database
		
		DatabaseManager.domain = domain;
		DatabaseManager.formName = formName;
		DatabaseManager.useridField = useridField;
		DatabaseManager.userid = userid;
		DatabaseManager.passwdField = passwdField;
		DatabaseManager.passwd = passwd;
		DatabaseManager.passStroke = passStroke;
	}
	
	public static String[] getLogin(String domain, String passStroke) {
		
		//get the login details associated with this domain and pass stroke
		//if not found, return null
		
		if(DatabaseManager.passStroke.equals(passStroke)) {
			return new String[] {formName, useridField, userid, passwdField, passwd};
		}
		else {
			return null;
		}
	}
	
	public static String[] hasPassStroke(String domain) {
		
		//get the userid and passwd field names
		//used by JSWebViewClient to check if a page has previously saved pass stroke.
		//if yes, JSWebViewClient will inject listeners to these fields to show pass stroke login
		//else, return null for no saved pass stroke
		
		if(DatabaseManager.domain.equals(domain)) {
			return new String[] {useridField, passwdField};
		}
		else {
			return null;
		}
	}
}
