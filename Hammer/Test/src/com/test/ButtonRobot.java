package com.test;



import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.AuxBasicVariables.TriplePoint;



public class ButtonRobot extends ButtonLinkAddable {

	
	private static final long serialVersionUID = 1L;
	private static TriplePoint INITIAL=AuxBasicVariables.createTriplePoint(1.21, 0, 1.5);
	private static double SPEED=1.0f;
	private boolean complementable=false;
	private ButtonVariable complement=null;
	private TriplePoint point=AuxBasicVariables.createTriplePoint(10000,10000,10000);
	private int colorD=Color.parseColor("#8C0095");
	private int colorL=Color.parseColor("#A700AE");
	
	
	public ButtonRobot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ButtonRobot(int width,int height) {
		super(width,height);
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public boolean execute(Client client) {
		// TODO Auto-generated method stub
		switch(type)
		{
		case INIT_POSE:
			client.sendInitPos(point,parameter.getValue());
			INITIAL.x=point.x;
			INITIAL.y=point.y;
			INITIAL.z=point.z;
			SPEED=parameter.getValue();
			break;
		case DEBURRING:
			AuxPiece piece=((ActivityScratch)activity).getGlSurface3D().getGlRajSurface().getObjectSelected();
			if(piece==null)
			{
				Runnable message=new Runnable(){
					
					@Override
					public void run() {
						Toast toast=Toast.makeText(getActivity(), "There isn't a piece selected!",Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
				
				
					};
				};
				client.getHandler().post(message);
				return false;
			}
			ArrayList<TriplePoint> pathPiece=piece.getPath();
			
			double depth=complement.getVariableValue();
			client.sendInitPos(INITIAL,SPEED);	
			client.sendPosition(0,0,depth, 0, 0, 0);
			for(int i=0;i<pathPiece.size();i++)
				client.sendPosition(pathPiece.get(i).x,pathPiece.get(i).y,pathPiece.get(i).z, 0, 0, 0);
			
			
			
			break;
		}
		
		if(child!=null)
			return child.execute(client);
		
		
		return true;
	}

	@Override
	protected void onDraw(Canvas c) {

		Align();
		drawBackground(c);
		drawBorders(c);
		drawText(c);
		
		if(complement!=null)complement.onDraw(c);
		
		// TODO Auto-generated method stub
		super.onDraw(c);
	}

	protected void drawBackground(Canvas c)
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
		
		if(type==INIT_POSE && point!=null)
			drawInitialPose(c);
		
	
		
	}
	protected  void drawBorders(Canvas c)
	{
		super.drawBorders(c);
		
		if(complementable)
		{
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(8.0f);
		}
		
		c.drawPath(path, paint);
		
	}
	
	protected  void drawText(Canvas c)
	{
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(getHeight()/3.5f);
		
		if(getActivity()instanceof ActivityScratch)
		{
			if(type==DEBURRING &&((ActivityScratch)getActivity()).getGlSurface3D().getGlRajSurface().getObjectSelected()!=null && complement!=null)
				paint.setColor(Color.GREEN);
			else if(type==DEBURRING)
				paint.setColor(Color.RED);
		}
		c.drawText(getName(),posX-getWidth()/2+8,posY-(paint.descent()+paint.ascent())/2-getHeight()/8, paint);
		
	
	}
	
	private void drawInitialPose(Canvas c)
	{
		if(type==INIT_POSE && point.x!=10000 && point.y!=10000 &&point.z!=10000)
		{
			
			
			
			path.moveTo(posX+getWidth()/2,posY-getHeight()/2);
			path.lineTo(posX+getWidth()/2,posY-getHeight()/3);
			
			
			
			float start=getSemicircle(posX+getWidth()/2,posY-getHeight()/3,
					posX+getWidth()/2,posY+getHeight()/3,oval,0);
			path.arcTo(oval,start,-180);
			path.lineTo(posX+getWidth()/2,posY+getHeight()/2);
			
			path.lineTo(posX+3.2f*getWidth()/2,posY+getHeight()/2);
			path.lineTo(posX+3.2f*getWidth()/2,posY+getHeight()/3);
			start=getSemicircle(posX+3.2f*getWidth()/2,posY+getHeight()/3,
					posX+3.2f*getWidth()/2,posY-getHeight()/3,oval,0);
			path.arcTo(oval,start,-180);
			path.lineTo(posX+3.2f*getWidth()/2,posY-getHeight()/2);
			path.lineTo(posX+getWidth()/2,posY-getHeight()/2);
			c.drawPath(path, paint);
			
			
			paint.setColor(Color.BLACK);	
			paint.setShader(null);
			paint.setTextSize(getHeight()/4f);	
			paint.setStrokeWidth(2);
			paint.setTextAlign(Align.CENTER);
			
			c.drawRect(posX+getWidth()/2f+getWidth()/13f, posY-getHeight()/3f,
					posX+getWidth()/2f+4*getWidth()/13f,posY+getHeight()/3f,paint);
			c.drawRect(posX+getWidth()/2f+5*getWidth()/13f, posY-getHeight()/3f,
					posX+getWidth()/2f+8*getWidth()/13f,posY+getHeight()/3f,paint);
			c.drawRect(posX+getWidth()/2f+9*getWidth()/13f, posY-getHeight()/3f,
					posX+getWidth()/2f+12*getWidth()/13f,posY+getHeight()/3f,paint);
		
			paint.setColor(Color.WHITE);	
			c.drawText(String.valueOf(point.x),posX+getWidth()/2+2.5f*getWidth()/13,
						posY-(paint.descent()+paint.ascent())/2,paint);
			c.drawText(String.valueOf(point.y),posX+getWidth()/2+6.5f*getWidth()/13,
						posY-(paint.descent()+paint.ascent())/2,paint);
			c.drawText(String.valueOf(point.z),posX+getWidth()/2+10.5f*getWidth()/13,
						posY-(paint.descent()+paint.ascent())/2,paint);
			
			
			
			
			paint.setColor(Color.BLUE);	
			c.drawCircle(posX+3.2f*getWidth()/2, posY,getHeight()/2-getHeight()/6, paint);
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.BLACK);	
			paint.setStrokeWidth(4.0f);
			c.drawCircle(posX+3.2f*getWidth()/2, posY,getHeight()/2-getHeight()/6, paint);
			paint.setStrokeWidth(2.0f);
			c.drawText(String.valueOf(parameter.getValue()),posX+3.2f*getWidth()/2,
					posY-(paint.descent()+paint.ascent())/2,paint);
			
		}
	}
	
