/*==============================================================================
Copyright (c) 2010-2013 QUALCOMM Austria Research Center GmbH.
All Rights Reserved.

@file
    ImageTargetsRenderer.java

@brief
    Sample for ImageTargets

==============================================================================*/


package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.qualcomm.QCAR.QCAR;
import com.qualcomm.ar.pl.DebugLog;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Polyline;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

/** The renderer class for the ImageTargets sample. */
public class ImageTargetsRenderer implements GLSurfaceView.Renderer
{
   
	private World world;
	private RobotPort client=null;
	private Object3D comau, irb;
	private AuxPiece objectSelected=null;
	private ArrayList<SimpleVector>vectors=new ArrayList<SimpleVector>();
	private Camera cam;
	private float[] modelViewMat=new float[16];
	private FrameBuffer fb;
	private Light sun;
	private float camfov;
	public boolean fd=false;
	private float camfovy;

	private SimpleVector camRight=new SimpleVector(0,0,0);

	private  SimpleVector camDir=new SimpleVector(0,0,0);
	private boolean renderJPCTModels=true;
	public int modelN=-1;
	private SimpleVector camPos=new SimpleVector(0,0,0);

	private SimpleVector sunPos;
	private SimpleVector camUp=new SimpleVector(0,0,0);
	
	
	
	private ArrayList<Polyline>polylines=new ArrayList<Polyline>();
	private ArrayList<Point3D> pointsList=new ArrayList<Point3D>();

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private ForceDetector forceDetector;
	private float auxData=0,auxData2=-4,auxData3=4;
	private boolean GROWING_MODE=true;
	
	
	
	public ImageTargetsRenderer(ImageTargets activity){
		
	
    this.mActivity = activity;
    
   
   // Config.viewportOffsetAffectsRenderTarget=true;
   // Config.viewportOffsetY =0.02f; 
 
    
    world = new World();
	world.setAmbientLight(10, 10, 10);

	sun = new Light(world);
	sun.setIntensity(125, 125, 125);

	
	String name="irb";
	// Create a texture out of the icon...:-)
	
	//Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(mActivity.getResources().getDrawable(R.drawable.ic_launcher)), 64, 64));
	//TextureManager.getInstance().addTexture("texture", texture);

	String path=Environment.getExternalStorageDirectory()
            .getAbsolutePath()+MainActivity.applicationFolder+My3DView.folder3D+name;
	
	File file;
	InputStream input = null;
	irb = new Object3D(0);	
	Object3D[] model =null;
	Object3D temp = null;
	
	String ext=".obj";
	file=new File(path+ext);
	if(file.exists())
	{
		InputStream material = null;
		try {
			material = new FileInputStream(path+".mtl");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			input = new FileInputStream(path+ext);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model = Loader.loadOBJ(input,material,1);
	
	
	}
	
	for (int i = 0; i < model.length; i++) 
	{
		temp = model[i];
		temp.setCenter(SimpleVector.ORIGIN);
	//	temp.rotateX((float)( -.5*Math.PI));
		temp.rotateMesh();
		temp.setRotationMatrix(new Matrix());
		irb = Object3D.mergeObjects(irb, temp);
		irb.build();
	}
	
	//cube = Primitives.getSphere(3);
	//cube.calcTextureWrapSpherical();
	//cube.setTexture("texture");
	//cube.strip();
	//cube.build();
	//cube.setScale(6.0f);
	
	
	irb.setScale(40);
	
	
	
	
	
	
	name="irb2";
	// Create a texture out of the icon...:-)
	
	//Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(mActivity.getResources().getDrawable(R.drawable.ic_launcher)), 64, 64));
	//TextureManager.getInstance().addTexture("texture", texture);

	path=Environment.getExternalStorageDirectory()
            .getAbsolutePath()+MainActivity.applicationFolder+My3DView.folder3D+name;
	
	
	InputStream input2 = null;
	comau = new Object3D(0);	
	 Object3D[] model2 =null;
	Object3D temp2 = null;
	
	ext=".obj";
	file=new File(path+ext);
	if(file.exists())
	{
		InputStream material2 = null;
		try {
			material2 = new FileInputStream(path+".mtl");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			input2 = new FileInputStream(path+ext);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model2 = Loader.loadOBJ(input2,material2,1);
	
	
	}
	
	for (int i = 0; i < model2.length; i++) 
	{
		temp2 = model2[i];
		temp2.setCenter(SimpleVector.ORIGIN);
	//	temp.rotateX((float)( -.5*Math.PI));
		temp2.rotateMesh();
		temp2.setRotationMatrix(new Matrix());
		comau = Object3D.mergeObjects(comau, temp2);
		comau.build();
	}
	
	//cube = Primitives.getSphere(3);
	//cube.calcTextureWrapSpherical();
	//cube.setTexture("texture");
	//cube.strip();
	//cube.build();
	//cube.setScale(6.0f);
	
	
	//o3d.setScale(40);
	//o3d.rotateX(3.1416f/2.0f);
	
	//o3d.translate(600,800,0);
	world.addObject(irb);
	

	
	//LoaderObjects loader=new LoaderObjects(new ProgressDialog(this.mActivity),world);
	
	
	//loader.execute(name);
	world.setClippingPlanes(0, 100000);
	cam = world.getCamera();
	MemoryHelper.compact();
    }

	
	
	
	public boolean mIsActive = false;

    /** Reference to main activity **/
    public ImageTargets mActivity;



	
	

    /** Native function for initializing the renderer. */
    public native void initRendering();

    /** Native function to update the renderer. */
    public native void updateRendering(int width, int height);


    /** Called when the surface is created or recreated. */
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        DebugLog.LOGD("GLRenderer::onSurfaceCreated","");

        // Call native function to initialize rendering:
        initRendering();

        // Call QCAR function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        QCAR.onSurfaceCreated();
    }


    /** Called when the surface changed size. */
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        DebugLog.LOGD("GLRenderer::onSurfaceChanged","");

        // Call native function to update rendering when render surface
        // parameters have changed:
        updateRendering(width, height);

        // Call QCAR function to handle render surface size changes:
        QCAR.onSurfaceChanged(width, height);
        
        if (fb != null) {
            fb.dispose();
       }
       fb = new FrameBuffer(width, height);
    }


