package com.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;

public class Variable{
	
	private String name="?";
	private double value=0;
	private RectF rect=new RectF();
	private int widthDraw=100;
	private int heightDraw=50;
	private float posDrawX=0,posDrawY=0;
	
	
	public Variable(){};
	
	public Variable(String name, double value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public void setDrawLimits(int width,int height)
	{
		widthDraw=width;
		heightDraw=height;
	}
	
	public void setDrawPosition(float x,float y)
	{
		posDrawX=x;
		posDrawY=y;
	}
	public void draw(Canvas c,Paint paint)
	{
		paint.setTextAlign(Align.CENTER);
		rect.set((posDrawX-widthDraw/2),(posDrawY-heightDraw/2),(posDrawX+widthDraw/2),(posDrawY+heightDraw/2));
		c.drawRoundRect(rect,widthDraw/10,heightDraw/10, paint);
		paint.setColor(Color.BLACK);
		c.drawText(name+" = "+String.valueOf(value),posDrawX, posDrawY-(paint.descent()+paint.ascent())/2, paint);
	}

	public float getPosDrawX() {
		return posDrawX;
	}

	public float getPosDrawY() {
		return posDrawY;
	}

	public int getWidthDraw() {
		return widthDraw;
	}

	public int getHeightDraw() {
		return heightDraw;
	}
	
}