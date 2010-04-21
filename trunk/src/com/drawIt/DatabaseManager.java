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
		
		Util.readLogFile();
		String logStr = Util.getDayTime() + "\t";
		if(c.getCount() > 0) {
			//search for valid passStroke in domain
			while(Util.isValid(passStroke, c.getString(6)) == false)
			{
				if(c.moveToNext() == false)
				{
					//no matching passStroke is found
					c.moveToPrevious();
					logStr += String.valueOf(Util.DTWDistance(passStroke, c.getString(6)));
					logStr += "\t" + String.valueOf(passStroke.length());
					Util.log.add(logStr);
					return null;
				}
			}
			String formName = c.getString(2);
			String userIDField = c.getString(3);
			String userID = c.getString(1);
			String passwordField = c.getString(4);
			String password = c.getString(5);
			
			
			logStr += String.valueOf(Util.DTWDistance(passStroke, c.getString(6)));
			logStr += "\t" + String.valueOf(passStroke.length());
			Util.log.add(logStr);
			Util.writeLogFile();
			
			c.close();
			//string array will contain formName, userIDField, userID, passwordField, password
			return new String[] {formName,userIDField,userID,passwordField,password};
		}
		else {
			c.close();
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
			String useridField = c.getString(0);
			String passwordField = c.getString(1);
			c.close();
			return new String[] {useridField, passwordField};
		}
		else {
			c.close();
			return null;
		}
	}
	
	public static boolean hasPassStroke(Context ctx, String domain, String userid) {
		
		//used by drawit to check if this set of password has already been saved. if saved, don prompt to save again
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		boolean exists = dbAdapt.checkExist(domain,userid);
		dbAdapt.close();
		
		if(exists)
			return true;
		else
			return false;
	}

	//gets all the domains in the database and returns them in a string array
	public static String[] getDomains(Context ctx) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		Cursor mCursor = dbAdapt.getDomains();
		dbAdapt.close();
		int numDomains = 0;
		numDomains = mCursor.getCount();
		String [] domains = new String[numDomains];
		
		for(int i=0;i<numDomains;i++)
		{
			domains[i] = mCursor.getString(0);
			mCursor.moveToNext();
		}
		mCursor.close();
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
				{
					c.close();
					return false;
				}
			
			}while (c.moveToNext()== true);
			
		}
		//no match found
		c.close();
		return true;
	}
	
	//checks if password is correct for a given domain and userID
	public static boolean isLoginCorrect(Context ctx, String domain, String userID, String password) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		//get the one domain/userID combination
		Cursor c = dbAdapt.getRow(domain, userID);
		dbAdapt.close();
		
		//column index 5 is the password field
		String storedPassword = c.getString(5);
		c.close();
		//compare the stored password with the one passed in and return true if they match
		if(storedPassword.compareTo(password) == 0)
			return true;
		else
			return false;
	}
	
	//updates password
	public static void updatePassword(Context ctx, String domain, String userID, String password) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		//get the one domain/userID combination
		Cursor c = dbAdapt.getRow(domain, userID);
		
		//column index 6 holds the passStroke
		String storedPassStroke = c.getString(6);
		c.close();
		
		dbAdapt.updatePassStroke(domain, userID, password, storedPassStroke);
		dbAdapt.close();
		
		Toast.makeText(ctx,"Password changed",Toast.LENGTH_LONG).show();
	}
	
	//updates passStroke
	public static void updatePassStroke(Context ctx, String domain, String userID, String passStroke) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		//get the one domain/userID combination
		Cursor c = dbAdapt.getRow(domain, userID);
		
		//column index 5 holds the password
		String storedPassword = c.getString(5);
		c.close();
		
		dbAdapt.updatePassStroke(domain, userID, storedPassword, passStroke);
		dbAdapt.close();
		
		Toast.makeText(ctx, "PassStroke changed", Toast.LENGTH_LONG).show();
	}
	
	//deletes a database row when given the userID and domain
	public static void deleteRow(Context ctx, String domain, String userID) {
		DBAdapter dbAdapt = new DBAdapter(ctx);
		dbAdapt.open();
		
		if(dbAdapt.deletePassStroke(domain, userID) == true) {
			Toast.makeText(ctx,"Entry deleted",Toast.LENGTH_LONG).show();
		}
		//the else clause should never be encountered
		else {
			Toast.makeText(ctx,"Error in deleting passStroke",Toast.LENGTH_LONG).show();
		}
		dbAdapt.close();
	}
}
