package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.ResultObj;

public class SendingPoolSlot {
	private ByteBuffer buffer;
	private int status;
	private int id;
	
	String filePath;
	long length;
	long start;
	int client;
	String mapID;
	int rID;
	long startTime;
	long originStartTime;
	
	Request req = null;
	AsynchronousFileChannel channel;
	
	public SendingPoolSlot(int bufferSize, int id){
		buffer = ByteBuffer.allocateDirect(bufferSize);
		status = Constants.FREE;
		this.id = id;
		ShuffleManager.result[id] = null;
	}
	
	public boolean checkFree(){
		return status == Constants.FREE ? true : false;
	}
	
	public void assignTask(SendingPoolWait w){
		status = Constants.BUSY;
		new AssignTaskThread(w).start();
	}
	
	class AssignTaskThread extends Thread{
		SendingPoolWait w;
		
		public AssignTaskThread(SendingPoolWait w){
			this.w = w;
		}
		
		@Override
		public void run(){
				filePath = w.filePath;
				length = w.length;
				start = w.start;
				client = w.client;
				mapID = w.mapID;
				rID = w.rID;
				originStartTime = w.startTime;
				
				startTime = System.currentTimeMillis();
				
				ShuffleManager.result[id] = new ResultObj(mapID, rID);
				buffer = ShuffleManager.mapOutputList.find(mapID, rID);
				ShuffleManager.result[id].setDone();
		}
	}
	
	public void progress() throws MPIException, IOException{
		if (ShuffleManager.result[id] != null && ShuffleManager.result[id].isDone()){
			// iSend here
			//System.out.println(MPI.COMM_WORLD.getRank() + ": " + mapID + "-" + rID);
			System.out.println(MPI.COMM_WORLD.getRank() + " Reading: " + (System.currentTimeMillis() - startTime));
			req = MPI.COMM_WORLD.iSend(buffer, (int) length, MPI.BYTE, client, Constants.DATA_TAG);
			ShuffleManager.result[id] = null;
		}
		
		if (req != null){
			// Check isend here and reset status
			Status sendStatus = req.testStatus();
			if (sendStatus != null){
				status = Constants.FREE;
				ShuffleManager.mapOutputList.remove(mapID, rID);
				req = null;
				System.out.println(MPI.COMM_WORLD.getRank() + " Sending: " + (System.currentTimeMillis() - startTime));	
				System.out.println(MPI.COMM_WORLD.getRank() + " All: " + (System.currentTimeMillis() - originStartTime));	
			}
		}
	}
}
