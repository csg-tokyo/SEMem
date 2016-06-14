package csg.chung.mrhpc.processpool;

public class SendingPoolWait {
	public String mapID;
	public int rID;
	public int client;
	public long startTime;
	
	public SendingPoolWait(String mapID, int rID, int client){
		this.mapID = mapID;
		this.rID = rID;
		this.client = client;
		startTime = System.currentTimeMillis();
	}
}
