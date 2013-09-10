package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityLoader extends Activity {

	static String loaderFolder="Objects3D/";
	
	private ListView list3D;
	private ArrayList<ListItem> itemList3D;
	private AdapterItems adapter3D;
	private MyRajawaliFragment glRajawaliFragment;
	private ListItem itemSelected;
	private FragmentManager fragmentManager;
	HammerEnvironment environment=null;
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model3d_loader);
		setTitle("Load 3d Objects");
		fragmentManager = getFragmentManager();

		glRajawaliFragment=(MyRajawaliFragment)fragmentManager.findFragmentById(R.id.loaderfrag);
		
		
		list3D=(ListView)findViewById(R.id.list3dobj);
		
	
	
		
		
		itemList3D=new ArrayList<ListItem>();
		adapter3D=new AdapterItems(this,itemList3D);
	
	
		
		list3D.setAdapter(adapter3D);
		list3D.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list3D.setOnItemClickListener(new MyClickListener());
		list3D.setSelector(R.drawable.bg_key);
	
	
	
	
		File yourDir =new File( Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+loaderFolder);
	
		for (File f : yourDir.listFiles())
			if (f.isFile())
				itemList3D.add(new ListItem(getResources().getDrawable(R.drawable.xmlfile),f.getName(),yourDir.getAbsolutePath()));

		adapter3D.notifyDataSetChanged();

}
	
	
	class MyClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {
			
			
			environment=new HammerEnvironment();
			itemSelected=itemList3D.get(position);
			
			try {
				
				environment.readXMLEnvironment(Environment.getExternalStorageDirectory()+MainActivity.applicationFolder
						+loaderFolder+itemSelected.getName());
				
				
				glRajawaliFragment.reLoad(environment);
				
				

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
