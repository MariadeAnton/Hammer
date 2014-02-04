package com.test;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Widget_ListView extends FrameLayout{

		
	private TextView tittle;
	private LinearLayout subtittle;
	private LinearLayout subtittleLay;
	private TextView subIcon;
	private ListView list;
	private ArrayList<Widget_ListView_Item> items=new ArrayList<Widget_ListView_Item>();
	private Widget_ListView_Adapter adapter;
	public Widget_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
		// TODO Auto-generated constructor stub
	}

	public Widget_ListView(Context context) {
		super(context);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
		// TODO Auto-generated constructor stub
	}

	public Widget_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onCreate(inflater);
		// TODO Auto-generated constructor stub
	}
	
	public void addSubtittle(int background,int colorText,String subIco,String... sub)
	{
		int length=sub.length;
		if(subIco!=null)
		{
			subIcon.setText(subIco);
			subIcon.setTextColor(colorText);
			subIcon.setGravity(Gravity.CENTER);
		}
		else
			subIcon.setVisibility(View.GONE);
		for(int i=0;i<length;i++)
		{	
			TextView text=new TextView(getContext());
			text.setText(sub[i]);
			text.setTextColor(colorText);
			text.setGravity(Gravity.CENTER);
			text.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));
			subtittle.addView(text);
		}
		subtittleLay.setVisibility(View.VISIBLE);
		subtittleLay.setBackgroundResource(background);
	}
	
	public void setTittle(String name,int shape, int colorText)
	{
		tittle.setText(name);
		setBackgroundResource(shape);
		tittle.setTextColor(colorText);
	}
	public void changeNameTittle(String name)
	{
		tittle.setText(name);
	}
	
	public void addItem(Widget_ListView_Item  item)
	{
		items.add(item);
		adapter.notifyDataSetChanged();
		
		
	}
	
	public void deleteItem(Widget_ListView_Item  item)
	{
		items.remove(item);
		adapter.notifyDataSetChanged();
	}
	
	public void deleteItem(int pos)
	{
		items.remove(pos);
		adapter.notifyDataSetChanged();
	}
	
	private void onCreate(LayoutInflater inflater)
	{
	
		FrameLayout layout=(FrameLayout)inflater.inflate(R.layout.widget_list,null);
		list=(ListView)layout.findViewById(R.id.list);
	//	list.setSelector((R.color.m_orange_d1));
		
		tittle=(TextView)layout.findViewById(R.id.tittle);
		subtittleLay=(LinearLayout)layout.findViewById(R.id.subtittleLay);
		subtittleLay.setVisibility(View.GONE);
		subtittle=(LinearLayout)layout.findViewById(R.id.subtittle);
		subIcon=(TextView)layout.findViewById(R.id.sub_ico);
		adapter=new Widget_ListView_Adapter((Activity)getContext(),items,this);
		list.setAdapter(adapter);
		
		addView(layout);
	}
	
	public void setBackgroundItemColor(int color)
	{
		list.setBackgroundColor(color);
	}

	
	public ArrayList<Widget_ListView_Item> getItems() {
		return items;
	}
	
	public void removeAllItems()
	{
		items.removeAll(items);
		adapter.notifyDataSetChanged();
	}
	
	public void setOnItemClickListener(OnItemClickListener listen)
	{
		list.setOnItemClickListener(listen);
	}
	
	  
	public void restoreBackgroundsItems()
	    {
	    	int size=adapter.getCount();
	    	for(int i=0;i<size;i++)
	    	{
	    		View view =adapter.getView(i,null,null);
	    		view.setBackground(getResources().getDrawable(R.drawable.m_blue));
	    		view.invalidate();
	    		view.requestLayout();
	    		adapter.notifyDataSetChanged();
	    		
	    	}
	    }
	
	public boolean isEmpty()
	{
		return list.getChildCount()==0?true:false;
			
	}
	
	public void setOnItemLongClickListener(OnItemLongClickListener listener)
	{
		list.setOnItemLongClickListener(listener);
			
	}
	
	public void setChoiceMode(int choiceMode)
	{
		list.setChoiceMode(choiceMode);
	}
	
	public void getItemIdAtPosition(int pos)
	{
		list.getItemIdAtPosition(pos);
	}
/*
	public class ScrollGoodWayFunction implements OnScrollListener
	{

		private View hightlight=null;
		private Drawable draw=null;
		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
			
			switch (scrollState) 
			{
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		        
					view.setSelector(android.R.color.transparent);
					break;
					
				case OnScrollListener.SCROLL_STATE_IDLE:
					view.setSelector(view.getResources().getDrawable(R.color.m_orange_d1));
					
			}
		}

		public void setHightlight(View hightlight) {
			this.hightlight = hightlight;
			draw=hightlight.getBackground();
		}
		 
		
	}
/*	
	public void setHightlightView(View view)
	{
		stateHightlight.setHightlight(view);
	}
*/
}
