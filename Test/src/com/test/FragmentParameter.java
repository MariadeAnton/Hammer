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

public class FragmentParameter extends Fragment {
	
	EditText par;
	Activity activity;
	LinearLayout layout;
	private Button accept;
	private Button cancel;
	private PositionableObject pos;

	
public FragmentParameter(PositionableObject obj) {
		
		pos=obj;
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
		
		layout=(LinearLayout)inflater.inflate(R.layout.params_value,container);
		par=(EditText)layout.findViewById(R.id.paramValue);
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
				if(par.getText().length()>0)
		 			pos.setParameter(Double.parseDouble(par.getText().toString()));
		 		par.getText().clear();
		 		
			}
			else 
			{
				par.getText().clear();
			}
			
			pos.setModificate(false);
			((ActivityScratching) activity).restoreButtons();
				
		}
		
	}
	

		
	
	
}

