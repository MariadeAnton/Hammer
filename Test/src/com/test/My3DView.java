package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;


import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;


public class My3DView extends GLSurfaceView implements OnTouchListener{

	
	static String folder3D="objects3D/";
	static final boolean ZOOM_P=true;
	static final boolean ZOOM_S=false;
	
	private My3DRenderer renderer;
	private OnSelectObject onSelectionListener=null;
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	private float oldScale=1.f;
	private float initTouchX,initTouchY;
	private boolean cameraMove=false;
	

	


	public My3DView(Context context) {
		super(context);
		renderer=new My3DRenderer(context);
		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		//renderContinuosly(true);
		 mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		 
	}

		// TODO Auto-generated constructor stub
	

	public My3DView(Context context, AttributeSet attrs) {
		super(context, attrs);
		renderer=new My3DRenderer(context);
		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

		// TODO Auto-generated constructor stub
	
	
	
	
	
	public static Object3D loadModel(String filename, float scale) {
		
		InputStream input;
		Object3D o3d = new Object3D(0);	
		Object3D[] model =null;
		Object3D temp = null;
		
		String path=Environment.getExternalStorageDirectory()
                .getAbsolutePath()+MainActivity.applicationFolder+My3DView.folder3D+filename;
		
		File file;
		String ext=".ser";
		file=new File(path+ext);
		try {
	/*		
		if(file.exists())
		{
			input = new FileInputStream(path+ext);
			Object3D modelser = Loader.loadSerializedObject(input);
			return o3d;	
		} 
			
		*/
		
		ext=".obj";
		file=new File(path+ext);
		if(file.exists())
		{
			InputStream material = new FileInputStream(path+".mtl");
			input = new FileInputStream(path+ext);
			model = Loader.loadOBJ(input,material,scale);
		
		
		}
		
		else
		{
			ext=".3DS";
			file=new File(path+ext);
			if(file.exists())
			{
				input = new FileInputStream(path+ext);
				model = Loader.load3DS(input, scale);
			
			}
			
			
			
		}
		
		for (int i = 0; i < model.length; i++) 
		{
			temp = model[i];
			temp.setCenter(SimpleVector.ORIGIN);
			temp.rotateX((float)( -.5*Math.PI));
			temp.rotateMesh();
			temp.setRotationMatrix(new Matrix());
			o3d = Object3D.mergeObjects(o3d, temp);
			o3d.build();
		}
		
		
		
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
		return o3d;	
		
	
	}

	public void addObject3D(Object3D addition)
	{
		renderer.createObject(addition);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		

				
				switch(event.getAction())
				{

				
				
				
				case MotionEvent.ACTION_DOWN:

					initTouchX=event.getX();
					initTouchY=event.getY();

					
					synchronized(renderer)
					{
						renderer.selectAnyObjectAt((int)event.getX(),(int)event.getY(),!cameraMove);
						try {
							renderer.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						if(cameraMove)break;
						
						if(onSelectionListener==null)return false;
						AuxPiece object=renderer.getObjectSelected();
						if(object!=null)
						{
							if(object.isTouched())
								onSelectionListener.selectObject(object);
							else
								onSelectionListener.deselectObject();	
						}
						else
							onSelectionListener.deselectObject();
					}
				
				
					
					break;
	


				case MotionEvent.ACTION_MOVE:
	
					if(!cameraMove)break;
					
					if(mScaleDetector.isInProgress())break;
					
					float difX=event.getX()-initTouchX;
					float difY=event.getY()-initTouchY;
					
					
					if(renderer.getObjectTrans()!=null)		
						renderer.moveObject3D(difX/renderer.getCamDist(),difY/renderer.getCamDist());
					else
						renderer.moveCamera(difX/5f,difY/5f);
						
				
					initTouchX=event.getX();
					initTouchY=event.getY();
					
					
					break;
	 
	 
				case MotionEvent.ACTION_UP:
					
					break;	
					
			}
		
				try {
					Logger.log("Move camera move");		
					Thread.sleep(20);
			
				} catch (InterruptedException e) {
		
			// TODO Auto-generated catch block
			
					e.printStackTrace();
			
				}
	
		return false;
	}

	public My3DRenderer getRenderer() {
		return renderer;
	}


	interface OnSelectObject
	{
		public void selectObject(AuxPiece object);
		public void deselectObject();
	}



	public void setOnSelectionListener(OnSelectObject selection) {
		onSelectionListener = selection;
	}
	
	
	private class ScaleListener  extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
				
			float detectorScale=detector.getScaleFactor();
		
			
				if(detectorScale>=1)
				mScaleFactor += detectorScale/50f;
				else
					mScaleFactor -= detectorScale/50f;
			
				
				
					// Don't let the object get too small or too large.
				
				//invalidate();
				
				if(mScaleFactor>=oldScale)
					renderer.setZoom(mScaleFactor,ZOOM_S);
				else if(mScaleFactor<oldScale)
					renderer.setZoom(mScaleFactor,ZOOM_P);
					
				oldScale=mScaleFactor;
				Logger.log("Scale event touch");
				return true;
		}
		
		
		
}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		
		if(mScaleDetector.onTouchEvent(event))return true;
		
		return false;
	}

	

	public void setCameraMove(boolean cameraMove) {
		this.cameraMove = cameraMove;
	}
	

	public void addPath(Path3D path,ArrayList<Path3D> paths)
	{
		renderer.addPath(path,paths);
	}
	


	public void removeAllPaths(ArrayList<Path3D>paths3D) {
		renderer.removeAllPaths(paths3D);
		
	}

	public void addPoint3D(Point3D point,ArrayList<Point3D> points) {
		renderer.addPoint(point,points);
		
	}
	
	public void removeAllPoints(ArrayList<Point3D>points)
	{
		renderer.removeAllPoints(points);
	}
	
	public void removePoint(Point3D point)
	{
		renderer.removePoint3D(point);
	}
}
	
	


