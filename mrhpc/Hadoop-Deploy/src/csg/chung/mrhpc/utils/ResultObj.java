package csg.chung.mrhpc.utils;

public class ResultObj {
	boolean done = false;
	public String mapID;
	public int rID;
	
	public ResultObj(String mapID, int rID){
		this.mapID = mapID;
		this.rID = rID;
		done = false;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public void setDone(){
		this.done = true;
	}
}
