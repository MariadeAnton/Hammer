package com.test;



import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.test.AuxBasicVariables.DoubleValue;
import com.test.AuxBasicVariables.Point3D;
import com.test.AuxBasicVariables.StringValue;
import com.threed.jpct.SimpleVector;

public class MyTouchableLayout extends FrameLayout implements Cloneable {

	private DoubleValue parameter=null;
	private StringValue string,string2;
	private SimpleVector point;
	private String instance="";
	private String name="";
	private int type=-1;
	private boolean intercept=true;
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	
	
	public void defineValues(String name,int type, double parameter,String instance)
	{
		this.name=name;
		this.type=type;
		this.parameter=AuxBasicVariables.createDoubleValue(parameter);
		this.instance=instance;
		
	}
	@Override
	protected Object clone() {
		Object object=null;
		try{
		// TODO Auto-generated method stub
		return super.clone();
		}
	catch(CloneNotSupportedException ex){
       
    }
		return object;
	}


	public int getType() {
		return type;
	}

	
	public void setType(int type) {
		this.type = type;
	}

	
	public DoubleValue getParameter() {
		return parameter;
	}

	
	public void setParameter(DoubleValue value) {
		this.parameter = value;
	}

	public MyTouchableLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTouchableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTouchableLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return intercept;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public void setIntercept(boolean intercept) {
		this.intercept = intercept;
	}

	public StringValue getStringValue() {
		return string;
	}

	public void setStringValue(StringValue string) {
		this.string = string;
	}

	public StringValue getStringValue2() {
		return string2;
	}

	public void setStringValue2(StringValue string2) {
		this.string2 = string2;
	}

	public SimpleVector getPoint() {
		return point;
	}

	public void setPoint(SimpleVector point) {
		this.point = point;
	}

	
}
