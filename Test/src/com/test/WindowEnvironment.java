package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.test.AuxBasicVariables.Paths;

import com.test.My3DView.OnSelectObject;
import com.threed.jpct.RGBColor;

public class WindowEnvironment extends FrameLayout {

	private Window3D window3D;
	private WindowList listPath;
	private WindowList listPoints;
	private ArrayList<Point3D>pList;
	private ArrayList<Paths>pathList;
	
	private HammerEnvironment environment;
	
	public WindowEnvironment(Context context) {
		super(context);
		environment=GeneralParameters.getEnvironment();
		
	}

	public WindowEnvironment(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		environment=GeneralParameters.getEnvironment();
	
	}

	public WindowEnvironment(Context context, AttributeSet attrs) {
		super(context, attrs);
		environment=GeneralParameters.getEnvironment();
		
	}
	
	public void onCreate(Activity activity,LayoutInflater inflater)
	{
		
		LinearLayout windowLayout=(LinearLayout)inflater.inflate(R.layout.window_environment,null);
		
		window3D=(Window3D) windowLayout.findViewById(R.id.window3D);
		window3D.setTittle("Environment");
		listPoints=(WindowList)windowLayout.findViewById(R.id.envList);
		listPath=(WindowList)windowLayout.findViewById(R.id.pathList);
		listPath.setMode(WindowList.MODE_PATH);
		listPath.setTittle("Part's Paths");
		
		pList=new ArrayList<Point3D>();
		
		pathList=new ArrayList<Paths>();
	
		
		
		listPoints.getListPath().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listPoints.getListPath().setOnItemClickListener(new MyEnvironmentPointsListener());
		
	
		listPath.getListPath().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listPath.getListPath().setOnItemClickListener(new MyPathPointsListener());
		
		addView(windowLayout);
		
	}
	
	public Window3D getWindow3D() {
		return window3D;
	}

	public WindowList getListPath() {
		return listPath;
	}

	public WindowList getListPoints() {
		return listPoints;
	}

	public ArrayList<Point3D> getpList() {
		return pList;
	}

	public ArrayList<Paths> getPathList() {
		return pathList;
	}

	

	class MyEnvironmentPointsListener implements OnItemClickListener
	{
	
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long arg3) {
		
				
				
				
			
		}
		
	}

	class MyPathPointsListener implements OnItemClickListener
	{
	
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long arg3) {
		
				
				
				
			
		}
		
		
		}

	class MyTouch3DObject implements OnSelectObject
	{
	
		@Override
		public void selectObject(AuxPiece objectSelected) {
			
			pathList.removeAll(pathList);
			pathList.addAll(objectSelected.getPaths());
		
			listPath.setTittle(objectSelected.getName()+" Paths");
			
		}
	
		@Override
		public void deselectObject() {
			pathList.removeAll(pathList);
			pList.removeAll(pList);
			pList.addAll(environment.getPoints());
			
			listPath.setTittle("Part's Paths");
			listPoints.setTittle(environment.getName()+" Points");
			
		}
		
	}

	public void setEnvironment(HammerEnvironment environment) {
		this.environment = environment;
		window3D.getView3D().setOnSelectionListener(new MyTouch3DObject());
	}

}
