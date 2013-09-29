package com.test;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.test.AuxBasicVariables.Paths;
import com.test.AuxBasicVariables.Point3D;
import com.test.My3DView.OnSelectObject;
import com.threed.jpct.Logger;







public class ActivityEnvironment extends Activity {
	
	
	static String environmentFolder="XML/";
	
	private ListView listXML;
	private ArrayList<ListItem> xList;
	private ArrayList<Point3D>pList;
	private ArrayList<Paths>pathList;
	private AdapterItems adapterXML;
	private Window3D window3D; 
	private WindowList listPoints,listPath;
	private AuxAdapterPoints adapterPoints;
	private AuxAdapterPath adapterPath;
	private ListItem itemSelected;
	private HammerEnvironment environment=null;
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment);
		setTitle("Create Environment");
		window3D=(Window3D) findViewById(R.id.window3D);
		window3D.setTittle("Environment");
		listXML=(ListView)findViewById(R.id.listEnv);
		listPoints=(WindowList)findViewById(R.id.listEnvPoints);
		listPath=(WindowList)findViewById(R.id.listPartPoints);
		listPath.setMode(WindowList.MODE_PATH);
		listPath.setTittle("Part's Paths");
		
		xList=new ArrayList<ListItem>();
		adapterXML=new AdapterItems(this,xList);
	
		pList=new ArrayList<Point3D>();
		adapterPoints=new AuxAdapterPoints(this,pList);
		pathList=new ArrayList<Paths>();
		adapterPath=new AuxAdapterPath(this,pathList);
		
		listXML.setAdapter(adapterXML);
		listXML.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listXML.setOnItemClickListener(new MyClickListener());
		listXML.setSelector(R.drawable.bg_key);
	
		
		listPoints.getListPath().setAdapter(adapterPoints);
		listPoints.getListPath().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listPoints.getListPath().setOnItemClickListener(new MyEnvironmentPointsListener());
		
		listPath.getListPath().setAdapter(adapterPath);
		listPath.getListPath().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listPath.getListPath().setOnItemClickListener(new MyPathPointsListener());
		
		window3D.getView3D().setOnSelectionListener(new MyTouch3DObject());
		
	
	File yourDir =new File( Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+environmentFolder);
	
	for (File f : yourDir.listFiles())
		if (f.isFile())
			xList.add(new ListItem(getResources().getDrawable(R.drawable.xmlfile),f.getName(),yourDir.getAbsolutePath()));

	adapterXML.notifyDataSetChanged();

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

class MyClickListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {
		
		
		environment=new HammerEnvironment();
		environment.setRenderer(window3D.getView3D());
		itemSelected=xList.get(position);
		
		try {
			
			environment.readXMLEnvironment(Environment.getExternalStorageDirectory()+MainActivity.applicationFolder
					+environmentFolder+itemSelected.getName());
			
			window3D.getView3D().loadEnvironment(environment);
			pList.removeAll(pList);
			pList.addAll(environment.getPoints());
			listPoints.setTittle(environment.getName());
			adapterPoints.notifyDataSetChanged();
			

			} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	
	}
	
		
}

class MyEnvironmentPointsListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position,
			long arg3) {
	
			AuxAdapterPoints adaptPoints=(AuxAdapterPoints)list.getAdapter();
			Point3D pointSelected=(Point3D)adaptPoints.getItem(position);
			pointSelected.setSelected(!pointSelected.isSelected());
			
			if(pointSelected.isSelected())
			{
				view.setBackgroundColor(R.drawable.m_skyblue);
				window3D.getView3D().getRenderer().addPoint(pointSelected);
			}
			else 
			{
				view.setBackgroundResource(android.R.color.transparent);
				window3D.getView3D().getRenderer().deletePoint(pointSelected);
			}
			
			
			
		
	}
	
}



class MyPathPointsListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position,
			long arg3) {
	
			AuxAdapterPath adaptPath=(AuxAdapterPath)list.getAdapter();
			Paths pathSelected=(Paths)adaptPath.getItem(position);
			pathSelected.setSelected(!pathSelected.isSelected());
			
			if(pathSelected.isSelected())
			{
				view.setBackgroundColor(R.drawable.m_skyblue);
			//	window3D.getView3D().getRenderer().addPath(pathSelected);
				pList.removeAll(pList);
				pList.addAll(pathSelected.getPathPoints());
				listPoints.setTittle(pathSelected.getName());
				adapterPoints.notifyDataSetChanged();
				
			}
			else 
			{
				view.setBackgroundResource(android.R.color.transparent);
			//	window3D.getView3D().getRenderer().deletePath(pathSelected);
				pList.removeAll(pList);
				pList.addAll(environment.getPoints());
				listPoints.setTittle(environment.getName()+" Points");
				adapterPoints.notifyDataSetChanged();
			}
			
			
			
		
	}
	
	
	}
	
class MyTouch3DObject implements OnSelectObject
	{

		@Override
		public void selectObject(AuxPiece objectSelected) {
			
			pathList.removeAll(pathList);
			pathList.addAll(objectSelected.getPaths());
			adapterPath.notifyDataSetChanged();
			listPath.setTittle(objectSelected.getName()+" Paths");
			
		}

		@Override
		public void deselectObject() {
			pathList.removeAll(pathList);
			pList.removeAll(pList);
			pList.addAll(environment.getPoints());
			adapterPoints.notifyDataSetChanged();
			adapterPath.notifyDataSetChanged();
			listPath.setTittle("Part's Paths");
			listPoints.setTittle(environment.getName()+" Points");
			
		}
		
	}
	
}
