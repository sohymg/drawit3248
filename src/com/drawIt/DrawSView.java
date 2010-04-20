package com.drawIt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
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
        
        
        
        paint = new Paint();
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor(attrs.getAttributeValue(null, "lineColor")));
       
        //Util.pl(attrs.getAttributeValue(null, "bkgrdColor"));
        bkgrdColor = Color.parseColor(attrs.getAttributeValue(null, "bkgrdColor"));
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = null;
		
		//draw on canvas one
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
            
            
            canvas.drawLine(20, 20, canvas.getWidth() - 20, 20, new Paint());
            canvas.drawLine(20, 20, 20, canvas.getHeight() - 20, new Paint());
            canvas.drawLine(canvas.getWidth() - 20, 20, canvas.getWidth() - 20, canvas.getHeight() - 20, new Paint());
            canvas.drawLine(20, canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20, new Paint());
            
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        
      //draw on canvas two
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(bkgrdColor);
            
            canvas.drawLine(20, 20, canvas.getWidth() - 20, 20, new Paint());
            canvas.drawLine(20, 20, 20, canvas.getHeight() - 20, new Paint());
            canvas.drawLine(canvas.getWidth() - 20, 20, canvas.getWidth() - 20, canvas.getHeight() - 20, new Paint());
            canvas.drawLine(20, canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20, new Paint());
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