    /** The native render function. */
    public native void renderFrame();
   
    public native void showTeapot(boolean teapot);

    /** Called to draw the current frame. */
    public void onDrawFrame(GL10 gl)
    {
        if (!mIsActive)
            return;

        // Update render view (projection matrix and viewport) if needed:
        mActivity.updateRenderView();
      
        // Call our native function to render content
        renderFrame();
        updateCamera();
        
        if(renderJPCTModels)
        {
        synchronized(world)
        {
        
        if(modelN==0)
        {
        	world.removeAllObjects();
        	if(forceDetector!=null)world.removePolyline(forceDetector.forceVector);
        	world.addObject(irb);
        	fd=false;
        	
        }
        	
        if (modelN==1)
        {
        	world.removeAllObjects();
        	if(forceDetector!=null)world.removePolyline(forceDetector.forceVector);
        	world.addObject(comau);
        	fd=false;
        }
        
        
        if(modelN==2)
        {
        	world.removeAllObjects();
        	if(forceDetector!=null)world.removePolyline(forceDetector.forceVector);
        	forceDetector=new ForceDetector(world,10000);
        	forceDetector.translate(500, 0, 0);
        	fd=true;
        	
        }
        
        if(modelN==3)
        {
        	world.removeAllObjects();
        	if(forceDetector!=null)world.removePolyline(forceDetector.forceVector);
        	fd=false;
        	
        }
        modelN=-1;
        
        if(fd)
        {
        		if(GROWING_MODE)
        		{
        			auxData+=0.3;
        			
        			if(auxData>=10)GROWING_MODE=false;
        		}
        		else
        		{
        			auxData-=0.3;
        			if(auxData<=-10)GROWING_MODE=true;
        		}
        			
        		if(client!=null)
        		{
        			int[] v=client.getValues();
        			forceDetector.updateSensor(v[1]/1000.0f,v[2]/1000.0f,v[0]/1000.0f);
        		}
        		//for(int i=0;i<polylines.size();i++)polylines.get(i).setColor(RGBColor.GREEN);
        		
        }
        
        world.renderScene(fb);
        world.draw(fb);
        fb.display();
        }
        }
    }
    
    
    public int selectAnyObjectAt( int touchX, int touchY){

    	SimpleVector ray=Interact2D.reproject2D3DWS(world.getCamera(),fb, touchX, touchY).normalize(); 
		
		float touchX2=touchX-fb.getWidth()/2;
		float touchY2=touchY-fb.getHeight();
		
		float scaleX=fb.getWidth()/30;
		float touch=(touchX2/fb.getWidth())*scaleX;
		
		SimpleVector pos=Interact2D.reproject2D3D(world.getCamera(),fb,(int) touchX, touchY,0);
		Object[] res = world.calcMinDistanceAndObject3D(world.getCamera().getPosition(), ray, 10000F);
		if (res==null || res[1] == null || res[0] == (Object)Object3D.RAY_MISSES_BOX) { 
			Log.d("SELECTION", "You missed! x="+touchX+" y="+touchY);
			objectSelected = null;
			return -1;
		}
		Object3D obj = (Object3D)res[1];
		Log.d("SELECTION", "x="+touchX+" y="+touchY+" id2="+obj.getID()+" name="+obj.getName());

		objectSelected =(AuxPiece)obj;		
		objectSelected.setTouched(!objectSelected.isTouched());
		return obj.getID();
    }
    
    
    
