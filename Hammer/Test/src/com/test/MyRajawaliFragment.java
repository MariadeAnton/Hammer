package com.test;




import java.util.ArrayList;

import rajawali.RajawaliFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.AuxBasicVariables.TriplePoint;




public class MyRajawaliFragment extends  RajawaliFragment implements OnTouchListener {

	private MyRajawaliRenderer glRajSurface;

	ListView listPath;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		glRajSurface=new MyRajawaliRenderer(getActivity());
		glRajSurface.setSurfaceView(mSurfaceView);
		mSurfaceView.setOnTouchListener(this);
		setRenderer(glRajSurface);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mLayout = (FrameLayout) inflater.inflate(R.layout.rajawali,
				container, false);
		mLayout.addView(mSurfaceView);
		
		
		

		return mLayout;
	}
	

	
	public void reLoad(HammerEnvironment env)
	{
		glRajSurface.setReload(env);
	
	}

	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				// this needs to be defined on the renderer:
				glRajSurface.getObjectAt(event.getX(), event.getY());
				
				
				////Wait to execute onObjectPicked////
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/////////////////////////////////////
				AuxPiece ap=glRajSurface.getObjectSelected();
				if(ap==null)return false;
					if(ap.isTouched())
					{
						ArrayList<TriplePoint>piecePath=new ArrayList<TriplePoint>();
						TextView text=(TextView)getActivity().findViewById(R.id.psele);
						if(text!=null)text.setText(ap.getName());
						for(int i=0;i<ap.getPath().size();i++)
							piecePath.add(ap.getPath().get(i));
						listPath.setAdapter(new AuxAdapterPoints(getActivity(),piecePath));
					}
				
						
				}
		
			return true;
		}// TODO Auto-generated method stub

	

		public MyRajawaliRenderer getGlRajSurface() {
			return glRajSurface;
		}

		public ListView getListPath() {
			return listPath;
		}

		public void setListPath(ListView adapterPath) {
			listPath = adapterPath;
		}

	
		
	}
	
	
	

