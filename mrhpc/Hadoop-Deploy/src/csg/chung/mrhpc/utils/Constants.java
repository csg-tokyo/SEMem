package csg.chung.mrhpc.utils;

public class Constants {
	public static final int ACK_CMD						= 0;
	public static final int ACK_TAG						= 0;	
	
	public static final int EXCHANGE_MSG_CMD			= 1;
	public static final int EXCHANGE_MSG_TAG			= 1;
	
	public static final int DATA_CMD					= 2;
	public static final int DATA_TAG					= 2;	
	
	public static final String[] MEANING = {"ACK_CMD", "EXCHANGE_MSG_CMD", "DATA_CMD"};
	
	public static final String UNKNOW				= "-1";
	
	public static final int UNKNOW_INT				= -1;
		
	public static final String CMD_FREE				= "0";	
	public static final String CMD_SPAWN			= "1";
	public static final String CMD_FETCH			= "2";
	public static final String CMD_FETCH_LIST		= "3";
		
	public final static String SPLIT_REGEX = "@@@";	
		
	public static final long TIME_WAIT_FOR_READING_MAP_OUTPUT = 1;	
}
