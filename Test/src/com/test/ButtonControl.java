package com.test;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;


public class ButtonControl extends ButtonLinkAddable {


	private static final long serialVersionUID = 1L;
	protected PositionableObject slave;
	protected ButtonComparer condition;		
	
	private boolean complementable=false;


	int widthSup=210;
	int heightSup=75;
	
	int widthMed=35;
	int heightMed=30;
	
	int widthLow=120;
    int heightLow=40;
	
	private float conditionPosX=posX+50+widthSup/3.5f;
	private float conditionWidth=180;
	private float conditionHeight=50;	

	private int colorD=Color.parseColor("#094AB2");
	private int colorL=Color.parseColor("#0A5BC4");
	
	private int colorDCond=Color.parseColor("#65052D");
	private int colorLCond=Color.parseColor("#87274F");
	
	
	public ButtonControl() {
		
	}
	
	public ButtonControl(int width,int height) {
		super(width,height);
		
	}
	


	public void setSlave(PositionableObject _slave)
	{
		slave=_slave;
		if(slave!=null)slave.owner=this;
	}
	
	
	public void resize()
	{
		
		if(slave==null) heightMed=35;
		else
		{
			if(slave instanceof ButtonIfElse)
				heightMed=slave.getHeight();
			else
				heightMed=slave.getTotalSchemaHeight();
		}
			

		height=heightLow+heightMed+heightSup;

		if(SearchFirstParent().getOwner()!=null)
			SearchFirstParent().getOwner().resize();
		
	
	}

	public void addOrder(PositionableObject object)
	{
		
		if(slave==null)
		{
			slave=object;
			slave.setOwner(this);
			
		}
		else		
			object.setParent(slave.SearchLastChild());
		
		resize();
	}
	
	public void setCondition(ButtonComparer comp)
	{
		condition=comp;
		if(comp!=null)comp.owner=this;
	}

	
	
	public int getHeightSup() {
		return heightSup;
	}

	public void setHeightSup(int heightSup) {
		this.heightSup = heightSup;
	}

	public int getWidthMed() {
		return widthMed;
	}

	public void setWidthMed(int widthMed) {
		this.widthMed = widthMed;
	}

	

	@Override
	protected  void onDraw(Canvas c) {
		
		
		Align();
		drawBackground(c);
		drawBorders(c);
		drawText(c);
		drawCondition(c);
		
		
		if(condition!=null)condition.onDraw(c);
		if(slave!=null)slave.onDraw(c);
		super.onDraw(c);
		
			
	}
	
	
	


