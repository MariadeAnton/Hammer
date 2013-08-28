package com.test;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;





public class MySchemaSurface extends MySurfaceView 
{

	
	 
	
	
	float initpx=0,scaleX=1.0f;
	float initpy=0,scaleY=1.0f;
	private PositionableObject  bitmapSelected=null;
	private ArrayList<PositionableObject > drawList=new ArrayList<PositionableObject >();
	private ArrayList<PositionableObject > objectList=new ArrayList<PositionableObject >();
	ArrayList<PositionableObject > reorderList=new ArrayList<PositionableObject >();
	Bitmap cancel=BitmapFactory.decodeResource(getResources(), R.drawable.del);
	
	public MySchemaSurface(Context context) {	
		super(context);	
		
	}
	
	public MySchemaSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
	}
	


	
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
	
		
		canvas.drawRGB(255, 255, 255);
		canvas.scale(scaleX,scaleY);
		for(int i=0;i<drawList.size();i++)
			{
				if(drawList.get(i).isErasable())
					canvas.drawBitmap(drawList.get(i).getModBitmap(),drawList.get(i).getPosX()-drawList.get(i).getWidth()/2, drawList.get(i).getPosY()-drawList.get(i).getHeight()/2, null);
				else
					drawList.get(i).onDraw(canvas);
			}
			
			
	
	}
			
	
	protected void addDropDraw(DragEvent event)
	{
		PositionableObject  bm=null;
		MyTouchableLayout layout=(MyTouchableLayout)event.getLocalState();
		if(layout==null) return; ////Because some errors happened when conversion failed
		
		if(layout.getInstance()=="ButtonMovement")
			bm=new ButtonMovement(210,75);
		else if(layout.getInstance()=="ButtonControl")
			bm=new ButtonControl(210,85);	
		else if(layout.getInstance()=="ButtonComparer")
			bm=new ButtonComparer(180,50);
		else if(layout.getInstance()=="ButtonOperator")
			bm=new ButtonOperator(210,75);
		else if(layout.getInstance()=="ButtonIfElse")
			bm=new ButtonIfElse(210,75);
		else if(layout.getInstance()=="ButtonRobot")
			bm=new ButtonRobot(210,75);
		else if(layout.getInstance()=="ButtonVariable")
			bm=new ButtonVariable(105,75);
		else if(layout.getInstance()=="ButtonUserCommand")
			bm=new ButtonUserCommand(210,75);
    	
		bm.setType(layout.getType());
       	bm.setName(layout.getName()); 
    	bm.setPosX(event.getX());
    	bm.setPosY(event.getY());	
    	bm.setActivity((Activity)context);
		bm.setModBitmap(cancel);
		objectList.add(bm);
		drawList.add(bm);
		
	}
	
	private void reorderShow(PositionableObject  bitmapSelected)
	{
		
		PositionableObject aux=drawList.get(drawList.size()-1);
		int auxInt=drawList.indexOf(bitmapSelected);
		drawList.set(drawList.size()-1,bitmapSelected);
		drawList.set(auxInt,aux);
		
	}
	
	private void deleteObjects(PositionableObject pos)
	{
		
		objectList.remove(pos);
		if(pos instanceof ButtonControl)
		{
			if(((ButtonControl) pos).getCondition()!=null)
				deleteObjects(((ButtonControl) pos).getCondition());
			
			if(((ButtonControl) pos).getSlave()!=null)
				deleteObjects(((ButtonControl) pos).getSlave());
			
		}
		else if(pos instanceof ButtonRobot)
		{
			if(((ButtonRobot) pos).getComplement()!=null)
				deleteObjects(((ButtonRobot) pos).getComplement());
		}
			
		if(pos.getChild()!=null)
			deleteObjects(pos.getChild());
		
		pos=null;
	}
	
	private void  getDrawableObjects(PositionableObject pos)
	{
		pos.setActivity((Activity)context);
		pos.setModBitmap(cancel);
		objectList.add(pos);
		if(pos instanceof ButtonControl)
		{
			if(((ButtonControl) pos).getCondition()!=null)
				getDrawableObjects(((ButtonControl) pos).getCondition());
			
			if(((ButtonControl) pos).getSlave()!=null)
				getDrawableObjects(((ButtonControl) pos).getSlave());
			
		}
		else if(pos instanceof ButtonRobot)
		{
			if(((ButtonRobot) pos).getComplement()!=null)
				getDrawableObjects(((ButtonRobot) pos).getComplement());
		}
			
		if(pos.getChild()!=null)
			getDrawableObjects(pos.getChild());
	
	}

	private void deleteSchemaObject(PositionableObject bitmapSelected)
	{
		for(int i=drawList.size()-1;i>=0;i--)
		{
			if(drawList.get(i).equals(bitmapSelected))
			{
				deleteObjects(drawList.get(i));
				drawList.remove(drawList.get(i));
				
			}
		}
		
		bitmapSelected=null;
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if(isOnPause())return false;		
		if(!touchEnabled)return false;
		
		
		float px=event.getX();
		float py=event.getY();
		
		
		gestureDetector.onTouchEvent(event);
		
		
		switch(event.getAction())
		{
		
		case MotionEvent.ACTION_DOWN:
		
			initpx=px;
			initpy=py;
			
			for(int i=objectList.size()-1;i>=0 ;i--)			
			{
				
				if(objectList.get(i) instanceof ButtonControl)
				{
					if( px<(objectList.get(i).getWidth()/3 + objectList.get(i).getPosX()) && px>( objectList.get(i).getPosX()-objectList.get(i).getWidth()/2)
					&& py<(((ButtonControl)objectList.get(i)).getHeightSup()/2 + objectList.get(i).getPosY()) && py>( objectList.get(i).getPosY()-((ButtonControl)objectList.get(i)).getHeightSup()/2))
					{
					bitmapSelected=objectList.get(i);
					if(drawList.contains(bitmapSelected))reorderShow(bitmapSelected);
					break;
					}
					
				}
				else if( px<(objectList.get(i).getWidth()/2 + objectList.get(i).getPosX()) && px>( objectList.get(i).getPosX()-objectList.get(i).getWidth()/2)
					&& py<(objectList.get(i).getHeight()/2 + objectList.get(i).getPosY()) && py>( objectList.get(i).getPosY()-objectList.get(i).getHeight()/2))
				{
						
					bitmapSelected=objectList.get(i);
					if(drawList.contains(bitmapSelected))reorderShow(bitmapSelected);
					break;	
						
				}
				else bitmapSelected=null;
			
			}
			
			break;
			
		
		
		case MotionEvent.ACTION_MOVE:
			
		
			if(bitmapSelected!=null &&(Math.abs(initpx-px)>10 || Math.abs(initpy-py)>10))
			{
				
						
						if(bitmapSelected.FreeLink())
						{
							drawList.add(bitmapSelected);
							reorderShow(bitmapSelected);
						}
						

						/*
 * 
						if(bitmapSelected instanceof ButtonControl)
							
							if(((ButtonControl)bitmapSelected).getType()==ObjectExecutable.ELSE_IF 
							|| ((ButtonControl)bitmapSelected).getType()==ObjectExecutable.ELSE_CLAUSE )
								
								((ButtonControl)bitmapSelected).reorderIfClauses();
				
				*/
						for(PositionableObject po:objectList)
						{
							if(po!=bitmapSelected)
							{
								bitmapSelected.setLinkable(false);
								bitmapSelected.setAddable(false);
								if(bitmapSelected.ShowPreliminarLink(po))break;
							}
						}
							
				
						if(px<=20  || px>=getWidth()-20 || py>=getHeight()-20 || py<=20)
							bitmapSelected.setErasable(true);
				
						else
						{
							bitmapSelected.setErasable(false);					
							bitmapSelected.setPosX(px);
							bitmapSelected.setPosY(py);	
					
						}			
			 	}
			
				break;
			 
			 
			case MotionEvent.ACTION_UP:
				
				if(bitmapSelected!=null)
				{
				
					if( bitmapSelected.isErasable())
						deleteSchemaObject(bitmapSelected);
					
					else 
						if(bitmapSelected.SetLink())
							drawList.remove(bitmapSelected);			
				}
				break;	
				
		}
		
		
	
		return true;
	}

	public ArrayList<PositionableObject > getDrawList() {
		return drawList;
	}

	public ArrayList<PositionableObject > getTotalObjectDrawnList() {
		return objectList;
	}
	
	public boolean saveObjectsList(ObjectOutputStream os ) throws IOException 
	
	{
		
		
		os.writeInt(drawList.size());
		for(PositionableObject po:drawList) 	Serializer.writeObject(os,po);		
		
		
		return true;
		
		
		
		
	}

	public boolean loadObjectsList(ObjectInputStream is) {
		
			clearSurface();
			PositionableObject obj=null;
			
			int selectableObjects;
			try {
				selectableObjects = is.readInt();
				for(int i=0;i<selectableObjects;i++)
				{
				obj=Serializer.readObject(is);
				obj.setActivity((Activity)context);
				obj.setModBitmap(cancel);
				drawList.add(obj);
				getDrawableObjects(obj);
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return true;
			
	}
	
	public void clearSurface()
	{
		drawList.removeAll(drawList);
		objectList.removeAll(objectList);
	}
	
	public void setScale(float x,float y)
	{	
		scaleX=x;
		scaleY=y;
	}
	
	public void restoreScale()
	{
		scaleX=1.0f;
		scaleY=1.0f;
	}
	
	
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {		
		for(int i=objectList.size()-1;i>=0 ;i--)
		{
			float px=e.getX();
			float py=e.getY();
			if( px<(objectList.get(i).getWidth()/2 + objectList.get(i).getPosX()) && px>( objectList.get(i).getPosX()-objectList.get(i).getWidth()/2)
				&& py<(objectList.get(i).getHeight()/2 + objectList.get(i).getPosY()) && py>( objectList.get(i).getPosY()-objectList.get(i).getHeight()/2))
			{
				bitmapSelected=objectList.get(i);
				bitmapSelected.showParametersLayout();	
				break;	
			}
			else bitmapSelected=null;
		
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
