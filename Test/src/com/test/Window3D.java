package com.test;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;

public class Window3D extends FrameLayout{

	

	
	private My3DView view3D;
	private MyOrientableSeekBar verticalSlider;
	private MyHorizontalSeekBar horizontalSlider;
	private TextView tittle;
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
		verticalSlider=(MyOrientableSeekBar)windowLayout.findViewById(R.id.rotateX);
		horizontalSlider=(MyHorizontalSeekBar)windowLayout.findViewById(R.id.rotateY);
		tittle=(TextView)windowLayout.findViewById(R.id.tittle3d);
		tittle3D=(TextView)windowLayout.findViewById(R.id.tittle3d);
		
		tittle3D.setVisibility(View.GONE);
		verticalSlider.setVisibility(View.GONE);
		horizontalSlider.setVisibility(View.GONE);
		horizontalSlider.setOnSeekBarChangeListener(new MyHorizontalSlider());
		verticalSlider.setOnSeekBarChangeListener(new MyVerticalSlider());
		camera=view3D.getRenderer().getWorld().getCamera();
		initialCamPos=camera.getPosition();
		//horizontalSlider.setMax( (int) (2*(Math.abs(camera.getPosition().z))));
		//horizontalSlider.setProgress((int) Math.abs(camera.getPosition().z));
		horizontalSlider.setMax(360);
		horizontalSlider.setPreviousProgress(180);
		horizontalSlider.setProgress(180);
		//horizontalSlider.setHorizontal();
		verticalSlider.setMax(180);
		verticalSlider.setPreviousProgress(90);
		verticalSlider.setProgress(90);
		
		
		ImageButton defCam=(ImageButton)windowLayout.findViewById(R.id.defCam);
		ImageButton camParam=(ImageButton)windowLayout.findViewById(R.id.camParam);
		
		camParam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				showCamSliders(view);
				
			}	
		});
		
		addView(windowLayout);
		
		
	}



	

	public void setTittle(String tittle) {
		
		tittle3D.setVisibility(View.VISIBLE);
		this.tittle.setText(tittle);
		this.tittle.setVisibility(View.VISIBLE);
	}
	
	public void showCameraControls(boolean show)
	{
	if(show)
	{
		verticalSlider.setVisibility(View.VISIBLE);
		horizontalSlider.setVisibility(View.VISIBLE);
	}
	
	else
	{
		verticalSlider.setVisibility(View.GONE);
		horizontalSlider.setVisibility(View.GONE);
		
	}
	
	}
	
	public void showCamSliders(View button)
	{
			showPar=!showPar;
			showCameraControls(showPar);
	}
	
	
	
	
	class MyHorizontalSlider implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
		
			
			MyHorizontalSeekBar bar=(MyHorizontalSeekBar)seekbar;
		
			
			//if(bar.getPreviousProgress()<progress)degrees=1;
			//else degrees=-1;
			
		
			camera.moveCamera(Camera.CAMERA_MOVEIN,30);
			camera.rotateY((float) Math.toRadians(progress-bar.getPreviousProgress()));
			camera.moveCamera(Camera.CAMERA_MOVEOUT, 30);
			bar.setPreviousProgress(progress);
			camera.lookAt(new SimpleVector(0,0,0));
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class MyVerticalSlider implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			MyOrientableSeekBar bar=(MyOrientableSeekBar)seekbar;
			
			
			//if(bar.getPreviousProgress()<progress)degrees=1;
			//else degrees=-1;
			
			
			camera.moveCamera(Camera.CAMERA_MOVEIN, 30);
			camera.rotateX((float) Math.toRadians(progress-bar.getPreviousProgress()));
			camera.moveCamera(Camera.CAMERA_MOVEOUT, 30);
			bar.setPreviousProgress(progress);
			camera.lookAt(new SimpleVector(0,0,0));
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public My3DView getView3D() {
			return view3D;
		}

		
}

	public My3DView getView3D() {
		return view3D;
	}
	}

