package com.test;

import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;

public class FragmentButtons extends Fragment{

	private MyTouchableLayout mup,mdo,mle,tux,tuy,tuz;
	private MyTouchableLayout pressexe,repeat,ifclause,dowhile,cwhile;
	private MyTouchableLayout greater,less,less_equal,greater_equal,equal,
							 c_greater,c_less,c_less_equal,c_greater_equal,c_equal,always;
	private MyTouchableLayout add,sustract,multiply,divide,addOne,sustractOne,reasign,value
							,c_add,c_sustract,c_multiply,c_divide;
	private MyTouchableLayout sip,deburring;
	private MyTouchableLayout conf;
	private GridLayout l_move,l_control,l_op,l_us;
	private ScrollView l_comp,l_var;
	private Button c_move,c_control,c_comp,c_var,c_op,c_us;
	private FrameLayout buttonsLay;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		onCreateButtons(inflater,container);
		
		return buttonsLay;
	}
	
	
	private void onCreateButtons(LayoutInflater inflater,ViewGroup container)
	{
		
		buttonsLay=(FrameLayout)inflater.inflate(R.layout.frag_buttons,container,false);
		
			
		mup=(MyTouchableLayout)buttonsLay.findViewById(R.id.mup);
		mup.defineValues("MOVE_X", ObjectExecutable.MOVE_X, 3,"ButtonMovement");
		mdo=(MyTouchableLayout)buttonsLay.findViewById(R.id.mdo);	
		mdo.defineValues("MOVE_Y", ObjectExecutable.MOVE_Y, 3,"ButtonMovement");
		mle=(MyTouchableLayout)buttonsLay.findViewById(R.id.mle);
		mle.defineValues("MOVE_Z",  ObjectExecutable.MOVE_Z, 3,"ButtonMovement");
		tux=(MyTouchableLayout)buttonsLay.findViewById(R.id.tux);
		tux.defineValues("TURN_X",  ObjectExecutable.TURN_X,90,"ButtonMovement");
		tuy=(MyTouchableLayout)buttonsLay.findViewById(R.id.tuy);
		tuy.defineValues("TURN_Y",  ObjectExecutable.TURN_Y, 90,"ButtonMovement");
		tuz=(MyTouchableLayout)buttonsLay.findViewById(R.id.tuz);
		tuz.defineValues("TURN_Z", ObjectExecutable.TURN_Z, 90,"ButtonMovement");
	
		
		repeat=(MyTouchableLayout)buttonsLay.findViewById(R.id.buttonrep);
		repeat.defineValues("Repeat",  ObjectExecutable.FOR_BUTTON, 2,"ButtonControl");	
		ifclause=(MyTouchableLayout)buttonsLay.findViewById(R.id.ifcontrol);
		ifclause.defineValues("If",  ObjectExecutable.IF_CLAUSE, 2f,"ButtonControl");
	/*	elseif=(MyTouchableLayout)buttonsLay.findViewById(R.id.elseif);
		elseif.defineValues("ElseIf",  ObjectExecutable.ELSE_IF, 1,"ButtonIfElse");
		elseclause=(MyTouchableLayout)buttonsLay.findViewById(R.id.elseclause);
		elseclause.defineValues("Else",  ObjectExecutable.ELSE_CLAUSE, 2,"ButtonIfElse");
	*/	dowhile=(MyTouchableLayout)buttonsLay.findViewById(R.id.dowhile);
		dowhile.defineValues("Do while",  ObjectExecutable.DO_WHILE,2,"ButtonControl");	
		cwhile=(MyTouchableLayout)buttonsLay.findViewById(R.id.cwhile);
		cwhile.defineValues("While",  ObjectExecutable.WHILE_CLAUSE, 2,"ButtonControl");	
	
		
		
		
		greater=(MyTouchableLayout)buttonsLay.findViewById(R.id.greater);
		greater.defineValues(">",  ObjectExecutable.GREATER_THAN, 2,"ButtonComparer");
		less=(MyTouchableLayout)buttonsLay.findViewById(R.id.less);
		less.defineValues("<",  ObjectExecutable.LESS_THAN, 2,"ButtonComparer");
		greater_equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.greater_equal);
		greater_equal.defineValues(">=",  ObjectExecutable.GREATER_EQUAL_THAN, 2,"ButtonComparer");
		less_equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.less_equal);
		less_equal.defineValues("<=",  ObjectExecutable.LESS_EQUAL_THAN,2,"ButtonComparer");
		equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.equal);
		equal.defineValues("==",  ObjectExecutable.EQUAL_THAN,2,"ButtonComparer");
		c_greater=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_greater);
		c_greater.defineValues(">",  ObjectExecutable.C_GREATER_THAN, 2,"ButtonComparer");
		c_less=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_less);
		c_less.defineValues("<",  ObjectExecutable.C_LESS_THAN, 2,"ButtonComparer");
		c_greater_equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_greater_equal);
		c_greater_equal.defineValues(">=",  ObjectExecutable.C_GREATER_EQUAL_THAN, 2,"ButtonComparer");
		c_less_equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_less_equal);
		c_less_equal.defineValues("<=",  ObjectExecutable.C_LESS_EQUAL_THAN,2,"ButtonComparer");
		c_equal=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_equal);
		c_equal.defineValues("==",  ObjectExecutable.C_EQUAL_THAN,2,"ButtonComparer");
		always=(MyTouchableLayout)buttonsLay.findViewById(R.id.always);
		always.defineValues("Always",  ObjectExecutable.ALWAYS,2,"ButtonComparer");
		
		add=(MyTouchableLayout)buttonsLay.findViewById(R.id.xpy);
		add.defineValues("+",  ObjectExecutable.ADD, 2,"ButtonOperator");
		sustract=(MyTouchableLayout)buttonsLay.findViewById(R.id.xly);
		sustract.defineValues("-",  ObjectExecutable.SUSTRACT, 2,"ButtonOperator");
		multiply=(MyTouchableLayout)buttonsLay.findViewById(R.id.xmy);
		multiply.defineValues("*",  ObjectExecutable.MULTIPLY,2,"ButtonOperator");
		divide=(MyTouchableLayout)buttonsLay.findViewById(R.id.xdy);
		divide.defineValues("/",  ObjectExecutable.DIVIDE, 2,"ButtonOperator");
		c_add=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_xpy);
		c_add.defineValues("+",  ObjectExecutable.C_ADD, 2,"ButtonOperator");
		c_sustract=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_xly);
		c_sustract.defineValues("-",  ObjectExecutable.C_SUSTRACT, 2,"ButtonOperator");
		c_multiply=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_xmy);
		c_multiply.defineValues("*",  ObjectExecutable.C_MULTIPLY,2,"ButtonOperator");
		c_divide=(MyTouchableLayout)buttonsLay.findViewById(R.id.c_xdy);
		c_divide.defineValues("/",  ObjectExecutable.C_DIVIDE, 2,"ButtonOperator");
		
		addOne=(MyTouchableLayout)buttonsLay.findViewById(R.id.xpp);
		addOne.defineValues("++",  ObjectExecutable.ADD_ONE, 2,"ButtonOperator");
		sustractOne=(MyTouchableLayout)buttonsLay.findViewById(R.id.xll);
		sustractOne.defineValues("--",  ObjectExecutable.SUSTRACT_ONE, 2,"ButtonOperator");
		reasign=(MyTouchableLayout)buttonsLay.findViewById(R.id.rvalue);
		reasign.defineValues("=",  ObjectExecutable.REASIGN_VALUE, 2,"ButtonOperator");
		
		value=(MyTouchableLayout)buttonsLay.findViewById(R.id.value);
		value.defineValues("Value",  ObjectExecutable.REASIGN_VALUE, 2,"ButtonVariable");
		
		sip=(MyTouchableLayout)buttonsLay.findViewById(R.id.sip);
		sip.defineValues("Initial Position",  ObjectExecutable.INIT_POSE, 2,"ButtonRobot");
		deburring=(MyTouchableLayout)buttonsLay.findViewById(R.id.deburrng);
		deburring.defineValues("Deburring",  ObjectExecutable.DEBURRING, 2,"ButtonRobot");
		
		conf=(MyTouchableLayout)buttonsLay.findViewById(R.id.conf);
		conf.defineValues("Loop Break",  ObjectExecutable.CONFIRM, 2,"ButtonUserCommand");
		pressexe=(MyTouchableLayout)buttonsLay.findViewById(R.id.pressexe);
		pressexe.defineValues("Start",  ObjectExecutable.PRESS_EXECUTE, 1,"ButtonUserCommand");
	
		
		
		l_move=(GridLayout)buttonsLay.findViewById(R.id.l_move);
		l_control=(GridLayout)buttonsLay.findViewById(R.id.l_control);
		l_comp=(ScrollView)buttonsLay.findViewById(R.id.l_comp);
		l_var=(ScrollView)buttonsLay.findViewById(R.id.l_var);
		l_op=(GridLayout)buttonsLay.findViewById(R.id.l_oper);
		l_us=(GridLayout)buttonsLay.findViewById(R.id.l_user);
		c_move=(Button)buttonsLay.findViewById(R.id.c_move);
		c_control=(Button)buttonsLay.findViewById(R.id.c_control);
		c_comp=(Button)buttonsLay.findViewById(R.id.c_comp);
		c_var=(Button)buttonsLay.findViewById(R.id.c_var);
		c_op=(Button)buttonsLay.findViewById(R.id.c_oper);
		c_us=(Button)buttonsLay.findViewById(R.id.c_user);
		

		mup.setOnTouchListener(new MyTouchListener());
		mdo.setOnTouchListener(new MyTouchListener());
		mle.setOnTouchListener(new MyTouchListener());
		tux.setOnTouchListener(new MyTouchListener());
		tuy.setOnTouchListener(new MyTouchListener());
		tuz.setOnTouchListener(new MyTouchListener());
		pressexe.setOnTouchListener(new MyTouchListener());
		repeat.setOnTouchListener(new MyTouchListener());
		ifclause.setOnTouchListener(new MyTouchListener());
	//	elseif.setOnTouchListener(new MyTouchListener());
	//	elseclause.setOnTouchListener(new MyTouchListener());
		dowhile.setOnTouchListener(new MyTouchListener());
		cwhile.setOnTouchListener(new MyTouchListener());
		greater.setOnTouchListener(new MyTouchListener());
		less.setOnTouchListener(new MyTouchListener());
		less_equal.setOnTouchListener(new MyTouchListener());
		greater_equal.setOnTouchListener(new MyTouchListener());
		equal.setOnTouchListener(new MyTouchListener());
		add.setOnTouchListener(new MyTouchListener());
		sustract.setOnTouchListener(new MyTouchListener());
		multiply.setOnTouchListener(new MyTouchListener());
		divide.setOnTouchListener(new MyTouchListener());
		addOne.setOnTouchListener(new MyTouchListener());
		sustractOne.setOnTouchListener(new MyTouchListener());
		reasign.setOnTouchListener(new MyTouchListener());
		value.setOnTouchListener(new MyTouchListener());
		c_greater.setOnTouchListener(new MyTouchListener());
		c_less.setOnTouchListener(new MyTouchListener());
		c_less_equal.setOnTouchListener(new MyTouchListener());
		c_greater_equal.setOnTouchListener(new MyTouchListener());
		c_equal.setOnTouchListener(new MyTouchListener());
		c_add.setOnTouchListener(new MyTouchListener());
		c_sustract.setOnTouchListener(new MyTouchListener());
		c_multiply.setOnTouchListener(new MyTouchListener());
		c_divide.setOnTouchListener(new MyTouchListener());
		always.setOnTouchListener(new MyTouchListener());
		
		
		
		sip.setOnTouchListener(new MyTouchListener());
		deburring.setOnTouchListener(new MyTouchListener());
		conf.setOnTouchListener(new MyTouchListener());
		
		
		
	}
	
	
	public class MyTouchListener implements OnTouchListener
	{
	
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN)
			{
			if(view instanceof MyTouchableLayout)	
				((MyTouchableLayout) view).onInterceptTouchEvent(event);
			ClipData data=ClipData.newPlainText("", "");
			DragShadowBuilder shadow= new View.DragShadowBuilder(view);
			view.startDrag(data, shadow, view, 0);
			}
			
			return false;
		}
		
	}
	
	public void onChangeCategories(View view)
	{
		l_control.setVisibility(View.INVISIBLE);
		l_comp.setVisibility(View.INVISIBLE);
		l_move.setVisibility(View.INVISIBLE);
		l_var.setVisibility(View.INVISIBLE);
		l_op.setVisibility(View.INVISIBLE);
		l_us.setVisibility(View.INVISIBLE);

		if(view==c_control)
			l_control.setVisibility(View.VISIBLE);
		
		else if(view==c_comp)
			l_comp.setVisibility(View.VISIBLE);
	
		else if(view==c_move)
			l_move.setVisibility(View.VISIBLE);
		
		else if(view==c_var)
			l_var.setVisibility(View.VISIBLE);
		
		else if(view==c_op)
			l_op.setVisibility(View.VISIBLE);
		
		else if(view==c_us)
			l_us.setVisibility(View.VISIBLE);
	}


	
	

	

	
	
	
	
	
	
	
	
}
