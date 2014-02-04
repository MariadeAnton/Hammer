package com.test;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class FragmentRobotRight extends Fragment {

	
	private FrameLayout layout;
	private FrameLayout frameContent;
	private View content;
	private ButtonsImplementation implementation;
	
	
	
	
	
	public FragmentRobotRight(View cont) {
		super();
		content=cont;
	//	LayoutInflater inflater=(LayoutInflater)content.getContext().getSystemService
	//		      				(Context.LAYOUT_INFLATER_SERVICE);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout=(FrameLayout) inflater.inflate(R.layout.frag_act_robot_slide, null);// TODO Auto-generated method stub
		
		frameContent=(FrameLayout)layout.findViewById(R.id.slideFrame);
		
		/////////////Corrige el error que sale de que content ya tiene padre////////
		if(content.getParent()!=null) 
			((ViewGroup)content.getParent()).removeView(content);
		//////////////////////////////////////////////////////////////////////////////
		frameContent.addView(content);
		if(implementation!=null)implementation.setButtons();
		return layout;
	}

	interface  ButtonsImplementation
	{
		public void setButtons();
	}

	

	public void addContent(View v) {
		frameContent.addView(v);
	}

	public void setImplementation(ButtonsImplementation implementation) {
		this.implementation = implementation;
	}

	
	

}