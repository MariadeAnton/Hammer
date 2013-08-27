package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityConfig extends Activity {
	
	private EditText ip,port;
	private Button accept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration);
		setTitle("Connections Parameters");
		ip=(EditText)findViewById(R.id.confAddress);
		ip.setHint(GeneralParameters.getIp());
		port=(EditText)findViewById(R.id.confPort);
		port.setHint(Integer.toString(GeneralParameters.getPort()));
		accept=(Button)findViewById(R.id.confA);
	
	}
	
	
	public void Modify(View v)
	{
		String IP;
		int PORT;
		if(v==accept)
		{
			
			
			
			if(ip.getText().toString().compareTo("")!=0 && port.getText().toString().compareTo("")!=0)
			{
				IP=ip.getText().toString();
				PORT=Integer.parseInt(port.getText().toString());
				GeneralParameters.setIp(IP);
				GeneralParameters.setPort(PORT);
				finish();
			}
			else
			{
				Toast toast=Toast.makeText(this, "Introduce both parameters or press Cancel",Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show(); 

			}
		}
		
		else
			finish();
		
	}

	
}
