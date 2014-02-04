package com.test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.test.ActivityEnvironment.Update;





public class HammerEnvironment  {

	
	private ArrayList<Point3D> points= new ArrayList<Point3D>();
	private ArrayList<AuxPiece> pieces=new ArrayList<AuxPiece>();
	private String name=new String();
	private Update update;
	private boolean cancelReading=false;


	

	public HammerEnvironment() {
	// TODO Auto-generated constructor stub
	}
	
	public void readXMLEnvironment(String file) throws XmlPullParserException, IOException
	{
		
		boolean readX=false;
		boolean readY=false;
		boolean readZ=false;
		
		AuxPiece piece=null;
		Point3D point = null;
		ArrayList<Point3D> auxPoints=new ArrayList<Point3D>();
		String namePath=new String();
		int numberPieces=0,pieceNumber=0;
	
				
		FileInputStream fin = null;
		 
		 
		      fin = new FileInputStream(file);
		  
		 
			   XmlPullParser parser = Xml.newPullParser();
			   
			      parser.setInput(fin, "UTF-8");
			 
			      int event = parser.next();
		      
			      while(event != XmlPullParser.END_DOCUMENT) {
			         
			    	  if(cancelReading)
			    	  {  
			    		  fin.close();
			    		  cancelReading=false;
			    		  break;
			    	  }
			    	  if(event == XmlPullParser.START_TAG) {
			        	
			    		  if(parser.getName().compareTo("Environment")==0)
			        		 name=parser.getAttributeValue(0);
			        	
			    		  else  if(parser.getName().compareTo("PointsList")==0)
			    			  auxPoints=new ArrayList<Point3D>();
			    		  
			    		  /*
			    		  else if(parser.getName().compareTo("Point")==0 || parser.getName().compareTo("Position")==0 )
			    			  point=AuxBasicVariables.createPoint3D();
*/
			    		  else if(parser.getName().compareTo("Path")==0)
			    		  {
			    			  namePath=(parser.getAttributeValue(1));
			    			  auxPoints=new ArrayList<Point3D>();
			    			 
			    		  }
			    		  else if(parser.getName().compareTo("X")==0)
			    			  readX=true;
			
			    		  else  if(parser.getName().compareTo("Y")==0)
			    			  readY=true;
			    		 
			    		  else  if(parser.getName().compareTo("Z")==0)
			    			  readZ=true;
			    		  else  if(parser.getName().compareTo("PiecesList")==0)
			    			  numberPieces=Integer.parseInt(parser.getAttributeValue(0));
			    		  
			    		  else if(parser.getName().compareTo("Piece")==0)
			    		  {
			    			  try {
			    				
			    				update.updateLoading(++pieceNumber,((pieceNumber-1)*100)/numberPieces,numberPieces);
					    		piece=new AuxPiece(My3DView.loadModel(parser.getAttributeValue(0),1));
					    		if(pieceNumber==numberPieces)update.updateLoading(pieceNumber,100,numberPieces);
					    		//update.updateLoading(pieceNumber,(pieceNumber*100)/numberPieces,numberPieces);
						    	
			    			  } catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			  piece.setName(parser.getAttributeValue(0));
			    			  piece.setScale(Float.parseFloat(parser.getAttributeValue(1)));
				    			
			    			  
		
			    		  }
			    		 
			    	  }
			    	  
			    	  
			    	  
			    	  else if(event == XmlPullParser.TEXT && parser.getText().trim().length() != 0)
			    	  {
			    		  /*
			    		  if(readX)
			    			  //point.translate((float) Double.parseDouble(parser.getText()),0,0);
			    		  else  if(readY)
			    			 // point.translate(0,(float) Double.parseDouble(parser.getText()),0);
			    		  else  if(readZ)
			    			 // point.translate(0,0,(float) Double.parseDouble(parser.getText()));
				    		*/
			    		 
			    	  }
			    	  
			    	  else if(event == XmlPullParser.END_TAG)
			    	  {
	
				    		 if(parser.getName().compareTo("Point")==0)
				    		  {
				    			  auxPoints.add(point);
				    			  
				    		  }
				    		 
				    		  else if(parser.getName().compareTo("Position")==0)
				    		  {
				    			 
				    			//  piece.translate(point.getTranslation());
				    			  
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
				    			
				    		  }
				    		 
				    		  else if(parser.getName().compareTo("PointsList")==0)	    			 
				    			 points=auxPoints;
				    		 
				    		  else if(parser.getName().compareTo("Path")==0)	    			 
					    		piece.addPath(namePath,auxPoints);

			    		  
			    	  }
	
			         event = parser.next();
			      }
			      fin.close();
			   
			 
	}		  
	

	
	public ArrayList<Point3D> getPoints() {
		return points;
	}

	public ArrayList<AuxPiece> getPieces() {
		return pieces;
	}

	public String getName() {
		return name;
	}
	


	public void setPoints(ArrayList<Point3D> points) {
		this.points = points;
	}

	public void setPieces(ArrayList<AuxPiece> pieces) {
		this.pieces = pieces;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUpdate(Update update)
	{
		this.update=update;
	}

	public void cancelReading()
	{
		cancelReading=true;
	}




	
	
	
	
	
}
