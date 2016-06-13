package csg.chung.mrhpc.utils;

public class ResultObj {
	boolean done = false;
	
	public ResultObj(){
		done = false;
	}
	
	public boolean isDone(){
		return done;
	}
	
	public void setDone(){
		this.done = true;
	}
}
