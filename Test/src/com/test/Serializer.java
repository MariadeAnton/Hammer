package com.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;

import com.threed.jpct.SimpleVector;


public class Serializer {
	
	private static ByteBuffer dst = null;
	 private static byte[] bytesar = null;

	 
	public static void writeVariable(ObjectOutputStream out,Variable var) throws IOException
	{
		
		out.writeObject(var.getName());
		out.writeDouble(var.getValue());
	}
	
	public static Variable readVariable(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		Variable var=new Variable();
		var.setName((String)in.readObject());
		var.setValue(in.readDouble());
		return var;
	}
	
	public static void writeObject(ObjectOutputStream out,PositionableObject obj) throws IOException
	{
		
		out.writeObject(obj.getClass().getName());
		if(obj instanceof ButtonMovement)
		{
			writePositionableFields(out,obj);
			writePositionableContainers(out,obj);
		}else if(obj instanceof ButtonControl)
		{	
			writeControlFields(out,(ButtonControl)obj);
			writeControlContainers(out,(ButtonControl)obj);
			
		}
		else if(obj instanceof ButtonComparer)
		{
			writeComparerFields(out,(ButtonComparer)obj);
			
		}
		else if(obj instanceof ButtonOperator)
		{
			writeOperatorFields(out,(ButtonOperator)obj);
			writePositionableContainers(out,obj);
		}
		else if(obj instanceof ButtonVariable)
		{
			writeVariableFields(out,(ButtonVariable)obj);
			
		}
		else if(obj instanceof ButtonUserCommand)
		{
			writeUserCommandFields(out,(ButtonUserCommand)obj);
			writePositionableContainers(out,obj);
		}
		
		else if(obj instanceof ButtonRobot)
		{	
			writeRobotFields(out,(ButtonRobot)obj);
			writeRobotContainers(out,(ButtonRobot)obj);
			
		}
	}
	public static PositionableObject readObject(ObjectInputStream in)
	       throws IOException, ClassNotFoundException {
		  	
	 		PositionableObject obj=null;
	 		String className=(String)in.readObject();
	 		if(className.compareTo("com.test.ButtonMovement")==0)
	 		{
				obj=new ButtonMovement();
	 			Serializer.readPositionableFields(in,obj);
	 			Serializer.readPositionableContainers(in,obj);
	 		}
	 		else if(className.compareTo("com.test.ButtonControl")==0)
	 		{
				obj=new ButtonControl();
				Serializer.readControlFields(in,(ButtonControl)obj);
				Serializer.readControlContainers(in,(ButtonControl)obj);
				((ButtonControl)obj).resize();
	 		}
	 	
	 		else if(className.compareTo("com.test.ButtonComparer")==0)
	 		{
				obj=new ButtonComparer();
				Serializer.readComparerFields(in,(ButtonComparer)obj);
			
	 		}
	 		else if(className.compareTo("com.test.ButtonOperator")==0)
	 		{
				obj=new ButtonOperator();
				readOperatorFields(in,(ButtonOperator)obj);
				readPositionableContainers(in,(ButtonOperator)obj);
			
	 		}
	 		else if(className.compareTo("com.test.ButtonUserCommand")==0)
	 		{
				obj=new ButtonUserCommand();
				readUserCommandFields(in,(ButtonUserCommand)obj);
				readPositionableContainers(in,(ButtonUserCommand)obj);
			
	 		}
	 		else if(className.compareTo("com.test.ButtonVariable")==0)
	 		{
				obj=new ButtonVariable();
				readVariableFields(in,(ButtonVariable)obj);
	 		}
	 		else if(className.compareTo("com.test.ButtonRobot")==0)
	 		{
				obj=new ButtonRobot();
				readRobotFields(in,(ButtonRobot)obj);
				readRobotContainers(in,(ButtonRobot)obj);
	 		}
		    
		    return obj;
		 
		   
	   }
	
	public static void writeOperatorFields(ObjectOutputStream out,ButtonOperator obj) throws IOException {
		
		 writePositionableFields(out,obj);
		 out.writeObject(obj.getValueX());
	     out.writeObject(obj.getValueY());

		
		
	}
	
	public static void readOperatorFields(ObjectInputStream in,ButtonOperator obj) throws IOException, ClassNotFoundException {
		
		 readPositionableFields(in,obj);
		 obj.setValueX((String)in.readObject());
		 obj.setValueY((String)in.readObject());
	  
		
		
	}
	
	public static void writeVariableFields(ObjectOutputStream out,ButtonVariable obj) throws IOException {
		
		 writePositionableFields(out,obj);
		 out.writeObject(obj.getNameVar());
	     

		
		
	}
	
