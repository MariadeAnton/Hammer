package com.test;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MySurfaceThread extends Thread {


	 private SurfaceHolder myThreadSurfaceHolder;
	 private MySurfaceView myThreadSurfaceView;
	 private boolean myThreadRun = false;
	 private boolean isDestroying = false;
	 
	 public MySurfaceThread(SurfaceHolder surfaceHolder, MySurfaceView surfaceView)
	 {
	  
		 myThreadSurfaceHolder = surfaceHolder;
		 myThreadSurfaceView = surfaceView;
	 }
	 
	 public void setRunning(boolean b) {
	  myThreadRun = b;
	 }
	 
	 public void setDestroying(boolean b) {
		  isDestroying = b;
		 }

	 @Override
	 public void run() {
	  // TODO Auto-generated method stub
	  
	while(!isDestroying)
	{
		if(myThreadRun){
			Canvas c = null;
			try{
				c = myThreadSurfaceHolder.lockCanvas(null);   
				synchronized (myThreadSurfaceHolder){
					if(c!=null)
						myThreadSurfaceView.onDraw(c);
				}
			}
			finally{
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) 	
					myThreadSurfaceHolder.unlockCanvasAndPost(c);
	   
			}
		}
		}

 }



}
