package com.test;




import java.io.Serializable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.test.AuxBasicVariables.DoubleValue;


public abstract class PositionableObject implements LinkableSchemaObject, ObjectExecutable,Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static ByteBuffer dst = null;
	//private static byte[] bytesar = null;
	protected float posX;
	protected float posY;
	protected int width=180;
	protected int height=60;
	protected int type;
	protected String name;
	protected Activity activity;
	private Bitmap modBitmap;
	protected Paint paint=new Paint();
	protected RectF oval = new RectF();
	protected Path path=new Path();
	
	protected boolean erasable=false;
	protected AuxBasicVariables basic;
	protected PositionableObject parent;
	protected PositionableObject child;
	protected PositionableObject auxParent;
	protected PositionableObject auxChild;
	protected ButtonControl owner;
	protected ButtonControl auxOwner;
	protected boolean linkable=false;
	protected boolean addable=false;
	protected DoubleValue parameter=AuxBasicVariables.createDoubleValue(1);
	
	protected abstract void onDraw(Canvas c);
	protected abstract void drawBackground(Canvas c);
	protected abstract void drawBorders(Canvas c);
	protected abstract void drawText(Canvas c);
	public abstract void showParametersLayout();



	public PositionableObject() {
		
	}

	public PositionableObject(int width, int height) {
		this.width=width;
		this.height=height;
		
		
	}
	
	public PositionableObject SearchFirstParent()
	{
		if(parent!=null)
			return parent.SearchFirstParent();
		else
			return this;
	}

	
	public PositionableObject SearchLastChild()
	{
		if(child!=null)
			return child.SearchLastChild();
		else
			return this;
	}
	
	public int getTotalSchemaHeight()
	{
		if(child!=null)
			return height+child.getTotalSchemaHeight();
		else
			return height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bitmap getModBitmap() {
		return modBitmap;
	}

	public void setModBitmap(Bitmap modBitmap) {
		this.modBitmap = modBitmap;
	}

	
	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public void setLinkable(boolean lnk) {
		linkable=lnk;// TODO Auto-generated method stub
		
	}


	public void setAddable(boolean add) {
		addable=add;// TODO Auto-generated method stub
		
	}






	public int getType() {
		return type;
	}






	public void setType(int type) {
		this.type = type;
	}




	public boolean isErasable() {
		return erasable;
	}

	public void setErasable(boolean erasable) {
		this.erasable = erasable;
		if(child!=null)
			child.setErasable(true);
		
	}






	public PositionableObject getParent() {
		return parent;
	}


	public void setParent(PositionableObject parent) {
	
		if(parent.getChild()!=null) 
			parent.getChild().setParent(SearchLastChild());
		this.parent = parent;
		parent.setChild(this);
		
	}


	public PositionableObject getChild() {
		return child;
	}






	public void setChild(PositionableObject child) {
		this.child = child;
		if(child!=null)this.child.parent=this;
	}






	public PositionableObject getAuxParent() {
		return auxParent;
	}






	public void setAuxParent(PositionableObject auxParent) {
		this.auxParent = auxParent;
	}






	public PositionableObject getAuxChild() {
		return auxChild;
	}






	public void setAuxChild(PositionableObject auxChild) {
		this.auxChild = auxChild;
	}






	public ButtonControl getOwner() {
		return owner;
	}






	public void setOwner(ButtonControl owner) {
		this.owner = owner;
		owner.setSlave(this);
	}






	public ButtonControl getAuxOwner() {
		return auxOwner;
	}






	public void setAuxOwner(ButtonControl auxOwner) {
		this.auxOwner = auxOwner;
	}



	public DoubleValue getParameter() {
		return parameter;
	}



	public void setParameter(DoubleValue parameter) {
		this.parameter = parameter;
	}
	
	protected static float getSemicircle(float xStart, float yStart, float xEnd,
            float yEnd, RectF ovalRectOUT, int direction) {
 
        float centerX = xStart + ((xEnd - xStart) / 2);
        float centerY = yStart + ((yEnd - yStart) / 2);
 
        double xLen = (xEnd - xStart);
        double yLen = (yEnd - yStart);
        float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);
 
        RectF oval = new RectF((float) (centerX - radius),
                (float) (centerY - radius), (float) (centerX + radius),
                (float) (centerY + radius));
 
        ovalRectOUT.set(oval);
 
        double radStartAngle = 0;
        if (direction == 0) {
            radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
        } else {
            radStartAngle = Math.atan2(yEnd - centerY, xEnd - centerX);
        }
        float startAngle = (float) Math.toDegrees(radStartAngle);
 
        return startAngle;
 
    }






	public Activity getActivity() {
		return activity;
	}






	public void setActivity(Activity activity) {
		this.activity = activity;
	}
 
	
}