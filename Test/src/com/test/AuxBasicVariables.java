package com.test;

import java.io.Serializable;
import java.util.ArrayList;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;





public class AuxBasicVariables implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	static public Point3D createPoint3D()
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new Point3D();
	}
	
	static public Point3D createPoint3D(SimpleVector pos)
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new Point3D(pos);
	}
	
	static public Paths createPaths()
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new Paths();
	}
	
	static public DoubleValue createDoubleValue(double v)
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new DoubleValue(v);
	}
	
	static public StringValue createString(String string)
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new StringValue(string);
	}
	
	
	class Point3D extends Object3D implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		private boolean growing=true;
		private boolean isSelected=false;
		private float radius;
		
		
		public Point3D() {
			super(Primitives.getSphere(0.5f));
			radius=0.5f;
			
		}
		
		
		
		public Point3D(SimpleVector pos) {
			super(Primitives.getSphere(0.5f));
			radius=0.5f;
			this.setOrigin(pos);
			
		}


		public boolean isSelected() {
			return isSelected;
		}


		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}


		public boolean isGrowing() {
			return growing;
		}


		public void setGrow(boolean grow) {
			this.growing = grow;
		}


		public float getRadius() {
			return getScale();
		}


		public void setRadius(float radius) {
			this.radius = radius;
			setScale(radius);
		}
		
	}

	class Paths
	{
		private String name;
		private ArrayList<Point3D> pathPoints=new ArrayList<Point3D>();
		private boolean isSelected=false;
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ArrayList<Point3D> getPathPoints() {
			return pathPoints;
		}
		public void setPathPoints(ArrayList<Point3D> pathPoints) {
			this.pathPoints = pathPoints;
		}
		public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
		
		
			
	}
	
	public class DoubleValue implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		private double value;

		
		public DoubleValue(double value) {
			super();
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
		
	}

	public class StringValue implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		private String string;

		
		public StringValue(String string) {
			super();
			this.string = string;
		}

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}
		
	}
}
