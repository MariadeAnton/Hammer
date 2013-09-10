package com.test;

import com.test.AuxBasicVariables.StringValue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;


public class ButtonVariable extends PositionableObject {

	

	private static final long serialVersionUID = 1L;
	private boolean complementable=false;
	private StringValue nameVar=AuxBasicVariables.createString("?");
	private ButtonRobot auxFunction=null;
	private ButtonRobot function=null;
	private int colorD=Color.parseColor("#008A00");
	private int colorL=Color.parseColor("#00A600");
	private MyVariableGrid variables;
	
	public ButtonVariable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ButtonVariable(int width,int height) {
		super(width,height);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public void Align() {
		// TODO Auto-generated method stub
		if(function==null)
			return;
		posX=function.getPosX()+function.getWidth()-getWidth()/2;
		posY=function.getPosY();
		
		
	}

	@Override
	public boolean ShowPreliminarLink(PositionableObject object) {
		
		if(object.getType()!=DEBURRING)return false;
		complementable=false;
		if(auxFunction!=null) auxFunction.setComplementable(false);
		auxFunction=null;  
		
		if(object instanceof ButtonRobot)
		{
		
			if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
					&& this.getPosY()>object.getPosY()-object.getHeight()/2 && this.getPosY()< object.getPosY()+object.getHeight()/2)
						
			{
				
					complementable=true;
					auxFunction=(ButtonRobot)object;
					auxFunction.setComplementable(true);
					return true;
			}
		}
		
		return false;
	}

	

	@Override
	public boolean SetLink() {
		if(complementable)
		{
			function=auxFunction;
			function.setComplement(this);
			complementable=false;
			return true;
		}
		else return false;
	}

	@Override
	public boolean FreeLink() {
		if(function!=null)
		{
			function.setComplement(null);
			function=null;
			return true;
		}
		else return false;
	}

	@Override
	public boolean execute(Client client) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onDraw(Canvas c) {
		
		Align();
		drawBackground(c);
		drawBorders(c);
		drawText(c);// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawBackground(Canvas c) {
		// TODO Auto-generated method stub
		path=new Path();
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		 
	
		path.moveTo(posX-getWidth()/2,posY-getHeight()/2);
		path.lineTo(posX+getWidth()/2,posY-getHeight()/2);
		
		float start=getSemicircle(posX+getWidth()/2,posY-getHeight()/2,
				posX+getWidth()/2,posY+getHeight()/2,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX-getWidth()/2,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/2,posY+getHeight()/3);
		start=getSemicircle(posX-getWidth()/2,posY+getHeight()/3,
				posX-getWidth()/2,posY-getHeight()/3,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX-getWidth()/2,posY-getHeight()/2);
		c.drawPath(path, paint);
	}

	@Override
	protected void drawBorders(Canvas c) 
	{
		// TODO Auto-generated method stub
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4.0f);
		paint.setStyle(Style.STROKE);
		paint.setShader(null);
		if(complementable)
		{
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(8.0f);
		}
		else if(modificate)
			paint.setStrokeWidth(16.0f);

			
		
		c.drawPath(path, paint);
	}

	@Override
	protected void drawText(Canvas c) {
		// TODO Auto-generated method stub
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(getHeight()/3.5f);
		c.drawText(nameVar.getString(),posX,posY-(paint.descent()+paint.ascent())/2, paint);
		
	}

	public ButtonRobot getFunction() {
		return function;
	}

	public void setFunction(ButtonRobot function) {
		this.function = function;
	}

	public String getNameVar() {
		return nameVar.getString();
	}

	public void setNameVar(String nameVar) {
		this.nameVar.setString(nameVar);
	}

	@Override
	public void showParametersLayout() {
		
		
         MyTouchableLayout varSelect=(MyTouchableLayout)activity.findViewById(R.id.selVarLay);
         varSelect.setIntercept(false);
         varSelect.setStringValue(nameVar);
         varSelect.setParameter(parameter);
         ((ActivityScratch)getActivity()).showButtonParameters(varSelect);
         modificate=true;
	}

	public double getVariableValue()
	{
		variables=((ActivityScratch)getActivity()).getVarGrid();
		return variables.searchVariable(nameVar.getString());
	}
	
	public void setVariablesGrid(MyVariableGrid grid)
	{
		variables=grid;
	}
}
