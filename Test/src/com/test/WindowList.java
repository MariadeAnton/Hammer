package com.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WindowList extends FrameLayout {

	static final int MODE_PATH=0;
	static final int MODE_POINT=1;
	
	private TextView tittle;
	private ListView listPath;
	private LinearLayout tittlePoints,tittlePath;
	private boolean showList=false;
	
	
	public WindowList(Context context) {
		super(context);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}

	
	

	public WindowList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}




	public WindowList(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}




	public void onCreate(LayoutInflater inflater) {
		
		isInEditMode();
		LinearLayout windowLayout=(LinearLayout)inflater.inflate(R.layout.list_points,null);
		
		tittle=(TextView)windowLayout.findViewById(R.id.tittle);
		listPath=(ListView)windowLayout.findViewById(R.id.list);
		tittlePoints=(LinearLayout)windowLayout.findViewById(R.id.subtittle);
		tittlePath=(LinearLayout)windowLayout.findViewById(R.id.tittlePath);
		tittlePath.setVisibility(View.GONE);
		addView(windowLayout);
}




	public void setTittle(String name) {
		tittle.setText(name);
	}


	public ListView getListPath() {
		return listPath;
	}
	
	public void setListPath(ListView list)
	{
		listPath=list;
		
	}
	
	public void setMode(int mode)
	{
		if(mode==0)
		{
			tittlePath.setVisibility(View.VISIBLE);
			tittlePoints.setVisibility(View.GONE);
		}
		else if(mode==1)
		{
			tittlePath.setVisibility(View.GONE);
			tittlePoints.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void showList(boolean show)
	{
		showList=show;
		if(show)listPath.setVisibility(View.GONE);
		else listPath.setVisibility(View.VISIBLE);
	}
	
	
		
	
	
	public boolean isShowingList()
	{
		return showList;
	}
	
	
	
}