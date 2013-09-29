package com.test;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;



public class ButtonMovement extends ButtonLinkAddable{
	

	
	private static final long serialVersionUID = 1L;

	private int colorD=Color.parseColor("#C69408");
	private int colorL=Color.parseColor("#CEA539");

	
	public  ButtonMovement()
	{
		super();
		
	}
	
	public ButtonMovement(int width,int height) {
		super(width,height);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(Client client) {
		
		
		double i=parameter.getValue();
		
		
		
			switch(type)
			{
			case MOVE_X:
				client.sendPosition(i,0,0,0,0,0);
				break;
			case MOVE_Y:
				client.sendPosition(0,i,0,0,0,0);
				break;
			case MOVE_Z:
				client.sendPosition(0,0,i,0,0,0);
				break;
			case TURN_X:
				client.sendPosition(0,0,0,i,0,0);
				break;
			case TURN_Y:
				client.sendPosition(0,0,0,0,i,0);
				break;
			case TURN_Z:
				client.sendPosition(0,0,0,0,0,i);
				break;
			}
	
		if(child!=null)
			return child.execute(client);
		
		
		return true;
	}

	@Override
	protected  void onDraw(Canvas c) {
	
	
		Align();
		
		drawBackground(c);
		drawBorders(c);
		drawText(c);
		

		super.onDraw(c);

	}
	
	




	protected  void drawBackground(Canvas c)
	{	
		
		path=new Path();
		
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		 
		
		path.moveTo(posX-getWidth()/2,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/2,posY-getHeight()/2);
		path.lineTo(posX-getWidth()/3,posY-getHeight()/2);
		float start=getSemicircle(posX-getWidth()/3,posY-getHeight()/2,
				posX-getWidth()/3+getHeight()/2,posY-getHeight()/2,oval,0);
		path.arcTo(oval,start, 180);
		path.lineTo(posX+getWidth()/2,posY-getHeight()/2);
		
		start=getSemicircle(posX+getWidth()/2,posY-getHeight()/3,
				posX+getWidth()/2,posY+getHeight()/3,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX+getWidth()/2,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2);
		start=getSemicircle(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2,
				posX-getWidth()/3,posY+getHeight()/2,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX-getWidth()/2,posY+getHeight()/2);	
		c.drawPath(path, paint);
	
		
		
	}
	protected  void drawBorders(Canvas c)
	{
	
		super.drawBorders(c);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL_AND_STROKE);
		c.drawCircle(posX+getWidth()/2, posY,getHeight()/2-getHeight()/6, paint);
	}
	
	protected void drawText(Canvas c)
	{
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(getHeight()/3.5f);
		c.drawText(getName(),posX-getWidth()/2+8,posY-(paint.descent()+paint.ascent())/2-getHeight()/8, paint);
		paint.setColor(Color.WHITE);	
		paint.setTextSize(getHeight()/4f);	
		
		paint.setTextAlign(Align.CENTER);
		c.drawText(String.valueOf(parameter.getValue()),posX+getWidth()/2,
				posY-(paint.descent()+paint.ascent())/2,paint);
		
		
	}
	
public void showParametersLayout() {
		
		
		MyTouchableLayout touchLay;

		touchLay=(MyTouchableLayout)activity.findViewById(R.id.valueLay);
		touchLay.setIntercept(false);
		touchLay.setParameter(parameter);

		((ActivityScratch)getActivity()).showButtonParameters(touchLay);
		modificate=true;
	}


	
	


	

}