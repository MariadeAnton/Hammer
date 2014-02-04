package com.test;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityModeling extends FrameLayout {

	private LinearLayout windowScratch;
	private WindowList windowPath;
	private Window3D window3D;
	private MySchemaSurface canvas;
	private TextView spaceVertical;
	private TextView spaceHorizontal;
	
	
	public ActivityModeling(Context context) {
		super(context);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
	}


	public ActivityModeling(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);}


	public ActivityModeling(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
		}


	
	
	
	
	
	private void onCreate(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		
		FrameLayout windowLayout=(FrameLayout)inflater.inflate(R.layout.modeling_layout,null);
		
		windowScratch=(LinearLayout)windowLayout.findViewById(R.id.windowScratch);
		windowPath=(WindowList)windowLayout.findViewById(R.id.windowPath);
		window3D=(Window3D)windowLayout.findViewById(R.id.modelingView);
		window3D.getView3D().getRenderer().loadEnvironment(GeneralParameters.getEnvironment());
		spaceVertical=(TextView)findViewById(R.id.vertSpace);
		spaceHorizontal=(TextView)findViewById(R.id.horSpace);	
		//windowScratch.setOnTouchListener(new MyExpandableTouch());
		windowPath.setOnTouchListener(new MyExpandableTouch());
		canvas=new MySchemaSurface(getContext());
		//canvas.onPause();
		canvas.setOnTouchListener(new MyExpandableTouch());
		windowScratch.addView(canvas);
		
		addView(windowLayout);
		
	}
	
	
	public class MyExpandableTouch implements OnTouchListener
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
	

			if(v instanceof WindowList)
				((WindowList)v).showList(!((WindowList)v).isShowingList());
			else
			{
				LinearLayout parent=(LinearLayout)v.getParent();
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        0, 4.0f);
				parent.setLayoutParams(param);
				
			}
			
			
			return false;
		}
		
	}
	
	public void setScratchView(MySchemaSurface surface)
	{
		;
		int length=surface.getDrawList().size();
		for(int i=0;i<length;i++)
			canvas.getDrawList().add(surface.getDrawList().get(i).copyFields(surface.getDrawList().get(i)));
		//canvas.onDraw(new Canvas());
	
		
	}
	
	public WindowList getPathView()
	{
		return windowPath;
	}


	public Window3D getWindow3D() {
		return window3D;
	}


	/**
	 * @param window3d the window3D to set
	 */
	public void setWindow3D(Window3D window3d) {
		window3D = window3d;
	}
	
	

}
