package com.test;


import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import ch.bretscherhochstrasser.android.stickyviewpager.StickyViewPager;

import com.test.FragmentRobotRight.ButtonsImplementation;
import com.test.Widget_IncrementalButtons.ButtonPressed;
import com.threed.jpct.Polyline;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;



public class ActivityRobot extends FragmentActivity  {

	private Window3D window;
	private My3DView view3D;
	private Robot3DOF robot;
	private Widget_IncrementalButtons wQ1,wQ2,wQ3,wX,wY,wZ,speed;
	private Widget_ListView listPoints,listPathPoints;
	private Widget_ListView_Grouping listPath;
	private Button rButton,pButton,sButton;
	private StickyViewPager pager;
	private ThreadSimulation threadSimulation;
	private FragmentRobotRight robotRightFrag,robotRightFrag2,robotRightFrag3;
	private Integer speedValue=10;
	private ArrayList<Path3D> paths3D=new ArrayList<Path3D>();
	private ArrayList<Point3D> points3D=new ArrayList<Point3D>();

	
	
	GradientDrawable drawable=new GradientDrawable();

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_robot);
		
	
		
		listPath=new Widget_ListView_Grouping(this);
		listPath.setTittle("Paths Recorded",R.drawable.shape_orange,getResources().getColor(R.color.m_white));
		listPath.addSubtittle(R.color.m_orange_d2, getResources().getColor(R.color.m_white),null,"Name's Path");
		listPath.setBackgroundItemColor(getResources().getColor(R.color.m_orange_l2));
		listPath.setOnItemClickListener(new SelectedPathFunction());
		listPath.setOnItemLongClickListener(new DeleteFunction(DeleteFunction.PATH_MODE));
		
		listPoints=new Widget_ListView(this);
		listPoints.setTittle("Points Recorded",R.drawable.shape_orange,getResources().getColor(R.color.m_white));
		listPoints.addSubtittle(R.color.m_orange_d2, getResources().getColor(R.color.m_white),"Number","Q1","Q2","Q3","X","Y","Z");
		listPoints.setBackgroundItemColor(getResources().getColor(R.color.m_orange_l2));
		listPoints.setOnItemClickListener(new SelectedPointFunction());
		listPoints.setOnItemLongClickListener(new DeleteFunction(DeleteFunction.POINT_MODE));
		
		listPathPoints=new Widget_ListView(this);
		listPathPoints.setTittle("No Path Selected",R.drawable.shape_orange,getResources().getColor(R.color.m_white));
		listPathPoints.addSubtittle(R.color.m_orange_d2, getResources().getColor(R.color.m_white),"Number","Q1","Q2","Q3","X","Y","Z");
		listPathPoints.setBackgroundItemColor(getResources().getColor(R.color.m_orange_l2));
		listPathPoints.setOnItemClickListener(new SelectedPointFunction());
		
		
		robotRightFrag=new FragmentRobotRight(listPoints);
		robotRightFrag2=new FragmentRobotRight(listPath);
		robotRightFrag3=new FragmentRobotRight(listPathPoints);
		
	
		
	
	    // Create an adapter with the fragments we show on the ViewPager
	    ViewPageAdapter adapter = new ViewPageAdapter(
	    getSupportFragmentManager());

	    adapter.addFragment(robotRightFrag);
	    adapter.addFragment(robotRightFrag2);
	    adapter.addFragment(robotRightFrag3);
	    
	    pager = (StickyViewPager)findViewById(R.id.pager);
	    pager.setAdapter(adapter);
	    
	    createMainView();
	    
		

	}
	

	

	public class ButtonPressedConf implements ButtonPressed
		{

		private int joint;
		private float param=0.5f;

		public ButtonPressedConf(int joint) {
			super();
			this.joint = joint;
		}

		@Override
		public void increaseValue() {
			 moveJoint(param,joint);
			
		}

		@Override
		public void decreaseValue() {
			moveJoint(-param,joint);
			
		}	
		
		private void moveJoint(float param,int joint)
		{
			robot.moveJoint(joint, param);
			
			
			updateInfoRobot();
			
		}
	
	}
	
	public class ButtonPressedPos implements ButtonPressed
	{

	private int direction;
	private float sum=0.1f;

	public ButtonPressedPos(int direction) {
		super();
		this.direction = direction;
	}

	@Override
	public void increaseValue() {
		moveTool(sum,direction);	
		
	}

	@Override
	public void decreaseValue() {
	
		moveTool(-sum,direction);
		
	}	
	
	private void moveTool(float sum, int direction)
	{
		
		
		
		switch(direction)
		{
		
		case 0:
			robot.moveToolTo(robot.getToolCoord().x+sum,robot.getToolCoord().y,robot.getToolCoord().z);
			break;
			
		case 1:
			robot.moveToolTo(robot.getToolCoord().x,robot.getToolCoord().y+sum,robot.getToolCoord().z);
			break;
			
		case 2:
			robot.moveToolTo(robot.getToolCoord().x,robot.getToolCoord().y,robot.getToolCoord().z+sum );
			break;
		}
		
		updateInfoRobot();
		

	}

}
	
	public class RecordPointFunction implements OnClickListener
	{

		
		@Override
		public void onClick(View arg0) {
			
			String[] valuesConf={String.format("%.2f",robot.getRobotConf().x),
							 	 String.format("%.2f",robot.getRobotConf().y),
							 	 String.format("%.2f",robot.getRobotConf().z),
							 	 String.format("%.2f",robot.getToolCoord().x),
							 	 String.format("%.2f",robot.getToolCoord().y),
							 	 String.format("%.2f",robot.getToolCoord().z)	
							 	};
		
		/////CREAMOS EL ITEM DE LA LISTA(((((((
		Widget_ListView_Item item=new Widget_ListView_Item(getResources().getColor(R.color.m_white),getResources().getDrawable(R.drawable.hammer_ico_small), valuesConf,null);			
		listPoints.addItem(item);
		
		////CREAMOS EL PUNTO 3D QUE LO REPRESENTARA EN EL RENDERER/////////
	//	Point3D point=new Point3D(robot.getToolCoord(),RGBColor.BLUE);
	//	points3D.add(point);
	//	view3D.addPoint3D(point);
		
		
		
		/////CREAMOS EL PATH QUE SE MOSTRARA SEGUN CREEMOS LA TRAYECTORIA///////////////
		Path3D path=itemsToPath(listPoints);	
		view3D.addPath(path,paths3D);
		pager.setCurrentItem(0);
		
		
			
		}
		
	}
	
	public class RecordPathFunction implements OnClickListener
	{

		@Override
		public void onClick(View view) {
			
			Context context=view.getContext();
			MyCustomDialog.Builder customBuilder=new MyCustomDialog.Builder(context);
			Dialog  dialog=null;
			
			final EditText saveName=new EditText(context);
				
		          
			customBuilder.setTitle("SAVE POINTS",null)
		                 .setMessage("Do you want to save the points?\nIntroduce name:",getResources().getDrawable(R.color.m_white))
		                 .setContentView(saveName,null)
		                 .setIcon(R.drawable.savedata)
		                 .setNegativeButton("Cancel",null, getResources().getDrawable(R.drawable.shape_orange_rounded),
			               
		                        new DialogInterface.OnClickListener() {
		                	 	public void onClick(DialogInterface dialog, int which) {
		                    	dialog.dismiss();
		                    }
		                 	})
		                  .setPositiveButton("Confirm", null,getResources().getDrawable(R.drawable.shape_orange_rounded),
					                
		                        new DialogInterface.OnClickListener() {
		                	  	@SuppressWarnings("unchecked")
								public void onClick(DialogInterface dialog, int which) 
		                	  	{
		                	  		listPath.addSet(saveName.getText().toString(),(ArrayList<Widget_ListView_Item>) listPoints.getItems().clone());
		                	  		listPoints.removeAllItems();
		                	  		
		                	  		view3D.removeAllPoints(points3D);
		                	  		view3D.removeAllPaths(paths3D);
		                    	
		                	  		dialog.dismiss();
		                	  		pager.setCurrentItem(1);
		                        
		                    }
		                });
		          dialog = customBuilder.create(getResources().getDrawable(R.drawable.shape_orange_rounded));
		          dialog.show();
		          
			
		}
	}
	
	class DeleteFunction implements OnItemLongClickListener
	{
		public static final int POINT_MODE=0;
		public static final int PATH_MODE=1;
		private int mode;
		
		
		public DeleteFunction(int mode) {
			super();
			this.mode = mode;
		}
		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int pos, long id) {
			
			final int  position=pos;
			final AdapterView<?> adapt=adapter;
			Context context=view.getContext();
			
			MyCustomDialog.Builder customBuilder=new MyCustomDialog.Builder(context);
			Dialog  dialog=null;
			String tittle=new String();
			String message=new String();
		
			
			switch(mode)
			{
				case POINT_MODE:
					
			
					tittle="DELETE POINT";
					message="Do you want to delete this point?";
					break;
					
				case PATH_MODE:
					tittle="DELETE PATH";
					message=new String("Do you want to delete this path?");
					break;
		
			}
		         
			
			
			customBuilder.setTitle(tittle,null)
		                 .setMessage(message,getResources().getDrawable(R.color.m_white))
		                 .setIcon(R.drawable.delwhite)
		                 .setNegativeButton("Cancel",null, getResources().getDrawable(R.drawable.shape_orange_rounded),
			               
		                        new DialogInterface.OnClickListener() {
		                	 	public void onClick(DialogInterface dialog, int which) {
		                    	dialog.dismiss();
		                    }
		                 	})
		                  .setPositiveButton("Confirm", null,getResources().getDrawable(R.drawable.shape_orange_rounded),
					                
		                        new DialogInterface.OnClickListener() {
		                	  
								public void onClick(DialogInterface dialog, int which) 
		                	  	{
									
									
									Widget_ListView_Adapter wAdapter=(Widget_ListView_Adapter)adapt.getAdapter();
									Widget_ListView_Item item=(Widget_ListView_Item)wAdapter.getItem(position);
									wAdapter.getParent().deleteItem(item);
									view3D.removeAllPoints(points3D);
									view3D.removeAllPaths(paths3D);
									
									switch(mode)
									{
										case POINT_MODE:
											
									
											Path3D path=itemsToPath(listPoints);
											view3D.addPath(path,paths3D);
											break;
											
										case PATH_MODE:
											listPathPoints.removeAllItems();
											listPathPoints.changeNameTittle("No path selected");
											break;
								
									}
									dialog.dismiss();
		                        
		                    }
		                });
		          dialog = customBuilder.create(getResources().getDrawable(R.drawable.shape_orange_rounded));
		          dialog.show();
			
			

			return false;
		}
		
	}
	
	public class SelectedPathFunction implements OnItemClickListener
	{

		Widget_ListView_Item itemPreview=null;
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int pos,
				long id) {

			
		
				Widget_ListView_Adapter wAdapter=(Widget_ListView_Adapter)adapter.getAdapter();
				Widget_ListView_Item item=(Widget_ListView_Item)wAdapter.getItem(pos);
				
			
				item.setSelected(!item.isSelected());
				if(itemPreview!=null && itemPreview!=item)itemPreview.setSelected(false);
				
				wAdapter.notifyDataSetChanged();
				
				if(item.isSelected())
				{
				PointsSet path=listPath.getPathSet().get(pos);
				listPathPoints.changeNameTittle(path.name);

				listPathPoints.removeAllItems();
				int size=path.pathPoints.size();
				for(int i=0;i<size;i++)
					listPathPoints.addItem(path.pathPoints.get(i));
			
				Path3D path3D=itemsToPath(listPathPoints);
				view3D.removeAllPaths(paths3D);
				view3D.addPath(path3D,paths3D);
				}
				else
				{
					view3D.removeAllPaths(paths3D);
					view3D.removeAllPoints(points3D);
					listPathPoints.changeNameTittle("No path selected");
					listPathPoints.removeAllItems();
				}
				
				itemPreview=item;
				
			
			
		}
		
		
		
	}
	
	public class SelectedPointFunction implements OnItemClickListener
	{

		Widget_ListView_Item itemPreview=null;
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int pos,
				long id) {
		
				Widget_ListView_Adapter wAdapter=(Widget_ListView_Adapter)adapter.getAdapter();
				Widget_ListView_Item item=(Widget_ListView_Item)wAdapter.getItem(pos);
				
				
				item.setSelected(!item.isSelected());
				if(itemPreview!=null && itemPreview!=item)itemPreview.setSelected(false);
				
				wAdapter.notifyDataSetChanged();
				
				String[] data=item.getName();
				
				
				if(item.isSelected())
				{
					Point3D point=new Point3D(new SimpleVector(Float.parseFloat(data[4].replace(",",".")),-Float.parseFloat(data[5].replace(",",".")),-Float.parseFloat(data[3].replace(",","."))),RGBColor.RED);
				
			
					//ESTAS FUNCIONES YA ACTUALIZAN LA LISTA POINTS3D//
					view3D.removeAllPoints(points3D);
					view3D.addPoint3D(point,points3D);
				}
				
				else
					view3D.removeAllPoints(points3D);
				
				itemPreview=item;
			
		}
		
	}
	
	public class SimulateFunction implements OnClickListener
	{


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!listPathPoints.isEmpty())
				{
					
					int size = listPathPoints.getItems().size();
					SimpleVector[] pathPoints= new SimpleVector[size];
					for(int i=0;i<size;i++)
					{
						String[] data=listPathPoints.getItems().get(i).getName();
						pathPoints[i]=new SimpleVector(Float.parseFloat(data[3].replace(",",".")),Float.parseFloat(data[4].replace(",",".")),Float.parseFloat(data[5].replace(",",".")));
					}
					
				
					threadSimulation=new ThreadSimulation(robot,pathPoints,speedValue);
					threadSimulation.start();		
			
				}
		}
		
		
	}
	
	public class IncreaseSpeedFunction implements ButtonPressed
	{

		private int sum=1;
		@Override
		public void increaseValue() {
			
			speedValue+=sum;
			if(threadSimulation!=null)
				if(threadSimulation.isAlive())threadSimulation.setSpeed(speedValue);
			speed.setInfoText(String.valueOf(speedValue));
		}

		@Override
		public void decreaseValue() {
			// TODO Auto-generated method stub
			speedValue-=sum;
			if(threadSimulation!=null)
				if(threadSimulation.isAlive())threadSimulation.setSpeed(speedValue);
			speed.setInfoText(String.valueOf(speedValue));
		}

	
		
	}
	public class CreateRightView implements ButtonsImplementation
	{
		
		@Override
		public void setButtons()
		{
		
		
		
		String[] hola={"Hola"};
		
		
		
		
		listPath.setTittle("Paths Recorded",R.drawable.shape_orange,getResources().getColor(R.color.m_white));
		listPath.addSubtittle(R.color.m_orange_l1, getResources().getColor(R.color.m_white),"Number","Name's Path");
		listPath.addItem(new Widget_ListView_Item(getResources().getDrawable(R.drawable.hammer_ico_small),
				hola,null));
		}
	
		
	}
	
	class ThreadSimulation extends Thread
	{
	
		private Robot3DOF robot;
		private SimpleVector[] path;
		private Integer speed;
		
		
		public ThreadSimulation(Robot3DOF robot, SimpleVector[] pathSimulated,int speed) {
			super();
			this.robot = robot;
			this.path = pathSimulated;
			this.speed=speed;
		}
	
		
	
		@Override
		public void run() 
		{
			float step;	
			float initPosX,initPosY,initPosZ;
			float difX,difY,difZ;
			
			int size=path.length;
			
			robot.moveToolTo(Robot3DOF.INITPOSX, Robot3DOF.INITPOSY, Robot3DOF.INITPOSZ);
			for (int i=0;i<size;i++)
			{
				
				
				initPosX=robot.getToolCoord().x;
				initPosY=robot.getToolCoord().y;
				initPosZ=robot.getToolCoord().z;
				
				difX=path[i].x-initPosX;
				difY=path[i].y-initPosY;
				difZ=path[i].z-initPosZ;
				
				step=0.01f;
				
				float max=Math.abs(Math.max(Math.abs(difX/step), Math.abs(difY/step)));
				max=Math.abs(Math.max(max, Math.abs(difZ/step)));
				
				for(int x=0;x<=max;x++)
				{
				
					
						robot.moveToolTo(initPosX+difX*(x/max),initPosY+difY*(x/max),initPosZ+difZ *(x/max));
						
						synchronized(speed)
						{
						try {
							Thread.sleep(100/speed);
						} catch (InterruptedException e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
				}
			
			}
		}
	
	
		
		public int getSpeed() {
			return speed;
		}
	
	
		public void setSpeed(Integer speed) {
			this.speed = speed;
		}
	
	
	
		
		public void setPath(SimpleVector[] path) {
			this.path = path;
		}
		
	}

	static public void changeCornersButtons(View v,int upLeft,int upRight,int downRight,int downLeft)
	{
		GradientDrawable g;
		float[] radi={upLeft,upLeft, upRight, upRight,downRight,downRight,downLeft,downLeft};
		
		if(v.getBackground() instanceof StateListDrawable)
		{
			StateListDrawable d=(StateListDrawable)v.getBackground();
			for(int i=0;i<d.getState().length;i++)
			{
				d.selectDrawable(i);
				d.invalidateSelf();
				g=(GradientDrawable)d.getCurrent();
				g.mutate();
				g.setCornerRadii(radi);
				g.invalidateSelf();
			}
		}
		
		else
		{
			g=(GradientDrawable)v.getBackground();
			g.mutate();
			g.setCornerRadii(radi);
			g.invalidateSelf();
		}
	}

	private void updateInfoRobot()
	{
		wQ1.setInfoText(String.format("%.2f",robot.getRobotConfAt(0).doubleValue()));
		wQ2.setInfoText(String.format("%.2f",robot.getRobotConfAt(1).doubleValue()));
		wQ3.setInfoText(String.format("%.2f",robot.getRobotConfAt(2).doubleValue()));
		wX.setInfoText(String.format("%.2f",robot.getToolCoord().x));
		wY.setInfoText(String.format("%.2f",robot.getToolCoord().y));
		wZ.setInfoText(String.format("%.2f",robot.getToolCoord().z));
		
	}

	private void createMainView()
	{

	
		window=(Window3D)findViewById(R.id.robotFrame);
		view3D=window.getView3D();
		robot=new Robot3DOF(window.getView3D().getRenderer().getWorld());
	    robot.scaleRobot(0.1f);
		
		
		wQ1=(Widget_IncrementalButtons)findViewById(R.id.widgetQ1);
		wQ1.create(false);
		wQ1.setActionPressed(new ButtonPressedConf(0));
		wQ1.setInfoText(String.format("%.2f", robot.getRobotConfAt(0)));
		wQ1.setInfo2Text("Q1");
		wQ1.roundButton(Widget_IncrementalButtons.ROUND_RIGHT, 40);
		
	
		
		wQ2=(Widget_IncrementalButtons)findViewById(R.id.widgetQ2);
		wQ2.create(false);
		wQ2.setActionPressed(new ButtonPressedConf(1));
		wQ2.setInfoText(String.format("%.2f", robot.getRobotConfAt(1)));
		wQ2.setInfo2Text("Q2");
		wQ2.roundButton(Widget_IncrementalButtons.ROUND_RIGHT, 40);
		
	
		
		wQ3=(Widget_IncrementalButtons)findViewById(R.id.widgetQ3);
		wQ3.create(false);
		wQ3.setActionPressed(new ButtonPressedConf(2));
		wQ3.setInfoText(String.format("%.2f", robot.getRobotConfAt(2)));
		wQ3.setInfo2Text("Q3");
		wQ3.roundButton(Widget_IncrementalButtons.ROUND_RIGHT, 40);
		
		
		
		wX=(Widget_IncrementalButtons)findViewById(R.id.widgetX);
		wX.create(true);
		wX.setActionPressed(new ButtonPressedPos(0));
		wX.setInfoText(String.format("%.2f", robot.getRobotConfAt(2)));
		wX.setInfo2Text("X");
		wX.roundButton(Widget_IncrementalButtons.ROUND_LEFT, 40);
		
		
		wY=(Widget_IncrementalButtons)findViewById(R.id.widgetY);
		wY.create(true);
		wY.setActionPressed(new ButtonPressedPos(1));
		wY.setInfoText(String.format("%.2f", robot.getRobotConfAt(2)));
		wY.setInfo2Text("Y");
		wY.roundButton(Widget_IncrementalButtons.ROUND_LEFT, 40);
		
		
		wZ=(Widget_IncrementalButtons)findViewById(R.id.widgetZ);
		wZ.create(true);
		wZ.setActionPressed(new ButtonPressedPos(2));
		wZ.setInfoText(String.format("%.2f", robot.getRobotConfAt(2)));
		wZ.setInfo2Text("Z");
		wZ.roundButton(Widget_IncrementalButtons.ROUND_LEFT, 40);
		
		
		speed=(Widget_IncrementalButtons)findViewById(R.id.spButton);
		speed.create(false);
		speed.setActionPressed(new IncreaseSpeedFunction());
		speed.setInfoText(String.valueOf(speedValue));
		speed.setInfo2Text(null);
		speed.roundButton(Widget_IncrementalButtons.ROUND_ALL, 40);
		

		
		rButton=(Button)findViewById(R.id.rButton);
		pButton=(Button)findViewById(R.id.pButton);
		sButton=(Button)findViewById(R.id.sButton);
		
		
		
		rButton.setOnClickListener(new RecordPointFunction());
		pButton.setOnClickListener(new RecordPathFunction());	
		sButton.setOnClickListener(new SimulateFunction());
			
		changeCornersButtons(rButton,0,40,40,0);
		changeCornersButtons(pButton,0,40,40,0);
		changeCornersButtons(listPoints,40,40,0,0);
		changeCornersButtons(listPathPoints,40,40,0,0);
		changeCornersButtons(listPath,40,40,0,0);
		
		
	}
	
	public Path3D itemsToPath(Widget_ListView listPoints)
	{
		Path3D path=new Path3D();
		PointsSet set=new PointsSet("Default",listPoints.getItems());
		
		int size = set.pathPoints.size();
		SimpleVector[] pathPoints= new SimpleVector[size];
		for(int i=0;i<size;i++)
		{
			String[] data=set.pathPoints.get(i).getName();
			pathPoints[i]=new SimpleVector(Float.parseFloat(data[4].replace(",",".")),-Float.parseFloat(data[5].replace(",",".")),-Float.parseFloat(data[3].replace(",",".")));
		}
		
		Polyline poly=new Polyline(pathPoints,RGBColor.GREEN);
		poly.setWidth(4);
		path.vertex=pathPoints;
		path.line=poly;
		return path;
	}
}
	
	

