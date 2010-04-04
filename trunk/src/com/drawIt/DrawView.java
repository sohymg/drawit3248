package com.drawIt;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
	
	Paint paint;
	ArrayList<Line> lineList;

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
		Iterator<Line> iterator = lineList.iterator();
		while(iterator.hasNext()) {
			Line line = iterator.next();
			canvas.drawLine(line.startX, line.startY, line.stopX, line.stopY, paint);
		}
	}

	public void addLine(float startX, float startY, float stopX, float stopY) {
		lineList.add(new Line(startX, startY, stopX, stopY));

		this.invalidate();
	}
	
	public void clearLines() {
		lineList.clear();
		
		this.invalidate();
	}
}

