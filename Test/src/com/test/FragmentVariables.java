package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;



public class FragmentVariables extends Fragment {

	private MyVariableGrid varGrid;
	private ListView pathPoints;
	private TextView partSelectedName;
	private FrameLayout variablesInfo;
	private Activity activity;
	
	
	
	
	
	public FragmentVariables(Activity act) {
		activity=act;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		onCreateWindows(inflater,null);
		
		return variablesInfo;
	}
	
	public void onCreateWindows(LayoutInflater inflater,ViewGroup container)
	{
		variablesInfo=(FrameLayout)inflater.inflate(R.layout.frag_var,container,false);
		
		varGrid=(MyVariableGrid)variablesInfo.findViewById(R.id.myVarSurface);
		pathPoints=(ListView)variablesInfo.findViewById(R.id.pathSelected);
		
		ArrayList<Point3D>pList=new ArrayList<Point3D>();
	//	AuxAdapterPoints adapterPoints=new AuxAdapterPoints(activity,pList);
	//	pathPoints.setAdapter(adapterPoints);
		
		partSelectedName=(TextView)variablesInfo.findViewById(R.id.psele);
		varGrid.setAdapter(new AuxAdapterVariables(activity,varGrid.getVariables()));
		varGrid.setOnItemLongClickListener(varGrid);
		varGrid.setOnItemClickListener(varGrid);
	}
	

	
	public MyVariableGrid getVarGrid() {
		return varGrid;
	}

	
	public ListView getPathPoints() {
		return pathPoints;
	}

	
	public TextView getPartSelectedName() {
		return partSelectedName;
	}
	
	

	
	
	
	
}
