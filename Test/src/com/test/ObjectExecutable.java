package com.test;

public interface ObjectExecutable {
	
	static final int MOVE_X=0;
	static final int MOVE_Y=1;
	static final int MOVE_Z=2;
	static final int TURN_X=3;
	static final int TURN_Y=4;
	static final int TURN_Z=5;
		
	static final int FOR_BUTTON=20;
	static final int PRESS_EXECUTE=21;
	static final int IF_CLAUSE=22;
	static final int ELSE_IF=23;
	static final int ELSE_CLAUSE=24;
	static final int DO_WHILE=25;
	static final int WHILE_CLAUSE=26;
	
	static final int GREATER_THAN=40;
	static final int LESS_THAN=41;
	static final int MULTIPLY=42;
	static final int ADD=43;
	static final int SUSTRACT=44;
	static final int DIVIDE=45;
	static final int EQUAL_THAN=46;
	static final int GREATER_EQUAL_THAN=47;
	static final int LESS_EQUAL_THAN=48;
	static final int REASIGN_VALUE=49;
	static final int ADD_ONE=50;
	static final int SUSTRACT_ONE=51;
	static final int ALWAYS=51;
	
	
	static final int CONSTANT=400;
	static final int C_LESS_THAN=401;
	static final int C_MULTIPLY=402;
	static final int C_ADD=403;
	static final int C_SUSTRACT=404;
	static final int C_DIVIDE=405;
	static final int C_EQUAL_THAN=406;
	static final int C_GREATER_EQUAL_THAN=407;
	static final int C_LESS_EQUAL_THAN=408;
	static final int C_GREATER_THAN=409;
	
	static final int  DEBURRING=60;
	static final int INIT_POSE=61;
	
	static final int CONFIRM=80;
	
	public boolean execute(Client client);

}
