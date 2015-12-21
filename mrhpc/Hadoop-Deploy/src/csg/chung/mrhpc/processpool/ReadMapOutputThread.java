package csg.chung.mrhpc.processpool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadMapOutputThread extends Thread{
	
	public static String buildPath(String hostname, String appID, String mapID, String fileName){
		String path = csg.chung.mrhpc.processpool.FX10.TMP_FOLDER + hostname + "/nm-local-dir/usercache/" + 
						csg.chung.mrhpc.processpool.Configure.USERNAME + "/appcache/" + appID +
						"/output/" + mapID + "/" + fileName;	
		
		return path;
	}	
			
	public static byte[] readMapOutputToByteArray(String hostname, String appID, String mapID, int rID) throws IOException{
		String indexFilePath = buildPath(hostname, appID, mapID, "file.out.index");

		ReadIndex info = new ReadIndex(indexFilePath, rID);
		String path = buildPath(hostname, appID, mapID, "file.out");			
		RandomAccessFile file = new RandomAccessFile(path, "r");
		
		long start = System.currentTimeMillis();
		String log = "ByteBuffer Allocating: " + (System.currentTimeMillis() - start);
		csg.chung.mrhpc.processpool.Configure.setFX10();
		csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);	  		
		
		start = System.currentTimeMillis();
		byte[] data = new byte[(int) info.getLength()];
		file.seek(info.getStart());
		file.read(data);
		file.close();
		
		log = "MapOutput Reading: " + (System.currentTimeMillis() - start);
		csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);	  				
		
		return data;		
	}
	
	public static ByteBuffer readMapOutputToBuffer(String hostname, String appID, String mapID, int rID) throws IOException{
		String indexFilePath = buildPath(hostname, appID, mapID, "file.out.index");

		ReadIndex info = new ReadIndex(indexFilePath, rID);
		String path = buildPath(hostname, appID, mapID, "file.out");			
		RandomAccessFile file = new RandomAccessFile(path, "r");
		
		long start = System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocateDirect((int)info.getLength());		

		String log = "ByteBuffer Allocating: " + (System.currentTimeMillis() - start);
		csg.chung.mrhpc.processpool.Configure.setFX10();
		csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);	  		
		
		start = System.currentTimeMillis();
		
		FileChannel	inChannel = file.getChannel();
		inChannel.position(info.getStart());		
		inChannel.read(buf);		
		inChannel.close();
		file.close();
		
		log = "MapOutput Reading: " + (System.currentTimeMillis() - start);
		csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.SHUFFLE_ENGINE_LOG + hostname + "_" + appID, log);	  		
		
		return buf;		
	}	
}