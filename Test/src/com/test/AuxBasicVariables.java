package com.test;

import java.io.Serializable;
import java.util.ArrayList;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;





public class AuxBasicVariables implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	
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
