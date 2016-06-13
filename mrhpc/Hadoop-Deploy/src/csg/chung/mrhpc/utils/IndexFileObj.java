package csg.chung.mrhpc.utils;

public class IndexFileObj {
	public String mapID;
	public int rID;
	public long length;
	public long start;
	
	public IndexFileObj(String mapID, int rID, long length, long start){
		this.mapID = mapID;
		this.rID = rID;
		this.length = length;
		this.start = start;
	}
	
	public long getLength(){
		return this.length;
	}
	
	public long getStart(){
		return this.start;
	}
	
	public int getRID(){
		return rID;
	}
	
	public String getMapID(){
		return mapID;
	}
}
