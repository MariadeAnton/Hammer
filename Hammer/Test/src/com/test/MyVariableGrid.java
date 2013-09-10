package com.test;




import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;



public class MyVariableGrid extends GridView implements OnItemClickListener,OnItemLongClickListener
{

	private   ArrayList<Variable>variables=new ArrayList<Variable>();
	private  Context context;
	
	
	public MyVariableGrid (Context context) {	
		super(context);
		this .context=context;
		
		
	}
	
	public MyVariableGrid (Context context, AttributeSet attrs) {
		super(context, attrs);
		this .context=context;
		
	}

	
	
	

	


	public  Double searchVariable(String name)
	{
		for(int i=0;i<variables.size();i++)
			if(variables.get(i).getName().equals(name))
				return variables.get(i).getValue();


		if(isDouble(name))
			return Double.parseDouble(name);
		
		else
		{
			Toast toast=Toast.makeText(context,"Variable "+"'"+name+"'"+" doesn't exit", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show(); 
			return null;
		}
		
	}
	
	public  void changeVariableValue(String name, double value)
	{
		for(int i=0;i<variables.size();i++)
		{
			if(variables.get(i).getName().equals(name))
			{
				variables.get(i).setValue(value);
				return;
			}
		}

		Toast toast=Toast.makeText(context,"Variable "+"'"+name+"'"+" doesn't exit", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show(); 
		return;
		
	}
	
	private   boolean isDouble(String input ) {
	    try {
	        Double.parseDouble(input);
	        return true;
	    }
	    catch( NumberFormatException e ) {
	        return false;
	    }
	}
	
	public void createVariable(Variable variable)
	{
		variables.add(variable);
	}

	public  ArrayList<Variable> getVariables() {
		return variables;
	}

	@Override
	public boolean onItemLongClick( AdapterView<?> adapter, View view, int pos,long arg3) {
		
		final AdapterView<?> adapt=adapter;
		final int position=pos;
		MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("Do you want to erase this variable?")
		.setIcon(R.drawable.delwhite)
       .setTitle("ERASE VARIABLE "+"'"+((Variable) adapt.getItemAtPosition(pos)).getName()+"'")
       .setPositiveButton("Erase", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   variables.remove((Variable) adapt.getItemAtPosition(position));
        	   	AuxAdapterVariables aux=(AuxAdapterVariables)((MyVariableGrid)adapt).getAdapter();
        	   	aux.notifyDataSetChanged();
        	   	dialog.dismiss();
             
           }
       })
       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
              dialog.dismiss();
           }
       });


		// 3. Get the AlertDialog from create()
	
		Dialog dialog = builder.create();
		dialog.show();
       // TODO Auto-generated method stub// TODO Auto-generated method stub
		return false;
	}

@Override
public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
	
	final EditText newValue=new EditText(context);
	newValue.setRawInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL);
	final AuxAdapterVariables adapt=(AuxAdapterVariables)((MyVariableGrid)adapter).getAdapter();
	final TextView name= (TextView)view.findViewById(R.id.cnameVar);
	MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);

	// 2. Chain together various setter methods to set the dialog characteristics
	builder
	.setIcon(R.drawable.mod)
	.setMessage("New Value:")
   .setTitle("Change '"+name.getText().toString()+"' value")
   .setContentView(newValue)
   .setPositiveButton("Change", new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int id) {
    	   changeVariableValue(name.getText().toString(),Double.valueOf(newValue.getText().toString()));
    	  adapt.notifyDataSetChanged();
    		dialog.dismiss();
         
       }
   })
   .setNegativeButton("Don't change", new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int id) {
    	  
    		dialog.dismiss();
       }
   });


	// 3. Get the AlertDialog from create()

	Dialog dialog = builder.create();
	dialog.show();
	
	
}

public boolean saveVariables(ObjectOutputStream os)
{
	
	try {
		os.writeInt(variables.size());
		for(Variable var:variables) 	Serializer.writeVariable(os,var);		
		return true;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return false;
}

public boolean loadVariables(ObjectInputStream is)
{
	
	
	try {
	
	Variable var=null;
	removeVariables();
	int varNumber=is.readInt();
	for(int i=0;i<varNumber;i++)
	{
		var=Serializer.readVariable(is);
		variables.add(var);
	}
	
	
	return true;
	
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return false;
}

public void removeVariables()
{
	variables.removeAll(variables);
}
	

}