package com.drawIt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawSView extends SurfaceView implements SurfaceHolder.Callback {
	
	SurfaceHolder surfaceHolder;
	
	int bkgrdColor;
	
	Paint paint;

	public DrawSView(Context context, AttributeSet attrs) {
		super(context, attrs);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
        
        //setFocusable(true); // make sure we get key events
        
        paint = new Paint();
        
        bkgrdColor = Color.parseColor("#CCCCCC");
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = null;
		
		//draw on canvas one
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        
      //draw on canvas two
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		 
	}
	
	public void drawLine(float startX, float startY, float stopX, float stopY) {
		Canvas canvas = null;
		
		//draw on canvas one
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        
        
      //draw on canvas two
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawLine(startX, startY, stopX, stopY, paint); 
        } finally {
        	if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
	}

	public void clearLines() {
		Canvas canvas = null;
		
		//draw on canvas one
		try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        
        //draw on canvas two
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
	}
}
