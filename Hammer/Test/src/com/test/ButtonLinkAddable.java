package com.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;

public class ButtonLinkAddable extends PositionableObject {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ButtonLinkAddable() {
		super();// TODO Auto-generated constructor stub
	}
	
	public ButtonLinkAddable(int width, int height) {
		this.width=width;
		this.height=height;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Align() {
		// TODO Auto-generated method stub
		if(owner!=null)
		{
			posX=owner.getPosX()+owner.getWidthMed();
			posY=owner.getPosY()+owner.getHeightSup();
		}
		else if(parent!=null)
		{	
			posX=parent.getPosX();
			posY=parent.getPosY()+parent.getHeight();
		}

	}

	@Override
	public boolean SetLink() {
		
		boolean success=false;
		if(linkable)
		{
			setParent(auxParent);
			parent.setLinkable(false);
			if(parent.SearchFirstParent().getOwner()!=null)
				parent.SearchFirstParent().getOwner().resize();
			
			success=true;
		}
		else if(addable)
		{	
			auxOwner.addOrder(this);
			auxOwner.setAddable(false);
			success=true;
		}
		
		linkable=false;
		addable=false;
		return success;

	}

	@Override
	public boolean FreeLink() {
		
		boolean success=false;
		if(parent!=null)
		{
			
			parent.setChild(null);
			if(parent.SearchFirstParent().getOwner()!=null)
				parent.SearchFirstParent().getOwner().resize();
			parent=null;
			success=true;
		}
		
		if(owner!=null)
		{
			owner.setSlave(null);
			owner.resize();
			owner=null;
			success=true;
		}
		
		return success;
	}

	@Override
	public boolean execute(Client client) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ShowPreliminarLink(PositionableObject object) {
		// TODO Auto-generated method stub
		if(auxParent!=null)
			auxParent.setLinkable(false);
		
		if(auxOwner!=null)
			auxOwner.setAddable(false);
		
		auxParent=null;
		auxOwner=null;
		
		
		
		if(object instanceof ButtonControl)
		{
		
			if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
			&& this.getPosY()>object.getPosY()-((ButtonControl)object).getHeightSup()/2 && this.getPosY()< object.getPosY()+((ButtonControl)object).getHeightSup()/2)
				{
					addable=true;
					linkable=false;
					auxOwner=(ButtonControl)object;
					auxOwner.setAddable(true);
					return true;
				}
			
			else if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
					&& this.getPosY()>object.getPosY()+((ButtonControl)object).getHeightSup()/2+((ButtonControl)object).getHeightMed() && this.getPosY()< object.getPosY()+((ButtonControl)object).getHeightSup()/2+((ButtonControl)object).getHeightMed()+((ButtonControl)object).getHeightLow())
			{
				addable=false;
				linkable=true;
				auxParent=object;
				auxParent.setLinkable(true);
				return true;
			}
		}
		
		else if(object instanceof ButtonIfElse)
		{
		
			if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
			&& this.getPosY()>object.getPosY()-((ButtonIfElse)object).getHeightSup()/2 && this.getPosY()< object.getPosY()+((ButtonIfElse)object).getHeightSup()/2)
			{
				if(!(object instanceof ButtonComparer) && object.getType()!=ELSE_IF && object.getType()!=ELSE_CLAUSE)
				{
					addable=false;
					linkable=true;
					auxParent=object;
					auxParent.setLinkable(true);
					return true;
					
				}
				
				
			}
		}
		else if((object instanceof ButtonComparer) || object.getType()==ELSE_IF || object.getType()==ELSE_CLAUSE || (object instanceof ButtonVariable))
		{
			return false;
		}
		
		
		else if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
					&& this.getPosY()>object.getPosY()-object.getHeight()/2 && this.getPosY()< object.getPosY()+object.getHeight()/2)
					{
						addable=false;
						linkable=true;
						auxParent=object;
						auxParent.setLinkable(true);
						return true;
							
						
					}
		
		
		return false;
		
	}
/*
	@Override
	public boolean isAboveObject(PositionableObject p) {
		
		if(p instanceof ButtonControl)
		{
			if(this.getPosX()>p.getPosX()-p.getWidth()/2 && this.getPosX()< p.getPosX()+p.getWidth()/2
				&& this.getPosY()>p.getPosY()+((ButtonControl)p).getHeightSup()/2 && this.getPosY()< p.getPosY()+p.getHeight()-((ButtonControl)p).getHeightSup()/2)
					return true;
		}
		else if(p instanceof ButtonIfElse)
			if(this.getPosX()>p.getPosX()-p.getWidth()/2 && this.getPosX()< p.getPosX()+p.getWidth()/2
					&& this.getPosY()>p.getPosY()-p.getHeight()/2 && this.getPosY()<p.getPosY()+p.getHeight()/2)
				// TODO Auto-generated method stub
		return false;
	}
	*/

	@Override
	protected  void onDraw(Canvas c) {
		if(child!=null)child.onDraw(c);// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawBackground(Canvas c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawBorders(Canvas c) {
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4.0f);
		paint.setShader(null);
		paint.setStyle(Style.STROKE);
		
		if(linkable)
		{
			paint.setStrokeWidth(8.0f);
			paint.setColor(Color.GREEN);
			
		}
		else if(addable)
		{
			paint.setStrokeWidth(8.0f);
			paint.setColor(Color.YELLOW);
		
		}
		
		else if(modificate)
		{
			paint.setStrokeWidth(8.0f);
			paint.setColor(Color.parseColor("#DF3A01"));
		}
		
		c.drawPath(path, paint);
		
	}

	@Override
	protected void drawText(Canvas c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showParametersLayout() {
		// TODO Auto-generated method stub
		
	}


}