	public static void readVariableFields(ObjectInputStream in,ButtonVariable obj) throws IOException, ClassNotFoundException {
		
		 readPositionableFields(in,obj);
		 obj.setNameVar((String)in.readObject());
		
	  
		
		
	}
	
	public static void writeUserCommandFields(ObjectOutputStream out,ButtonUserCommand obj) throws IOException {
		
		 writePositionableFields(out,obj);
		
	}
	
	public static void readUserCommandFields(ObjectInputStream in,ButtonUserCommand obj) throws IOException, ClassNotFoundException {
		
		 readPositionableFields(in,obj);
	
	}
	
	public static void writeRobotFields(ObjectOutputStream out,ButtonRobot obj) throws IOException {
		
		 writePositionableFields(out,obj);
		 out.writeObject(obj.getPoint());

		
	}
	
	public static void readRobotFields(ObjectInputStream in,ButtonRobot obj) throws IOException, ClassNotFoundException {
		
		 readPositionableFields(in,obj);
		 obj.setPoint((SimpleVector)in.readObject());
	
	}
	
	public static void writeRobotContainers(ObjectOutputStream out,ButtonRobot obj) throws IOException
	{
		writePositionableContainers(out,obj);
		
		if(obj.getComplement()!=null)
		{
			out.writeBoolean(true);		
			Serializer.writeVariableFields(out,obj.getComplement());
		}
		else out.writeBoolean(false);
		
	}
	
	public static void readRobotContainers(ObjectInputStream in,ButtonRobot obj) throws OptionalDataException, IOException, ClassNotFoundException
	{
		
		ButtonVariable variable=new ButtonVariable();
		readPositionableContainers(in,obj);
	
		if(in.readBoolean())
		{
			
				Serializer.readVariableFields(in,variable);
				obj.setComplement(variable);
				
		}
	}
	
	
	public static void writeComparerFields(ObjectOutputStream out,ButtonComparer obj) throws IOException {
		
		 writePositionableFields(out,obj);
		 out.writeObject(obj.getvA());
	     out.writeObject(obj.getvB());
		
		
	}
	public static void readComparerFields(ObjectInputStream in,ButtonComparer obj) throws OptionalDataException, ClassNotFoundException, IOException {
		
		readPositionableFields(in,obj);
		obj.setvA((String)in.readObject());
	    obj.setvB((String)in.readObject());
		
	}
	public static void writeBitmap(Bitmap bitmap, ObjectOutputStream out) throws IOException
	{
		
		 out.writeInt(bitmap.getRowBytes());
		 out.writeInt(bitmap.getHeight());
	     out.writeInt(bitmap.getWidth());
		 int bmSize = bitmap.getRowBytes() * bitmap.getHeight();
		    if(dst==null || bmSize > dst.capacity())
		        dst= ByteBuffer.allocate(bmSize);

		    out.writeInt(dst.capacity());

		    dst.position(0);

		    bitmap.copyPixelsToBuffer(dst);
		    if(bytesar==null || bmSize > bytesar.length)
		        bytesar=new byte[bmSize];

		    dst.position(0);
		    dst.get(bytesar);


		    out.write(bytesar, 0, bytesar.length);
	}

	public static Bitmap readBitmap(ObjectInputStream in) throws IOException
	{
		 @SuppressWarnings("unused")
		int nbRowBytes=in.readInt();
		 int height=in.readInt();
		 int width=in.readInt();

		 int bmSize=in.readInt();



		  if(bytesar==null || bmSize > bytesar.length)
		        bytesar= new byte[bmSize];


		   int offset=0;

		    while(in.available()>0){
		        offset=offset + in.read(bytesar, offset, in.available());
		    }


		    if(dst==null || bmSize > dst.capacity())
		        dst= ByteBuffer.allocate(bmSize);
		    dst.position(0);
		    dst.put(bytesar);
		    dst.position(0);
		    Bitmap bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		    bitmap.copyPixelsFromBuffer(dst);
		return bitmap;
		
	}
	
	public static void writePositionableFields(ObjectOutputStream out,PositionableObject obj) throws IOException
	{
		
		 out.writeFloat(obj.getPosX());
	     out.writeFloat(obj.getPosY());
	     out.writeInt(obj.getWidth());
	     out.writeInt(obj.getHeight());
	     out.writeInt(obj.getType());     
	     out.writeObject(obj.getName()); 
	     out.writeDouble(obj.getParameter());
	     
	    
	}

