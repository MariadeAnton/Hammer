package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Widget_ListView_Adapter extends BaseAdapter {

	protected Activity activity;
    protected ArrayList<Widget_ListView_Item> items;
    protected Widget_ListView parent;
 
    public Widget_ListView_Adapter(Activity activity, ArrayList<Widget_ListView_Item> items,Widget_ListView parent) {
        this.activity = activity;
        this.items = items;
        this.parent=parent;
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
            v = inf.inflate(R.layout.widget_list_adapter, null);
        }
 
  
        Widget_ListView_Item it = items.get(position);

        FrameLayout picture = (FrameLayout) v.findViewById(R.id.widgetImage);
       
        
        if(it.getPicture()!=null)
        {
        	ImageView image=new ImageView(picture.getContext());
        	image.setBackground(it.getPicture());
        	image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        	picture.addView(image);
        }
        	
        	
        	
        else if(it.hasIndex())
        {
        	TextView indexNumber=new TextView(picture.getContext());
        	indexNumber.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        	indexNumber.setGravity(Gravity.CENTER);
        	indexNumber.setTextColor(it.getColorIndex());
        	indexNumber.setText(String.valueOf(items.indexOf(it)));
        	picture.setBackground(it.getIndexBackground());
        	picture.addView(indexNumber);
        }
        	
        	
        else picture.setVisibility(View.GONE);
 
        LinearLayout nameLayout=(LinearLayout)v.findViewById(R.id.nameLayout);
        nameLayout.removeAllViews();
       
        LinearLayout infoLayout=(LinearLayout)v.findViewById(R.id.infoLayout);
        infoLayout.removeAllViews();
       
        
        for(int i=0;i<it.getName().length;i++)
        {
        	TextView name=new TextView(activity);
        	name.setGravity(Gravity.CENTER);
        	name.setText(it.getName()[i]);
        	name.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));	
        	nameLayout.addView(name);
        		
        	
        	if(it.getInfo()!=null)
        	{
        		TextView info=new TextView(activity);
        		info.setGravity(Gravity.CENTER);
        		info.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));
        		info.setText(it.getInfo()[i]);
        		info.setTextSize(10);
        		info.setTextColor(Color.GRAY);
        		infoLayout.addView(info);
        		
        	}
        	
        	else infoLayout.setVisibility(View.GONE);
        	
        	
        	
        		
        }
      
          	
        if(it.isSelected())v.setBackground(v.getResources().getDrawable(R.color.m_orange_d1));  
        else v.setBackground(null);
       
        return v;
    }

    public void restoreBackgroundsItems()
    {
    	int size=getCount();
    	for(int i=0;i<size-1;i++)
    		getView(i,new View(activity),null).setBackground(null);
    }

	public Widget_ListView getParent() {
		return parent;
	}
 
}

