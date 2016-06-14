package csg.chung.mrhpc.processpool;

public class SendingPoolWait {
	public String mapID;
	public int rID;
	public String filePath;
	public long length;
	public long start;
	public int client;
	public long startTime;
	
	public SendingPoolWait(String mapID, int rID, String filePath, long length, long start, int client){
		this.mapID = mapID;
		this.rID = rID;
		this.filePath = filePath;
		this.length = length;
		this.start = start;
		this.client = client;
		startTime = System.currentTimeMillis();
	}
}
