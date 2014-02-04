package com.test;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MyOrientableSeekBar extends SeekBar {

	private boolean horizontal=false;
	private int previousProgress=0;
	
    public MyOrientableSeekBar(Context context) {
        super(context);
        
    }

    public MyOrientableSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyOrientableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        
    	if(!horizontal)
    	{
    		c.rotate(-90);
    		c.translate(-getHeight(), 0);
    	}
    	else
    	{
    		c.rotate(0);
    		c.translate(-getHeight(), 0);
    		
    	}

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        
        
        if (!isEnabled()) {
            return false;
        }
        
        

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            	if(!horizontal)
            	{
            		setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
            		onSizeChanged(getWidth(), getHeight(), 0, 0);
            		break;
            	}
            	else
            	{
            		setProgress(getMax() - (int) (getMax() * event.getX() / getWidth()));
            		onSizeChanged(getWidth(), getHeight(), 0, 0);
            		break;
            	}
            	
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    
    public void setHorizontal()
    {
    	horizontal=true;
    }

	public int getPreviousProgress() {
		return previousProgress;
	}

	public void setPreviousProgress(int previousProgress) {
		this.previousProgress = previousProgress;
		
	}
}