	public static void readPositionableFields(ObjectInputStream in, PositionableObject obj) throws OptionalDataException, ClassNotFoundException, IOException
	 {
	
		obj.posX=in.readFloat();
	    obj.posY =in.readFloat();
	    obj.width=in.readInt();
	    obj.height = in.readInt();
	    obj.type =in.readInt();	
	    obj.name  =(String)in.readObject();    
	    obj.parameter=in.readDouble();
	   
	    
		 
	 }

	public static void writePositionableContainers(ObjectOutputStream out,PositionableObject obj) throws IOException
	{
		
		/* if(obj.getParent()!=null)
	     {
	    	out.writeBoolean(true);
	    	if(obj.getParent() instanceof ButtonControl)
	    	{
	    		out.writeInt(0);
	    		Serializer.writeControlFields(out,(ButtonControl)obj.getParent());
	    		Serializer.writeControlContainers(out,(ButtonControl)obj.getParent());
	    	
	    	}
	    	else
	    	{
	    		out.writeInt(1);
	    		Serializer.writePositionableFields(out,obj.getParent());
	    		Serializer.writePositionableContainers(out,obj.getParent());
	    	}
	    	}
	     else out.writeBoolean(false);
		 */
		 if(obj.getChild()!=null) 
	     {
	    	 out.writeBoolean(true);
	    	 if(obj.getChild() instanceof ButtonControl)
	    	 {
	    		 out.writeInt(0);
	    		 Serializer.writeControlFields(out,(ButtonControl)obj.getChild());
	    		 Serializer.writeControlContainers(out,(ButtonControl)obj.getChild());
	    	 }
		    		
		    else if(obj.getChild() instanceof ButtonOperator)
		    {
		    	out.writeInt(1);
		    	Serializer.writeOperatorFields(out,(ButtonOperator)obj.getChild());
		    	Serializer.writePositionableContainers(out,obj.getChild());
		    }
		   
		    else if(obj.getChild() instanceof ButtonMovement)
		    {
		    	out.writeInt(2);
		    	Serializer.writePositionableFields(out,obj.getChild());
		    	Serializer.writePositionableContainers(out,obj.getChild());
		    }
	    	else if(obj.getChild() instanceof ButtonRobot)
		    {
		    	out.writeInt(3);
		    	Serializer.writeRobotFields(out,(ButtonRobot)obj.getChild());
		    	Serializer.writeRobotContainers(out,(ButtonRobot)obj.getChild());
		    }  
		    else if(obj.getChild() instanceof ButtonUserCommand)
		    {
		    	out.writeInt(4);
		    	Serializer.writeUserCommandFields(out,(ButtonUserCommand)obj.getChild());
		    	Serializer.writePositionableContainers(out,obj.getChild());
		    } 
	     }
		 else out.writeBoolean(false);
		 
		/*  if(obj.getOwner()!=null)
		     {
		    	 out.writeBoolean(true);
		    	 Serializer.writeControlFields(out, obj.getOwner());
		    	 Serializer.writeControlContainers(out,obj.getOwner());
		     }
		  else  out.writeBoolean(false);
		  */
		  
		  
	}
	
	public static void readPositionableContainers(ObjectInputStream in,PositionableObject obj) throws OptionalDataException, IOException, ClassNotFoundException
	{
		ButtonMovement move=new ButtonMovement();
		ButtonControl control=new ButtonControl();
		ButtonOperator operator=new ButtonOperator();
		ButtonRobot robot=new ButtonRobot();
		ButtonUserCommand user=new ButtonUserCommand();
	/*	if(in.readBoolean())
		{
			switch(in.readInt())
			{
			case 0:
				Serializer.readControlFields(in,control);
				obj.setParent(control);
				readControlContainers(in,control);
				break;
			case 1:
				Serializer.readPostionableFields(in,move);
				obj.setParent(move);
				readPositionableContainers(in,move);
				break;
			}
		
		}
		*/
		if(in.readBoolean())
		{
			switch(in.readInt())
			{
			case 0:
				Serializer.readControlFields(in,control);
				obj.setChild(control);
				readControlContainers(in,control);
				break;
			case 1:
				Serializer.readOperatorFields(in,operator);
				obj.setChild(operator);
				readPositionableContainers(in,operator);
				break;
			case 2:
				Serializer.readPositionableFields(in,move);
				obj.setChild(move);
				readPositionableContainers(in,move);
				break;
			case 3:
				Serializer.readRobotFields(in,robot);
				obj.setChild(robot);
				readRobotContainers(in,robot);
				break;
			case 4:
				Serializer.readUserCommandFields(in,user);
				obj.setChild(user);
				readPositionableContainers(in,user);
				break;
				
			}
		
		}
	/*	if(in.readBoolean())
		{
			Serializer.readControlFields(in, control);
			obj.setOwner(control);
			readControlContainers(in,control);
		}
		*/	
		
		
		
	}
	
