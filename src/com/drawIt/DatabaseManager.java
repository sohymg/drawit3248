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
	
	public static void addPassStroke(Context ctx, String domain, String formName, String userIDField, String userID, 
									String passwdField, String password, String passStroke) {
		long result;
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		
		//add a new pass stroke into the database
		result = dbAdapt.insertPassStroke(domain, userID, formName, userIDField, passwdField, password, passStroke);
		if(result == -1)
			Toast.makeText(ctx,"Error saving to database",Toast.LENGTH_LONG).show();
		else
			Toast.makeText(ctx,"PassStroke Saved",Toast.LENGTH_LONG).show();
		
		dbAdapt.close();
	
	}
	
	public static String[] getLogin(Context ctx, String domain, String passStroke) {
		
		//get the login details associated with this domain and pass stroke
		//if not found, return null
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor c = dbAdapt.getPassStroke(domain);
		dbAdapt.close();
		
		if(c.getCount() > 0) {
			//search for valid passStroke in domain
			while(Util.isValid(passStroke, c.getString(6)) == false)
			{
				if(c.moveToNext() == false)
					return null;
			}
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

	public static String[] getDomains(Context ctx) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor mCursor = dbAdapt.getDomains();
		dbAdapt.close();
		int numDomains = mCursor.getCount();
		String [] domains = new String[numDomains];
		
		for(int i=0;i<numDomains;i++)
		{
			domains[i] = mCursor.getString(0);
		}
		return domains;
	}
	//checks if a passStroke is too close to an existing one
	public static boolean isUnique(Context ctx, String domain, String passStroke)
	{
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		//get all passStrokes in that domain
		Cursor c = dbAdapt.getPassStroke(domain);
		dbAdapt.close();
		
		//use twice the error threshold to make sure the new one is different enough
		double errThreshold = Util.ERROR_THRESHOLD * 2;
		double err;
		String existing;
		if(c.getCount() > 0)
		{
			do
			{
				existing = c.getString(6);
				err = (double)Util.DTWDistance(passStroke, existing)/(double)existing.length();
				//passStroke is too close to an existing one
				if(err <= errThreshold)
					return false;
			
			}while (c.moveToNext()== true);
			
		}
		//no match found
		return true;
	}
}
