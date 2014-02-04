package com.test;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.threed.jpct.SimpleVector;

public class FragmentInitialPos extends Fragment {

	
	
	EditText par,x,y,z;
	Activity activity;
	LinearLayout layout;
	private Button accept;
	private Button cancel;

	private ButtonRobot robot;

	
	public FragmentInitialPos(ButtonRobot obj) {
		
		robot=obj;
		activity=obj.activity;
	
	
		}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		onCreateButtons(inflater,null);

		return layout;

	}


	
	private void onCreateButtons(LayoutInflater inflater,ViewGroup container)
	{
		
		layout=(LinearLayout)inflater.inflate(R.layout.params_initial_pos,container);
		
		par=(EditText)layout.findViewById(R.id.speedValue);
		
		
 		x=(EditText)layout.findViewById(R.id.xipos);
 		y=(EditText)layout.findViewById(R.id.yipos);
 		z=(EditText)layout.findViewById(R.id.zipos);
		
		accept=(Button)layout.findViewById(R.id.plus);
		cancel=(Button)layout.findViewById(R.id.sust);
		accept.setOnClickListener(new MyClickListener());
		cancel.setOnClickListener(new MyClickListener());
			
		
	}
	

	
	class MyClickListener implements View.OnClickListener
	{

		

		@Override
		public void onClick(View view) {

			if(view.equals(accept))
			{
				
				double xv=0,yv=0,zv=0;
				if(par.getText().length()>0)
					robot.setParameter(Double.parseDouble(par.getText().toString()));
		 		
				if(x.getText().length()>0)
		 			xv=(float) Double.parseDouble(x.getText().toString());		
		 		if(y.getText().length()>0)
		 			yv= (float)Double.parseDouble(y.getText().toString());
		 		if(z.getText().length()>0)
		 			zv= (float)Double.parseDouble(z.getText().toString());
		 		
		 		robot.setPoint(new SimpleVector(xv,yv,zv));
		 		
		 		
		 		
			}
			
			par.getText().clear();
			x.getText().clear();
			y.getText().clear();
			z.getText().clear();
			
			robot.setModificate(false);	
			((ActivityScratching) activity).restoreButtons();
			
				
		}
		
	}
	
	
	
	
	
	
	
	
}
