package com.drawIt;

import android.hardware.*; 

public class AccelerometerReader {// implements SensorEventListener{ 

	/* private float m_totalForcePrev; // stores the previous total force value 

	    // do your constructor and all other important stuff here 
	    // make sure you set totalForcePrev to 0 
	    // ... 


		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent event) {
			if(sensor == SensorManager.SENSOR_ACCELEROMETER) 
	          { 
	               double forceThreshHold = 1.5f; 
	                
	               double totalForce = 0.0f; 
	               totalForce += Math.pow(values[SensorManager.DATA_X]/SensorManager.GRAVITY_EARTH, 2.0); 
	               totalForce += Math.pow(values[SensorManager.DATA_Y]/SensorManager.GRAVITY_EARTH, 2.0); 
	               totalForce += Math.pow(values[SensorManager.DATA_Z]/SensorManager.GRAVITY_EARTH, 2.0); 
	               totalForce = Math.sqrt(totalForce); 
	                
	               if((m_gameState == STATE_RUNNING) && (totalForce < forceThreshHold) && (m_totalForcePrev > forceThreshHold)) 
	               { 
	                    doWrenchWord(); 
	               } 
	                
	               m_totalForcePrev = totalForce; 
	          } 
			
		} */
}