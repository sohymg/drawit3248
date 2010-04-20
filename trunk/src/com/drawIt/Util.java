package com.drawIt;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.Toast;


public class Util {
	
	static double ERROR_THRESHOLD = 0.19;
	static final String LOG_FILE = "drawItLog.txt";
	static ArrayList<String> log = new ArrayList<String>();
	
	public static void showMsg(Context context, String msg) {
		new AlertDialog.Builder(context)
		.setMessage(msg)
		.setPositiveButton(android.R.string.ok, null)
		.setCancelable(false)
		.create()
		.show();
	}

	public static void pl(String msg) {
		System.out.println(msg);
	}

	public static int LevenshteinDistance(String s, String t) { //m,n
		int m = s.length();
		int n = t.length();

		// d is a table with m+1 rows and n+1 columns
		int[][] d = new int[m+1][n+1];

		for(int i=0; i<=m; i++)
			d[i][0] = i; // deletion

		for(int j=0; j<=n; j++)
			d[0][j] = j; // insertion

		for(int j=1; j<=n; j++)
		{
			for(int i=1; i<=m; i++)
			{
				if(s.charAt(i-1) == t.charAt(j-1)) 
					d[i][j] = d[i-1][j-1];
				else
					d[i][j] = Math.min(Math.min
							(
									d[i-1][j] + 1,  // deletion
									d[i][j-1] + 1),  // insertion
									d[i-1][j-1] + 1 // substitution
					);
			}
		}

		return d[m][n];
	}
	
	public static int DTWDistance(String s, String t) { //n,m
		/*
		 *          0
		 *       7     1
		 *    6     *     2
		 *      5       3
		 *          4
		 */
		
		int[][] dd = {
				{0, 1, 2, 3, 4, 3, 2, 1}, //0
				{1, 0, 1, 2, 3, 4, 3, 2}, //1
				{2, 1, 0, 1, 2, 3, 4, 3}, //2
				{3, 2, 1, 0, 1, 2, 3, 4}, //3
				{4, 3, 2, 1, 0, 1, 2, 3}, //4
				{3, 4, 3, 2, 1, 0, 1, 2}, //5
				{2, 3, 4, 3, 2, 1, 0, 1}, //6
				{1, 2, 3, 4, 3, 2, 1, 0}  //7
				
		};
		
		int n = s.length();
		int m = t.length();
		
	    int[][] DTW = new int[n+1][m+1];
	    int i, j, cost;

	    for(i=1; i<=m; i++)
	        DTW[0][i] = Integer.MAX_VALUE;
	    for(i=1; i<=n; i++)
	        DTW[i][0] = Integer.MAX_VALUE;
	    DTW[0][0] = 0;

	    for(i=1; i<=n; i++)
	    	for(j=1; j<=m; j++) {
	    		int x = (s.charAt(i-1) - 48);
	    		int y = (t.charAt(j-1) - 48);
	    		
	    		cost = dd[x][y];
	            
	            DTW[i][j] = cost + Math.min(Math.min(DTW[i-1][j  ],    // insertion
	                                        DTW[i  ][j-1]),    // deletion
	                                        DTW[i-1][j-1]);    // match
	                                        
	    	}

	    return DTW[n][m];
	}
	
	//checks if the 2 passStrokes are an acceptable 'distance' from each other
	public static boolean isValid(String passStroke,String passStrokeCfm)
	{
		double err = (double)DTWDistance(passStroke, passStrokeCfm)/(double)passStroke.length();
		
		if(err <= ERROR_THRESHOLD)
			return true;
		else
			return false;
	}
	
	//vibrates the phone for a user specified length of time
	public static void vibrate(long length)
	{
		Vibrator v = (Vibrator) drawIt.activity.getSystemService(Context.VIBRATOR_SERVICE);    
		v.vibrate(length);  
	}
	
	/** Method used to load the log file from the HTC Magic's microSD card
	*/
	public static void readLogFile() {
		try {
			log.clear();
			File f = new File(Environment.getExternalStorageDirectory() + "/" + Util.LOG_FILE);
			FileInputStream fileIS = new FileInputStream(f);
			BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = new String();
			
			//just reading each line and pass it on the debugger
			while((readString = buf.readLine())!= null) {
				log.add(readString.trim());
			}
			
		}
		
		catch(Exception e) {
			log.clear();
			log.add("Log file first created on " + getDayTime() + "\n");
			writeLogFile();
			readLogFile();
		}
		
	}
	
	/** Method used to write the log file to the HTC Magic's microSD card
	*/
	public static void writeLogFile() {
		try {
			File root = Environment.getExternalStorageDirectory();
			if (root.canWrite()){
				File gpxfile = new File(root, Util.LOG_FILE);
				FileWriter gpxwriter = new FileWriter(gpxfile);
				BufferedWriter out = new BufferedWriter(gpxwriter);
				for(int i = 0; i < log.size(); i ++) {
					out.write(log.get(i));
					out.write("\n");
				}
				
				out.close();
			}
			
		}
		catch (Exception e) {
			Toast.makeText(drawIt.context, "Error creating log file", Toast.LENGTH_LONG).show();
		}
		
	}
	
	/** Method used to get the current system date & time.
	 *  Format the string in DD/MM/YYYY HH:MM:SS
	 *  @return	Formatted date-time string
	*/
	public static String getDayTime() {
		Calendar cNow = Calendar.getInstance();
		String date = "" + cNow.get(Calendar.DAY_OF_MONTH) + "/" + (cNow.get(Calendar.MONTH) + 1) + "/" + cNow.get(Calendar.YEAR);
		String time = "" + cNow.get(Calendar.HOUR_OF_DAY) + ":" + cNow.get(Calendar.MINUTE) + ":" + cNow.get(Calendar.SECOND) + " h";
		return date + "\t" + time;
	}
	//private static int d(char a, char b) {
		/*
		 *          0
		 *       7     1
		 *    6     *     2
		 *      5       3
		 *          4
		 */
		
	/*	int[][] dd = {
				{0, 1, 2, 3, 4, 3, 2, 1}, //0
				{1, 0, 1, 2, 3, 4, 3, 2}, //1
				{2, 1, 0, 1, 2, 3, 4, 3}, //2
				{3, 2, 1, 0, 1, 2, 3, 4}, //3
				{4, 3, 2, 1, 0, 1, 2, 3}, //4
				{3, 4, 3, 2, 1, 0, 1, 2}, //5
				{2, 3, 4, 3, 2, 1, 0, 1}, //6
				{1, 2, 3, 4, 3, 2, 1, 0}  //7
		};
		
	//	int i = Integer.parseInt(a + "");
	//	int j = Integer.parseInt(b + "");
		
		return dd[a-48][b-48];
	}*/
}
