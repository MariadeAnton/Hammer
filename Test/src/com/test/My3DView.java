package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;


public class My3DView extends GLSurfaceView implements OnTouchListener{

	
	static String folder3D="objects3D/";
	private My3DRenderer renderer;
	private OnSelectObject onSelectionListener;
	


	public My3DView(Context context) {
		super(context);
		renderer=new My3DRenderer(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}

	public My3DView(Context context, AttributeSet attrs) {
		super(context, attrs);
		renderer=new My3DRenderer(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
	public void  loadEnvironment(HammerEnvironment environment)
	{
		if(environment==null)return;
		renderer.getWorld().removeAllObjects();
		for(AuxPiece piece:environment.getPieces())
		{
		
			piece.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
			renderer.getWorld().addObject(piece);
		}
		
		
	}
	
	
	public Object3D loadModel(String filename, float scale) {
		
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
			
		if(file.exists())
		{
			input = new FileInputStream(path+ext);
			Object3D modelser = Loader.loadSerializedObject(input);
			return o3d;	
		} 
			
		
		
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
		
		return o3d;	
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
		return o3d;	
	}

	

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		renderer.selectAnyObjectAt((int)event.getX(),(int)event.getY());
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
	
	



}
	
	