    public void updateModelviewMatrix(float mat[]) {
        modelViewMat = mat;
    }
    
    public void updateCamera() {
    	
     
        cam.setPosition(camPos);
        cam.setOrientation(camDir, camUp); 
      
       
    	cam.setYFOV(camfovy);
    	cam.setFOV(camfov);
    	
    	 Matrix m = new Matrix();
    	m.setDump(modelViewMat);
    	cam.setBack(m);
        
        
         sun.setPosition(cam.getPosition());
        }
    
    public void setFov(float fov)
    {
    	camfov=fov;
    }
    
    public void setFovy(float fovy)
    {
    	camfovy=fovy;
    }
    
    public void setCamVectors(float[] vect)
    {
    	
    		camRight.set(vect[0], vect[1], vect[2]);
    		camUp.set(-vect[4], -vect[5], -vect[6]);
    		camDir.set(vect[8], vect[9], vect[10]);
    		camPos.set(vect[12], vect[13], vect[14]);
  	
    }
    
    public void renderJPCTModels(boolean render)
    {
    	renderJPCTModels=render;
    }
    
   
	
	

	/**
	 * @return the objectSelected
	 */
	public AuxPiece getObjectSelected() {
		return objectSelected;
	}

	/**
	 * @param objectSelected the objectSelected to set
	 */
	public void setObjectSelected(AuxPiece objectSelected) {
		this.objectSelected = objectSelected;
	}
	
	
	public void launchConnection(boolean bool)
	{
		if(bool)
		{
			 client=new RobotPort("192.168.1.46",2344);
			 client.start();
		}
		
		else
		{
			client.disconnect();
			client=null;
		}
	}
	
}


class ForceDetector
{
	private float heightAxis=10000;
	private float scale=30;
	private double maxModule,maxComponent;
	private Axis x_axis,y_axis,z_axis;
	public Polyline forceVector;
	private Object3D origin;
	private RGBColor colorRGB=new RGBColor();
	private SimpleVector[] vectorPoints={new SimpleVector(0,0,0),new SimpleVector(10,10,10)};
	private World world;
	private float x=0,y=0,z=0;
	
	
	public ForceDetector(World w,int maxForce)
	{
		world=w;
		maxModule=Math.sqrt(Math.pow(maxForce, 2)+Math.pow(maxForce, 2)+Math.pow(maxForce,2));
		maxComponent=maxForce;
		origin=Primitives.getSphere(2*scale);
		origin.setAdditionalColor(RGBColor.BLUE);
		
		forceVector=new Polyline(vectorPoints,RGBColor.GREEN);
		forceVector.setWidth(scale/3);
		world.addPolyline(forceVector);
		onCreate(scale,heightAxis,heightAxis,heightAxis);
	}
	
	public void translate(float x ,float y ,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		origin.translate(x, y, z);
		vectorPoints[0]=new SimpleVector(x,y,z);
		forceVector.update(vectorPoints);
	}
	
	private void onCreate(float scale,float x_heightAxis,float y_heightAxis,float z_heightAxis)
	{
		int xo=(x_heightAxis>0)?90:-90;
		int yo=(y_heightAxis<0)?90:-90;
		int zo=(z_heightAxis<0)?0:-180;
		
		x_axis=new Axis(new SimpleVector(xo,0,0),new SimpleVector(0,0,1),RGBColor.GREEN, scale,x_heightAxis,x,y,z);
		y_axis=new Axis(new SimpleVector(0,0,yo),new SimpleVector(1,0,0),RGBColor.GREEN, scale,y_heightAxis,x,y,z);
		z_axis=new Axis(new SimpleVector(0,0,zo),new SimpleVector(0,1,0),RGBColor.GREEN, scale,z_heightAxis,x,y,z);
	
		
		
		
		
		x_axis.createAxis(world);
	
		y_axis.createAxis(world);
		
		z_axis.createAxis(world);
		
		
		world.addObject(origin);	
		
	}
	