	protected  void drawBackground(Canvas c)
	{
		
		height=heightLow+heightMed+heightSup;
	

		
		path=new Path();
		paint.setStyle(Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		 
		
		
		path.moveTo(posX-getWidth()/2,posY-heightSup/2);
		path.lineTo(posX-getWidth()/3,posY-heightSup/2);
		float start=getSemicircle(posX-getWidth()/3,posY-heightSup/2,
				posX-getWidth()/3+heightSup/2,posY-heightSup/2,oval,0);
		path.arcTo(oval,start, 180);
		path.lineTo(posX+getWidth()/2+50,posY-heightSup/2);
		
		start=getSemicircle(posX+getWidth()/2+50,posY-heightSup/2,
				posX+getWidth()/2+50,posY+heightSup/2,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX+getWidth()/2+50,posY+heightSup/2);
		path.lineTo(widthMed+posX-getWidth()/3+heightSup/2,posY+heightSup/2);
		start=getSemicircle(posX-getWidth()/3+heightSup/2+widthMed,posY+heightSup/2,
				posX-getWidth()/3+widthMed,posY+heightSup/2,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX-getWidth()/2+widthMed,posY+heightSup/2);
		path.lineTo(posX-getWidth()/2+widthMed,posY+heightMed+heightSup/2);

		path.lineTo(widthMed+posX-getWidth()/3,posY+heightSup/2+heightMed);
		start=getSemicircle(widthMed+posX-getWidth()/3,posY+heightSup/2+heightMed,
				posX-getWidth()/3+widthMed+heightSup/2,posY+heightSup/2+heightMed,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX+getWidth()/3,posY+heightSup/2+heightMed);
		start=getSemicircle(posX+getWidth()/3,posY+heightSup/2+heightMed,
				posX+getWidth()/3,posY+heightSup/2+heightMed+heightLow,oval,0);
		path.arcTo(oval,start,180);
		path.lineTo(posX-getWidth()/3+heightSup/2,posY+heightSup/2+heightMed+heightLow);
		start=getSemicircle(posX-getWidth()/3+heightSup/2,posY+heightSup/2+heightMed+heightLow,
				posX-getWidth()/3,posY+heightSup/2+heightMed+heightLow,oval,0);
		path.arcTo(oval,start,-180);
		path.lineTo(posX-getWidth()/2,posY+heightSup/2+heightMed+heightLow);
		path.lineTo(posX-getWidth()/2,posY-heightSup/2);
		c.drawPath(path, paint);
		
    	
	}
	
	
	
	protected  void drawText(Canvas c)
	{
		
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(heightSup/3f);
		c.drawText(getName(),posX-widthSup/2 +8,posY, paint);
			
		
	}
	

	private void drawCondition(Canvas c)
	{
		
		conditionPosX=posX +widthSup/2.5f;
		
		if(type==ObjectExecutable.FOR_BUTTON)
		{
			c.drawCircle(conditionPosX+widthSup/4,posY,paint.getTextSize(), paint);
			paint.setColor(Color.WHITE);	
			paint.setTextSize(heightSup/4f);	
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(2.5f);
			paint.setTextAlign(Align.CENTER);
			c.drawText(String.valueOf(parameter.getValue()),conditionPosX+widthSup/4,
						posY-(paint.descent()+paint.ascent())/2,paint);
		}
		
		else if(type==ObjectExecutable.ELSE_CLAUSE)
			return;
		
		else 
		{
			path=new Path();
			paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorDCond,colorLCond,  Shader.TileMode.MIRROR));
			 
			
			paint.setStyle(Style.FILL_AND_STROKE);
			path.moveTo(conditionPosX-conditionWidth/2,posY);
			path.lineTo(conditionPosX-conditionWidth/3,posY-conditionHeight/2);
			path.lineTo(conditionPosX+conditionWidth/3,posY-conditionHeight/2);
			path.lineTo(conditionPosX+conditionWidth/2,posY);
			path.lineTo(conditionPosX+conditionWidth/3,posY+conditionHeight/2);
			path.lineTo(conditionPosX-conditionWidth/3,posY+conditionHeight/2);
			path.lineTo(conditionPosX-conditionWidth/2,posY);
			c.drawPath(path,paint);
			
			if(complementable)
			{
				paint.setColor(Color.MAGENTA);
				paint.setStyle(Style.FILL);
				paint.setStrokeWidth(3);
				c.drawPath(path,paint);
			}
			
			
		}
	
	}

	@Override
	public boolean execute(Client client) {
		// TODO Auto-generated method stub
		switch(type)
		{
		
		case ObjectExecutable.IF_CLAUSE:
			
			if(condition!=null)
			{
				if(condition.execute(client) && slave!=null)
						slave.execute(client);
				//else if(elseClause!=null)
					//elseClause.execute(client);
			}
					
					
			break;
		
		case ObjectExecutable.FOR_BUTTON:
			for(int i=0;i<parameter.getValue();i++)
			{
				if(slave!=null)
				{
					if(!slave.execute(client))
						break;
				}
				else break;
			}
			break;
			
			
		case ObjectExecutable.DO_WHILE:
			
			if(slave!=null)
				slave.execute(client);
			else break;
			
			if(condition!=null)
				while(condition.execute(client))	
				{
					if(slave!=null)
					{
						if(!slave.execute(client))
							break;
					}
					else break;
				}
				
			break;
			
		case ObjectExecutable.WHILE_CLAUSE:
			
			if(condition!=null && slave!=null)
			{
				while(condition.execute(client))
				{
					if(slave!=null)
					{
						if(!slave.execute(client))
							break;
					}
					else break;
				}
			}
			break;
			
		}

		if(child!=null)
				return child.execute(client);		
		
	
		return true;
		
	
	}
	
public void showParametersLayout() {
		
		if(type==FOR_BUTTON)
		{
		
		MyTouchableLayout touchLay;

		touchLay=(MyTouchableLayout)activity.findViewById(R.id.valueLay);
		touchLay.setIntercept(false);
		touchLay.setParameter(parameter);

		((ActivityScratch)getActivity()).showButtonParameters(touchLay);
		modificate=true;
		}
	}


	public float getConditionPosX() {
		return conditionPosX;
	}

	public void setConditionPosX(float conditionPosX) {
		this.conditionPosX = conditionPosX;
	}

	public float getConditionWidth() {
		return conditionWidth;
	}

	public void setConditionWidth(float conditionWidth) {
		this.conditionWidth = conditionWidth;
	}

	public float getConditionHeight() {
		return conditionHeight;
	}

	public void setConditionHeight(float conditionHeight) {
		this.conditionHeight = conditionHeight;
	}

	public boolean isComplementable() {
		return complementable;
	}

	public void setComplementable(boolean complementable) {
		this.complementable = complementable;
	}

	public ButtonComparer getCondition() {
		return condition;
	}

	public PositionableObject getSlave() {
		// TODO Auto-generated method stub
		return slave;
	}


	
	

		public int getWidthSup() {
			return widthSup;
		}

		public int getHeightMed() {
			return heightMed;
		}

		public void setHeightMed(int heightMed) {
			this.heightMed = heightMed;
		}

		public int getWidthLow() {
			return widthLow;
		}

		public int getHeightLow() {
			return heightLow;
		}

		

		public void setErasable(boolean erase)
		{
			super.setErasable(erase);
			if(slave!=null)
				slave.setErasable(erase);
			
		}
		
}

	
	

