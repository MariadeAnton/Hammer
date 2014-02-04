package com.test;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;



public class FragmentCanvas extends Fragment{

	
	private Thread canvasThread;
	private MySchemaSurface canvas;
	private FrameLayout canvasLay;
	

	public FragmentCanvas(Activity activity) {
		
		
		canvas=new MySchemaSurface(activity);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		onCreateButtons(inflater,null);
		
		return canvasLay;
	}
	
	
	private void onCreateButtons(LayoutInflater inflater,ViewGroup container)
	{
		
		
		canvasLay=(FrameLayout)inflater.inflate(R.layout.frag_canvas,container,false);
		((FrameLayout)canvasLay.findViewById(R.id.schema)).addView(canvas);
		canvas.setOnDragListener(new MyDragListener());
		
		
	}
	
	

	public class MyDragListener implements OnDragListener {
		
		
		  @SuppressLint("NewApi")
		@Override
		  public boolean onDrag(View v, DragEvent event) {
		    switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		    // Do nothing
		      break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		
		      break;
		    case DragEvent.ACTION_DRAG_EXITED:        
		     
		      break;
		    case DragEvent.ACTION_DROP:
		
		    	((MySchemaSurface)v).addDropDraw(event);
		    	break;
		
		    case DragEvent.ACTION_DRAG_ENDED:
		
		      default:
		      break;
		    }
		    return true;
		  }
		}

	/**
	 * @return the canvas
	 */
	
	
	
	
	public MySchemaSurface getCanvas() {
		return canvas;
	}
	
}
