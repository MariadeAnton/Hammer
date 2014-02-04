package com.test;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;



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
	private World world =new World();
	private int fps = 0;
	private Light sun = null;
	private long time = System.currentTimeMillis();
	private Camera camera;
	private AuxPiece objectSelected=null;
	private AuxPiece objectTrans=null;
	private int widthGrid=200;
	private int heightGrid=200;
	private int lineSpace=2;
	private int camDist=50;
	private SimpleVector origin=new SimpleVector(0,0,0);

	

	
	
	/////////////////////RENDER FLAGS////////////////////////////////////7
	public  ArrayList<Integer>flags=new ArrayList<Integer>();
	private int currentFlag=-1;
	static final int DEFAULT=-1;
	static final int ADD_PATH=0;
	static final int REMOVE_PATH=1;
	static final int REMOVE_ALL_PATHS=2;
	static final int ADD_POINT3D=3;
	static final int OBJECT_SELECTION=4;
	static final int LOAD_ENVIRONMENT=5;
	static final int MOVE_OBJECT=6;
	static final int MOVE_CAMERA=7;
	static final int ZOOM_P=8; 
	static final int ZOOM_S=9; 
	static final int OBJECT_TRANSLATION=10;
	static final int ADD_OBJECT3D=11;
	static final int REMOVE_ALL_POINTS=12;
	static final int REMOVE_POINT3D=13;
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////

	///////////////////////////////VARIABLES RENDER////////////////////////////
	
	Object3D modObject3D;
	SimpleVector[] modVector;
	HammerEnvironment modEnvironment;
	float modTouchX,modTouchY;
	float modCameraRotX,modCameraRotY;
	float modZoom;
	Point3D modPoint3D;
	Path3D modPath3D;
	ArrayList<Point3D> modPoints3DArray;
	ArrayList<Path3D> modPath3DArray;
	public Object lock=new Object();
	
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////
	
	
	
	
	public My3DRenderer(Context context) {
		super();
		flags.add(0,DEFAULT);
		
	
	}

	
	@Override
	public void onDrawFrame(GL10 arg0) {

		int auxSize;
		
		currentFlag=flags.get(0);
	
		switch(currentFlag)
		{
			case DEFAULT:
				break;
			
			case ADD_PATH:
					
				modPath3D.createPath(world);
				modPath3DArray.add(modPath3D);
				flags.remove(0);
				break;
			
				
			case REMOVE_ALL_PATHS:
				auxSize=modPath3DArray.size();
				for(int i=0;i<auxSize;i++)
				{		
						world.removePolyline(modPath3DArray.get(i).line);
						int sizeVertex=modPath3DArray.get(i).getVertex3D().length;
						for(int j=0;j<sizeVertex;j++)
							world.removeObject(modPath3DArray.get(i).getVertex3D()[j]);
				}
				
				modPath3DArray.removeAll(modPath3DArray);
				flags.remove(0);
				break;
				
			case REMOVE_ALL_POINTS:
				auxSize=modPoints3DArray.size();
				for(int i=0;i<auxSize;i++)
				{
					
					world.removeObject(modPoints3DArray.get(i).getObject3D());
				}
				modPoints3DArray.removeAll(modPoints3DArray);
				flags.remove(0);
				break;
				
				
			case ADD_POINT3D:
				modPoint3D.createPoint(world);
				modPoints3DArray.add(modPoint3D);
				flags.remove(0);
				break;
				
			case REMOVE_POINT3D:
				world.removeObject(modPoint3D.getObject3D());
				modPoint3D=null;
				flags.remove(0);
				break;
			
			case OBJECT_SELECTION:
			case OBJECT_TRANSLATION:
				synchronized(this)
				{
				SimpleVector ray=Interact2D.reproject2D3DWS(world.getCamera(),fb,(int) modTouchX, (int)modTouchY).normalize(); 
				
				//	float touchX2=touchX-fb.getWidth()/2;
				//	float touchY2=touchY-fb.getHeight();
					
				//	float scaleX=fb.getWidth()/30;
				//	float touch=(touchX2/fb.getWidth())*scaleX;
					
					//SimpleVector pos=Interact2D.reproject2D3D(world.getCamera(),fb,(int) touch, touchY,0);
					
					Object[] res = world.calcMinDistanceAndObject3D(world.getCamera().getPosition(), ray, 10000F);
					if (res==null || res[1] == null || res[0] == (Object)Object3D.RAY_MISSES_BOX) { 
						Log.d("SELECTION", "You missed! x="+modTouchX+" y="+modTouchY);
						if(currentFlag==OBJECT_SELECTION)
						{
							if(objectSelected!=null)objectSelected.setTouched(!objectSelected.isTouched());
							objectSelected = null;
							
						}
				
						else objectTrans=null;
						
					}
					else if(res[1]!=null)
					{
						Object3D obj = (Object3D)res[1];
						Log.d("SELECTION", "x="+modTouchX+" y="+modTouchY+" id2="+obj.getID()+" name="+obj.getName());

						if(currentFlag==OBJECT_SELECTION)
						{
						if(objectSelected!=null && objectSelected!=obj)objectSelected.setTouched(!objectSelected.isTouched());
						objectSelected =(AuxPiece)obj;		
						objectSelected.setTouched(!objectSelected.isTouched());
						}
						else
							objectTrans=(AuxPiece)obj;
						
						
					}
					notify();
				}
				flags.remove(0);
				break;
					
			case LOAD_ENVIRONMENT:
				if(modEnvironment==null)return;
				world.removeAllObjects();
				for(AuxPiece piece:modEnvironment.getPieces())
				{
				
					piece.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
					world.addObject(piece);
				}
				flags.remove(0);
				break;
				
			case MOVE_OBJECT:
				objectTrans.translate(modTouchX,modTouchY,0);
				flags.remove(0);
				break;
				
			case MOVE_CAMERA:
			
				camera.moveCamera(Camera.CAMERA_MOVEIN,camDist);
				
				camera.rotateX( (float) Math.toRadians(modCameraRotY));				
				camera.rotateY((float)Math.toRadians(modCameraRotX));
				
				camera.moveCamera(Camera.CAMERA_MOVEOUT, camDist);
				camera.lookAt(origin);	
				
				flags.remove(0);
				break;
			
			case ZOOM_P:
			case ZOOM_S:
				
				if(currentFlag==ZOOM_P)
					camera.increaseFOV(camera.convertDEGAngleIntoFOV(modZoom));
				else if(currentFlag==ZOOM_S)
					camera.decreaseFOV(camera.convertDEGAngleIntoFOV(modZoom));
				
				flags.remove(0);
				break;
				
			case ADD_OBJECT3D:
				
				world.addObject(modObject3D);
				flags.remove(0);
				break;
		/*		
			case SIMULATE_ROBOT:
				
				float stepX,stepY,stepZ;
				
				
				float initPosX,initPosY,initPosZ;
				float difX,difY,difZ;
				
				int size=modPath.length;
				
				modRobot.moveToolTo(Robot3DOF.INITPOSX, Robot3DOF.INITPOSY, Robot3DOF.INITPOSZ);
				for (int i=0;i<size;i++)
				{
					
					
					initPosX=modRobot.getToolCoord().x;
					initPosY=modRobot.getToolCoord().y;
					initPosZ=modRobot.getToolCoord().z;
					
					difX=modPath[i].x-initPosX;
					difY=modPath[i].y-initPosY;
					difZ=modPath[i].z-initPosZ;
					
					stepX=stepY=stepZ=0.01f;
					
					float max=Math.abs(Math.max(Math.abs(difX/stepX), Math.abs(difY/stepY)));
					max=Math.abs(Math.max(max, Math.abs(difZ/stepZ)));
					
					for(int x=0;x<max;x++)
					{
						
						
						if(initPosX==modPath[i].x)stepX=0;
						if(initPosY==modPath[i].y)stepY=0;
						if(initPosZ==modPath[i].z)stepZ=0;
						
						if(difX!=0.0f)
							initPosX=initPosX+(difX/Math.abs(difX))*stepX;
						if(difY!=0.0f)
							initPosY=initPosY+(difY/Math.abs(difY))*stepY;
						if(difZ!=0.0f)
							initPosZ=initPosZ+(difZ/Math.abs(difZ))*stepZ;
						
						modRobot.moveToolTo(initPosX,initPosY,initPosZ);
						
						
						fb.clear(RGBColor.BLACK);
						world.renderScene(fb);
						world.draw(fb);
						fb.display();
						

						if (System.currentTimeMillis() - time >= 1000) {
							Logger.log(fps + "fps");
							fps = 0;
							time = System.currentTimeMillis();
							
					
							fps++;
						}
					}
				}
				break;
				
				*/
				
				
	
		
		}
		
		
		
		
		
		
		if(objectSelected!=null && objectSelected.isTouched()) objectSelected.rotateY(0.01f);
		//for(int i=0;i<pointsList.size();i++)animationPoint(pointsList.get(i),0.5f);
		
		
		//MemoryHelper.compact();
				
				
		//fb.freeMemory();		
		fb.clear(RGBColor.BLACK);
		world.renderScene(fb);
		world.draw(fb);	
		fb.display();
		

		if (System.currentTimeMillis() - time >= 1000)
		{
			Logger.log(fps + "fps");
			fps = 0;
			time = System.currentTimeMillis();
		}

		fps++;
		//if(RENDER_CONTINUOSLY)view3D.requestRender();
	
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
			camera=world.getCamera();
			setCamera(camera);
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
			synchronized(world){world.addPolyline(lines.get(i));}
	}

	public void selectAnyObjectAt( int touchX, int touchY,boolean selectable){
		
		modTouchX=touchX;
		modTouchY=touchY;
		flags.add(flags.size()-1,(selectable)?OBJECT_SELECTION:OBJECT_TRANSLATION);
		
		
		
	}
	
	private void setCamera(Camera cam)
	{
		
		cam.setPosition(0,-10,-camDist);
		cam.lookAt(new SimpleVector(0,0,0));
		
	}
	
	public World getWorld() {
		return world;
	}

	public void  loadEnvironment(HammerEnvironment environment)
	{
		modEnvironment=environment;
		flags.add(flags.size()-1,LOAD_ENVIRONMENT);
		
		
		
	}
	

	public AuxPiece getObjectSelected() {
		return objectSelected;
	}
	
	
	
	public void createObject(Object3D creation)
	{
		modObject3D=creation;
		flags.add(flags.size()-1,ADD_OBJECT3D);
	}

