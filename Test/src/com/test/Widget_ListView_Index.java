package com.test;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Widget_ListView_Index extends Drawable{

	private int radius=1;
	private int textColor=0;
	private int backgroundColor=0;
	private Paint paint=new Paint();
	private int index=-1;
	private int cx=0,cy=0;
	private Canvas canvas=new Canvas() ;
	
	
	Widget_ListView_Index(int textColor,int backgroundColor)
	{
		
		this.textColor=textColor;
		this.backgroundColor=backgroundColor;
		
	}
	@Override
	public void draw(Canvas canvas) {
		
		
		paint.setColor(backgroundColor);
		this.canvas.drawCircle(cx,cy, radius, paint);
		paint.setColor(textColor);
		paint.setTextSize(radius/2);
		this.canvas.drawText(String.valueOf(index), cx, cy-(paint.descent()+paint.ascent())/2, paint);
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setIndex(int index) {
		this.index = index;
		this.draw(canvas);
	}
	
	public void setPosition(int x, int y)
	{
		cx=x;
		cy=y;
	}
	public int getRadius() {
		return radius;
	}
	public int getTextColor() {
		return textColor;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	
}
