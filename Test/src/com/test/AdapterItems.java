package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterItems extends BaseAdapter {
	
		protected Activity activity;
	    protected ArrayList<ListItem> items;
	 
	    public AdapterItems(Activity activity, ArrayList<ListItem> items) {
	        this.activity = activity;
	        this.items = items;
	      }
	 
	    @Override
	    public int getCount() {
	        return items.size();
	    }
	 
	    @Override
	    public Object getItem(int arg0) {
	        return items.get(arg0);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return items.get(position).getId();
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	 
	        
	        View v = convertView;
	 
	     
	        if(convertView == null){
	            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inf.inflate(R.layout.item_list, null);
	        }
	 
	  
	        ListItem it = items.get(position);
	
	        ImageView picture = (ImageView) v.findViewById(R.id.fileImage);
	        picture.setImageDrawable(it.getPicture());
	 
	        TextView name = (TextView) v.findViewById(R.id.fileName);
	        name.setText(it.getName());
	      
	        TextView info = (TextView) v.findViewById(R.id.fileFolder);
	        info.setText(it.getInfo());
	 
	    
	        return v;
	    }

	 
}
