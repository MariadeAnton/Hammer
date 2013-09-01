package com.test;

public  class GeneralParameters {
	
	static String ip="192.168.2.6";
	static int port=2344;
	static HammerEnvironment environment=null;
	
	
	
	
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		GeneralParameters.ip = ip;
	}
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		GeneralParameters.port = port;
	}
	public static HammerEnvironment getEnvironment() {
		return environment;
	}
	public static void setEnvironment(HammerEnvironment environment) {
		GeneralParameters.environment = environment;
	}
	
	
	

}
