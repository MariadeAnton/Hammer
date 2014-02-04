package com.test;


import android.graphics.drawable.Drawable;


public class Widget_ListView_Item {

	private Drawable picture;
	private String[] name;
	private String[] info;
	private int indexTextColor=-1;
	private Drawable indexBackground;
	static private long id=0;
	private boolean selected=false;
	private boolean index=false;
	

	public Widget_ListView_Item(Drawable picture, String[] name, String[] info) {
		
		super();
		this.picture = picture;
		this.name=name;
		this.info=info;
		id++;
		
		
	}
	
public Widget_ListView_Item(int indexTextColor,Drawable indexBackground, String[] name, String[] info) {
		
		super();
		this.name=name;
		this.info=info;
		this.indexTextColor=indexTextColor;
		this.indexBackground=indexBackground;
		index=true;
		id++;
		
	}
	
	public Widget_ListView_Item(Drawable picture, String name, String info) {
		
		super();
		this.picture = picture;
		this.name=new String[1];
		this.name[0]=name;
		this.info=new String[1];
		this.info[0]=info;
		
	}
	
	
	
	public Drawable getPicture() {
		return picture;
	}
	public void setPicture(Drawable picture) {
		this.picture = picture;
	}
	public String[] getName() {
		return name;
	}
	public void setName(String[] name) {
		this.name = name;
	}
	public String[] getInfo() {
		return info;
	}
	public void setInfo(String[] info) {
		this.info = info;
	}

	public long getId() {
		return id;
	}

	

	public boolean hasIndex() {
		return index;
		
	}

	public int getColorIndex() {
		// TODO Auto-generated method stub
		return indexTextColor;
	}

	public Drawable getIndexBackground() {
		// TODO Auto-generated method stub
		return indexBackground;
	}
	
	public void setSelected(boolean sel)
	{
		selected=sel;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	


}
