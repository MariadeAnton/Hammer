package com.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class MyHorizontalSeekBar extends SeekBar {

	int previousProgress;
	
	public MyHorizontalSeekBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public int getPreviousProgress() {
		return previousProgress;
	}

	public void setPreviousProgress(int previousProgress) {
		this.previousProgress = previousProgress;
		
	}
}
