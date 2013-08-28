package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityProgram extends Activity {
	
	
	static String programFolder="Programs/";
	
	private MyRajawaliFragment rajFragment;
	private MyVariableGrid var;
	private MySchemaSurface schema;
	private ListView list,pathList,pointsList;
	private ArrayList<ListItem> aList;
	private AdapterItems adapter;
	private ListItem itemSelected;
	private Activity activity=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ListItem item;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program);
		setTitle("Create Program");
		activity=this;
		var=(MyVariableGrid)findViewById(R.id.infoVariables);
		list=(ListView)findViewById(R.id.listProgram);
		pointsList=(ListView)findViewById(R.id.pointsLoaded);
		pathList=(ListView)findViewById(R.id.pathLoaded);
		rajFragment=(MyRajawaliFragment) getFragmentManager().findFragmentById(R.id.rajFragment);
		schema=(MySchemaSurface)findViewById(R.id.glEnvironment);
		schema.enableTouchEvent(false);
		schema.setScale(0.8f,0.5f);
		if(GeneralParameters.getEnvironment()!=null)
		pointsList.setAdapter(new AuxAdapterPoints(this,GeneralParameters.getEnvironment().getPoints()));
		
		rajFragment.setListPath(pathList);
		aList=new ArrayList<ListItem>();
		adapter=new AdapterItems(this,aList);
		
		
		
		
		File yourDir =new File( Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+programFolder);
		
		for (File f : yourDir.listFiles())
		{
			if (f.isFile())
			{
				item=new ListItem(getResources().getDrawable(R.drawable.hammerfile),f.getName(),yourDir.getAbsolutePath());
				aList.add(item);
			}	
			
       
		}
			list.setAdapter(adapter);
			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			list.setOnItemClickListener(new MyClickListener());
		//	list.setBackgroundColor(R.drawable.bg_key);
			list.setSelector(R.drawable.bg_key);
	}

	class MyClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {

			view.setSelected(true);
			schema.clearSurface();
			schema.onResume();
			itemSelected=aList.get(position);
		
			
			
			try {
				FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().toString()+MainActivity.applicationFolder+
						ActivityProgram.programFolder+itemSelected.getName());
				ObjectInputStream is = new ObjectInputStream(fis);
				schema.loadObjectsList(is);
				var.loadVariables(is);
				AuxAdapterVariables adapterVar=new AuxAdapterVariables(activity,var.getVariables());
				var.setAdapter(adapterVar);
				adapter.notifyDataSetChanged();
				is.close();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
			
	}
	
}
	
	public void newProgram(View view)
	{
		Intent i=new Intent(this,ActivityScratch.class);
		startActivity(i);
		var.removeVariables();
		finish();
		
	}
	
	
	public void loadProgram(View view)
	{
		if(itemSelected==null)		
		{
			Toast toast=Toast.makeText(this, "Select a program to load",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return;
		}
		Intent i=new Intent(this,ActivityScratch.class);
		i.putExtra("loadprogram",itemSelected.getName());
		startActivity(i);
		finish();
	
	}


	@Override
	public void onBackPressed() {
		
		var.removeVariables();
		finish();
	}
}
	
	

