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
import android.widget.TextView;



public class FragmentCreateVariable extends Fragment {
	

	Activity activity;
	LinearLayout layout;
	private Button accept;
	private Button cancel;
	private Button load;
	
	private TextView name,cname,valueVar;
	private EditText vname,vvalue;
	private MyVariableGrid varGrid;

	
	public FragmentCreateVariable(Activity act,MyVariableGrid grid) {
		
	
		varGrid=grid;
		activity=act;
		
	
	
		}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		onCreateButtons(inflater,null);

		return layout;

	}


	
	private void onCreateButtons(LayoutInflater inflater,ViewGroup container)
	{
		
		layout=(LinearLayout)inflater.inflate(R.layout.params_variable,container);
		load=(Button)layout.findViewById(R.id.button3);
		name=(TextView)layout.findViewById(R.id.nameVar);
		
		cname=(TextView)layout.findViewById(R.id.cnameVar);
		name.setTextSize((float) (1.9*cname.getTextSize()));

		valueVar=(TextView)layout.findViewById(R.id.valueVar);
		vname=(EditText)layout.findViewById(R.id.vName);
		vvalue=(EditText)layout.findViewById(R.id.vValue);
		accept=(Button)layout.findViewById(R.id.plus);
		cancel=(Button)layout.findViewById(R.id.sust);
		accept.setOnClickListener(new MyClickListener());
		cancel.setOnClickListener(new MyClickListener());
		load.setOnClickListener(new MyClickListener());
			
		
	}
	

	
	class MyClickListener implements View.OnClickListener
	{

		

		@Override
		public void onClick(View view) {

			if(view.equals(accept))
			{
				
				Variable variable=new Variable();
				
			
				if(cname.getText().length()>0)variable.setName(cname.getText().toString());
				else variable.setName("¿?");
				
				if(valueVar.getText().length()>0 && valueVar.getText().toString().compareTo("¿?")!=0)variable.setValue(Double.valueOf(valueVar.getText().toString()));
				else variable.setValue(0);
				
				varGrid.createVariable(variable);
				
				((AuxAdapterVariables)varGrid.getAdapter()).notifyDataSetChanged();
			
				((ActivityScratching) activity).restoreButtons();
		 		
			}
			
			else if(view.equals(load))
			{
				
				if(vname.getText().length()>0)name.setText(vname.getText().toString());
				else name.setText("¿?");
				
				cname.setText(name.getText().toString());
				if(vvalue!=null)valueVar.setText(vvalue.getText().toString());
				else valueVar.setText("¿?");
				
				vname.getText().clear();
				vvalue.getText().clear();
			}
			
			
			
		
		}
		
	}
	
	



	
	

	
	

}
