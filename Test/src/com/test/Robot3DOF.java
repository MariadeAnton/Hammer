package com.test;

import java.text.DecimalFormat;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Robot3DOF extends Object3D {
	
	static final int INITPOSX=0;
	static final int INITPOSY=3;
	static final int INITPOSZ=4;
	
	
	private static final long serialVersionUID = 1L;
	private Object3D base;
	private Object3D jointL;
	private Object3D barL;
	private Object3D jointM;
	private Object3D barM;
	private Object3D barH;
	private Object3D end;
	
	private float wBase=20;
	private float hBase=wBase/4f;
	
	private float wBarL=5;
	private float hBarL=wBarL*5;
	
	private float hJointL=7;

	private float wBarM=5;
	private float hBarM=wBarM*3;
	
	private float hJointM=7;
	
	private float wBarH=5;
	private float hBarH=wBarH*2;
	
	private float hEnd=7;

	private float l1=hBase*2+hBarL*2;
	private float l2=hBarM*2;
	private float l3=hBarH*2;
	private double preQ1=Math.PI/2,preQ2=0,preQ3=-Math.PI/2;
	double Q1,Q2,Q3;
	
	public Robot3DOF(World world)
	{
		
		super(1000);
		robotCreation();
		world.addObjects(base,barL,jointL,barM,jointM,barH,end);
		world.addObject(this);
		setName("ROBOT");
		
	}
	
	public Robot3DOF(int maxTriangles)
	{
		super(maxTriangles);
		setName("ROBOT");
		robotCreation();
		// TODO Auto-generated constructor stub
	}

	private void robotCreation()
	{
	//	Texture text=new Texture(32,32,new RGBColor(200,200,200));
	//	TextureManager.getInstance().addTexture("texture", text);
		base=Primitives.getBox(wBase,hBase/wBase);
		base.translate(0,-hBase,0);
	
	//	base.setTexture("texture");
	//	base.setAdditionalColor(0,0,0);
		barL=Primitives.getCylinder(1000, wBarL, hBarL/wBarL);
		barL.translate(0,-(hBarL+hBase*2),0);
	//	barL.setTexture("texture");
	//	barL.setAdditionalColor(new RGBColor(255,0,0));
		jointL=Primitives.getSphere(hJointL);
		jointL.translate(0,-(hBarL),0);
	//	jointL.setTexture("texture");
	//	jointL.setAdditionalColor(new RGBColor(255,255,255));
		barM=Primitives.getCylinder(1000, wBarM, hBarM/wBarM);
		barM.rotateZ( (float) -(Math.PI/2f));
		barM.translate(hBarM,0,0);
	//	barM.setTexture("texture");
	//	barM.setAdditionalColor(new RGBColor(0,255,0));
		
		jointM=Primitives.getSphere(hJointM);
		jointM.translate(0,-hBarM,0);
	//	jointM.setTexture("texture");
	//	jointM.setAdditionalColor(new RGBColor(255,255,255));
		barH=Primitives.getCylinder(1000, wBarH, hBarH/wBarH);
		barH.rotateZ((float) -(Math.PI/2f));
		barH.translate(hBarH,0,0);
//		barH.setTexture("texture");
		barH.clearAdditionalColor();
	//	barH.setAdditionalColor(new RGBColor(0,0,255));
		end=Primitives.getSphere(hEnd);
		end.translate(0,-hBarH,0);
//		end.setTexture("texture");
	//	end.setAdditionalColor(new RGBColor(255,255,0));
		
		addChild(base);
		addChild(barL);
		barL.addChild(jointL);
		jointL.addChild(barM);
		barM.addChild(jointM);
		jointM.addChild(barH);
		barH.addChild(end);
		
		setAnglesDOF();
	}
	
	public void moveJoint(int j,float degrees)
	{
		
		
		switch(j)
		{
			case 0:
				//barL.clearRotation();
				
				barL.rotateY((float) Math.toRadians(degrees));
				break;
			case 1:
				//jointL.clearRotation();
			//	if(degrees>-70)
				jointL.rotateZ((float) Math.toRadians(degrees));
				break;
			case 2:
			//	jointM.clearRotation();
			//	if(degrees>-140)
				jointM.rotateZ((float) Math.toRadians(degrees));
				break;
				
				
		}
		
		setAnglesDOF();
	}
	
	public SimpleVector getToolCoord()
	{
		SimpleVector absCoord=base.getTransformedCenter();
		absCoord.add(end.getTransformedCenter());
		
		DecimalFormat twoDecimal=new DecimalFormat("#.##");
		SimpleVector transfCoord=new SimpleVector(-Float.valueOf(twoDecimal.format(end.getTransformedCenter().z).replace(",",".")),
												   Float.valueOf(twoDecimal.format(end.getTransformedCenter().x).replace(",",".")),
												  -Float.valueOf(twoDecimal.format(end.getTransformedCenter().y).replace(",",".")));
		
		
		return transfCoord;
	}
	
	public SimpleVector getRobotConf()
	{
		
		return new SimpleVector(Math.toDegrees(preQ1),Math.toDegrees(preQ2),Math.toDegrees(preQ3));
	}
	
	public Double getRobotConfAt(int joint)
	{
		switch(joint)
		{
			case 0:
				return Math.toDegrees(preQ1);	
			case 1:
				return Math.toDegrees(preQ2);
			case 2:
				return Math.toDegrees(preQ3);	
		}
		return null;
	}
	
	public void moveToolTo(float px,float py,float pz)
	{
		///////////////////INVERSE KINEMATIC//////////////////////////////
		
		pz=pz-6;
		
	//	setLimits(0,-140,-70);
		
		Q1=Math.atan2(py, px);
		Q1=Q1-preQ1;
		
		
		double cosQ3=(Math.pow(px, 2)+Math.pow(py, 2)+Math.pow(pz, 2)-Math.pow(l2,2)-Math.pow(l3,2))/(2*l2*l3);
		Q3=-Math.atan2(Math.sqrt(1-Math.pow(cosQ3, 2)), cosQ3);
		
		double auxQ3=Q3;
		Q3=Q3-preQ3;
	
		
		
		
		Q2=Math.atan2(pz,Math.sqrt((Math.pow(px, 2)+Math.pow(py, 2))))-Math.atan2(l3*Math.sin(auxQ3), l2+l3*Math.cos(auxQ3));
		Q2=Q2-preQ2;
		if(Q1!=Q1 || Q2!=Q2 || Q3!=Q3)return;
		
		
		
		barL.rotateY((float) -Q1);
		jointL.rotateZ((float) Q2);
		jointM.rotateZ((float) Q3);
		setAnglesDOF();
			
			
		
		
		
	}
	
	public void scaleRobot(float scale)
	{
		l1=l1*scale;
		l2=l2*scale;
		l3=l3*scale;
		hBarL=hBarL*scale;
		hBase=hBase*scale;
		
		scale(scale);
	}
	
	private void setAnglesDOF()
	{
		preQ1=Math.atan2(jointM.getTransformedCenter().x,-jointM.getTransformedCenter().z);
		
		
		double r=Math.sqrt(Math.pow(jointM.getTransformedCenter().x, 2)+Math.pow(jointM.getTransformedCenter().z,2));
		preQ2=Math.atan2((-jointM.getTransformedCenter().y)-(hBarL+hBase)*2,r);
		
		
		SimpleVector vectDirQ3=jointM.getTransformedCenter().calcSub(jointL.getTransformedCenter());
		SimpleVector vectQ3=end.getTransformedCenter().calcSub(jointM.getTransformedCenter());
		double angle=Math.toDegrees(vectDirQ3.calcAngle(vectQ3));
		preQ3=-vectDirQ3.calcAngle(vectQ3);
		
		
		if((-end.getTransformedCenter().y)>-jointM.getTransformedCenter().y && preQ3>0)preQ3=-preQ3;
		
		
			
		
		
	
	}
	
	public void setLimits(double lQ1,double lQ2,double lQ3)
	{
		if(lQ1 !=0 && preQ1<lQ1)preQ1=Math.toRadians(lQ1);
		if(lQ2 !=0 && preQ2<lQ2)preQ2=Math.toRadians(lQ2);
		if(lQ3 !=0 && preQ3<lQ3)preQ3=Math.toRadians(lQ3);
	}
	


}
