package com.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public abstract class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener

{

	
	SurfaceHolder holder;
	protected MySurfaceThread thread;
	protected GestureDetectorCompat gestureDetector;
	protected Context context;
	private boolean onPause=false;
	protected boolean touchEnabled=true;
	
	public MySurfaceView(Context context) {	
		super(context);	
		this.context=context;
		
		init();
	}
	
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
		
	}
	
	private void init()
	{
		getHolder().addCallback(this);
		thread=new MySurfaceThread(getHolder(),this);
		gestureDetector=new GestureDetectorCompat(context,this);
		
		
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height) {
		
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		thread.setDestroying(false);
		thread.setRunning(true);
		thread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) 
	{
		// TODO Auto-generated method stub
		boolean retry = true;
		  thread.setDestroying(true);
		  while (retry) {
		   try {
		    thread.join();
		    retry = false;
		   }
		   catch (InterruptedException e) {
		   }
		  }
	}
	
	 protected abstract void onDraw(Canvas canvas);
	 
	 public void onPause()
	    {
	    	thread.setRunning(false);
	    	onPause=true;
	    }
	    
	 public void onResume()
	    {
	    	thread.setRunning(true);
	    	onPause=false;
	    }

	public boolean isOnPause() {
		return onPause;
	}

	public void enableTouchEvent(boolean touchEnabled)
	{
		this.touchEnabled=touchEnabled;
	}
}
