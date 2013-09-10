package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.test.AuxBasicVariables.TriplePoint;






public class ActivityScratch extends Activity {

	
	
	LayoutInflater inflater;
	private SlidingMenu menu; 
	private ListView listPath;
	
	private MySchemaSurface surface;
	private MyVariableGrid varGrid;
	private FrameLayout menuView,slidMenu;
	private LinearLayout dispatcher;
	private MyTouchableLayout mup,mdo,mle,tux,tuy,tuz;
	private MyTouchableLayout pressexe,repeat,ifclause,dowhile,cwhile;
	private MyTouchableLayout greater,less,less_equal,greater_equal,equal,
							 c_greater,c_less,c_less_equal,c_greater_equal,c_equal,always;
	private MyTouchableLayout add,sustract,multiply,divide,addOne,sustractOne,reasign,value
							,c_add,c_sustract,c_multiply,c_divide;
	private MyTouchableLayout sip,deburring;
	private MyTouchableLayout conf;
	private GridLayout l_move,l_control,l_op,l_us;
	private ScrollView l_comp,l_var;
	private Button c_move,c_control,c_comp,c_var,c_op,c_us;
	private MyRajawaliFragment glSurface3D;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Client client=null;
	private Handler handler;
	private Activity activity;


	
	
	//private TabHost tab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scratch);
		Bundle bundle=getIntent().getExtras();
		
	
		activity=this;
		dispatcher=(LinearLayout)findViewById(R.id.dispatcherLay);
		handler=new MyHandler();
		
		client=new Client(this,"127.0.0.1",12000);
			 
		
		
		
		surface=(MySchemaSurface)findViewById(R.id.schemaView);
		menuView = (FrameLayout)findViewById(R.id.scratching);
		slidMenu=(FrameLayout)findViewById(R.id.slidmenu);
		slidMenu.setVisibility(View.VISIBLE);
		glSurface3D=(MyRajawaliFragment) getFragmentManager().findFragmentById(R.id.slideFragment);
		menuView.removeView(slidMenu);
		varGrid=(MyVariableGrid)findViewById(R.id.myVarSurface);
		varGrid.setAdapter(new AuxAdapterVariables(this,varGrid.getVariables()));
		varGrid.setOnItemLongClickListener(varGrid);
		varGrid.setOnItemClickListener(varGrid);
		
		createComparerParamLayout();
		createComparerParamConstLayout();
		createSelectVariableLayout();
		
