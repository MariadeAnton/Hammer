
package com.test;

import android.graphics.Canvas;
import android.graphics.Color;



public class ButtonIfElse extends ButtonControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ButtonIfElse auxIfClause=null;
	private ButtonIfElse ifClause=null;
	private ButtonIfElse elseClause=null;
	private boolean alternable=false;
	private int defHeight=heightMed+heightLow+heightSup;
	
	
	
public ButtonIfElse(int width, int height) {
		super(width,height);
		
		
	}

public boolean execute(Client client) {
		
		switch(type)
		{
		
	
			
		case ObjectExecutable.IF_CLAUSE:
			
			if(condition!=null)
			{
				if(condition.execute(client) && slave!=null)
						slave.execute(client);
				else if(elseClause!=null)
					elseClause.execute(client);
			}
					
					
			break;
			
		case ObjectExecutable.ELSE_IF:
			
			if(condition!=null)
			{
				if(condition.execute(client) && slave!=null)
						slave.execute(client);
				else if(elseClause!=null)
					elseClause.execute(client);
			}
					
					
			break;
			
			
		case ObjectExecutable.ELSE_CLAUSE:
			
			if(slave!=null)
				slave.execute(client);
			break;
					
		}

		if(child!=null)
				child.execute(client);		
		else return false;
	
		return false;
		
	
		
	}

public ButtonIfElse getIfClause() {
	return ifClause;
}

public void setIfClause(ButtonIfElse ifClause) {
	
	if(ifClause.getElseClause()!=null) 
		ifClause.getElseClause().setIfClause(SearchLastElseClause());
	
	this.ifClause = ifClause;
	ifClause.setElseClause(this);
	
	
}

public ButtonIfElse getAuxIfClause() {
	return auxIfClause;
}

public void setAuxIfClause(ButtonIfElse auxIfClause) {
	this.auxIfClause = auxIfClause;
}


/*
public void reorderIfClauses()
{
	
	if(elseClause!=null && ifClause!=null)
		ifClause.setElseClause(elseClause);
	else if(ifClause!=null)
		ifClause.setNoAlternatives();
	
	setNoIfOwner();
	setNoAlternatives();
		
	
}
*/
public ButtonIfElse getElseClause() {
	return elseClause;
}

public void setElseClause(ButtonIfElse elseClause) {
	
		this.elseClause = elseClause;

}

public ButtonIfElse searchLastElseClause()
{
	if(elseClause!=null)
		return elseClause.searchLastElseClause();
	else
		return this;
}

public ButtonIfElse searchMainIf()
{
	if(ifClause!=null)
		return ifClause.searchMainIf();
	else
		return this;
}


public void setAlternable(boolean alt){
	alternable=alt;
}

public void setAsLastElseClause(ButtonIfElse elseClause) {
	
	if(elseClause!=null)
	{
		if(elseClause.type==ELSE_CLAUSE)
		{
			ButtonIfElse elsec=elseClause;
			setElseClause(elseClause);
			elseClause.setElseClause(elsec);
		}
		else
			elseClause.setAsLastElseClause(elseClause);
	}
	else
		setElseClause(elseClause);
		
	
}

public ButtonIfElse SearchLastElseClause()
{
	if(elseClause!=null)
		return elseClause.SearchLastElseClause();
	else
		return this;
}

public int getAlternativesHeight()
{
	if(elseClause!=null)
		return elseClause.getHeight()+elseClause.getAlternativesHeight();
	
	return 0;
}

private void upDateDefHeight()
{
	if(slave!=null)
		defHeight=heightSup+heightLow+slave.getTotalSchemaHeight();
	else
		defHeight=heightSup+heightLow+heightMed;
}
@Override
public void onDraw(Canvas c) {
 
 upDateDefHeight();
 searchMainIf().setHeight(searchMainIf().defHeight+searchMainIf().getAlternativesHeight());
 
 super.onDraw(c);
 if(alternable)
 {
	paint.setStrokeWidth(7);
	paint.setColor(Color.BLUE);
	//c.drawLines(drawBorders(paint.getStrokeWidth()), paint);
	
 }
 
}

@Override
public boolean ShowPreliminarLink (PositionableObject object) {

	alternable=false;
	if(auxIfClause!=null)
		auxIfClause.setAlternable(false);
	auxIfClause=null;
	
	if(type==IF_CLAUSE )
		return super.ShowPreliminarLink(object);
	
	else if(type==ELSE_IF && object instanceof ButtonIfElse)
		{
		
			if(this.getPosX()>object.getPosX()-object.getWidth()/2 && this.getPosX()< object.getPosX()+object.getWidth()/2
			&& this.getPosY()>object.getPosY()-getHeightSup()/2 && this.getPosY()< object.getPosY()+object.getHeight()-getHeightSup()/2)
				{
					alternable=true;
					auxIfClause=(ButtonIfElse) object;
					auxIfClause.setAlternable(true);
					return true;
				}
		}
	
	
	return false;
	
	
	}
		
@Override
public boolean SetLink() {
	
	boolean success=false;
	if(alternable)
	{
		auxIfClause.setAlternable(false);
		
		if(type==ELSE_CLAUSE)
			setIfClause(auxIfClause.SearchLastElseClause());
		else if(type==ELSE_IF)		
			setIfClause(auxIfClause);
		
		success=true;
	}
	else 
		success=super.SetLink();
	
	alternable=false;
	return success;
	
	
	
	
	
}
		


@Override
public void Align()
{
	if(ifClause!=null)
	{
			posX=ifClause.getPosX();
			posY=ifClause.getPosY()+ifClause.defHeight;
	}
	else
		super.Align();
		
}
@Override
public boolean FreeLink()
{
	if(ifClause!=null)
	{
		searchMainIf().setHeight(searchMainIf().getHeight()-height);
		ifClause.setElseClause(null);
		ifClause=null;	
		return true;
	}
	
	else
		return super.FreeLink();
	
	
}


}