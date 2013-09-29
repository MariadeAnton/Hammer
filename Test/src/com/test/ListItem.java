package com.test;

import android.graphics.drawable.Drawable;

public class ListItem {

	private Drawable picture;
	private String name;
	private String info;
	private long id;
	

	public ListItem(Drawable picture, String name, String info) {
		super();
		this.picture = picture;
		this.name = name;
		this.info = info;
	}
	
	public ListItem(Drawable picture, String name, String info,long id) {
		super();
		this.picture = picture;
		this.name = name;
		this.info = info;
		this.id=id;
	}
	
	
	public Drawable getPicture() {
		return picture;
	}
	public void setPicture(Drawable picture) {
		this.picture = picture;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
