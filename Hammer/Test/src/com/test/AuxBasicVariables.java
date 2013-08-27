package com.test;

import java.io.Serializable;





public class AuxBasicVariables implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	static public TriplePoint createTriplePoint()
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new TriplePoint();
	}
	
	static public TriplePoint createTriplePoint(double x, double y,double z)
	{
		AuxBasicVariables var = new AuxBasicVariables();
		return  var.new TriplePoint(x,y,z);
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
	
	
	class TriplePoint implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		public double x;
		public double y;
		public double z;
		
		public TriplePoint()
		{
			super();
		}
		
		
		public TriplePoint(double x, double y,double z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
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
