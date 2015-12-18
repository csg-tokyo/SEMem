package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.nio.ByteBuffer;

import mpi.MPIException;
import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import csg.chung.mrhpc.utils.SendRecv;

public class ShuffleManager {
	private int rank;
	private String hostname;
	
	public ShuffleManager(int rank){
		this.rank = rank;
		this.hostname = Lib.getHostname();
	}
	
	public void waiting() throws IOException, MPIException{
		SendRecv sr = new SendRecv();
		while (true) {
			String cmd = sr.exchangeMsgDes(rank);
			String split[] = cmd.split(Constants.SPLIT_REGEX);
			if (split[0].equals(Constants.CMD_FETCH)) {
				String appID = split[2];
				String mapID = split[3];
				int rID = Integer.parseInt(split[4]);

				ByteBuffer bytes = ReadMapOutputThread.readMapOutputToBuffer(hostname, appID, mapID, rID);
				long start = System.currentTimeMillis();
				sr.exchangeByteSrc_Send(rank, Integer.parseInt(split[1]), bytes, mapID, rID);
				
				String log = "Data Sending: " + (System.currentTimeMillis() - start) + " " + bytes.capacity();
				csg.chung.mrhpc.processpool.Configure.setFX10();
				csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);					
			}
		}
	}
}