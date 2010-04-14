/*
 * this class shows the drawing interface for pass stroke.
 * used for 5 scenarios:
 * 	1. login
 * 	2. save a new pass stroke
 * 	3. redraw to confirm the new pass stroke
 * 	4. save a new stroke for password management
 * 	5. redraw to confirm the new pass stroke for password management
 * 
 * 	to use the class for a scenario, start it with intent.putExtra("mode", DrawScreen.DRAW_TO_SAVE);
 */

package com.drawIt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class DrawScreen extends Activity {

	public static final int DRAW_TO_LOGIN 		= 0;
	public static final int DRAW_TO_SAVE	 	= 1;
	public static final int DRAW_TO_CFM 		= 2;
	public static final int DRAW_TO_PM_SAVE 	= 3; //used for password management
	public static final int DRAW_TO_PM_CFM 		= 4; //used for password management
	int drawMode;
	
	static int MIN_PS_LENGTH = 3; //the minimum length of pass stroke in segments
	
	//this is the squared dist, to save on square root computation
	//change this value to set the length of each line/stroke segment
	static double MIN_SQR_DIST_BET_PT = 100; 
	static double ERROR_THRESHOLD = 0.15;
	
	float startX, startY;
	
	Bundle extras;

	String passStroke, passStrokeCfm;
	
	DrawView drawView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		extras = getIntent().getExtras();
		drawMode = extras.getInt("mode");
		
		switch(drawMode) {
			case DRAW_TO_LOGIN:				
				setContentView(R.layout.draw_to_login);
				break;
			case DRAW_TO_SAVE:				
				setContentView(R.layout.draw_to_save);
				break;
			case DRAW_TO_CFM:
				passStroke = extras.getString("passStroke");
				setContentView(R.layout.draw_to_cfm);
				break;
			case DRAW_TO_PM_SAVE:
				break;
			case DRAW_TO_PM_CFM:
				break;
		}
	
		drawView = (DrawView)findViewById(R.id.drawView); 
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startDraw(event.getX(), event.getY());
				break;
				
			case MotionEvent.ACTION_MOVE:
				drawing(event.getX(), event.getY());
				break;
				
			case MotionEvent.ACTION_UP:
				endDraw();
				break;
				
		}
		return true;
	}
	
	private void startDraw(float startX, float startY) {
		this.startX = startX;
		this.startY = startY;
		
		switch(drawMode) {
			case DRAW_TO_LOGIN:
			case DRAW_TO_SAVE:
			case DRAW_TO_PM_SAVE:
				passStroke = "";
				break;
			case DRAW_TO_CFM:
			case DRAW_TO_PM_CFM:
				passStrokeCfm = "";
				break;
		}
	}
	
	private void drawing(float x, float y) {
		double sqrDist = Math.pow(startX - x, 2) + Math.pow(startY - y, 2);
		
		if(sqrDist > MIN_SQR_DIST_BET_PT) { //if the dist between the pts is far enough, add a new line
			
			int clockValue = toClockValue(startX, startY, x, y);
			float[] stopPts = toClockLine(startX, startY, clockValue);
			
			drawView.addLine(startX, startY, stopPts[0], stopPts[1]);
			
			startX = stopPts[0];
			startY = stopPts[1];
			
			switch(drawMode) {
				case DRAW_TO_LOGIN:
				case DRAW_TO_SAVE:
				case DRAW_TO_PM_SAVE:
					passStroke += clockValue;
					break;
				case DRAW_TO_CFM:
				case DRAW_TO_PM_CFM:
					passStrokeCfm += clockValue;
					break;
			}
		}
	}
	
	private void endDraw() {
		drawView.clearLines();
		
		Intent intent;
		
		switch(drawMode) {
			case DRAW_TO_LOGIN:
				if(DatabaseManager.getLogin(drawIt.context, extras.getString("domain"), passStroke) != null) { //if pass stroke exist
					intent = new Intent();
					intent.putExtra("domain", extras.getString("domain"));
					intent.putExtra("passStroke", passStroke);
					
					setResult(RESULT_OK, intent);
					finish(); //return to drawit
				}
				else {
					Util.showMsg(this, "Pass Stroke not recognized. Please try again");
				}
				break;
			case DRAW_TO_SAVE:
				if(passStroke.length() >= MIN_PS_LENGTH) { //if pass stroke is of min. length
					intent = new Intent(this, DrawScreen.class);
					intent.putExtra("mode", DRAW_TO_CFM);
					intent.putExtra("passStroke", passStroke);
					
					startActivityForResult(intent, DRAW_TO_CFM); //show redraw to confirm save screen
				}
				else {
					Util.showMsg(this, "Please draw a longer pass stroke");
				}
				break;
			case DRAW_TO_CFM:
				Util.showMsg(this, "length: " + passStroke.length() 
						+ " LD: " + Util.LevenshteinDistance(passStroke, passStrokeCfm)
						+ " DTW: " + Util.DTWDistance(passStroke, passStrokeCfm));
				 //if redraw matches 1st draw, save is successful
				if((double)Util.DTWDistance(passStroke, passStrokeCfm)/(double)passStroke.length() < ERROR_THRESHOLD) {
					setResult(RESULT_OK);
					finish(); //return to draw_to_save
				}
				else {
					Util.showMsg(this, "Your Pass-Strokes do not match.\nPlease try again.");
				}
				break;
			case DRAW_TO_PM_SAVE:
				break;
			case DRAW_TO_PM_CFM:
				break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == DRAW_TO_CFM && resultCode == RESULT_OK) {
			Intent intent = new Intent();
			intent.putExtra("domain", extras.getString("domain"));
			intent.putExtra("formName", extras.getString("formName"));
			intent.putExtra("useridField", extras.getString("useridField"));
			intent.putExtra("userid", extras.getString("userid"));
			intent.putExtra("passwdField", extras.getString("passwdField"));
			intent.putExtra("passwd", extras.getString("passwd"));
			intent.putExtra("passStroke", passStroke);
			
			setResult(RESULT_OK, intent);
			finish(); //return to drawit
		}
		else if(requestCode == DRAW_TO_PM_CFM && resultCode == RESULT_OK) {
			
		}
	}
	
	public static int toClockValue(float startX, float startY, float stopX, float stopY) {
		//change line to a clock value
		
		/*
		 *          0
		 *       7     1
		 *    6     *     2
		 *      5       3
		 *          4
		 */
		
		float xDist = stopX - startX;
		float yDist = stopY - startY;
		double angle = Math.abs(Math.atan(yDist / xDist) * (180 / 3.1415));
		
		//determine angle
		if(xDist < 0 && yDist >= 0)
			angle = 180 - angle;
		else if(xDist < 0 && yDist < 0)
			angle = 180 + angle;
		else if(xDist >= 0 && yDist < 0)
			angle = 360 - angle;
		
		//determine clock value
		if(angle >= 337.5 || angle < 22.5)
			return 2;
		else if(angle >= 22.5 && angle < 67.5)
			return 3;
		else if(angle >= 67.5 && angle < 112.5)
			return 4;
		else if(angle >= 112.5 && angle < 157.5)
			return 5;
		else if(angle >= 157.5 && angle < 202.5)
			return 6;
		else if(angle >= 202.5 && angle < 247.5)
			return 7;
		else if(angle >= 247.5 && angle < 292.5)
			return 0;
		else if(angle >= 292.5 && angle < 337.5)
			return 1;
		
		return -1;
	}
	
	public static float[] toClockLine(float startX, float startY, int clockValue) {
		//plot the stop points of a line in the direction of the clock value
		
		float[] stopPts = {startX, startY};
		
		double strDist = Math.sqrt(MIN_SQR_DIST_BET_PT);
		double diaDist = Math.sin(Math.PI/4) * strDist;
		
		switch(clockValue) {
			case 4:
				stopPts[1] += strDist;
				break;
			case 3:
				stopPts[0] += diaDist;
				stopPts[1] += diaDist;
				break;
			case 2:
				stopPts[0] += strDist;
				break;
			case 1:
				stopPts[0] += diaDist;
				stopPts[1] -= diaDist;
				break;
			case 0:
				stopPts[1] -= strDist;
				break;
			case 7:
				stopPts[0] -= diaDist;
				stopPts[1] -= diaDist;
				break;
			case 6:
				stopPts[0] -= strDist;
				break;
			case 5:
				stopPts[0] -= diaDist;
				stopPts[1] += diaDist;
				break;
		}
		
		return stopPts;
	}
}