package csg.chung.mrhpc.utils;

import java.nio.ByteBuffer;

public class MapOutputObj {

	private final String mapID;
	private int reduceID;
	private ByteBuffer data;
	private int length;
	
	public MapOutputObj(String mapID, int rID, ByteBuffer data, int length){
		this.mapID = mapID;
		this.reduceID = rID;
		this.data = data;
		this.length = length;
	}
	
	public String getMapID(){
		return this.mapID;
	}
	
	public int getReduceID(){
		return this.reduceID;
	}
		
	public ByteBuffer getData(){
		return this.data;
	}
	
	public int getDataLength(){
		return this.length;
	}
}