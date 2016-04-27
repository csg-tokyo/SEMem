package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

import csg.chung.mrhpc.utils.Constants;

public class SendingPoolSlot {
	private ByteBuffer buffer;
	private int status;
	
	String filePath;
	long length;
	long start;
	int client;
	long startTime;
	
	Future<Integer> result = null;
	Request req = null;
	AsynchronousFileChannel channel;
	
	public SendingPoolSlot(int bufferSize){
		buffer = ByteBuffer.allocateDirect(bufferSize);
		status = Constants.FREE;
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
			try{
				filePath = w.filePath;
				length = w.length;
				start = w.start;
				client = w.client;
				
				startTime = System.currentTimeMillis();
				Path path = Paths.get(filePath);
				channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);				
				buffer.position(0);
				buffer.limit((int)length);
				result = channel.read(buffer, start);	
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public void progress() throws MPIException, IOException{
		if (result != null && result.isDone()){
			// iSend here
			//System.out.println(MPI.COMM_WORLD.getRank() + " Reading: " + (System.currentTimeMillis() - startTime));
			req = MPI.COMM_WORLD.iSend(buffer, (int) length, MPI.BYTE, client, Constants.DATA_TAG);
			result = null;
		}
		
		if (req != null){
			// Check isend here and reset status
			Status sendStatus = req.testStatus();
			if (sendStatus != null){
				status = Constants.FREE;
				buffer.clear();
				req = null;
				channel.close();
				//System.out.println(MPI.COMM_WORLD.getRank() + " Sending: " + (System.currentTimeMillis() - startTime));				
			}
		}
	}
}
