package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.test.AuxBasicVariables.TriplePoint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;





public class AuxAdapterPoints extends BaseAdapter {
	
	protected Activity activity;
    protected ArrayList<TriplePoint> points;
 
    public AuxAdapterPoints(Activity activity, ArrayList<TriplePoint> points) {
        this.activity = activity;
        this.points =points;
      }
 
    @Override
    public int getCount() {
        return points.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        return points.get(arg0);
    }
 
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        
        View v = convertView;
 
     
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.point_list, null);
        }
 
  
        TriplePoint pt = points.get(position);

        TextView number = (TextView) v.findViewById(R.id.number);
        number.setText(String.valueOf(position));
        
        TextView x = (TextView) v.findViewById(R.id.x);
        x.setText(String.valueOf(pt.x));
      
        TextView y = (TextView) v.findViewById(R.id.y);
        y.setText(String.valueOf(pt.y));
        
        TextView z = (TextView) v.findViewById(R.id.z);
        z.setText(String.valueOf(pt.z));
 
       
        return v;
    }

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