		if(bundle!=null && bundle.containsKey("loadprogram")){

			try {
				FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().toString()+MainActivity.applicationFolder+
						ActivityProgram.programFolder+bundle.getString("loadprogram"));
				
				input = new ObjectInputStream(fis);
				surface.loadObjectsList(input);
				varGrid.loadVariables(input);
				input.close();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		menu = new SlidingMenu(this);  
        menu.setOnOpenListener(new menuOpenListener());
        menu.setOnOpenedListener(new menuOpenedListener());
        menu.setOnCloseListener(new menuCloseListener());
        menu.setOnClosedListener(new menuClosedListener());
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setBehindWidth(1700);
        menu.setMenu(slidMenu);
 
        
       
       
        
		
		listPath=(ListView)findViewById(R.id.pathSelected);		
		listPath.setAdapter(new AuxAdapterPoints(this,new ArrayList<TriplePoint>()));
		glSurface3D.setListPath(listPath);
	
		mup=(MyTouchableLayout)findViewById(R.id.mup);
		mup.defineValues("MOVE_X", ObjectExecutable.MOVE_X, 3,"ButtonMovement");
		mdo=(MyTouchableLayout)findViewById(R.id.mdo);	
		mdo.defineValues("MOVE_Y", ObjectExecutable.MOVE_Y, 3,"ButtonMovement");
		mle=(MyTouchableLayout)findViewById(R.id.mle);
		mle.defineValues("MOVE_Z",  ObjectExecutable.MOVE_Z, 3,"ButtonMovement");
		tux=(MyTouchableLayout)findViewById(R.id.tux);
		tux.defineValues("TURN_X",  ObjectExecutable.TURN_X,90,"ButtonMovement");
		tuy=(MyTouchableLayout)findViewById(R.id.tuy);
		tuy.defineValues("TURN_Y",  ObjectExecutable.TURN_Y, 90,"ButtonMovement");
		tuz=(MyTouchableLayout)findViewById(R.id.tuz);
		tuz.defineValues("TURN_Z", ObjectExecutable.TURN_Z, 90,"ButtonMovement");
	
		
		repeat=(MyTouchableLayout)findViewById(R.id.buttonrep);
		repeat.defineValues("Repeat",  ObjectExecutable.FOR_BUTTON, 2,"ButtonControl");	
		ifclause=(MyTouchableLayout)findViewById(R.id.ifcontrol);
		ifclause.defineValues("If",  ObjectExecutable.IF_CLAUSE, 2f,"ButtonControl");
	/*	elseif=(MyTouchableLayout)findViewById(R.id.elseif);
		elseif.defineValues("ElseIf",  ObjectExecutable.ELSE_IF, 1,"ButtonIfElse");
		elseclause=(MyTouchableLayout)findViewById(R.id.elseclause);
		elseclause.defineValues("Else",  ObjectExecutable.ELSE_CLAUSE, 2,"ButtonIfElse");
	*/	dowhile=(MyTouchableLayout)findViewById(R.id.dowhile);
		dowhile.defineValues("Do while",  ObjectExecutable.DO_WHILE,2,"ButtonControl");	
		cwhile=(MyTouchableLayout)findViewById(R.id.cwhile);
		cwhile.defineValues("While",  ObjectExecutable.WHILE_CLAUSE, 2,"ButtonControl");	
	
		
		
		
		greater=(MyTouchableLayout)findViewById(R.id.greater);
		greater.defineValues(">",  ObjectExecutable.GREATER_THAN, 2,"ButtonComparer");
		less=(MyTouchableLayout)findViewById(R.id.less);
		less.defineValues("<",  ObjectExecutable.LESS_THAN, 2,"ButtonComparer");
		greater_equal=(MyTouchableLayout)findViewById(R.id.greater_equal);
		greater_equal.defineValues(">=",  ObjectExecutable.GREATER_EQUAL_THAN, 2,"ButtonComparer");
		less_equal=(MyTouchableLayout)findViewById(R.id.less_equal);
		less_equal.defineValues("<=",  ObjectExecutable.LESS_EQUAL_THAN,2,"ButtonComparer");
		equal=(MyTouchableLayout)findViewById(R.id.equal);
		equal.defineValues("==",  ObjectExecutable.EQUAL_THAN,2,"ButtonComparer");
		c_greater=(MyTouchableLayout)findViewById(R.id.c_greater);
		c_greater.defineValues(">",  ObjectExecutable.C_GREATER_THAN, 2,"ButtonComparer");
		c_less=(MyTouchableLayout)findViewById(R.id.c_less);
		c_less.defineValues("<",  ObjectExecutable.C_LESS_THAN, 2,"ButtonComparer");
		c_greater_equal=(MyTouchableLayout)findViewById(R.id.c_greater_equal);
		c_greater_equal.defineValues(">=",  ObjectExecutable.C_GREATER_EQUAL_THAN, 2,"ButtonComparer");
		c_less_equal=(MyTouchableLayout)findViewById(R.id.c_less_equal);
		c_less_equal.defineValues("<=",  ObjectExecutable.C_LESS_EQUAL_THAN,2,"ButtonComparer");
		c_equal=(MyTouchableLayout)findViewById(R.id.c_equal);
		c_equal.defineValues("==",  ObjectExecutable.C_EQUAL_THAN,2,"ButtonComparer");
		always=(MyTouchableLayout)findViewById(R.id.always);
		always.defineValues("Always",  ObjectExecutable.ALWAYS,2,"ButtonComparer");
		
		add=(MyTouchableLayout)findViewById(R.id.xpy);
		add.defineValues("+",  ObjectExecutable.ADD, 2,"ButtonOperator");
		sustract=(MyTouchableLayout)findViewById(R.id.xly);
		sustract.defineValues("-",  ObjectExecutable.SUSTRACT, 2,"ButtonOperator");
		multiply=(MyTouchableLayout)findViewById(R.id.xmy);
		multiply.defineValues("*",  ObjectExecutable.MULTIPLY,2,"ButtonOperator");
		divide=(MyTouchableLayout)findViewById(R.id.xdy);
		divide.defineValues("/",  ObjectExecutable.DIVIDE, 2,"ButtonOperator");
		c_add=(MyTouchableLayout)findViewById(R.id.c_xpy);
		c_add.defineValues("+",  ObjectExecutable.C_ADD, 2,"ButtonOperator");
		c_sustract=(MyTouchableLayout)findViewById(R.id.c_xly);
		c_sustract.defineValues("-",  ObjectExecutable.C_SUSTRACT, 2,"ButtonOperator");
		c_multiply=(MyTouchableLayout)findViewById(R.id.c_xmy);
		c_multiply.defineValues("*",  ObjectExecutable.C_MULTIPLY,2,"ButtonOperator");
		c_divide=(MyTouchableLayout)findViewById(R.id.c_xdy);
		c_divide.defineValues("/",  ObjectExecutable.C_DIVIDE, 2,"ButtonOperator");
		
		addOne=(MyTouchableLayout)findViewById(R.id.xpp);
		addOne.defineValues("++",  ObjectExecutable.ADD_ONE, 2,"ButtonOperator");
		sustractOne=(MyTouchableLayout)findViewById(R.id.xll);
		sustractOne.defineValues("--",  ObjectExecutable.SUSTRACT_ONE, 2,"ButtonOperator");
		reasign=(MyTouchableLayout)findViewById(R.id.rvalue);
		reasign.defineValues("=",  ObjectExecutable.REASIGN_VALUE, 2,"ButtonOperator");
		
		value=(MyTouchableLayout)findViewById(R.id.value);
		value.defineValues("Value",  ObjectExecutable.REASIGN_VALUE, 2,"ButtonVariable");
		
		sip=(MyTouchableLayout)findViewById(R.id.sip);
		sip.defineValues("Initial Position",  ObjectExecutable.INIT_POSE, 2,"ButtonRobot");
		deburring=(MyTouchableLayout)findViewById(R.id.deburrng);
		deburring.defineValues("Deburring",  ObjectExecutable.DEBURRING, 2,"ButtonRobot");
		
		conf=(MyTouchableLayout)findViewById(R.id.conf);
		conf.defineValues("Loop Break",  ObjectExecutable.CONFIRM, 2,"ButtonUserCommand");
		pressexe=(MyTouchableLayout)findViewById(R.id.pressexe);
		pressexe.defineValues("Start",  ObjectExecutable.PRESS_EXECUTE, 1,"ButtonUserCommand");
	
		
		
		l_move=(GridLayout)findViewById(R.id.l_move);
		l_control=(GridLayout)findViewById(R.id.l_control);
		l_comp=(ScrollView)findViewById(R.id.l_comp);
		l_var=(ScrollView)findViewById(R.id.l_var);
		l_op=(GridLayout)findViewById(R.id.l_oper);
		l_us=(GridLayout)findViewById(R.id.l_user);
		c_move=(Button)findViewById(R.id.c_move);
		c_control=(Button)findViewById(R.id.c_control);
		c_comp=(Button)findViewById(R.id.c_comp);
		c_var=(Button)findViewById(R.id.c_var);
		c_op=(Button)findViewById(R.id.c_oper);
		c_us=(Button)findViewById(R.id.c_user);
		

		mup.setOnTouchListener(new MyTouchListener());
		mdo.setOnTouchListener(new MyTouchListener());
		mle.setOnTouchListener(new MyTouchListener());
		tux.setOnTouchListener(new MyTouchListener());
		tuy.setOnTouchListener(new MyTouchListener());
		tuz.setOnTouchListener(new MyTouchListener());
		pressexe.setOnTouchListener(new MyTouchListener());
		repeat.setOnTouchListener(new MyTouchListener());
		ifclause.setOnTouchListener(new MyTouchListener());
	//	elseif.setOnTouchListener(new MyTouchListener());
	//	elseclause.setOnTouchListener(new MyTouchListener());
		dowhile.setOnTouchListener(new MyTouchListener());
		cwhile.setOnTouchListener(new MyTouchListener());
		greater.setOnTouchListener(new MyTouchListener());
		less.setOnTouchListener(new MyTouchListener());
		less_equal.setOnTouchListener(new MyTouchListener());
		greater_equal.setOnTouchListener(new MyTouchListener());
		equal.setOnTouchListener(new MyTouchListener());
		add.setOnTouchListener(new MyTouchListener());
		sustract.setOnTouchListener(new MyTouchListener());
		multiply.setOnTouchListener(new MyTouchListener());
		divide.setOnTouchListener(new MyTouchListener());
		addOne.setOnTouchListener(new MyTouchListener());
		sustractOne.setOnTouchListener(new MyTouchListener());
		reasign.setOnTouchListener(new MyTouchListener());
		value.setOnTouchListener(new MyTouchListener());
		c_greater.setOnTouchListener(new MyTouchListener());
		c_less.setOnTouchListener(new MyTouchListener());
		c_less_equal.setOnTouchListener(new MyTouchListener());
		c_greater_equal.setOnTouchListener(new MyTouchListener());
		c_equal.setOnTouchListener(new MyTouchListener());
		c_add.setOnTouchListener(new MyTouchListener());
		c_sustract.setOnTouchListener(new MyTouchListener());
		c_multiply.setOnTouchListener(new MyTouchListener());
		c_divide.setOnTouchListener(new MyTouchListener());
		always.setOnTouchListener(new MyTouchListener());
		
		
		
		sip.setOnTouchListener(new MyTouchListener());
		deburring.setOnTouchListener(new MyTouchListener());
		
		conf.setOnTouchListener(new MyTouchListener());
		surface.setOnDragListener(new MyDragListener());
	
		

		
	
	}

	
	@Override
	public void onBackPressed() {
		final EditText saveName=new EditText(this);
		saveName.setHint("hammer.hcm");
		saveName.setHintTextColor(Color.parseColor("#008A00"));
		
		MyCustomDialog.Builder customBuilder=new MyCustomDialog.Builder(this);
		Dialog  dialog=null;
		customBuilder.setTitle("SAVE PROGRAM")
                .setMessage("Do you want to save the program?\nIntroduce name:")
                .setContentView(saveName)
                .setIcon(R.drawable.savedata)
                .setNegativeButton("Cancel", 
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    	 dialog.dismiss();
                    	 finish();
                    }
                })
                .setPositiveButton("Confirm", 
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	 saveProgram(saveName.getText().toString());
                    	 dialog.dismiss();
                    	 finish();
                        
                    }
                });
          dialog = customBuilder.create();
          dialog.show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		if(client==null)return super.onCreateOptionsMenu(menu);
		
		if(client.isConnected())
			menu.getItem(2).setIcon(R.drawable.line_on);
		else
			menu.getItem(2).setIcon(R.drawable.line_off);
		
		
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//MenuItem connected=menu.getItem(1).setVisible(false);
		//MenuItem disconnected= menu.getItem(2).setVisible(false);
		
		//if(client.Connect()) disconnected.setVisible(true);
		
		//else connected.setVisible(true);
		
	
		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	
		MyCustomDialog.Builder customBuilder=new MyCustomDialog.Builder(this);
		Dialog  dialog=null;
		switch(item.getItemId()){
		
		case R.id.save_program:
			
			final EditText saveName=new EditText(this);
			
	          
			customBuilder.setTitle("SAVE PROGRAM")
	                .setMessage("Do you want to save the program?\nIntroduce name:")
	                .setContentView(saveName)
	                .setIcon(R.drawable.savedata)
	                .setNegativeButton("Cancel", 
	                        new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	dialog.dismiss();
	                    }
	                })
	                .setPositiveButton("Confirm", 
	                        new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	 saveProgram(saveName.getText().toString());
	                    	 dialog.dismiss();
	                        
	                    }
	                });
	          dialog = customBuilder.create();
	          dialog.show();
			
	          break;
			
		case R.id.connect_cad:
			
			if(!client.isConnected())
			{
			
				
		          
				customBuilder.setTitle("CONNECT TO EASYROB")
		                .setMessage("Do you want to connect the program at IP "+GeneralParameters.ip+
		    					" : "+GeneralParameters.port+" ?")
		                .setIcon(R.drawable.connect)
		                .setNegativeButton("Cancel", 
		                        new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                    	dialog.dismiss();
		                    }
		                })
		                .setPositiveButton("Confirm", 
		                        new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                    	 client=new Client(activity,GeneralParameters.getIp(),GeneralParameters.getPort());
								client.setHandler(handler);
								client.start();
								 dialog.dismiss();
		                        
		                    }
		                });
		          dialog = customBuilder.create();
		          dialog.show();
			}
			
			else
			{
				customBuilder.setMessage("Do you want to disconnect the program?")
				.setIcon(R.drawable.connect)
		       .setTitle("CONNECT TO EASYROB")
		       .setNegativeButton("Disconnect", new DialogInterface.OnClickListener() {
		          

				public void onClick(DialogInterface dialog, int id) {
		        	   client.disconnect();
		        	   dialog.dismiss();
		        	 
		        	 
		             
		           }
		       })
		       .setPositiveButton("Don't disconnect", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });


				dialog = customBuilder.create();
				dialog.show();
				break;
			}
		 
		
	
		
		
		
		}
		
		invalidateOptionsMenu();
		return false;
	}

	public void execute(View view)

	{
		
		ArrayList<PositionableObject > list=surface.getDrawList();
		
		
		for(PositionableObject lso:list)
		{
			if(lso.getType()==ObjectExecutable.PRESS_EXECUTE)
				{
					client.setExe(lso.getChild());
					
					client.sendData();
					break;
				 }
		}
		
	}
	
	public void onSetParameter(View view)
	{
		EditText par=(EditText)findViewById(R.id.paramValue);
		MyTouchableLayout parameter=(MyTouchableLayout)findViewById(R.id.valueLay);
		
		
 		
 		if(par.getText().length()>0)
 			parameter.getParameter().setValue(Double.parseDouble(par.getText().toString()));
 		
 		showButtonParameters(dispatcher);
		
		par.getText().clear();
		
	}
	
	public void onCancelParameter(View view)
	{
		EditText par=(EditText)findViewById(R.id.paramValue);
		showButtonParameters(dispatcher);
		par.getText().clear();
		
	}
	
	public void onSetInitParameters(View view)
	{
		EditText par=(EditText)findViewById(R.id.speedValue);
		MyTouchableLayout parameters=(MyTouchableLayout)findViewById(R.id.initPosParam);
		
		EditText x,y,z;
 		x=(EditText)findViewById(R.id.xipos);
 		y=(EditText)findViewById(R.id.yipos);
 		z=(EditText)findViewById(R.id.zipos);
 		
 		if(par.getText().length()>0)
 			parameters.getPoint().x=Double.parseDouble(x.getText().toString());
 		if(x.getText().length()>0)
 			parameters.getPoint().y=Double.parseDouble(y.getText().toString());
 		if(y.getText().length()>0)
 			parameters.getPoint().z=Double.parseDouble(z.getText().toString());
 		if(z.getText().length()>0)
 			parameters.getParameter().setValue(Double.parseDouble(par.getText().toString()));
		
 		showButtonParameters(dispatcher);
		par.getText().clear();
		x.getText().clear();
		y.getText().clear();
		z.getText().clear();
		
	}
	
	public void onCancelPos(View view)
	{
		EditText par=(EditText)findViewById(R.id.speedValue);
	
	
		EditText x,y,z;
 		x=(EditText)findViewById(R.id.xipos);
 		y=(EditText)findViewById(R.id.yipos);
 		z=(EditText)findViewById(R.id.zipos);
 		
 		showButtonParameters(dispatcher);
		
		par.getText().clear();
		x.getText().clear();
		y.getText().clear();
		z.getText().clear();
	}
	
	
	public void createVariableLayout(View view)
	
	{
		LinearLayout var=(LinearLayout) findViewById(R.id.varLay);
		var.setVisibility(View.VISIBLE);
		dispatcher.setVisibility(View.INVISIBLE);
		((TextView)var.findViewById(R.id.nameVar)).setTextSize
		(((TextView)var.findViewById(R.id.valueVar)).getTextSize()*1.9f);
	}


	public void onSetVariableParams(View view)
	{
		TextView name,cname,value;
		Variable variable=new Variable();
		LinearLayout var=(LinearLayout) findViewById(R.id.varLay);
		name=(TextView)var.findViewById(R.id.nameVar);
		cname=(TextView)var.findViewById(R.id.cnameVar);
		if(cname.getText().length()>0)variable.setName(cname.getText().toString());
		else variable.setName("¿?");
		value=(TextView)var.findViewById(R.id.valueVar);
		if(value.getText().length()>0 && value.getText().toString().compareTo("¿?")!=0)variable.setValue(Double.valueOf(value.getText().toString()));
		else variable.setValue(0);
		
		varGrid.createVariable(variable);
		showButtonParameters(dispatcher);
		((AuxAdapterVariables)varGrid.getAdapter()).notifyDataSetChanged();
	
		name.setText("¿?");
		cname.setText("¿?");
		value.setText("¿?");
	}
	
	public void onCancelVariable(View view)
	{
		LinearLayout var=(LinearLayout) findViewById(R.id.varLay);
		showButtonParameters(dispatcher);
		
		TextView name,cname,value;
		name=(TextView)var.findViewById(R.id.nameVar);
		cname=(TextView)var.findViewById(R.id.cnameVar);
		value=(TextView)var.findViewById(R.id.valueVar);
		
	
		name.setText("?");
		cname.setText("?");
		value.setText("?");
	}
	
	
	
	public void loadVariable(View view)
	{
		LinearLayout var=(LinearLayout) findViewById(R.id.varLay);
		TextView par;
		EditText name=(EditText)var.findViewById(R.id.vName);
		EditText value=(EditText)var.findViewById(R.id.vValue);
		par=(TextView)var.findViewById(R.id.nameVar);
		if(name.getText().length()>0)par.setText(name.getText().toString());
		else par.setText("¿?");
		par=(TextView)var.findViewById(R.id.cnameVar);
		par.setText(name.getText().toString());
		par=(TextView)var.findViewById(R.id.valueVar);
		if(value!=null)par.setText(value.getText().toString());
		else par.setText("¿?");
		
		name.getText().clear();
		value.getText().clear();
		
		
		
	}
	public void createComparerParamLayout()
	{
		
		LinearLayout comparerValue1=(LinearLayout)findViewById(R.id.v1);
		LinearLayout comparerValue2=(LinearLayout)findViewById(R.id.v2);
		
		ListView listVar=(ListView)findViewById(R.id.listVar1);
		ListView listVar2=(ListView)findViewById(R.id.listVar2);
		AuxAdapterVariables adapterVar=new AuxAdapterVariables(this,varGrid.getVariables());
		adapterVar.setBlue(true);
		listVar.setAdapter(adapterVar);
		listVar2.setAdapter(adapterVar);
		listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listVar2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listVar.setOnItemClickListener(new MyParamClickListener(comparerValue1));
		listVar2.setOnItemClickListener(new MyParamClickListener(comparerValue2));
		listVar.setSelector(R.drawable.bg_key);
		listVar2.setSelector(R.drawable.bg_key);
		
		((TextView)comparerValue1.findViewById(R.id.nameVar)).setTextSize
		(((TextView)comparerValue1.findViewById(R.id.valueVar)).getTextSize()*1.9f);
		
		((TextView)comparerValue2.findViewById(R.id.nameVar)).setTextSize
		(((TextView)comparerValue2.findViewById(R.id.valueVar)).getTextSize()*1.9f);
		
	}
	
	public void onSetComparerParams(View view)
	{
		LinearLayout comparerValue1=(LinearLayout)findViewById(R.id.v1);
		LinearLayout comparerValue2=(LinearLayout)findViewById(R.id.v2);
		MyTouchableLayout compLay=(MyTouchableLayout)findViewById(R.id.compLay);
		compLay.setIntercept(false);
		
		TextView par;
		par=(TextView)comparerValue1.findViewById(R.id.cnameVar);
		compLay.getStringValue().setString(par.getText().toString());
		par=(TextView)comparerValue2.findViewById(R.id.cnameVar);
		compLay.getStringValue2().setString(par.getText().toString());
		showButtonParameters(dispatcher);
	}
	
	
	
	public void onCancelComparer(View view)
	{
		showButtonParameters(dispatcher);
	}


	public void createComparerParamConstLayout()
	{
		
		LinearLayout comparerValue1=(LinearLayout)findViewById(R.id.v3);
		
		
		ListView listVar=(ListView)findViewById(R.id.listVar3);
		
		AuxAdapterVariables adapterVar=new AuxAdapterVariables(this,varGrid.getVariables());
		adapterVar.setBlue(true);
		listVar.setAdapter(adapterVar);
	
		listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		listVar.setOnItemClickListener(new MyParamClickListener(comparerValue1));
		listVar.setSelector(R.drawable.bg_key);
		
		
		
		
	}
	
	public void onSetComparerParamsConst(View view)
	{
		
		MyTouchableLayout compLayConst=(MyTouchableLayout)findViewById(R.id.compLayConst);
		
		TextView par=(TextView)compLayConst.findViewById(R.id.cnameVar);
		compLayConst.getStringValue().setString(par.getText().toString());
		
		EditText edit=(EditText)findViewById(R.id.constant);
		if(edit.getText().length()>0)compLayConst.getStringValue2().setString(edit.getText().toString());
		else compLayConst.getStringValue2().setString("¿?");
		
		showButtonParameters(dispatcher);
	}
	
	
	
	public void onCancelComparerConst(View view)
	{
		showButtonParameters(dispatcher);
	}
	
	
	public void createSelectVariableLayout()
	{
		
		LinearLayout selVar=(LinearLayout)findViewById(R.id.selVar);
		ListView listVar=(ListView)findViewById(R.id.listSelVar);
		AuxAdapterVariables adapterVar=new AuxAdapterVariables(this,varGrid.getVariables());
		adapterVar.setBlue(true);
		listVar.setAdapter(adapterVar);
		listVar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listVar.setOnItemClickListener(new MyParamClickListener(selVar));	
		listVar.setSelector(R.drawable.bg_key);
		((TextView)selVar.findViewById(R.id.nameVar)).setTextSize
		(((TextView)selVar.findViewById(R.id.valueVar)).getTextSize()*1.9f);
		
	}
	
	
	
	public void onSetSelectVariableParams(View view)
	{
		
		TextView par;
		MyTouchableLayout selVarLay=(MyTouchableLayout)findViewById(R.id.selVarLay);
		selVarLay.setIntercept(false);
		par=(TextView)selVarLay.findViewById(R.id.cnameVar);
		selVarLay.getStringValue().setString(par.getText().toString());
		selVarLay.getParameter().setValue(varGrid.searchVariable(par.getText().toString()));
		showButtonParameters(dispatcher);
	}
	
	public void onCancelSelVar(View view)
	{
		
		showButtonParameters(dispatcher);
	}
	
	
	public void onChangeCategories(View view)
	{
		l_control.setVisibility(View.INVISIBLE);
		l_comp.setVisibility(View.INVISIBLE);
		l_move.setVisibility(View.INVISIBLE);
		l_var.setVisibility(View.INVISIBLE);
		l_op.setVisibility(View.INVISIBLE);
		l_us.setVisibility(View.INVISIBLE);

		if(view==c_control)
			l_control.setVisibility(View.VISIBLE);
		
		else if(view==c_comp)
			l_comp.setVisibility(View.VISIBLE);
	
		else if(view==c_move)
			l_move.setVisibility(View.VISIBLE);
		
		else if(view==c_var)
			l_var.setVisibility(View.VISIBLE);
		
		else if(view==c_op)
			l_op.setVisibility(View.VISIBLE);
		
		else if(view==c_us)
			l_us.setVisibility(View.VISIBLE);
	}

	

	public void saveProgram(String nameProgram)
	{
		File folder=new File(Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+ActivityProgram.programFolder);
		if (!folder.exists()) folder.mkdir();
		File data=new File(folder.getAbsolutePath()+"/"+nameProgram+".hcm");
		
			
		try {
			data.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(data);
		output = new ObjectOutputStream(fos);	
		surface.saveObjectsList(output);
		varGrid.saveVariables(output);
		output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public class menuOpenListener implements SlidingMenu.OnOpenListener
	{
	
		@Override
		public void onOpen(int position) {
			surface.onPause();
			
			
			
			
		}
	
	}

	public class menuOpenedListener implements SlidingMenu.OnOpenedListener
	{
	
		@Override
		public void onOpened() {
			surface.onResume();
			
			
			
			
		}
		
	}

	public class menuCloseListener implements SlidingMenu.OnCloseListener
	{
	
		@Override
		public void onClose() {
			surface.onPause();
		
			
			
		}
		
	}

	public class menuClosedListener implements SlidingMenu.OnClosedListener
	{
	
		@Override
		public void onClosed() {
	
			surface.onResume();
		
			
			
		}
		
	}

	public class MyTouchListener implements OnTouchListener
	{
	
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN)
			{
			if(view instanceof MyTouchableLayout)	
				((MyTouchableLayout) view).onInterceptTouchEvent(event);
			ClipData data=ClipData.newPlainText("", "");
			DragShadowBuilder shadow= new View.DragShadowBuilder(view);
			view.startDrag(data, shadow, view, 0);
			}
			
			return false;
		}
		
	}

	class MyDragListener implements OnDragListener {
	
	
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




	public MyRajawaliFragment getGlSurface3D() {
		return glSurface3D;
	}


	public MyVariableGrid getVarGrid() {
		return varGrid;
	}


	public void showButtonParameters(View show)
	{
		View var= findViewById(R.id.varLay);
		var.setVisibility(View.INVISIBLE);
		View compLay=findViewById(R.id.compLay);
		compLay.setVisibility(View.INVISIBLE);
		View parameters=findViewById(R.id.initPosParam);
		parameters.setVisibility(View.INVISIBLE);
		View compLayConst=findViewById(R.id.compLayConst);
		compLayConst.setVisibility(View.INVISIBLE);
		View selVarLay=findViewById(R.id.selVarLay);
		selVarLay.setVisibility(View.INVISIBLE);
		View parameter=findViewById(R.id.valueLay);
		parameter.setVisibility(View.INVISIBLE);
		
		dispatcher.setVisibility(View.INVISIBLE);
		show.setVisibility(View.VISIBLE);
		for(PositionableObject po:surface.getTotalObjectDrawnList())po.setModificate(false);
	}
	

	class MyHandler extends Handler
	{
	
		

			@Override
			public void handleMessage(Message msg) {
				
				Button exe=(Button)findViewById(R.id.execute);
				switch(msg.what)
				{
				case 0:
					((AuxAdapterVariables)varGrid.getAdapter()).notifyDataSetChanged();
					break;
				case 1:
					exe.setEnabled(false);
					break;
				case 2:
					exe.setEnabled(true);
					break;
				}
			}
	}


	
	
}
