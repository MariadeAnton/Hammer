package com.test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.test.AuxBasicVariables.TriplePoint;



public class HammerEnvironment {

	
	private ArrayList<TriplePoint> points= new ArrayList<TriplePoint>();
	private ArrayList<AuxPiece> pieces=new ArrayList<AuxPiece>();
	private String name=new String();

	

	public HammerEnvironment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void readXMLEnvironment(String file) throws XmlPullParserException, IOException
	{
		
		boolean readX=false;
		boolean readY=false;
		boolean readZ=false;
		boolean readList=false;
		boolean readPiece=false;
		AuxPiece piece=null;
		TriplePoint point = null;
		
				FileInputStream fin = null;
		 
		 
		      fin = new FileInputStream(file);
		  
		 
			   XmlPullParser parser = Xml.newPullParser();
			   
			      parser.setInput(fin, "UTF-8");
			 
			      int event = parser.next();
		      
			      while(event != XmlPullParser.END_DOCUMENT) {
			         
			    	  if(event == XmlPullParser.START_TAG) {
			        	
			    		  if(parser.getName().compareTo("Environment")==0)
			        		 name=parser.getAttributeValue(0);
			        	
			    		  else  if(parser.getName().compareTo("PointsList")==0)
			    			  readList=true;
			    		  
			    		  else if(parser.getName().compareTo("Point")==0)
			    			  point=AuxBasicVariables.createTriplePoint();

			    		  else if(parser.getName().compareTo("X")==0)
			    			  readX=true;
			
			    		  else  if(parser.getName().compareTo("Y")==0)
			    			  readY=true;
			    		 
			    		  else  if(parser.getName().compareTo("Z")==0)
			    			  readZ=true;
			    		  
			    		  
			    		  else if(parser.getName().compareTo("Piece")==0)
			    		  {
			    			  piece=new AuxPiece(MyRajawaliRenderer.createPiece(parser.getAttributeValue(0)));
			    			  piece.setName(parser.getAttributeValue(0));
			    			  readPiece=true;
		
			    		  }
			    		 
			    	  }
			    	  
			    	  
			    	  
			    	  else if(event == XmlPullParser.TEXT && parser.getText().trim().length() != 0)
			    	  {
			    		  if(readX)
			    			  point.x=Double.parseDouble(parser.getText());
			    		  else  if(readY)
			    			  point.y=Double.parseDouble(parser.getText());
			    		  else  if(readZ)
			    			  point.z=Double.parseDouble(parser.getText());
			    		  
			    		 
			    	  }
			    	  
			    	  else if(event == XmlPullParser.END_TAG)
			    	  {
	
				    		 if(parser.getName().compareTo("Point")==0 && readList)
				    		  {
				    			  points.add(point);
				    			  
				    		  }
				    		  else if(parser.getName().compareTo("Point")==0 && readPiece)
				    		  {
				    			  piece.getPath().add(point);
				    			  
				    		  }
				    		 
				    		  else if(parser.getName().compareTo("X")==0)
				    			  readX=false;
				
				    		  else  if(parser.getName().compareTo("Y")==0)
				    			  readY=false;
				    		 
				    		  else  if(parser.getName().compareTo("Z")==0)
				    			  readZ=false;
				    		 
				    		  else if(parser.getName().compareTo("Piece")==0)
				    		  {
				    			 pieces.add(piece);
				    			 readPiece=false;
			
				    		  }
				    		 
				    		  else if(parser.getName().compareTo("PointsList")==0)	    			 
				    			 readList=false;

			    		  
			    	  }
	
			         event = parser.next();
			      }
			      fin.close();
			   
			 
	}		  
	

	
	public ArrayList<TriplePoint> getPoints() {
		return points;
	}

	public ArrayList<AuxPiece> getPieces() {
		return pieces;
	}

	public String getName() {
		return name;
	}
	
	
	
	
	
	
	
	
	
}
