package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.nio.ByteBuffer;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;
import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import csg.chung.mrhpc.utils.SendRecv;

public class ShuffleManager {
	public static int RECV_BUFFER_CAPACITY = 4*1024;
	
	private int rank;
	private String hostname;
	private SendingPool sendingPool;
	
	public ShuffleManager(int rank){
		this.rank = rank;
		this.hostname = Lib.getHostname();
		this.sendingPool = new SendingPool();
	}
	
	public void waitingNonblocking() throws MPIException, IOException{
		ByteBuffer buf = ByteBuffer.allocateDirect(RECV_BUFFER_CAPACITY);
		Request req = null;
		
		while (true){
			if (req == null){
				req = MPI.COMM_WORLD.iRecv(buf, buf.capacity(), MPI.BYTE, MPI.ANY_SOURCE, Constants.EXCHANGE_MSG_TAG);
			}
			
			Status status = req.testStatus();
			if (status != null){
				String cmd = Lib.getStringByNumberOfCharacters(buf, status.getCount(MPI.BYTE) / Lib.getUTF_16_Character_Size());
				buf.clear();
				req = null;
				
				String split[] = cmd.split(Constants.SPLIT_REGEX);
				if (split[0].equals(Constants.CMD_FETCH)) {
					String appID = split[2];
					String mapID = split[3];
					int rID = Integer.parseInt(split[4]);

					sendingPool.addToWaitList(hostname, appID, mapID, rID, Integer.parseInt(split[1]));
				}				
			}
			
			sendingPool.progress();
		}
	}
	
	public void waitingBlocking() throws IOException, MPIException{
		SendRecv sr = new SendRecv();
		while (true) {
			String cmd = sr.exchangeMsgDes(rank);
			String split[] = cmd.split(Constants.SPLIT_REGEX);
			if (split[0].equals(Constants.CMD_FETCH)) {
				String appID = split[2];
				String mapID = split[3];
				int rID = Integer.parseInt(split[4]);

				//ByteBuffer bytes = ReadMapOutputThread.readMapOutputToBuffer(hostname, appID, mapID, rID);
				byte[] bytes = ReadMapOutputThread.readMapOutputToByteArray(hostname, appID, mapID, rID);
				long start = System.currentTimeMillis();
				sr.exchangeByteSrc_Send(rank, Integer.parseInt(split[1]), bytes, mapID, rID);
				
				String log = "Data Sending: " + (System.currentTimeMillis() - start) + " " + bytes.length;
				csg.chung.mrhpc.processpool.Configure.setFX10();
				csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);					
			}
		}
	}
}