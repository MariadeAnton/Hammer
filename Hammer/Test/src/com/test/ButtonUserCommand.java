package com.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.EditText;

public class ButtonUserCommand extends ButtonLinkAddable {

	
	

	private int colorD=Color.parseColor("#AC193D");
	private int colorL=Color.parseColor("#BF1E4B");
	
	
	
	
	
	
	
	
	
	
	
	private static final long serialVersionUID = 1L;

	public ButtonUserCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ButtonUserCommand(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected  void onDraw(Canvas c) {
		
		
		Align();
		
		
		drawBackground(c);
		drawBorders(c);
		drawText(c);
		super.onDraw(c);
		// TODO Auto-generated method stub
		
	}

	protected  void drawBackground(Canvas c)
	{	
		
		
		if(type==PRESS_EXECUTE)
		{
			path=new Path();
			paint.setStyle(Style.FILL);
			paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
			 
			
			path.moveTo(posX-getWidth()/2,posY+getHeight()/2);
			path.lineTo(posX-getWidth()/2,posY-getHeight()/2);
			path.lineTo(posX-getWidth()/4,posY-getHeight()/2);
			float start=getSemicircle(posX-getWidth()/4,posY-getHeight()/2,
					posX+getWidth()/4,posY-getHeight()/2,oval,0);
			path.arcTo(oval,start, 180);
			path.lineTo(posX+getWidth()/2,posY-getHeight()/2);
			path.lineTo(posX+getWidth()/2,posY+getHeight()/2);
			path.lineTo(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2);
			start=getSemicircle(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2,
					posX-getWidth()/3,posY+getHeight()/2,oval,0);
			path.arcTo(oval,start,-180);
			path.lineTo(posX-getWidth()/2,posY+getHeight()/2);
			c.drawPath(path, paint);
		}
		
		else
		{
	
		path=new Path();
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		 
		
		path.moveTo(posX-getWidth()/2,posY+getHeight()/2+3);
		path.lineTo(posX-getWidth()/2,posY-getHeight()/2);
		path.lineTo(posX-getWidth()/3,posY-getHeight()/2);
		float start=getSemicircle(posX-getWidth()/3,posY-getHeight()/2,
				posX-getWidth()/3+getHeight()/2,posY-getHeight()/2,oval,0);
		path.arcTo(oval,start, 180);
		path.lineTo(posX+getWidth()/2,posY-getHeight()/2);
		path.lineTo(posX+getWidth()/2,posY-getHeight()/4);
		path.lineTo(posX+3*getWidth()/4,posY-getHeight()/4);
		path.lineTo(posX+3*getWidth()/4,posY+getHeight()/4);
		path.lineTo(posX+getWidth()/2,posY+getHeight()/4);
		path.lineTo(posX+getWidth()/2,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2);
		start=getSemicircle(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2,
				posX-getWidth()/3,posY+getHeight()/2,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX-getWidth()/2,posY+getHeight()/2);
		path.addCircle(posX+3*getWidth()/4, posY, getHeight()/3,Direction.CW);
		c.drawPath(path, paint);
		
		
		}
	}
	
	protected  void drawBorders(Canvas c)
	{
		super.drawBorders(c);
		if(type==PRESS_EXECUTE)return;
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		c.drawCircle(posX+3*getWidth()/4, posY, getHeight()/3, paint);
		
	}

	protected  void drawText(Canvas c)
	{
		paint.setTextAlign(Align.LEFT);
		paint.setShader(null);
		
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextSize(getHeight()/3f);
		paint.setColor(Color.BLACK);
		c.drawText(getName(),posX-getWidth()/2+8,posY, paint);
	
	}
	
	
public boolean execute(Client client) {
			
		
		MyRunnable dialogUser=new MyRunnable();
		client.getHandler().post(dialogUser);
	
		synchronized(dialogUser.getLock())
		{
			try {
				
				dialogUser.getLock().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(dialogUser.getContinue())
		{
			if(child!=null)
				return child.execute(client);
		}
		else
			return false;
		
		return true;
}
	
	class MyRunnable implements Runnable
	{
		
		final Object lock=new Object();;
		boolean cont=false;
		Dialog dialog=null;
		@Override
		public void run() {
				// TODO Auto-generated method stub
		
			
			MyCustomDialog.Builder customBuilder=new MyCustomDialog.Builder(activity);
			customBuilder
				.setIcon(R.drawable.info)
				.setMessage("Do you want to continue this loop?")
				.setTitle("Continue")
				.setPositiveButton("Continue",new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						cont=true;
						synchronized(lock){	lock.notify();}
						dialog.dismiss();
						
					}
					
				})
				.setNegativeButton("Don't continue",new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						cont=false;// TODO Auto-generated method stub
						synchronized(lock){	lock.notify();}
						dialog.dismiss();
					}
					
				});
	     dialog = customBuilder.create();
		 dialog.show();
		 
		
		  }


		public Object getLock() {
			return lock;
		}
		public	boolean getContinue()
		{
			return cont;
		}
		
	}
	
}
