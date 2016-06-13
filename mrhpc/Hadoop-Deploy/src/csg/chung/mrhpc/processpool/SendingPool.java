package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.util.LinkedList;
import mpi.MPIException;

public class SendingPool {
	public static int POOL_SIZE = 5;
	public static int SLOT_BUFFER_SIZE = 10*1024*1024;
	
	private SendingPoolSlot slots[] = new SendingPoolSlot[POOL_SIZE];
	private LinkedList<SendingPoolWait> waiting;
	
	public SendingPool(){
		for (int i=0; i < POOL_SIZE; i++){
			slots[i] = new SendingPoolSlot(SLOT_BUFFER_SIZE);
		}
		
		waiting = new LinkedList<SendingPoolWait>();
	}
	
	public void addToWaitList(String hostname, String appID, String mapID, int rID, int client){
		new WaitListThread(hostname, appID, mapID, rID, client).start();
	}
	
	class WaitListThread extends Thread{
		String hostname;
		String appID;
		String mapID;
		int rID;
		int client;
		
		public WaitListThread(String hostname, String appID, String mapID, int rID, int client){
			this.hostname = hostname;
			this.appID = appID;
			this.mapID = mapID;
			this.rID = rID;
			this.client = client;
		}
		
		@Override
		public void run() {
			try {
				String indexFilePath = buildPath(hostname, appID, mapID,
						"file.out.index");

				ReadIndex info = new ReadIndex(indexFilePath, rID);
				String path = buildPath(hostname, appID, mapID, "file.out");
				long length = info.getLength();
				long start = info.getStart();

				SendingPoolWait newReading = new SendingPoolWait(mapID, rID, path, length, start, client);
				synchronized (waiting) {
					waiting.push(newReading);
				}
			} catch (IOException e) {

			}
		}
	}	
	
	public void progress() throws IOException, MPIException{
		// Progress
		for (int i=0; i < POOL_SIZE; i++){
			slots[i].progress();
		}
		
		// Pop from waiting list
		if (!waiting.isEmpty()){
			for (int i=0; i < POOL_SIZE; i++){
				if (slots[i].checkFree()){
					synchronized (waiting) {
						slots[i].assignTask(waiting.pop());						
					}
				}
				
				if (waiting.isEmpty()){
					break;
				}
			}
		}
	}
	
	public static String buildPath(String hostname, String appID, String mapID, String fileName){
		String path = csg.chung.mrhpc.processpool.FX10.TMP_FOLDER + hostname + "/nm-local-dir/usercache/" + 
						csg.chung.mrhpc.processpool.Configure.USERNAME + "/appcache/" + appID +
						"/output/" + mapID + "/" + fileName;	
		
		return path;
	}	
}
