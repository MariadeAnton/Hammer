package com.test;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;

public class Window3D extends FrameLayout{

	

	
	static final int ROT_X=0;
	static final int ROT_Y=1;  
	
	private My3DView view3D;
	private Camera camera;
	private TextView tittle3D;
	private SimpleVector initialCamPos;
	private boolean showPar=false;
	
	
	
	
	
	public Window3D(Context context) {
		super(context);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}

	
	

	public Window3D(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}




	public Window3D(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}




	public void onCreate(LayoutInflater inflater) {
		
		
		FrameLayout windowLayout=(FrameLayout)inflater.inflate(R.layout.view3d_layout,null);
		
		view3D=(My3DView)windowLayout.findViewById(R.id.myView3D);
		view3D.setOnTouchListener(view3D);
		tittle3D=(TextView)windowLayout.findViewById(R.id.tittle3d);
		tittle3D.setVisibility(View.GONE);
		camera=view3D.getRenderer().getWorld().getCamera();
		initialCamPos=camera.getPosition();
		
		//horizontalSlider.setHorizontal();
		
		
		ImageButton defCam=(ImageButton)windowLayout.findViewById(R.id.defCam);
		ImageButton camParam=(ImageButton)windowLayout.findViewById(R.id.camParam);
		
		defCam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				camera.setPosition(0,-10,-50);
				camera.lookAt(new SimpleVector(0,0,0));
				
			}	
		});
		
		camParam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				showCamSliders(view);
				//view3D.setZoomMode(!view3D.getZoomMode());
				
			}	
		});
		
		addView(windowLayout);
		
	}



	

	public void setTittle(String tittle) {
		
		tittle3D.setVisibility(View.VISIBLE);
		tittle3D.setText(tittle);
		
	}
	
	
	
	public void showCamSliders(View button)
	{
		ImageButton b=(ImageButton)button;
	
			showPar=!showPar;	
			if(showPar)b.setImageResource(R.drawable.cam_act);
			else b.setImageResource(R.drawable.cam);
			view3D.setCameraMove(showPar);
			
	}
	public My3DView getView3D() {
			return view3D;
		}

		


	
	

}

