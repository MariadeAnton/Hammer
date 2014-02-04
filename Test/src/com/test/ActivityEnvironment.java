package com.test;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.threed.jpct.Logger;







public class ActivityEnvironment extends Activity {
	
	
	static String environmentFolder="XML/";
	
	private ListView listXML;
	private ArrayList<ListItem> xList;
	private AdapterItems adapterXML;
	private ListItem itemSelected;
	private WindowEnvironment envWindow;
	private HammerEnvironment environment=null;
	private ProgressDialog dialog;
	
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment);
		setTitle("Create Environment");
		listXML=(ListView)findViewById(R.id.listEnv);
		
		
		xList=new ArrayList<ListItem>();
		adapterXML=new AdapterItems(this,xList);
		envWindow=(WindowEnvironment)findViewById(R.id.envWindow);
		envWindow.onCreate(this, (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		listXML.setAdapter(adapterXML);
		listXML.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listXML.setOnItemClickListener(new MyClickListener());
		listXML.setSelector(R.drawable.bg_key);
	
		
		
		
	
	File yourDir =new File( Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+environmentFolder);
	
	for (File f : yourDir.listFiles())
		if (f.isFile())
			xList.add(new ListItem(getResources().getDrawable(R.drawable.xmlfile),f.getName(),yourDir.getAbsolutePath()));

	adapterXML.notifyDataSetChanged();
	dialog=new ProgressDialog(this);
	
}
	
	
	public void loadProgram(View view)
	{
		if(itemSelected==null)		
		{
			Toast toast=Toast.makeText(this, "Select a environment to load",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
		else
		{
			GeneralParameters.setEnvironment(environment);
			Toast toast=Toast.makeText(this, "Environment loaded!",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		
		GeneralParameters.setEnvironment(null);
		finish();
	}
	
	private void copy(Object src) {
		try {
			Logger.log("Copying data from master Activity!");
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

private class MyClickListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {
		
	
		itemSelected=xList.get(position);
		environment=new HammerEnvironment();
		MyAsyncTask loadEnvironment=new MyAsyncTask();
		loadEnvironment.execute(Environment.getExternalStorageDirectory()
	            .getAbsolutePath()+MainActivity.applicationFolder+ActivityEnvironment.environmentFolder+itemSelected.getName());
		//envWindow.getWindow3D().setTittle(environment.getName());

	}
	
		
}
interface Update{
		public void updateLoading(int i, int length,int total);
	}

class MyAsyncTask extends AsyncTask<String,Integer,Boolean> 

{
	
	@Override
	protected Boolean doInBackground(String... params) {


		
			
		environment.setUpdate(new Update(){
				@Override
				public void updateLoading(int i,int percent,int total) {
					if(isCancelled())
							environment.cancelReading();
					publishProgress(i,percent,total);
					
					}});
		
	
		
			
		try {
			environment.readXMLEnvironment(params[0]);
		
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return true;
	}
	

	
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	
	
	@Override
	protected void onCancelled() {
		
		cancel(true);
		Toast toast=Toast.makeText(dialog.getContext(), "Environment loading cancelled!",Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	
	}
	


	@Override
	protected void onCancelled(Boolean result) {
		cancel(true);
		Toast toast=Toast.makeText(dialog.getContext(), "Environment loading cancelled!",Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}




	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		if(result)
			{
				 
				envWindow.getWindow3D().getView3D().getRenderer().loadEnvironment(environment);
				envWindow.getpList().removeAll(	envWindow.getpList());
				envWindow.getpList().addAll(environment.getPoints());
				envWindow.getListPoints().setTittle(environment.getName());
			
				envWindow.setEnvironment(environment);
				envWindow.getWindow3D().setTittle(environment.getName());
				dialog.dismiss(); 
				
			}
     
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	protected void onPreExecute() {
		
		   dialog.setOnCancelListener(new OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                MyAsyncTask.this.cancel(true);
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


	
	
	
	
	
}
	
}
