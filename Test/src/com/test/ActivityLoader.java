package com.test;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ActivityLoader extends Activity {

	static String loaderFolder="Objects3D/";
	
	private ListView list3D;
	private ArrayList<ListItem> itemList3D;
	private AdapterItems adapter3D;
	private My3DView view3D;
	

	HammerEnvironment environment=null;
	Context context;
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model3d_loader);
		setTitle("Load 3d Objects");
		
		context=this;
		
		
	      
	      
	     
	
	    view3D=(My3DView)findViewById(R.id.view3d);
	    view3D.setOnTouchListener(view3D);
		//gameView=initializeForView(new libgdx(),cfg);
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
			String nam=itemList3D.get(position).getName().substring(0,itemList3D.get(position).getName().length()-4);
			
			ArrayList<AuxPiece>pieces=new ArrayList<AuxPiece>();
			AuxPiece piece=new AuxPiece(My3DView.loadModel(nam,1));
			piece.setName(nam);
			pieces.add(piece);
			environment.setPieces(pieces);
			view3D.getRenderer().loadEnvironment(environment);
		//	environment.setRenderer(glRajawaliFragment.getGlRajSurface());

			
		//	glRajawaliFragment.reLoad(environment);
		//	glRajawaliFragment.setListPath(new ListView(context));
			
		
		
		}
	
	
	}
}
