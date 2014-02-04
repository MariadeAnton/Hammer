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

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.test.My3DView.OnSelectObject;


public class ActivityScratching extends Activity implements  OnSelectObject,OnCloseListener,OnClosedListener,OnOpenedListener{


	private FragmentButtons buttonsFrag;
	private FragmentCanvas canvasFrag;
	private FragmentVariables variables;
	private Window3D window3D;
	private FragmentTransaction fragmentTrans;
	private Client client=null;
	private Handler handler;
	private Activity activity ;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Fragment auxLayout=null;
	private MySchemaSurface canvas;
	private MyVariableGrid grid;
	private Activity act;
	private SlidingMenu menu;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		
		act=this;
		canvas=		canvasFrag.getCanvas();
		grid=variables.getVarGrid();
		Bundle bundle=getIntent().getExtras();
		
		if(bundle!=null && bundle.containsKey("loadprogram")){

			try {
				FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().toString()+MainActivity.applicationFolder+
						ActivityProgram.programFolder+bundle.getString("loadprogram"));
				
				input = new ObjectInputStream(fis);
				canvas.loadObjectsList(input);
				grid.loadVariables(input);
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
		
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_scratch);
		
		activity=this;
		canvasFrag=new FragmentCanvas(this);
		buttonsFrag=new FragmentButtons();
		variables=new FragmentVariables(this);
		
		fragmentTrans=getFragmentManager().beginTransaction();
		fragmentTrans.replace(R.id.frameButtons,buttonsFrag).commit();
		
		fragmentTrans=getFragmentManager().beginTransaction();
		fragmentTrans.replace(R.id.frameCanvas,canvasFrag).commit();
		
		fragmentTrans=getFragmentManager().beginTransaction();
		fragmentTrans.replace(R.id.frameVariables,variables).commit();
		
		
		window3D=new Window3D(this);
        window3D.getView3D().getRenderer().loadEnvironment(GeneralParameters.getEnvironment());
        window3D.getView3D().setOnSelectionListener(this);
		
        menu = new SlidingMenu(this);  
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setBehindWidth(1700);  
        menu.setMenu(window3D);
        menu.setOnCloseListener(this);
        menu.setOnOpenedListener(this);
	
        handler=new MyHandler();
		client=new Client(this,"127.0.0.1",12000);
		
			
	}

	
	
	public void onChangeCategories(View v)
	{
		buttonsFrag.onChangeCategories(v);
	}
	
	public void onCreateVariables(View v)
	{
		
		showButtonLayout(new FragmentCreateVariable(this,variables.getVarGrid()),null);
		
	}
	
	
	@Override
	public void onBackPressed() {
	/*	final EditText saveName=new EditText(this);
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
	*/	
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
	/*
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
		        	   act.invalidateOptionsMenu();
		        	 
		        	 
		             
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
		
		invalidateOptionsMenu();*/
		return false;
		
	}

	public void execute(View view)

	{
		
		ArrayList<PositionableObject > list=canvas.getDrawList();
		
		
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

	


	

	public void saveProgram(String nameProgram)
	{
		File folder=new File(Environment.getExternalStorageDirectory()+MainActivity.applicationFolder+ActivityProgram.programFolder);
		if (!folder.exists()) folder.mkdir();
		File data=new File(folder.getAbsolutePath()+"/"+nameProgram+".hcm");
		
			
		try {
			data.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(data);
		output = new ObjectOutputStream(fos);	
		canvas.saveObjectsList(output);
		grid.saveVariables(output);
		output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void showButtonLayout(Fragment layout, PositionableObject obj)
	{
		for(PositionableObject pos:canvas.getObjectList())pos.setModificate(false);
		auxLayout=layout;
		fragmentTrans=getFragmentManager().beginTransaction();	
		fragmentTrans.replace(R.id.frameButtons,auxLayout);
		if(obj!=null)obj.setModificate(true);
		//fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
		canvas.invalidate();
		
		
	}
	
	public void restoreButtons()
	{

		fragmentTrans=getFragmentManager().beginTransaction();
		fragmentTrans.replace(R.id.frameButtons,buttonsFrag);
		fragmentTrans.commit();
		for(PositionableObject pos:canvas.getObjectList())pos.setModificate(false);
		canvas.invalidate();
	}
	

	@Override
	public void onClose() {
		
		
}


	@Override
	public void onOpened() {
	
	}


	@Override
	public void onClosed() {
		
	}


	
	
	class MyHandler extends Handler
	{
	
			@Override
			public void handleMessage(Message msg) {
				
				Button exe=(Button)findViewById(R.id.execute);
				switch(msg.what)
				{
				case 0:
					((AuxAdapterVariables)variables.getVarGrid().getAdapter()).notifyDataSetChanged();
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

	
	@Override
	public void selectObject(AuxPiece object) {
		variables.getPartSelectedName().setText(object.getName());	
		canvas.invalidate();
	}


	@Override
	public void deselectObject() {
		
		variables.getPartSelectedName().setText("No part Selected");
		canvas.invalidate();
	}
	
	
	public FragmentVariables getVariables() {
		return variables;
	}



	public Window3D getWindow3D() {
		return window3D;
	}

}
