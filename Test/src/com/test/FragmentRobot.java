package com.test;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.test.FragmentRobotRight.ButtonsImplementation;

public class FragmentRobot extends Fragment {

	private LinearLayout layout;
	public Window3D window;
	public Widget_IncrementalButtons wQ1,wQ2,wQ3,wX,wY,wZ;
	public Button rButton;
	private ButtonsImplementation implementation;
	
	



	public void setImplementation(ButtonsImplementation implementation) {
		this.implementation = implementation;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		layout=(LinearLayout) inflater.inflate(R.layout.frag_robot, null);// TODO Auto-generated method stub
		
		window=(Window3D)layout.findViewById(R.id.robotFrame);
		
		
		wQ1=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetQ1);
		wQ1.create(false);
	
		
		wQ2=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetQ2);
		wQ2.create(false);
	
		
		wQ3=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetQ3);
		wQ3.create(false);
		
		
		wX=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetX);
		wX.create(true);
		
		
		wY=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetY);
		wY.create(true);
		
		
		wZ=(Widget_IncrementalButtons)layout.findViewById(R.id.widgetZ);
		wZ.create(true);
		
		
		rButton=(Button)layout.findViewById(R.id.rButton);
		if(implementation!=null)implementation.setButtons();
		
		return layout;
	}

	

}
