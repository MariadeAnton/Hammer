package com.test;



import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentSelectVar extends Fragment{

	
	
	TextView par;
	Activity activity;
	LinearLayout layout;
	private Button accept;
	private Button cancel;
	private LinearLayout selVar;
	private ListView listVar;
	private MyVariableGrid varGrid;
	private ButtonVariable variable;

	public FragmentSelectVar(MyVariableGrid grid,ButtonVariable obj) {
		
		variable=obj;
		varGrid=grid;
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
		
		layout=(LinearLayout)inflater.inflate(R.layout.params_sel_variable,container);
		selVar=(LinearLayout)layout.findViewById(R.id.selVar);
		listVar=(ListView)layout.findViewById(R.id.listSelVar);
		AuxAdapterVariables adapterVar=new AuxAdapterVariables(activity,varGrid.getVariables());
		adapterVar.setBlue(true);
		listVar.setAdapter(adapterVar);
		listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listVar.setOnItemClickListener(new MyParamClickListener(selVar));	
		listVar.setSelector(R.drawable.bg_key);
		((TextView)selVar.findViewById(R.id.nameVar)).setTextSize
		(((TextView)selVar.findViewById(R.id.valueVar)).getTextSize()*1.9f);
		
		
		
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
				
				par=(TextView)selVar.findViewById(R.id.cnameVar);
				variable.setNameVar(par.getText().toString());
				variable.setParameter(varGrid.searchVariable(par.getText().toString()));		
		 		
			}
			
			variable.setModificate(false);	
			((ActivityScratching) activity).restoreButtons();
			
				
		}
		
	}
	
	class MyParamClickListener implements OnItemClickListener
	{

		private LinearLayout lay;
		public MyParamClickListener(LinearLayout lay) {
			this.lay = lay;
		}
		
		@Override
		public void onItemClick(AdapterView<?> it, View view, int position,long arg3) {
			
		
	
			Variable var=varGrid.getVariables().get(position);
			TextView v1;
			v1=(TextView)lay.findViewById(R.id.nameVar);
			v1.setText(var.getName());
			v1=(TextView)lay.findViewById(R.id.cnameVar);
			v1.setText(var.getName());	
			v1=(TextView)lay.findViewById(R.id.valueVar);
			v1.setText(String.valueOf(var.getValue()));		
		}	
		
		
	}
	

		
	
	
	
	

	
	
	
}
