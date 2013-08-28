package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.test.AuxBasicVariables.TriplePoint;







public class ActivityEnvironment extends Activity {
	
	
	static String environmentFolder="XML/";
	
	private MySchemaSurface schema;
	private ListView listXML,listPoints,listPath;
	private ArrayList<ListItem> xList;
	private ArrayList<TriplePoint>pList,pthList;
	private AdapterItems adapterXML;
	private MyRajawaliFragment glSurface;
	private AuxAdapterPoints adapterPoints,adapterPath;
	private ListItem itemSelected;
	private FragmentManager fragmentManager;
	HammerEnvironment environment=null;
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment);
		setTitle("Create Environment");
		fragmentManager = getFragmentManager();

		glSurface=(MyRajawaliFragment)fragmentManager.findFragmentById(R.id.rajFragment);
		
		
		listXML=(ListView)findViewById(R.id.listEnv);
		listPoints=(ListView)findViewById(R.id.listPoints);
		listPath=(ListView)findViewById(R.id.listPath);
		
		schema=new MySchemaSurface(this);
		schema.setScale(1.0f,0.5f);
	
		
		
		xList=new ArrayList<ListItem>();
		adapterXML=new AdapterItems(this,xList);
	
		pList=new ArrayList<TriplePoint>();
		adapterPoints=new AuxAdapterPoints(this,pList);
		pthList=new ArrayList<TriplePoint>();
		adapterPath=new AuxAdapterPoints(this,pthList);
		
		listXML.setAdapter(adapterXML);
		listXML.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listXML.setOnItemClickListener(new MyClickListener());
		listXML.setSelector(R.drawable.bg_key);
	
		
		listPoints.setAdapter(adapterPoints);
		listPoints.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		listPath.setAdapter(adapterPath);
		listPath.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		
		glSurface.setListPath(listPath);
	
	
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

class MyClickListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {
		
		
		environment=new HammerEnvironment();
		itemSelected=xList.get(position);
		
		try {
			
			environment.readXMLEnvironment(Environment.getExternalStorageDirectory()+MainActivity.applicationFolder
					+environmentFolder+itemSelected.getName());
			
			
			glSurface.reLoad(environment);
			for(TriplePoint p:environment.getPoints()) pList.add(p);
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

}
