package com.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.test.AuxBasicVariables.TriplePoint;

public class Client extends Thread {


	
	Activity act=null;
	PositionableObject exe;
	String IP;
	Handler handler=null;
	byte[] out=new byte[500];
	byte[] in=new byte[500];
	int PORT;	
	double values[]={0,0,0,0,0,0};
	double speed=2;
	boolean execute=false,receive=false,disconnected=false;
	Socket myClient=null;
	InputStream inMessage;
	OutputStream outMessage;
	
	
	
	public void setExe(PositionableObject exe) {
		this.exe = exe;
	}


	public void sendData(){
		execute=true;
	}


	Client(Activity act,String IP, int PORT)
	{
		this.IP=IP;
		this.PORT=PORT;
		this.act=act;
		
		
	}
	
	public void setHandler(Handler handler)
	{
		this.handler=handler;
	}

	public Handler getHandler() {
		return handler;
	}


	public void sendInitPos(TriplePoint pos,double speed)
	{
		
	
		this.speed=speed;
		values[0]=pos.x;
		values[1]=pos.y;
		values[2]=pos.z;	
		values[3]=0;
		values[4]=165;
		values[5]=0;
		SendMessage(ApoloMessage.INIT_POSE);
	
	}

	
	
	public void sendPosition(double x, double y, double z,double r,double p,double yw)
	{
		
		
		values[0]=x;
		values[1]=y;
		values[2]=z;	
		values[3]=r;
		values[4]=p;
		values[5]=yw;
		SendMessage(ApoloMessage.MOVE_ROBOT);
	}

	private void SendMessage(char type)
	{
		try 
		{
			
			
			
			InputStream inMessage=myClient.getInputStream();
			OutputStream outMessage = myClient.getOutputStream();

				switch(type)
				{
				
					case ApoloMessage.MOVE_ROBOT:
						ApoloMessage.writePlaceRobot(out,values);
						break;
				
					case ApoloMessage.INIT_POSE:
						ApoloMessage.writeSetSpeed(out, speed);
						outMessage.write(out);
						Thread.sleep(100);
						inMessage.read(in);
						ApoloMessage.writeInitialPosition(out,values);
						break;
					
					
					
				}
				
				outMessage.write(out);
				inMessage.read(in);
				Thread.sleep(600);
				
		}
		catch (IOException e) 
		{
				e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void run() {
		super.run();
		disconnected=false;
		while(!disconnected)
		{
		
			try {
			
				myClient=new Socket(IP,PORT);
				if(myClient!=null)
				{
					inMessage=myClient.getInputStream();
					outMessage = myClient.getOutputStream();
					Runnable message=new Runnable(){
						
						@Override
						public void run() {
					Toast toast=Toast.makeText(act, "Client connected",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					act.invalidateOptionsMenu();
					
						};
					};
					handler.post(message);
					handler.sendEmptyMessage(2);
					
				}
				
				}
					
			
			catch (UnknownHostException e) {		
				e.printStackTrace();
		
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
			while(myClient!=null)
			{	
				
				if(execute)
				{
					exe.execute(this);
					execute=false;
					handler.sendEmptyMessage(0);
				}
				
				
				if(myClient.isClosed())
				{
					Runnable message=new Runnable(){
						
						@Override
						public void run() {
					Toast toast=Toast.makeText(act, "Client disconnected",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					
						};
					};
					handler.post(message);
					handler.sendEmptyMessage(1);
					act.invalidateOptionsMenu();
					break;
				}
				
			
			}
		
		
		
		
		}
		
		act.invalidateOptionsMenu();
		myClient=null;
		
		
		
		
	}
		
	public void disconnect()
	{
		
		try {
			
			myClient.close();
			disconnected=true;
			
			} 
		
		catch (IOException e) {
		
				e.printStackTrace();
			}
	}
	
	public boolean isConnected()
	{
		if(myClient!=null)return !myClient.isClosed();
		else return false;
	}
		
}

