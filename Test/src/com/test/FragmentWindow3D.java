package com.test;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentWindow3D extends Fragment {
	
	
	private Window3D window3D;;
	
	

	public FragmentWindow3D() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FragmentWindow3D(Activity activity) {
		window3D=new Window3D(activity);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
				
		return window3D;
	}

	/**
	 * @return the window3D
	 */
	public Window3D getWindow3D() {
		return window3D;
	}
	
	
	

}
