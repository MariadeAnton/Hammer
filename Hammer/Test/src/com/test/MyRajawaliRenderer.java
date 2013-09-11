package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.SerializedObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.textures.ATexture.TextureException;

import rajawali.math.vector.Vector3;

import rajawali.parser.AParser.ParsingException;
import rajawali.parser.Max3DSParser;
import rajawali.parser.ObjParser;
import rajawali.primitives.Line3D;
import rajawali.renderer.RajawaliRenderer;

import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;

import android.content.Context;
import android.os.Environment;


public class MyRajawaliRenderer extends RajawaliRenderer implements OnObjectPickedListener {

	static String folder3D="objects3D/";
	
	private DirectionalLight mLight;
	private ArrayList<AuxPiece> objects3D=new ArrayList<AuxPiece>();
	private AuxPiece objectSelected=null;
	private boolean onResume=true;
	private ObjectColorPicker mPicker;
	private HammerEnvironment auxEnv=null;
	private boolean reload=false;
	Context context;


	public boolean isReload() {
		return reload;
	}



	public void setReload(HammerEnvironment env) {
		reload = true;
		auxEnv=env;
	}



	public MyRajawaliRenderer(Context context) {
		super(context);
		setFrameRate(60);
		this.context=context;
		
	}
	

	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		
		if(!onResume)return;
		super.onDrawFrame(glUnused);
		if(reload)reloadEnvironmentPieces(auxEnv);
		if(objectSelected!=null)
			objectSelected.getModel3D().setRotY(objectSelected.getModel3D().getRotY()+1);
		
	}



	@Override
	protected void initScene() {
		
		
		
		mLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(1.0f, 1.0f, 1.0f);
		mLight.setPower(2);
		mPicker = new ObjectColorPicker(this);
		mPicker.setOnObjectPickedListener(this);
		getCurrentCamera().setZ(20);
		getCurrentCamera().setY(5);
		getCurrentCamera().setX(0);
		getCurrentCamera().setLookAt(0, 0, 0);
		reloadEnvironmentPieces(GeneralParameters.getEnvironment());
		createGrid(200,200,2,0xffffff00);
		/*
		try{
		ObjParser parser;
		parser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.cardoor_obj);
		
		
			parser.parse();
		
		
		BaseObject3D obj=parser.getParsedObject();
		//DiffuseMaterial material = new DiffuseMaterial();
		
		//Texture texture=new Texture(R.drawable.logo);


	//	material.addTexture(texture);
		

	//	obj.setMaterial(material);
	
		obj.addLight(mLight);
		obj.setPosition(0,0,0);
	//	obj.setScale(0.5f, 0.5f, 0.5f);

		MeshExporter exporter=new MeshExporter(obj);
		exporter.export("mydoor",SerializationExporter.class);
		
		
		
		addChild(obj);
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
		}
	

	
	public boolean isOnResume() {
		return onResume;
	}

	public void setOnResume(boolean onResume) {
		this.onResume = onResume;
	}




	@Override
	public void onObjectPicked(BaseObject3D object) {
		
	
		
		for(int i=0;i<objects3D.size();i++)	
		{
			if(objects3D.get(i).getModel3D().equals(object))
			{
					if(!objects3D.get(i).isTouched())
					{
						for(int z=0;z<objects3D.size();z++)objects3D.get(z).setTouched(false);
						objectSelected=objects3D.get(i);
						objectSelected.setTouched(true);
						break;
					}
			
					else
					{
					objects3D.get(i).setTouched(false);
					objectSelected=null;
					break;
						
					}
				
			}
			
		}
		
		
		
		
		
		
			
		
	}
		
	public void getObjectAt(float x, float y) {
			mPicker.getObjectAt(x, y);
		}




	public ArrayList<AuxPiece> getObjects3D() {
		return objects3D;
	}
	
	
	public void reloadEnvironmentPieces(HammerEnvironment env)
	{
		if(env==null)return;
		for(AuxPiece child:objects3D)removeChild(child.getModel3D());
		objects3D=new ArrayList<AuxPiece>();
		
		try {
			for(AuxPiece piece:env.getPieces())
			{	

			AuxPiece aux=new AuxPiece();
			
			
			aux.setModel3D(createPiece(piece.getName()));
			aux.setPositionXYZ(piece.getPositionXYZ());
			
			aux.setPath(piece.getPath());
			aux.setName(piece.getName());
			DiffuseMaterial material = new DiffuseMaterial();
			material.setUseSingleColor(true);
			
			//Texture texture=new Texture(R.drawable.logo);
	
		
			//material.addTexture(texture);

			aux.getModel3D().setMaterial(material);
			aux.getModel3D().addLight(mLight);
			aux.getModel3D().setPosition((float)aux.getPositionXYZ().x,(float)aux.getPositionXYZ().y
					,(float)aux.getPositionXYZ().z);	
				
			addChild(aux.getModel3D());
			 
			mPicker.registerObject(aux.getModel3D());
			objects3D.add(aux);
			reload=false;			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
		
	
	
	public BaseObject3D createPiece(String name) 
	{
	

		File file;	
		String path=Environment.getExternalStorageDirectory()
                .getAbsolutePath()+MainActivity.applicationFolder+MyRajawaliRenderer.folder3D+name;
		String ext;
		BaseObject3D obj3D;
		InputStream in;
		ObjectInputStream ois;
		try {
			ext=".ser";
			file=new File(path+ext);
			if(file.exists())
			{
			
				in = new FileInputStream (file);
			
				ois=new ObjectInputStream(in);
				obj3D=new BaseObject3D((SerializedObject3D)ois.readObject());
				ois.close();
				return obj3D;
		
			}
		
			ext=".obj";
			file=new File(path+ext);
			if(file.exists())
			{

				ObjParser parserOBJ=new ObjParser(this,file);
				parserOBJ.parse();
				obj3D=parserOBJ.getParsedObject();

				return obj3D;	
				
			}
		
			ext=".3DS";
			file=new File(path+ext);
			if(file.exists())
			{
				Max3DSParser parser3DS=new Max3DSParser(this,file);
			
				parser3DS.parse();	parser3DS.build();
				obj3D=parser3DS.getParsedObject();
				return obj3D;
			}
		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TextureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}


	

	public AuxPiece getObjectSelected() {
		return objectSelected;
	}



	public void createGrid(int height,int width,int linesSpace,int color)
	{
	
		
		
		//HORIZONTAL LINES//
		ArrayList<Line3D> lines=new ArrayList<Line3D>();
		SimpleMaterial material = new SimpleMaterial();
		material.setUseSingleColor(true);
		
		
		for(int i=0;i<=height;i++)
		{
			Stack<Vector3> pointsH =new Stack<Vector3>();
			pointsH.add(new Vector3((-width/2f)*linesSpace,0,(-height/2f+i)*linesSpace));
			pointsH.add(new Vector3((-width/2f+width)*linesSpace,0,(-height/2f+i)*linesSpace));
			Line3D horizontal = new Line3D(pointsH, 1, color);
			horizontal.setMaterial(material);
			lines.add(horizontal);
		}
		
		//VERTICAL LINES//
		
		for(int i=0;i<=width;i++)
		{
			Stack<Vector3> pointsV =new Stack<Vector3>();
			pointsV.add(new Vector3((-width/2f+i)*linesSpace,0,(-height/2f)*linesSpace));
			pointsV.add(new Vector3((-width/2f+i)*linesSpace,0,(-height/2f+height)*linesSpace));
			Line3D vertical = new Line3D(pointsV, 1, color);
			vertical.setMaterial(material);
			lines.add(vertical);
		}

		/**
		 * A Line3D takes a Stack of <Number3D>s, thickness and a color
		 */
		for(int i=0;i<lines.size();i++)
			addChild(lines.get(i));
	}

	
}

	




	




