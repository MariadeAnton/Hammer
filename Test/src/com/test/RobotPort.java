package com.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class RobotPort extends Thread {

	

	String IP;
	int PORT;	
	byte[] out=new byte[500];
	byte[] in=new byte[500];
	Socket myClient=null;
	InputStream inMessage;
	OutputStream outMessage;
	public int val[]= new int[3];
	public byte[] read=new byte[10];
	boolean disconnect=false;
	
	
	
	


	RobotPort(String IP, int PORT)
	{
		this.IP=IP;
		this.PORT=PORT;
	}


	@Override
	public void run() {
		super.run();
	
		while(!disconnect)
		{
	
				try {
					myClient=new Socket(IP,PORT);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(myClient!=null)
				{
				
					while(!disconnect)
					{
						try {
							inMessage=myClient.getInputStream();
							inMessage.read(in);
							readData();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					try {
						myClient.close();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
			}
		
		disconnect=false;
		
		
		}
		
	
	public void disconnect()
	{
		disconnect=true;
	}


	private void readData()
	{
		
		int begin=0;
		int index=0;
		int z;
		
		
		for(int i=1;i<=in.length;i++)
		{
	
			if(in[i]==',' && i!=1)
			{
				
				val[index++]=convertToInt(read);
				read=new byte[10];
				begin=0;
			}
			else if(i!=1)
				read[begin++]=in[i];
			
			if(index==3)break;
			
		}
	}

	
	public int convertToInt(byte[] byt)
	{
		String string=new String(byt);
		
		 return Integer.valueOf(string.trim());
		
	
		
		
	}

	public int[] getValues()
	{
		return val;
	}
	
}
		
	