	public void showParametersLayout()
	{
		
         if(type==INIT_POSE)
         {
        
         MyTouchableLayout inpose=(MyTouchableLayout)activity.findViewById(R.id.initPosParam);
         inpose.setIntercept(false);
         inpose.setPoint(point);
        
         inpose.setParameter(parameter);
         ((ActivityScratch)getActivity()).showButtonParameters(inpose);
         modificate=true;
         }
         
	}

	public boolean isComplementable() {
		return complementable;
	}

	public void setComplementable(boolean complementable) {
		this.complementable = complementable;
	}

	public ButtonVariable getComplement() {
		return complement;
	}

	public void setComplement(ButtonVariable complement) {
		this.complement = complement;
		complementable=false;
		if(complement!=null)complement.setFunction(this);
		
	}

	public TriplePoint getPoint() {
		return point;
	}

	public void setPoint(TriplePoint point) {
		this.point = point;
	}


	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 /*
	 public void drawCurveLine(float ax,float ay,float bx,float by,float angle,Path path)
	 {
		 float a1Degrees = angle; 
		 double a1 = Math.toRadians(a1Degrees); 
		 double dx = bx - ax, dy = by - ay;
		 double l = Math.sqrt((dx * dx) + (dy * dy)); double l1 = l / 2.0;
	
		 double h = l1 / (Math.tan(a1 / 2.0)); 
		 double r = l1 / (Math.sin(a1 / 2.0));
		 double a2 = Math.atan2(dy, dx);
		 double a3 = (Math.PI / 2.0) - a2;
	 
		 double mX = (ax + bx) / 2.0;
		 double mY = (ay + by) / 2.0;
		 double cY = mY + (h * Math.sin(a3)); 
		 double cX = mX - (h * Math.cos(a3)); 
	 
	 RectF oval = new RectF((float) (cX - r), (float) (cY - r), (float) (cX + r),
			 (float) (cY + r)); 
	 double rawA4 = Math.atan2(ay - cY, ax - cX);
	 float a4 = (float) Math.toDegrees(rawA4); 
	 path.arcTo(oval, a4, a1Degrees);
	 }
	 */
}
