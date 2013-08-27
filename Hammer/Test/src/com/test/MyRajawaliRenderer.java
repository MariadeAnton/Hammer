package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.SerializedObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
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
		
	}
	

	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		
		if(!onResume)return;
		super.onDrawFrame(glUnused);
		if(reload)reloadEnvironmentPieces(auxEnv);
		for(int i=0;i<objects3D.size();i++)
			objects3D.get(i).getModel3D().setRotY(objects3D.get(i).getModel3D().getRotY()+1);
		
		
	}



	@Override
	protected void initScene() {
		
	
		mLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(1.0f, 1.0f, 1.0f);
		mLight.setPower(2);
		mPicker = new ObjectColorPicker(this);
		mPicker.setOnObjectPickedListener(this);
		reloadEnvironmentPieces(GeneralParameters.getEnvironment());
		getCurrentCamera().setZ(10);
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
				objectSelected=objects3D.get(i);
				objectSelected.setTouched(true);
			}
		}
		
		
		for(int i=0;i<objects3D.size();i++)
			if(!objects3D.get(i).equals(objectSelected))
				objects3D.get(i).setTouched(false);
		
		
		for(AuxPiece ap:objects3D)
		{
			if(ap.isTouched())
				ap.getModel3D().setScale(2.0f,2.0f,2.0f);
			else
				ap.getModel3D().setScale(1.0f,1.0f,1.0f);
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
		int i=0;
		for(AuxPiece child:objects3D)removeChild(child.getModel3D());
		objects3D=new ArrayList<AuxPiece>();
		for(AuxPiece piece:env.getPieces())
		{	
			try {
			
			AuxPiece aux=new AuxPiece();
			aux.setModel3D(createPiece(piece.getName()));
			aux.setPath(piece.getPath());
			aux.setName(piece.getName());
			DiffuseMaterial material = new DiffuseMaterial();
			Texture texture=new Texture(R.drawable.logo);
			
			
			material.addTexture(texture);
			
			
			aux.getModel3D().setMaterial(material);
			aux.getModel3D().addLight(mLight);
			aux.getModel3D().setPosition(-4+i*8,0,0);
			aux.getModel3D().setScale(1.0f, 1.0f, 1.0f);
			addChild(aux.getModel3D()); 
			mPicker.registerObject(aux.getModel3D());
			objects3D.add(aux);
			reload=false;
			
			}
			catch (TextureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			i++;
		}
		
		
	}
	
	static public BaseObject3D createPiece(String name)
	{
	
		try {
			
			File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()+MainActivity.applicationFolder+MyRajawaliRenderer.folder3D+name+".ser");
			InputStream in = new FileInputStream (file);
			ObjectInputStream ois=new ObjectInputStream(in);
			BaseObject3D obj3D=new BaseObject3D((SerializedObject3D)ois.readObject());
			ois.close();
			
			return obj3D;
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}



	public AuxPiece getObjectSelected() {
		return objectSelected;
	}




	
	



}
	




