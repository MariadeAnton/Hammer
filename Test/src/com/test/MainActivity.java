package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;



public class MainActivity extends Activity {

	static String applicationFolder="/Hammer/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
	
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void ScratchGLTest(View view)
	{
		Intent i=new Intent(this,ActivityScratching.class);
		startActivity(i);
	
		
	}
	
	
	public void Configuration(View view)
	{
		Intent i=new Intent(this,ActivityConfig.class);
		startActivity(i);
	}
	
	public void Program(View view)
	{
		Intent i=new Intent(this,ActivityProgram.class);
		startActivity(i);
	}
	
	public void Loader(View view)
	{
		Intent i=new Intent(this,ActivityLoader.class);
		startActivity(i);
	}
	
	public void Environment(View view)
	{
		Intent i=new Intent(this,ActivityEnvironment.class);
		startActivity(i);
	}
	
	public void Targets(View view)
	{
		Intent i=new Intent(this,ImageTargets.class);
		startActivity(i);
	}
	
	public void Exit(View view)
	{
		finish();
	}
	public void Robot(View view)
	{
		Intent i=new Intent(this,ActivityRobot.class);
		startActivity(i);
	}
	public void Graphics(View view)
	{
		Intent i=new Intent(this,ActivityGraphics.class);
		startActivity(i);
	}
	
}
