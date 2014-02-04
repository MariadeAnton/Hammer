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

public class FragmentComparerConst extends Fragment
{
	
	
	private Activity activity;
	private LinearLayout layout;
	private LinearLayout comparerValue1;
	
	private ListView listVar;
	
	private MyVariableGrid varGrid;
	private ButtonComparer comparer=null;
	private ButtonOperator operator=null;
	private Button accept;
	private Button cancel;
	private EditText edit;
	private TextView par;
	private TextView oper;
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
				
	
	
		public FragmentComparerConst(MyVariableGrid grid,ButtonComparer obj) {
		
		comparer=obj;
		varGrid=grid;
		activity=obj.getActivity();
		// TODO Auto-generated constructor stub
		}
	
	
public FragmentComparerConst(MyVariableGrid grid,ButtonOperator obj) {
			
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
			layout=(LinearLayout)inflater.inflate(R.layout.params_comparer_constant,container);// TODO Auto-generated method stub
			
			edit=(EditText)layout.findViewById(R.id.constant);
			
			
			
			comparerValue1=(LinearLayout)layout.findViewById(R.id.v3);
			oper=(TextView)layout.findViewById(R.id.oper);
			if(comparer!=null)oper.setText(comparer.getName());
			else oper.setText(operator.getName());
			
			listVar=(ListView)layout.findViewById(R.id.listVar3);
			
			AuxAdapterVariables adapterVar=new AuxAdapterVariables(activity,varGrid.getVariables());
			adapterVar.setBlue(true);
			listVar.setAdapter(adapterVar);
		
			listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			listVar.setOnItemClickListener(new MyParamClickListener(comparerValue1));
			listVar.setSelector(R.drawable.bg_key);
			
			
	
			comparerValue1=(LinearLayout)layout.findViewById(R.id.v1);		
			
			
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
				v1.setTextSize((((TextView)lay.findViewById(R.id.valueVar)).getTextSize()*1.9f));
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
					par=(TextView)layout.findViewById(R.id.cnameVar);
					
					if(comparer!=null)
						comparer.setvA(par.getText().toString());
					else
						operator.setValueX(par.getText().toString());
					
					if(edit.getText().length()>0)
						{
							if(comparer!=null)
								comparer.setvB((edit.getText().toString()));
							else
								operator.setValueY((edit.getText().toString()));
								
						}
					else 
						{
						if(comparer!=null)
							comparer.setvB(new String("¿?"));
						else
							operator.setValueY(new String("¿?"));
						
						}
					
					
				
				}
				
				if(comparer!=null)comparer.setModificate(false);
				else operator.setModificate(false);
				((ActivityScratching) activity).restoreButtons();
			}
			
		}
		
	

		
	
	

}
