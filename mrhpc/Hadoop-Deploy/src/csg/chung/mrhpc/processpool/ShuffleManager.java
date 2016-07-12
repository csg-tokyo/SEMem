package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.nio.ByteBuffer;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;
import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import csg.chung.mrhpc.utils.MapOutputObj;
import csg.chung.mrhpc.utils.ResultObj;
import csg.chung.mrhpc.utils.SendRecv;

public class ShuffleManager {
	public String ID = "ShuffleManager";
	public static int RECV_BUFFER_CAPACITY = 4*1024;
	
	private int rank;
	private String hostname;
	private SendingPool sendingPool;
	public static MapOutputList mapOutputList;
	public static ResultObj result[] = new ResultObj[SendingPool.POOL_SIZE];
	
	public ShuffleManager(int rank){
		this.rank = rank;
		this.hostname = Lib.getHostname();
		this.sendingPool = new SendingPool();
		mapOutputList = new MapOutputList();
	}
	
	public void waitingNonblocking() throws MPIException, IOException{
		ByteBuffer buf = ByteBuffer.allocateDirect(SendingPool.SLOT_BUFFER_SIZE);
		Request req = null;
		
		while (true){
			if (req == null){
				buf.clear();
				req = MPI.COMM_WORLD.iRecv(buf, buf.capacity(), MPI.BYTE, MPI.ANY_SOURCE, Constants.EXCHANGE_MSG_TAG);
			}

			Status status = req.testStatus();
			if (status != null) {
				String cmd = Constants.DUMMY_STRING;
				if (status.getCount(MPI.BYTE) < Constants.CMD_FETCH_MAX_LENGTH){
					cmd = Lib.getStringByNumberOfCharacters(buf,
							status.getCount(MPI.BYTE) / Lib.getUTF_16_Character_Size());
				}
				req = null;

				String split[] = cmd.split(Constants.SPLIT_REGEX);
				if (split[0].equals(Constants.CMD_FETCH)) {
					String appID = split[2];
					String mapID = split[3];
					int rID = Integer.parseInt(split[4]);
					System.out.println(ID + ": " + "rID:" + mapID + " - " + rID);
					sendingPool.addToWaitList(hostname, appID, mapID, rID, Integer.parseInt(split[1]));
				}else
				if (split[0].equals(Constants.CMD_CHECK_SPACE)){
					String msg = Constants.CMD_CHECK_SPACE + Constants.SPLIT_REGEX + Constants.AVAILABLE;
					//String msg = Constants.CMD_CHECK_SPACE + Constants.SPLIT_REGEX + Constants.FULL;
					if (mapOutputList.checkFull()){
						msg = Constants.CMD_CHECK_SPACE + Constants.SPLIT_REGEX + Constants.FULL;
					}
					int client = status.getSource();
					
					ByteBuffer bufSend = ByteBuffer.allocateDirect(ShuffleManager.RECV_BUFFER_CAPACITY);
					bufSend.position(0);
					Lib.putString(bufSend, msg);
					MPI.COMM_WORLD.iSend(bufSend, Lib.getStringLengthInByte(msg), MPI.BYTE, client, Constants.EXCHANGE_MSG_TAG);			
				}else
				if (split[0].equals(Constants.CMD_NOTIFY_EXTRA_NODE)){
					MapOutputObj obj = Lib.storeExtraNodeInfo(split[1], Integer.parseInt(split[2]), Integer.parseInt(split[3]));
					mapOutputList.add(obj);
					System.out.println(ID + ": " + "Notify: " + cmd);
				}
				else {
					MapOutputObj obj = Lib.readDataFromBuffer(buf, status.getCount(MPI.BYTE));
					mapOutputList.add(obj);
					System.out.println(ID + ": " + "Store Map ID: " + obj.getMapID() + "-" + obj.getReduceID());
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