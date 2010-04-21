package com.drawIt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    public static final String KEY_DOMAIN = "Domain";		//the first 2 fields form the primary key
    public static final String KEY_USERID = "UserID";
    public static final String KEY_FORMNAME = "FormName";
    public static final String KEY_USERIDFIELD = "UserIDField";
    public static final String KEY_PASSWDFIELD = "PasswdField";
    public static final String KEY_PASSWORD = "Password";
    public static final String KEY_PASSSTROKE = "PassStroke";    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "drawItDB";
    private static final String DATABASE_TABLE = "PassStrokes";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table PassStrokes (Domain text not null, UserID text not null,"
    	+ "FormName text not null, UserIDField text not null,"
        + "PasswdField text not null, Password text not null, " 
        + "PassStroke text not null," 
        + "PRIMARY KEY(Domain,UserID));";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PassStrokes");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertPassStroke(String domain, String userID, String formName, 
    		String userIDField, String passwdField, String password, String passStroke) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DOMAIN,domain);
        initialValues.put(KEY_USERID, userID);
        initialValues.put(KEY_FORMNAME,formName);
        initialValues.put(KEY_USERIDFIELD, userIDField);
        initialValues.put(KEY_PASSWDFIELD, passwdField);
        initialValues.put(KEY_PASSWORD, password);
        initialValues.put(KEY_PASSSTROKE, passStroke);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular entry based on rowID---
    public boolean deletePassStroke(String domain,String userID) 
    {
        return db.delete(DATABASE_TABLE, KEY_DOMAIN + 
        		"='" + domain + "' AND " + KEY_USERID +
        		"='" + userID + "'", null) > 0;
    }
    
    //--deletes all data saved in the database---
    public void deleteAllPassStroke()
    {
    	db.delete(DATABASE_TABLE, null, null);
    }

    //---retrieves all the passStrokes---
    public Cursor getAllPassStrokes() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_DOMAIN,
        		KEY_USERID,
        		KEY_PASSWORD,
                KEY_PASSSTROKE}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves passwordField and userIDField columns based on domain---
    public Cursor getFields(String domain) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_USERIDFIELD,
                		KEY_PASSWDFIELD
                },
                		KEY_DOMAIN + "='" + domain + "'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
  //---retrieves all fields based on domain---
    public Cursor getPassStroke(String domain) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, null,
                		KEY_DOMAIN + "='" + domain + "'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
  //---checks if a given domain, userID and password have been saved---
    public boolean checkExist(String domain, String userID) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, null,
                		KEY_DOMAIN + "='" + domain + "' AND "
                		+ KEY_USERID + "='" + userID + "'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if(mCursor.getCount() > 0)
        	return true;
        else
        	return false;
    }

    //---updates a passStroke---
    public boolean updatePassStroke(String domain, String userID, 
    String password, String passStroke) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_USERID, userID);
        args.put(KEY_PASSWORD, password);
        args.put(KEY_PASSSTROKE, passStroke);
        return db.update(DATABASE_TABLE, args, 
        		KEY_DOMAIN + "='" + domain + "' AND "
        		+ KEY_USERID + "='" + userID + "'", null) > 0;
    }
    
    //returns a cursor to a table with all the domains in the database
    public Cursor getDomains()
    {
    	Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{"domain"},
    			null, null, null, null, null, null);
    	
    	mCursor.moveToFirst();
    	return mCursor;
    }
}