	public void updateSensor(float x, float y, float z)
	{
		
		vectorPoints[1]=new SimpleVector(2*x*scale+this.x,2*y*scale+this.y,2*z*scale+this.z);
		forceVector.update(vectorPoints);
		calculateModuleVector(forceVector,vectorPoints[1]);
		
		
		x_axis.clearAxis(world);
		y_axis.clearAxis(world);
		z_axis.clearAxis(world);
		world.removeObject(origin);
		
		onCreate(scale,x,y,z);
	
		
	}
	
	public void calculateModuleVector(Polyline polyline,SimpleVector vector)
	{
		
		
		float module=(vector.distance(vectorPoints[0]))/(scale*2);
		
		if(module<maxModule*0.5f)			colorRGB.setTo(0,255,0,125);
		else if(module<maxModule*0.8f)	colorRGB.setTo(255,255,0,125);
		else						colorRGB.setTo(255,0,0,125);
		
		polyline.setColor(colorRGB);
	}

	private class Axis
	{
		private SimpleVector orientation,direction;
		private RGBColor color;
		private float size;
		private float height;
		private float absHeight;
		
		private Object3D cone;
		private Object3D cylinder;
		private float x=0,y=0,z=0;
			
		
		public Axis(SimpleVector orientation,SimpleVector direction,RGBColor color, float size, float height,float x,float y,float z) {
			
			
			this.orientation = orientation;
			this.direction=direction;
			this.color = color;
			this.size = size;
			this.height =height;
			absHeight=10000;
			this.x=x;
			this.y=y;
			this.z=z;
			onCreate();
			
		}
		
		private void onCreate()
		{
	
		
			
			cylinder=Primitives.getCylinder(100, size, height);		
			cone=Primitives.getCone(1);
			cone.setScale(2*size);
			
			//MAximo partido en 6: 3-5-6
			
			if(absHeight<maxComponent*0.5f)   	color.setTo(0,255,0,255);			//color.setTo((int)((height/(3*max/6))*255),255,0,1);
			else if(absHeight<maxComponent*0.8f)  color.setTo(255,255,0,255);		//color.setTo(255,(int)(255-(height/(5*max/6))*255),0,1);
			else  color.setTo(255,0,0,255);
			
			
			cylinder.setAdditionalColor(color);
			cone.setAdditionalColor(color);
			
			cylinder.rotateX((float) Math.toRadians(orientation.x));
			cylinder.rotateY((float) Math.toRadians(orientation.y));
			cylinder.rotateZ((float) Math.toRadians(orientation.z));
			cone.rotateX((float) Math.toRadians(orientation.x));
			cone.rotateY((float) Math.toRadians(orientation.y));
			cone.rotateZ((float) Math.toRadians(orientation.z));
			
			cylinder.translate(direction.x*(height*size),direction.y*(height*size),direction.z*(height*size));
			cone.translate(direction.x*2*(height*size),direction.y*2*(height*size),direction.z*2*(height*size));
			
			cylinder.translate(x,y,z);
			cone.translate(x,y,z);
		}
			
		
		
		
		public void createAxis(World w){
			
			w.addObject(cylinder);
			w.addObject(cone);
			
		}
		
		public void clearAxis(World w)
		{
			w.removeObject(cylinder);
			w.removeObject(cone);
		}

	
		public void setOrientation(SimpleVector orientation) {
			
			this.orientation = orientation;
		
			cylinder.rotateX((float) Math.toRadians(orientation.x));
			cylinder.rotateY((float) Math.toRadians(orientation.y));
			cylinder.rotateZ((float) Math.toRadians(orientation.z));
			cone.rotateX((float) Math.toRadians(orientation.x));
			cone.rotateY((float) Math.toRadians(orientation.y));
			cone.rotateZ((float) Math.toRadians(orientation.z));
		}
		
		public void setDirection(SimpleVector direction)
		{
			this.direction=direction;
			cylinder.translate(direction.x*(height*size),direction.y*(height*size),direction.z*(height*size));
			cone.translate(direction.x*2*(height*size),direction.y*2*(height*size),direction.z*2*(height*size));
			
		}
		
		public void setColor(RGBColor colorRGB)
		{
			cylinder.setAdditionalColor(colorRGB);
			cone.setAdditionalColor(colorRGB);
		}
	
		
	
			
	}
	
	
	
}
