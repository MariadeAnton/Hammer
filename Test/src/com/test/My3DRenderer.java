package com.test;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.test.AuxBasicVariables.Point3D;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Polyline;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

public class My3DRenderer implements GLSurfaceView.Renderer {

	
	

	private ActivityEnvironment master=null;
	private FrameBuffer fb = null;
	private SimpleVector[] drawline=new SimpleVector[100];
	private Polyline polyline;
	private int numberPoints=0;
	private ArrayList<Point3D> pointsList=new ArrayList<Point3D>();
	private World world =new World();
	private RGBColor back = new RGBColor(50, 50, 100);
	private int fps = 0;
	private Light sun = null;
	private long time = System.currentTimeMillis();
	private AuxPiece objectSelected=null;
	private int widthGrid=200;
	private int heightGrid=200;
	private int lineSpace=2;


	
	public My3DRenderer(Context context) {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onDrawFrame(GL10 arg0) {

		if(objectSelected!=null && objectSelected.isTouched()) objectSelected.rotateY(0.008f);
		fb.clear(RGBColor.BLACK);
		for(int i=0;i<pointsList.size();i++)animationPoint(pointsList.get(i),1);
		world.renderScene(fb);
		world.draw(fb);
		fb.display();

		if (System.currentTimeMillis() - time >= 1000) {
			Logger.log(fps + "fps");
			fps = 0;
			time = System.currentTimeMillis();
		}
	
		fps++;
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(gl, width, height);

		//if (master == null) {

		
			world.setAmbientLight(20, 20, 20);
			sun = new Light(world);
			sun.setIntensity(250, 250, 250);

			// Create a texture out of the icon...:-)
			

			RGBColor colorGrid=new RGBColor();
			colorGrid.setTo(255, 255, 0, 255);
			
			createGrid(widthGrid,heightGrid,lineSpace,colorGrid);
			setCamera(world.getCamera());
			SimpleVector sv = new SimpleVector();
			//sv.set(object3D.getTransformedCenter());
			sv.y -= 100;
			sv.z -= 100;
			sun.setPosition(sv);
			MemoryHelper.compact();
/*
			if (master == null) {
				Logger.log("Saving master Activity!");
				master = ActivityEnvironment.this;
			}
			*/
	//	}
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}

	public void setMaster(ActivityEnvironment masterEnv)
	{
		master=masterEnv;
	}
	
	public void createGrid(int height,int width,int linesSpace,RGBColor color)
	{
	
		
		
		//HORIZONTAL LINES//
		ArrayList<Polyline> lines=new ArrayList<Polyline>();
		
		
		
		
		for(int i=0;i<=height;i++)
		{
			SimpleVector pointsH[] =new SimpleVector[2];
			pointsH[0]=new SimpleVector((-width/2f)*linesSpace,0,(-height/2f+i)*linesSpace);
			pointsH[1]=new SimpleVector((-width/2f+width)*linesSpace,0,(-height/2f+i)*linesSpace);
			Polyline horizontal = new Polyline(pointsH, color);
			lines.add(horizontal);
		}
		
		//VERTICAL LINES//
		
		for(int i=0;i<=width;i++)
		{
			SimpleVector pointsV[] =new SimpleVector[2];
			pointsV[0]=new SimpleVector((-width/2f+i)*linesSpace,0,(-height/2f)*linesSpace);
			pointsV[1]=new SimpleVector((-width/2f+i)*linesSpace,0,(-height/2f+height)*linesSpace);
			Polyline vertical = new Polyline(pointsV, color);
			lines.add(vertical);
		}

		/**
		 * A Line3D takes a Stack of <Number3D>s, thickness and a color
		 */
		for(int i=0;i<lines.size();i++)
			world.addPolyline(lines.get(i));
	}

	public int selectAnyObjectAt( int touchX, int touchY){
		
		
		SimpleVector ray=Interact2D.reproject2D3DWS(world.getCamera(),fb, touchX, touchY).normalize(); 
		
		float touchX2=touchX-fb.getWidth()/2;
		float touchY2=touchY-fb.getHeight();
		
		float scaleX=fb.getWidth()/30;
		float touch=(touchX2/fb.getWidth())*scaleX;
		
		SimpleVector pos=Interact2D.reproject2D3D(world.getCamera(),fb,(int) touch, touchY,0);
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
	
	private void setCamera(Camera cam)
	{
		
		cam.setPosition(0,-10,-30);
		cam.lookAt(new SimpleVector(0,0,0));
		
	}
	
	public World getWorld() {
		return world;
	}


	public AuxPiece getObjectSelected() {
		return objectSelected;
	}
	
	public void addPoint(Point3D sphere)
	{
		pointsList.add(sphere);
		world.addObject(sphere);
	}
	
	public void deletePoint(Point3D sphere)
	{
		world.removeObject(sphere);
		pointsList.remove(sphere);
	}
	
	private void animationPoint(Point3D point,float max)
	{
		
		if(point.isGrowing())
			{
			
				point.setRadius(point.getRadius()+0.01f);
				if(point.getRadius()>max)point.setGrow(false);
			}		
		else 			{
			
		
			point.setRadius(point.getRadius()-0.01f);
			if(point.getRadius()<max/2)point.setGrow(true);
			}
		
	
	}
}
