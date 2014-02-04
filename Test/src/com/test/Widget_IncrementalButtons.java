package com.test;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Widget_IncrementalButtons extends FrameLayout {

	static final int PLUS=0;
	static final int SUST=1;
	static final int ROUND_RIGHT=2;
	static final int ROUND_LEFT=3;
	static final int ROUND_ALL=4;
	static final int ROUND_NOTHING=5;
	
	
	private Button plus,sustract;
	private TextView info;
	private TextView info2;
	private Handler mHandler;
	private ButtonPressed actionPressed;
	private Action mAction=new Action();
	private FrameLayout windowLayout;
	private LayoutInflater inflater;
	private int mode=-1;
	private boolean reverse;


	
	
	




	/**
	 * @param actionPressed the actionPressed to set
	 */
	public void setActionPressed(ButtonPressed actionPressed) {
		this.actionPressed = actionPressed;
	}




	public Widget_IncrementalButtons(Context context) {
		super(context);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	
	

	public Widget_IncrementalButtons(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}




	




	public Widget_IncrementalButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	private void onCreate(LayoutInflater inflater) {
		
		
		
			
			
		windowLayout=(FrameLayout)inflater.inflate(R.layout.incremental_buttons,null);
		
			
		plus=(Button)windowLayout.findViewById(R.id.plus);
		plus.setOnTouchListener(new MyIncrementalTouch());
		sustract=(Button)windowLayout.findViewById(R.id.sust);
		sustract.setOnTouchListener(new MyDecrementalTouch());
		info=(TextView)windowLayout.findViewById(R.id.info);
		info2=(TextView)windowLayout.findViewById(R.id.info2);
		
		if(reverse)
		{
			
			((ViewGroup)info2.getParent()).removeView(info2);
			((ViewGroup)plus.getParent()).removeView(plus);
			((ViewGroup)info.getParent()).removeView(info);
			((ViewGroup)sustract.getParent()).removeView(sustract);
			LinearLayout container=(LinearLayout)windowLayout.findViewById(R.id.container);
			
			container.addView(info2);
			container.addView(plus);
			container.addView(info);
			container.addView(sustract);
		}
		
		addView(windowLayout);
		
	}
	
	public void setInfoText(String info)
	{
		this.info.setText(info);
	}
	
	public void setInfo2Text(String info2)
	{
		if(info2==null)
			this.info2.setVisibility(View.GONE);
		else
			this.info2.setText(info2);
	}
	
	public void roundButton(int mode,int radio)
	{
		switch(mode)
		{
		
		case ROUND_RIGHT:
			if(reverse)
				ActivityRobot.changeCornersButtons(sustract,  0, radio, radio, 0);
			else
				ActivityRobot.changeCornersButtons(plus,  0, radio, radio, 0);
			break;
		case ROUND_LEFT:
			if(reverse)
				ActivityRobot.changeCornersButtons(plus, radio, 0, 0, radio);
			else
				ActivityRobot.changeCornersButtons(sustract, radio, 0, 0, radio);
			break;
		case ROUND_ALL:
			ActivityRobot.changeCornersButtons(plus, 0, radio, radio, 0);
			ActivityRobot.changeCornersButtons(sustract,radio, 0, 0, radio);
			
			break;
		case ROUND_NOTHING:
			break;
		}
	}
	
	

	

class MyIncrementalTouch implements OnTouchListener
{

	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mHandler != null) return true;
            mHandler = new Handler();
            mAction.setMode(PLUS);
            mHandler.postDelayed(mAction, 10);
            break;
        case MotionEvent.ACTION_UP:
            if (mHandler == null) return true;
            mHandler.removeCallbacks(mAction);
            mHandler = null;
            break;
        }
        return false;
    }

	
}

class MyDecrementalTouch implements OnTouchListener
{

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mHandler != null) return true;
            mHandler = new Handler();
            mAction.setMode(SUST);
            mHandler.postDelayed(mAction, 10);
            break;
        case MotionEvent.ACTION_UP:
            if (mHandler == null) return true;
            mHandler.removeCallbacks(mAction);
            mHandler = null;
            break;
        }
        return false;
    }

	
}

class Action implements Runnable
{

	private int mode;
	@Override
	public void run() {
		switch(mode)
		{
		case PLUS:
			if(actionPressed!=null)actionPressed.increaseValue();
			break;
		case SUST:
			if(actionPressed!=null)actionPressed.decreaseValue();
			break;
		}
		
		mHandler.post(this);
		
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
}

interface ButtonPressed

{
	
	public void increaseValue();
	public void decreaseValue();
}

public void create(boolean reverse)
{
	this.reverse=reverse;
	onCreate(inflater);
}







}