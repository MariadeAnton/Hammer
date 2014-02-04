package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Environment;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class LoaderObjects extends AsyncTask<String,Integer,Boolean> {

	
	private Object3D[] objectsToLoad;
	private World world;
	private ProgressDialog dialog;
	

	public LoaderObjects(ProgressDialog dialog,World world) {
		
		this.dialog=dialog;
		this.world=world;
		
	}
	
	
	
	

	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		if(result)
			{
				dialog.dismiss();   
				
			}
     
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		
		   dialog.setOnCancelListener(new OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                LoaderObjects.this.cancel(true);
	            }
	        });
	 ////////////Obligatorio ponerlo el message antes del style que si no luego no aparece/////////////
		   		dialog.setMessage("Loading Objects");
		   		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		   		dialog.setCancelable(true);
		   		dialog.setMax(100);
	            dialog.setProgress(0);
	            dialog.show();
	 }
	

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		
		int progress=values[0].intValue();
		int percent=values[1].intValue();
		int total=values[2].intValue();
		dialog.setMessage("Processing and loading objects to scene: "+progress+"/"+total);
		dialog.setProgress(percent);
		
	}

	@Override
	protected Boolean doInBackground(String... objects) {
		
		
		objectsToLoad=new Object3D[objects.length];
		
		for(int i=0;i<objects.length;i++)
		{
			publishProgress(i+1,((i+1)*50/objects.length),objects.length);
			String path=Environment.getExternalStorageDirectory()
	            .getAbsolutePath()+MainActivity.applicationFolder+My3DView.folder3D+objects[i];
		
			File file;
			InputStream input = null;
			Object3D o3d = new Object3D(0);	
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
			publishProgress(i+1,((i+1)*100/objects.length),objects.length);
			for (int j = 0; j < model.length; j++) 
			{
				temp = model[j];
				temp.setCenter(SimpleVector.ORIGIN);
				temp.rotateMesh();
				temp.setRotationMatrix(new Matrix());
				o3d = Object3D.mergeObjects(o3d, temp);
				o3d.build();
			}
	
			
			objectsToLoad[i]=o3d;
			
			}
		
		synchronized(world){world.addObjects(objectsToLoad);}
		return true;
		
	}

}
