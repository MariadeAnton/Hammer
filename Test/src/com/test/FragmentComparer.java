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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class FragmentComparer extends Fragment {

	
	private Activity activity;
	private LinearLayout layout;
	private LinearLayout comparerValue1;
	private LinearLayout comparerValue2;
	private ListView listVar;
	private ListView listVar2;
	private MyVariableGrid varGrid;
	private ButtonComparer comparer=null;
	private ButtonOperator operator=null;
	private Button accept;
	private Button cancel;
	private TextView oper;
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	
	
	
		public FragmentComparer(MyVariableGrid grid,ButtonComparer obj) {
		
		comparer=obj;
		varGrid=grid;
		activity=obj.getActivity();
		// TODO Auto-generated constructor stub
		}
	
	
		public FragmentComparer(MyVariableGrid grid,ButtonOperator obj) {
			
			operator=obj;
			varGrid=grid;
			activity=obj.getActivity();
			// TODO Auto-generated constructor stub
			}
		
		
			
		


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
			onCreateButtons(inflater,null);
	
		return layout;

		}
	
	
		private void onCreateButtons(LayoutInflater inflater,ViewGroup container)
		{
			layout=(LinearLayout)inflater.inflate(R.layout.params_comparer,container);// TODO Auto-generated method stub
			
			comparerValue1=(LinearLayout)layout.findViewById(R.id.v1);		
			comparerValue2=(LinearLayout)layout.findViewById(R.id.v2);
			
			listVar=(ListView)layout.findViewById(R.id.listVar1);
			listVar2=(ListView)layout.findViewById(R.id.listVar2);
			AuxAdapterVariables adapterVar=new AuxAdapterVariables(activity,varGrid.getVariables());
			adapterVar.setBlue(true);
			listVar.setAdapter(adapterVar);
			listVar2.setAdapter(adapterVar);
			listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listVar2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listVar.setOnItemClickListener(new MyParamClickListener(comparerValue1));
			listVar2.setOnItemClickListener(new MyParamClickListener(comparerValue2));
			listVar.setSelector(R.drawable.bg_key);
			listVar2.setSelector(R.drawable.bg_key);
			
			oper=(TextView)layout.findViewById(R.id.oper);
			if(comparer!=null)oper.setText(comparer.getName());
			else oper.setText(operator.getName());
			
			((TextView)comparerValue1.findViewById(R.id.nameVar)).setTextSize
			(((TextView)comparerValue1.findViewById(R.id.valueVar)).getTextSize()*1.9f);
			
			((TextView)comparerValue2.findViewById(R.id.nameVar)).setTextSize
			(((TextView)comparerValue2.findViewById(R.id.valueVar)).getTextSize()*1.9f);
			
			accept=(Button)layout.findViewById(R.id.plus);
			cancel=(Button)layout.findViewById(R.id.sust);
			
			accept.setOnClickListener(new MyClickListener());
			cancel.setOnClickListener(new MyClickListener());
				
			
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
		
		class MyClickListener implements View.OnClickListener
		{

			

			@Override
			public void onClick(View view) {

				if(view.equals(accept))
				{
				TextView par;
				par=(TextView)comparerValue1.findViewById(R.id.cnameVar);
				if(comparer!=null)
					comparer.setvA(par.getText().toString());
				else
					operator.setValueX(par.getText().toString());
				par=(TextView)comparerValue2.findViewById(R.id.cnameVar);
				
				if(comparer!=null)
					comparer.setvB(par.getText().toString());
				else
					operator.setValueY(par.getText().toString());
				
				
				}
				
				if(comparer!=null)comparer.setModificate(false);
				else operator.setModificate(false);
				((ActivityScratching) activity).restoreButtons();
			}
			
		}
		
	
			
		
		public void onSetComparerParams(View view)
		{
			
			
		}
		
		public void onCancelComparer(View view)
		{
			
		}
}
