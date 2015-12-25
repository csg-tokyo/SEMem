package csg.chung.mrhpc.processpool;

public class SendingPoolWait {

	public String filePath;
	public long length;
	public long start;
	public int client;
	
	public SendingPoolWait(String filePath, long length, long start, int client){
		this.filePath = filePath;
		this.length = length;
		this.start = start;
		this.client = client;
	}
}
