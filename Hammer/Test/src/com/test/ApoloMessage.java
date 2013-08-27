package com.test;

import java.nio.ByteBuffer;


public class ApoloMessage {
	
	
	public static char AP_NONE='0';
	static char AP_SETJOINTS='J';
	public static char AP_PLACE ='P';
	static char AP_CHECKJOINTS ='j';
	static char AP_UPDATEWORLD= 'U';
	static char AP_TRUE ='T';
	static char AP_FALSE ='F';
	static char AP_PLACE_WB ='p';
	static char AP_MOVE_WB= 'm';
	static char AP_GETLOCATION ='G';
	static char AP_GETLOCATION_WB ='g';
	static char AP_DVECTOR ='D';
	static final char MOVE_ROBOT= 'L';
	static final char INIT_POSE= 'I';
	static final char SPEED= 'S';
	
	
	
	byte[] pData=new byte[500];//pointer to a byte secuence that has a message (header+size+type+specific data)
	byte[] world=new byte[500],name=new byte[500],bindata=new byte[500]; //utility fields to avoid reinterpretation
	int size=0;
	char type;
	
	
	ApoloMessage(byte[]buffer,int size,char type)
	{
		byte[] aux=new byte[500];
		pData=buffer; 
		this.size=size;
		this.type=type;
		if(type==AP_NONE)return;
		switch(type)
		{//command with world and name
			case 'J':
			case 'P':
			case 'G':
			
				if(pData[5]!=0){
					System.arraycopy(pData,6,world,0,pData[5]);
					System.arraycopy(pData,6+pData[5],aux,0,pData.length-(6+pData[5]));
				}else System.arraycopy(pData,6,aux,0,pData.length-6);
				
				if(aux[0]!=0){
					System.arraycopy(aux,1,name,0,aux[0]);
					System.arraycopy(name,aux[0],aux,0,aux.length-name[0]);
				}else System.arraycopy(aux,1,aux,0,aux.length-1);;
				bindata=aux;
			break;
			case 'U'://commands with world only
				if(pData[5]!=0){
					System.arraycopy(pData,6,world,0,pData[5]);
					System.arraycopy(pData,6+pData[5],aux,0,pData.length-(6+pData[5]));
				}else System.arraycopy(pData,6,aux,0,pData.length-6);
				bindata=aux;
			break;
			default: //commands without world neither
				System.arraycopy(pData,7,bindata,0,pData.length-7);
			break;
		}
	}
	
	
	
	static int writeString(byte [] buffer, byte [] cad,int begin){
		int len;
		final int n=begin;
		if(cad!=null)
		{ //not null
			if(cad[0]==0)	buffer[begin++]=0;//empty string
			else
			{
				len=1+cad.length;
				buffer[begin++]=(byte)((len>255)?255:len);
				for(int i=0;i<len-1;i++)buffer[begin++]=cad[i];
				buffer[begin++]=0;
			}
		}
		else buffer[begin++]=0;
		return begin-n;
	}
	


	public static byte[] Double2Byte(double value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble(value);
	    //////////////reorder//////////////
	    byte[] auxbytes=bytes.clone();
	    int j=7;
	    for(int i=0;i<8;i++)bytes[i]=auxbytes[j--];
	    ///////////////////////////
	    return bytes;
	}
	
	public static double Byte2Double(byte[] bytes)
	{
		return ByteBuffer.wrap(bytes).getDouble();	    
	}
	

	//public static double Bytes2Double(char[] bytes) {
	//    return ByteBuffer.wrap(bytes).getDouble();
	//}
	
	static void insertSize(byte[] message, int size)//size including the header
	{
		message[2]=(byte)(size%255);
		message[3]=(byte)(size/255);
	}
	 //tamaño minimo de mensaje es 5
	static int writeHeader(byte [] buffer,char command) //escribe la cabecera 
	{
		buffer[0]='a';
		buffer[1]='a';
		insertSize(buffer,5);
		buffer[4]=(byte)command;
		return 5;
	}

	public static int writeDouble(byte []buffer, double val,int n){
		
		byte[] auxBuffer=Double2Byte(val);
		for (int i=0;i<8;i++)
			buffer[n++]=auxBuffer[i];
		return 8;
	}
	
	public static int writeUpdateWorld(byte[] buffer, byte[] world)
	{
		int n=0;
		n+=writeHeader(buffer,'U');//command
		n+=writeString(buffer,world,n);
		insertSize(buffer,n);
		return n;
	}
	
	public static int writePlaceObject(byte[] buffer, byte[] world,byte[] object, double[] xyzrpy)
	{
		int n=0,i;
		n+=writeHeader(buffer,'P');//command
		n+=writeString(buffer,world,n);//world
		n+=writeString(buffer,object,n);//object
		for(i=0;i<6;i++)
			n+=writeDouble(buffer,xyzrpy[i],n);
		insertSize(buffer,n);
		return n;
	}
	
	public static int writePlaceRobot(byte[] buffer,double[] xyzrpy)
	{
		int n=0,i;
		n+=writeHeader(buffer,'L');//command
		for(i=0;i<6;i++)
			n+=writeDouble(buffer,xyzrpy[i],n);
		insertSize(buffer,n);
		return n;
	}
	
	public static int writeSetSpeed(byte[] buffer,double speed)
	{
		int n=0;
		n+=writeHeader(buffer,'S');//command
		n+=writeDouble(buffer,speed,n);
		insertSize(buffer,n);
		return n;
	}
	
	public static int writeInitialPosition(byte[] buffer,double[] xyzrpy)
	{
		int n=0,i;
		n+=writeHeader(buffer,'I');//command
		for(i=0;i<6;i++)
			n+=writeDouble(buffer,xyzrpy[i],n);
		insertSize(buffer,n);
		return n;
	}
	
	
	
	public static int writeGetLocation(byte[] buffer, byte[] world, byte[] object)
	{
		int n=0;
		n+=writeHeader(buffer,'G');//command
		n+=writeString(buffer,world,n);//world
		n+=writeString(buffer,object,n);//object
		insertSize(buffer,n);
		return n;
	}
	
	public static ApoloMessage getApoloMessage(byte[] buffer, int max)
	{
		int i=0;
		while(i+4<max){
			if(buffer[i]=='a'&&buffer[i+1]=='a')
			{
				int size=buffer[i+2]+buffer[i+3]*255;
				byte  type=buffer[i+4];
				//si el mensaje es correcto... crea el mensaje y situa el puntero al final
				//ojo... el mensaje mantiene unos punteros sobre el buffer original. El mensaje no reserva memoria
				if(i+size<=max){
					System.arraycopy(buffer,i,buffer,0,buffer.length-i);
					ApoloMessage message=new ApoloMessage(buffer,size,(char)type);
					System.arraycopy(buffer,size,buffer,0,buffer.length-size);
					return message;
				}//si no lo es retorna null
				else return null;
				
			}
		i++;
		}
		return null;
	}
	
	double getDoubleAt(int offset)
	{

		byte[] bytes=new byte[8];
		int doubleAt=offset*8;
		for(int i=0;i<8;i++)
			bytes[7-i]=bindata[doubleAt+i];
		return Byte2Double(bytes);
	}

}
