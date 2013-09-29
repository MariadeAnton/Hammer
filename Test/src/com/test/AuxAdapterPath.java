package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.AuxBasicVariables.Paths;
import com.test.AuxBasicVariables.Point3D;



public class AuxAdapterPath extends BaseAdapter {

	protected Activity activity;
    protected ArrayList<Paths> paths;
 
    public AuxAdapterPath(Activity activity, ArrayList<Paths> paths) {
        this.activity = activity;
        this.paths =paths;
      }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return paths.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return paths.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		View v = convertView;
		 
	     
	        if(convertView == null){
	            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inf.inflate(R.layout.adapter_string, null);
	        }
	 
	  
	        final  Paths pt = paths.get(position);

	        TextView number = (TextView) v.findViewById(R.id.paths);
	        number.setText(String.valueOf(position));
	        
	        TextView pathName = (TextView) v.findViewById(R.id.pathName);
	        pathName.setText(paths.get(position).getName());
	      
	        TextView pathPoints = (TextView) v.findViewById(R.id.pathPoints);
	        pathPoints.setText(String.valueOf(paths.get(position).getPathPoints().size()));
	        
	        
	 
	       
	        return v;
	}


	
	public ArrayList<Paths> getItemList()
	{
		return paths;
	}
}