/*	
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


	/**
	 * @param objectSelected the objectSelected to set
	 */
	public void setObjectSelected(AuxPiece objectSelected) {
		this.objectSelected = objectSelected;
	}
	
	public void moveObject3D(float x, float y)
	{
		modTouchX=x;
		modTouchY=y;
		flags.add(flags.size()-1,MOVE_OBJECT);
		
	}
	
	public void moveCamera(float moveX,float moveY)
	{
		modCameraRotX=moveX;
		modCameraRotY=moveY;
		flags.add(flags.size()-1,MOVE_CAMERA);
	}
	
	
	
	public void setZoom(float zoom,boolean mode)
	{
		modZoom=zoom;
		if(mode)
			flags.add(flags.size()-1,ZOOM_P);
		else
			flags.add(flags.size()-1,ZOOM_S);
	}


	public int getCamDist() {
		return camDist;
	}


	public void setCamDist(int camDist) {
		this.camDist = camDist;
	}


	public AuxPiece getObjectTrans() {
		return objectTrans;
	}


	public void setObjectTrans(AuxPiece objectTrasn) {
		this.objectTrans = objectTrasn;
	}
	
	public void addPath(Path3D path,ArrayList<Path3D>paths)
	{
		modPath3D=path;
		modPath3DArray=paths;
		flags.add(flags.size()-1,ADD_PATH);
	}


	public void removeAllPaths(ArrayList<Path3D>paths3D) {
		
		modPath3DArray=paths3D;
		flags.add(flags.size()-1,REMOVE_ALL_PATHS);
		
	}
	
	public void removePath(Path3D path3D)
	{
		modPath3D=path3D;
		flags.add(flags.size()-1,REMOVE_PATH);
	}


	public void addPoint(Point3D point,ArrayList<Point3D>points)
	{
		modPoint3D=point;
		modPoints3DArray=points;
		flags.add(flags.size()-1,ADD_POINT3D);
	}


	public void removeAllPoints(ArrayList<Point3D>points3D) {
		
		modPoints3DArray=points3D;
		flags.add(flags.size()-1,REMOVE_ALL_POINTS);
		
	}


	public void removePoint3D(Point3D point) {
		
		modPoint3D=point;
		flags.add(flags.size()-1,REMOVE_POINT3D);
		
		
	}
}

class Path3D
{
	public SimpleVector[] vertex;
	public Polyline line;
	private Object3D[] vertex3D;
	
	public void createPath(World world)
	{
		world.addPolyline(line);
		
		int size=vertex.length;
		vertex3D=new Object3D[vertex.length];
		for(int i=0;i<size;i++)
		{
			vertex3D[i]=Primitives.getSphere(0.2f);
			vertex3D[i].setAdditionalColor(RGBColor.BLUE);
			vertex3D[i].translate(vertex[i]);
			world.addObject(vertex3D[i]);
			
		}
	}

	public Object3D[] getVertex3D() {
		return vertex3D;
	}
	
}

class Point3D 
{
	public SimpleVector position;
	public RGBColor color;
	private Object3D point=Primitives.getSphere(0.4f);
	static private long id;
	
	public Point3D(SimpleVector pos)
	{
		position=pos;
	
	}
	
	public Point3D(SimpleVector pos, RGBColor col)
	{
		position=pos;
		color=col;
	
	}

	public void createPoint(World world)
	{
		point.translate(position);
		point.setAdditionalColor(color);
		world.addObject(point);
	}

	public Object3D getObject3D() {
		return point;
	}

	public static long getId() {
		return id;
	}
	
	
	
	
}
