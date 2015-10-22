package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadMapOutputThread extends Thread{
	
	public static String buildPath(String hostname, String appID, String mapID, String fileName){
		String path = csg.chung.mrhpc.processpool.FX10.TMP_FOLDER + hostname + "/nm-local-dir/usercache/" + 
						csg.chung.mrhpc.processpool.Configure.USERNAME + "/appcache/" + appID +
						"/output/" + mapID + "/" + fileName;	
		
		return path;
	}	
			
	public static byte[] readMapOutput(String hostname, String appID, String mapID, int rID) throws IOException{
		//long start = System.currentTimeMillis();
		String indexFilePath = buildPath(hostname, appID, mapID, "file.out.index");

		ReadIndex info = new ReadIndex(indexFilePath, rID);
		//System.out.println(mapID + " -1 " + rID + ": " + (System.currentTimeMillis() - start));	    	    

		String path = buildPath(hostname, appID, mapID, "file.out");	
		//System.out.println(mapID + " -2 " + rID + ": " + (System.currentTimeMillis() - start));
		
		RandomAccessFile file = new RandomAccessFile(path, "r");
		byte[] data = new byte[(int) info.getLength()];
		file.seek(info.getStart());
		file.read(data);
		file.close();
		
		//System.out.println(mapID + " -3 " + rID + ": " + (System.currentTimeMillis() - start));		
		
		return data;		
	}
}