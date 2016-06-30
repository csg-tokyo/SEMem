package csg.chung.mrhpc.extranode;

import java.io.IOException;
import java.nio.ByteBuffer;

import csg.chung.mrhpc.processpool.Configure;
import csg.chung.mrhpc.processpool.ShuffleManager;
import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

public class ExtranodeMgr {
	// Store status of each extra node
	private int extraNodeDataStatus[];
	private int roundRobinCount = 0;
	
	public ExtranodeMgr(){
		this.extraNodeDataStatus = new int[Configure.NUMBER_OF_EXTRA_NODE - 1];
		for (int i=0; i < extraNodeDataStatus.length; i++){
			this.extraNodeDataStatus[i] = Constants.AVAILABLE;
		}
	}
	
	public void waitingNonblocking() throws MPIException, IOException{
		ByteBuffer buf = ByteBuffer.allocateDirect(ShuffleManager.RECV_BUFFER_CAPACITY);
		Request req = null;
		
		while (true){
			if (req == null){
				buf.clear();
				req = MPI.COMM_WORLD.iRecv(buf, buf.capacity(), MPI.BYTE, MPI.ANY_SOURCE, Constants.EXCHANGE_MSG_TAG);
			}

			Status status = req.testStatus();
			if (status != null) {
				String cmd = Lib.getStringByNumberOfCharacters(buf,
							status.getCount(MPI.BYTE) / Lib.getUTF_16_Character_Size());
				req = null;

				String split[] = cmd.split(Constants.SPLIT_REGEX);
				if (split[0].equals(Constants.CMD_CHECK_SPACE)) {
					int client = status.getSource();
					int nodeData = findExtraNode();
					String msg = Constants.CMD_CHECK_SPACE + Constants.SPLIT_REGEX + nodeData;
					
					ByteBuffer bufSend = ByteBuffer.allocateDirect(ShuffleManager.RECV_BUFFER_CAPACITY);
					bufSend.position(0);
					Lib.putString(bufSend, msg);
					MPI.COMM_WORLD.iSend(bufSend, Lib.getStringLengthInByte(msg), MPI.BYTE, client, Constants.EXCHANGE_MSG_TAG);
				}
			}	
		}
	}	
	
	public int findExtraNode(){
		while (true){
			if (extraNodeDataStatus[roundRobinCount] == Constants.AVAILABLE){
				return roundRobinCount;
			}else{
				roundRobinCount = (roundRobinCount + 1) % (Configure.NUMBER_OF_EXTRA_NODE - 1);
			}
		}
	}
	
	public static void main(String args[]) throws MPIException{
		MPI.Init(args);
		Lib.printNodeInfo(MPI.COMM_WORLD.getRank(), MPI.COMM_WORLD.getSize());
		MPI.Finalize();
	}
}
