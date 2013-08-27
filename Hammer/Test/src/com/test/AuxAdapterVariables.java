package com.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AuxAdapterVariables extends BaseAdapter {

	
	protected Activity activity;
    protected ArrayList<Variable> variable;
    protected boolean blue=false;
 
    public AuxAdapterVariables(Activity activity, ArrayList<Variable> vars) {
        this.activity = activity;
        this.variable =vars;
      }
 
    @Override
    public int getCount() {
        return variable.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        return variable.get(arg0);
    }
 
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        
        View v = convertView;
 
     
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.variable, null);
        }
 
  
       Variable var = variable.get(position);

        TextView name = (TextView) v.findViewById(R.id.nameVar);
        name.setText(String.valueOf(var.getName()));
        TextView cname = (TextView) v.findViewById(R.id.cnameVar);
        cname.setText(String.valueOf(var.getName()));
        TextView value = (TextView) v.findViewById(R.id.valueVar);
        value.setText(String.valueOf(var.getValue()));
        
        name.setTextSize(value.getTextSize()*1.9f);
        
        
        if(position%2==0)
        	if(blue)
        		v.setBackgroundResource(R.drawable.m_blue);
        	else
        		v.setBackgroundResource(R.drawable.m_green);
        else
        	if(blue)
        		v.setBackgroundResource(R.drawable.m_skyblue);
        	else
        		v.setBackgroundResource(R.drawable.m_greenlight);
        	
        
 
       
        return v;
    }

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setBlue(boolean blue) {
		this.blue = blue;
	}
}
