package com.drawIt;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Util {
	
	static double ERROR_THRESHOLD = 0.15;
	
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
