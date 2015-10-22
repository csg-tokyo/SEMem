package csg.chung.mrhpc.utils;

public class MapOutputObj {

	private final String mapID;
	private int reduceID;
	private byte[] data;
	
	public MapOutputObj(String mapID, int rID){
		this.mapID = mapID;
		this.reduceID = rID;
	}
	
	public String getMapID(){
		return this.mapID;
	}
	
	public int getReduceID(){
		return this.reduceID;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return this.data;
	}
}