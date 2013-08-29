package com.test;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.AuxBasicVariables.StringValue;

public class ButtonOperator extends ButtonLinkAddable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringValue valueX=AuxBasicVariables.createString("?");
	private StringValue valueY=AuxBasicVariables.createString("?");
	private int colorD=Color.parseColor("#008A00");
	private int colorL=Color.parseColor("#00A600");
	private MyVariableGrid variables;
	
	
	public ButtonOperator() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ButtonOperator(int width,int height) {
		super(width,height);
		
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
		
		start=getSemicircle(posX+getWidth()/2,posY-getHeight()/2,
				posX+getWidth()/2,posY+getHeight()/2,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX+getWidth()/2,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2);
		start=getSemicircle(posX-getWidth()/3+getHeight()/2,posY+getHeight()/2,
				posX-getWidth()/3,posY+getHeight()/2,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX-getWidth()/2,posY+getHeight()/2);
		c.drawPath(path, paint);
	}
	
	

	protected  void drawText(Canvas c)
	{
		paint.setTextAlign(Align.CENTER);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextSize(getHeight()/3f);
		paint.setColor(Color.BLACK);
		if(type==ObjectExecutable.ADD_ONE || type== ObjectExecutable.SUSTRACT_ONE)
			c.drawText(valueX.getString()+" "+name+" ",posX, posY-(paint.descent()+paint.ascent())/2, paint);
		else
			c.drawText(valueX.getString()+" "+name+" "+valueY.getString(),posX, posY-(paint.descent()+paint.ascent())/2, paint);	
			
	}
	public void setValueX(String vB) {
		valueX.setString(vB);
	}
	
	public void setValueY(String vA) {
		valueY.setString(vA);
	}

	public String getValueX() {
		return valueX.getString();
	}

	public String getValueY() {
		return valueY.getString();
	}

	@Override
	public boolean execute(Client client) {
	
		double vX=0;
		double vY=0;
		variables=((ActivityScratch)getActivity()).getVarGrid();
	
		if(type!=ObjectExecutable.ADD_ONE && type!= ObjectExecutable.SUSTRACT_ONE)
		{
			if(variables.searchVariable(valueX.getString())==null || variables.searchVariable(valueY.getString())==null)
				return false;
			vX=variables.searchVariable(valueX.getString());
			vY=variables.searchVariable(valueY.getString());
		}
		else
		{
			if(variables.searchVariable(valueX.getString())==null)return false;
			vX=variables.searchVariable(valueX.getString());
		}
			
		
		
		switch (type)
		{
			case ObjectExecutable.C_ADD:
			case ObjectExecutable.ADD:
				variables.changeVariableValue(valueX.getString(),vX+vY);	
				break;		
				
			case ObjectExecutable.C_SUSTRACT:
			case ObjectExecutable.SUSTRACT:
				variables.changeVariableValue(valueX.getString(),vX-vY);	
				break;		
			
			case ObjectExecutable.C_MULTIPLY:
			case ObjectExecutable.MULTIPLY:
				variables.changeVariableValue(valueX.getString(),vX*vY);	
				break;		
		
			case ObjectExecutable.C_DIVIDE:
			case ObjectExecutable.DIVIDE:
				variables.changeVariableValue(valueX.getString(),vX/vY);	
				break;				
				
			case ObjectExecutable.ADD_ONE:
				variables.changeVariableValue(valueX.getString(),++vX);	
				break;	
				
			case ObjectExecutable.SUSTRACT_ONE:
				variables.changeVariableValue(valueX.getString(),--vX);	
				break;	
				
			case ObjectExecutable.REASIGN_VALUE:
				variables.changeVariableValue(valueX.getString(),vY);	
				break;	
				
			default:
				break;
			
		}
		
		client.getHandler().sendEmptyMessage(0);
		
		if(child!=null)
			return child.execute(client);		
		

	
		
		return true;
		
	
	}

	public void showParametersLayout() {
		
	
		MyTouchableLayout touchLay=null;
		
		if(type==ADD_ONE || type==SUSTRACT_ONE)
		{
			touchLay=(MyTouchableLayout)activity.findViewById(R.id.selVarLay);
			
			
		}
		else if(type>CONSTANT)
		{
			touchLay=(MyTouchableLayout)activity.findViewById(R.id.compLayConst);
			touchLay.setStringValue2(valueY);
			((TextView)touchLay.findViewById(R.id.oper)).setText(name);
	         
		}
		else
		{
			touchLay=(MyTouchableLayout)activity.findViewById(R.id.compLay);
			touchLay.setStringValue2(valueY);
			((TextView)touchLay.findViewById(R.id.oper)).setText(name);
		}
		
		touchLay.setIntercept(false);
		touchLay.setStringValue(valueX);
		((ActivityScratch)getActivity()).showButtonParameters(touchLay);
		modificate=true;
	}
	
	public void setVariablesGrid(MyVariableGrid grid)
	{
		variables=grid;
	}
	
}
