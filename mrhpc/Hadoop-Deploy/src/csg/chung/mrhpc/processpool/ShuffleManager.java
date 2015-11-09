package csg.chung.mrhpc.processpool;

import java.io.IOException;
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
	
	public void waiting() throws IOException{
		try {
			SendRecv sr = new SendRecv();						
			while (true) {
					String cmd = sr.exchangeMsgDes(rank);	
					String split[] = cmd.split(Constants.SPLIT_REGEX);
					if (split[0].equals(Constants.CMD_FETCH)){
						String appID = split[2];
						String mapID = split[3];
						int rID = Integer.parseInt(split[4]);
						
						byte[] bytes = ReadMapOutputThread.readMapOutput(hostname, appID, mapID, rID);
						long start= System.currentTimeMillis();
						sr.exchangeByteSrc(rank, Integer.parseInt(split[1]), bytes);
						System.out.println(mapID + " -4 " + rID + ": " + (System.currentTimeMillis() - start));
					}
			}
		} catch (MPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}