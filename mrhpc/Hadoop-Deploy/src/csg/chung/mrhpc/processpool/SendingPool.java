package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.util.LinkedList;
import mpi.MPIException;

public class SendingPool {
	public static int POOL_SIZE = 5;
	public static int SLOT_BUFFER_SIZE = 3145728;
	
	private SendingPoolSlot slots[] = new SendingPoolSlot[POOL_SIZE];
	private LinkedList<SendingPoolWait> waiting;
	
	public SendingPool(){
		for (int i=0; i < POOL_SIZE; i++){
			slots[i] = new SendingPoolSlot(SLOT_BUFFER_SIZE, i);
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
			SendingPoolWait newReading = new SendingPoolWait(mapID, rID, client);
			synchronized (waiting) {
				waiting.push(newReading);
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
}