	public static void writeControlContainers(ObjectOutputStream out,ButtonControl obj) throws IOException
	{
		writePositionableContainers(out,obj);
		if(obj.getSlave()!=null)
		{
			out.writeBoolean(true);
			
			if(obj.getSlave() instanceof ButtonControl)	
			{
				out.writeInt(0);
				Serializer.writeControlFields(out,(ButtonControl)obj.getSlave());
				writeControlContainers(out,(ButtonControl)obj.getSlave());
				
			}
			else if(obj.getSlave() instanceof ButtonOperator)
			{
			    out.writeInt(1);
			    Serializer.writeOperatorFields(out,(ButtonOperator)obj.getSlave());
			    Serializer.writePositionableContainers(out,obj.getSlave());
			}
			else if(obj.getSlave() instanceof ButtonMovement)
			{
			    out.writeInt(2);
			    Serializer.writePositionableFields(out,obj.getSlave());
			    Serializer.writePositionableContainers(out,obj.getSlave());
			}
			else if(obj.getSlave() instanceof ButtonRobot)
			{
			    out.writeInt(3);
			    Serializer.writeRobotFields(out,(ButtonRobot)obj.getSlave());
			    Serializer.writeRobotContainers(out,(ButtonRobot)obj.getSlave());
			}  
			else if(obj.getSlave() instanceof ButtonUserCommand)
			{
			    out.writeInt(4);
			    Serializer.writeUserCommandFields(out,(ButtonUserCommand)obj.getSlave());
			    Serializer.writePositionableContainers(out,obj.getSlave());
			} 
			
		}
		else out.writeBoolean(false);
		
		if(obj.getCondition()!=null)
		{
			out.writeBoolean(true);		
			Serializer.writeComparerFields(out,obj.getCondition());
		}
		else out.writeBoolean(false);
		
	}
	
	public static void readControlContainers(ObjectInputStream in,ButtonControl obj) throws OptionalDataException, IOException, ClassNotFoundException
	{
		ButtonMovement move=new ButtonMovement();
		ButtonControl control=new ButtonControl();
		ButtonComparer comparer=new ButtonComparer();
		ButtonOperator operator=new ButtonOperator();
		ButtonRobot robot=new ButtonRobot();
		ButtonUserCommand user=new ButtonUserCommand();
		readPositionableContainers(in,obj);
		if(in.readBoolean())
		{
			switch(in.readInt())
			{
			case 0:
				Serializer.readControlFields(in,control);
				obj.setSlave(control);
				readControlContainers(in,control);
				break;
			case 1:
				Serializer.readOperatorFields(in,operator);
				obj.setSlave(operator);
				readPositionableContainers(in,operator);
				break;
				
			case 2:
				Serializer.readPositionableFields(in,move);
				obj.setSlave(move);
				readPositionableContainers(in,move);
				break;
			case 3:
				Serializer.readRobotFields(in,robot);
				obj.setSlave(robot);
				readRobotContainers(in,robot);
				break;
			case 4:
				Serializer.readUserCommandFields(in,user);
				obj.setSlave(user);
				readPositionableContainers(in,user);
				break;
						
				}
				
			}
		
		
		
		if(in.readBoolean())
		{
			
				Serializer.readComparerFields(in,comparer);
				obj.setCondition(comparer);
				
		}
		
		obj.resize();
	
	}
	
	 public static void readControlFields(ObjectInputStream in,ButtonControl obj) throws OptionalDataException, ClassNotFoundException, IOException
	 {
		 readPositionableFields(in,obj);
		
		 obj.setConditionPosX(in.readFloat());
		 obj.setConditionWidth(in.readFloat());
		 obj.setConditionHeight(in.readFloat());
		 obj.setHeightMed(in.readInt());
		 
	 }
	 public static void writeControlFields(ObjectOutputStream out,ButtonControl obj) throws IOException
	 {
		 	writePositionableFields(out,obj);
		 	
		 		
			//protected ButtonComparer condition;		
			out.writeFloat(obj.getConditionPosX());
			out.writeFloat(obj.getConditionWidth());
			out.writeFloat(obj.getConditionHeight());
			out.writeInt(obj.getHeightMed());
			
			
			
			
			
			

			
			
	 }
		
}
