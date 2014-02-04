package com.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.Typeface;



public class ButtonComparer extends PositionableObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String vA=new String("?");
	private String vB=new String("?"); 
	private boolean complementable=false;
	
	private int colorD=Color.parseColor("#D24726");
	private int colorL=Color.parseColor("#DC572E");
	private MyVariableGrid variables;
	
	
	public ButtonComparer() {
		super();
		width=180;
		// TODO Auto-generated constructor stub
	}
	
	public ButtonComparer(int width,int height) {
		super(width,height);
		
		// TODO Auto-generated constructor stub
	}

	

	
	
	@Override
	protected  void onDraw(Canvas c) {
		
		Align();
		drawBackground(c);
		drawBorders(c);
		drawText(c);
		
		// TODO Auto-generated method stub
		
	}

	protected  void drawBackground(Canvas c)
	{
		path.reset();
		paint.setShader(new LinearGradient(0, 0, getWidth(),0,colorD,colorL,  Shader.TileMode.MIRROR));
		 
		paint.setStyle(Style.FILL_AND_STROKE);
		path.moveTo(posX-getWidth()/2,posY);
		path.lineTo(posX-getWidth()/3,posY-getHeight()/2);
		path.lineTo(posX+getWidth()/3,posY-getHeight()/2);
		path.lineTo(posX+getWidth()/2,posY);
		path.lineTo(posX+getWidth()/3,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/3,posY+getHeight()/2);
		path.lineTo(posX-getWidth()/2,posY);
		c.drawPath(path,paint);
		
	}
	
	protected  void drawBorders(Canvas c)
	{
		paint.setShader(null);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4.0f);
		paint.setStyle(Style.STROKE);
		
		if(complementable)
		{
			paint.setStrokeWidth(8.0f);
			paint.setColor(Color.LTGRAY);
			
		}
		else if(modificate)
			paint.setStrokeWidth(16.0f);

	
		c.drawPath(path, paint);
	}

	protected  void drawText(Canvas c)
	{
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2.5f);
		paint.setTypeface(Typeface.create("Helvetica",Typeface.BOLD_ITALIC));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(getHeight()/2);
		c.drawText(getName(),posX,posY-(paint.descent()+paint.ascent())/2, paint);
		if(type==ALWAYS)return;
		c.drawText(vA,posX-width/4,posY-(paint.descent()+paint.ascent())/2, paint);
		c.drawText(vB,posX+width/4,posY-(paint.descent()+paint.ascent())/2, paint);
		
	}
	
	public boolean isComplementable() {
		return complementable;
	}

	
	public void setComplementable(boolean complementable) {
		this.complementable = complementable;
	}


	public String getvA() {
		return vA;
	}

	public void setvA(String vA) {
		this.vA = vA;
	}

	public String getvB() {
		return vB;
	}

	public void setvB(String vB) {
		this.vB = vB;
	}
	
	

	@Override
	public boolean execute(Client client) {
		
		variables=((ActivityScratching)getActivity()).getVariables().getVarGrid();
		if(type!=ALWAYS)
			if(variables.searchVariable(vA)==null || variables.searchVariable(vB)==null)
					return false;
		switch (type)
		{
			case ObjectExecutable.GREATER_THAN:
				if(variables.searchVariable(vA)>variables.searchVariable(vB))	return true;
				break;		
				
			case ObjectExecutable.LESS_THAN:
				if(variables.searchVariable(vA)<variables.searchVariable(vB))	return true;
				break;
			
			case ObjectExecutable.EQUAL_THAN:
				if(variables.searchVariable(vA)==variables.searchVariable(vB))	return true;
				break;
		
			case ObjectExecutable.GREATER_EQUAL_THAN:
				if(variables.searchVariable(vA)>=variables.searchVariable(vB))	return true;
				break;		
				
			case ObjectExecutable.LESS_EQUAL_THAN:
				if(variables.searchVariable(vA)<=variables.searchVariable(vB))	return true;
				break;
			case ObjectExecutable.ALWAYS:
				return true;
				
				
			default:
				break;
			
		}
		
		
		return false;
		
	
	}

	@Override
	public void Align() {
		if(owner!=null)
		{
			posX=owner.getConditionPosX();
			posY=owner.getPosY();
		}
		
	}

	@Override
	public boolean ShowPreliminarLink(PositionableObject object) {
		
		complementable=false;
		if(auxOwner!=null) auxOwner.setComplementable(false);
		auxOwner=null;  
		
		if(object instanceof ButtonControl)
		{
		
			if(this.getPosX()>((ButtonControl)object).getConditionPosX()- ((ButtonControl)object).getWidth()/2 && this.getPosX()< ((ButtonControl)object).getConditionPosX()+((ButtonControl)object).getWidth()/2
			&& this.getPosY()>object.getPosY()-((ButtonControl)object).getConditionHeight()/2 && this.getPosY()< object.getPosY()+((ButtonControl)object).getConditionHeight()/2)
				
			{
					complementable=true;
					auxOwner=(ButtonControl)object;
					auxOwner.setComplementable(true);
					return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean SetLink() {
		
		if(complementable)
		{
			owner=auxOwner;
			owner.setCondition(this);
			complementable=false;
			return true;
		}
		else return false;
		
	}

	@Override
	public boolean FreeLink() {
		if(owner!=null)
		{
			owner.setCondition(null);
			owner=null;
			return true;
		}
		else return false;
	}
	
	@Override
	public void showParametersLayout() {
	/*	
		if(type==ALWAYS)return;
		MyTouchableLayout comParam; 
		   
		if(type>=CONSTANT)
			comParam=(MyTouchableLayout)activity.findViewById(R.id.compLayConst);

		else
			comParam=(MyTouchableLayout)activity.findViewById(R.id.compLay);
		 
*/
		
		if(type==ALWAYS)return;
		
		if(type>=CONSTANT)
        ((ActivityScratching) activity).showButtonLayout(new FragmentComparerConst(((ActivityScratching)activity).getVariables().getVarGrid(),this),this);
		else
		((ActivityScratching) activity).showButtonLayout(new FragmentComparer(((ActivityScratching)activity).getVariables().getVarGrid(),this),this);
		
		
		
	
		
		modificate=true;
       
	}
	
	public void setVariablesGrid(MyVariableGrid grid)
	{
		variables=grid;
	}

	public ButtonComparer(PositionableObject obj) {
		super(obj);
		ButtonComparer comparer=(ButtonComparer)obj;
		vA=comparer.vA;
		vB=comparer.vB;
		variables=comparer.variables;
		
	}

	@Override
	public PositionableObject copyFields(PositionableObject obj) {
		
		return new ButtonComparer(obj);
		
	}

	
}
