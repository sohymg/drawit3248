package com.drawIt;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
	
	Paint paint;
	ArrayList<Line> lineList;
	
	Bitmap b;
	Canvas c;
	Rect bitmapRect;

	public DrawView(Context context) {
		super(context);
		init();
	}

	public DrawView(Context context, AttributeSet attrs) { 
		super(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint();
		lineList = new ArrayList<Line>();
	}

	protected void onDraw(Canvas canvas) {
		/*if(b != null)
			canvas.drawBitmap(b, bitmapRect,bitmapRect,  null);*/
		
		Iterator<Line> iterator = lineList.iterator();
		while(iterator.hasNext()) {
			Line line = iterator.next();
			canvas.drawLine(line.startX, line.startY, line.stopX, line.stopY, paint);
		}
	}

	public void addLine(float startX, float startY, float stopX, float stopY) {
		lineList.add(new Line(startX, startY, stopX, stopY));

		/*if(b == null) {
			b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			c = new Canvas();
			c.setBitmap(b);
			bitmapRect = new Rect(0,0,b.getWidth(),b.getHeight());
		}
		
		c.drawLine(startX, startY, stopX, stopY, paint);*/
		
		
		this.invalidate();
	}
	
	public void clearLines() {
		lineList.clear();
		
		/*b.recycle();
		b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		c.setBitmap(b);*/
		
		this.invalidate();
	}
}

