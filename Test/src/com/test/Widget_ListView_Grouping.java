package com.test;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;


public class Widget_ListView_Grouping extends Widget_ListView {

	private ArrayList<PointsSet> sets=new ArrayList<PointsSet>();
	
	public Widget_ListView_Grouping(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Widget_ListView_Grouping(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Widget_ListView_Grouping(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void addSet(String name,ArrayList<Widget_ListView_Item> items)
	{
		addItem(new Widget_ListView_Item(null,name,"Composed of "+String.valueOf(items.size())+" points"));
		sets.add(new PointsSet(name,items));
	}
	
	
	
	public ArrayList<PointsSet> getPathSet()
	{
		return sets;
	}
	
	
}

class PointsSet
{
	public String name="";
	public ArrayList<Widget_ListView_Item> pathPoints=new ArrayList<Widget_ListView_Item>();
	
	
	
	public PointsSet() {
		super();
		// TODO Auto-generated constructor stub
	}



	public PointsSet(String name, ArrayList<Widget_ListView_Item> path) {
		super();
		this.name = name;
		this.pathPoints = path;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void addPoint(Widget_ListView_Item point) {
		pathPoints.add(point);
	}
	
	
	
	
}