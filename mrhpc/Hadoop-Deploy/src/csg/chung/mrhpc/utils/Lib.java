package csg.chung.mrhpc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import mpi.MPI;
import mpi.MPIException;
import csg.chung.mrhpc.processpool.SendingPool;
import csg.chung.mrhpc.utils.Constants;

public class Lib {
	public static String MAP_OUTPUT_DATA = "/group/gc83/c83014/hadoop-mpi-inmemory/deploy/mapoutput.txt";
	public static String MAP_OUTPUT_DATA_ORI = "/group/gc83/c83014/hadoop-mpi-inmemory/deploy/mapoutputOri.txt";	
	public static ByteBuffer bufData = ByteBuffer.allocateDirect(SendingPool.SLOT_BUFFER_SIZE);
	
	public static void writeToFile(String path, ByteBuffer buf) throws IOException{
		FileOutputStream out = new FileOutputStream(path, true);
		out.write(Lib.getByteFromByteBuffer(buf));
		out.close();
	}

	public static void writeToBuffer(ByteBuffer buf, byte[] data, int off, int len){
		buf.put(data, off, len);
	}

	public static void writeIntToBuffer(ByteBuffer buf, int data){
		buf.putInt(data);
	}
	
	public static byte[] getByteFromByteBuffer(ByteBuffer data){
		byte[] result = new byte[data.limit()];
		
		data.position(0);
		for (int i=0; i < data.limit(); i++){
			result[i] = data.get();
		}
		
		return result;
	}
	
	public static void printNodeInfo(int rank, int size){
		try {
			InetAddress ip = InetAddress.getLocalHost();
			long memory = Runtime.getRuntime().maxMemory();
			System.out.println("P" + rank + "/" + size + ": " + ip.getHostName() + " - " + ip.getHostAddress() + " --> memory: " + memory);						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printNodeInfo(int rank, int size, String prefix){
		try {
			InetAddress ip = InetAddress.getLocalHost();
			long memory = Runtime.getRuntime().maxMemory();
			System.out.println(prefix + " P" + rank + "/" + size + ": " + ip.getHostName() + " - " + ip.getHostAddress() + " --> memory: " + memory);						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static String getHostname(){
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}
	
	public static String getHostAddress(){
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}		
	
	public static int getRankFromHost(String hostfile, String host){
		int rank = Constants.UNKNOW_INT;
		
		try {
			FileReader fr = new FileReader(new File(hostfile));
			BufferedReader in = new BufferedReader(fr);
			
			String line;
			while ((line = in.readLine()) != null){
				String split[] = line.split(Constants.SPLIT_REGEX);
				if (split[1].equals(host)){
					rank = Integer.parseInt(split[0]);
				}
			}
			
			in.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rank;
	}	
	
	public static int getRank(){
		try {
			return MPI.COMM_WORLD.getRank();
		} catch (MPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Call bash command
	 * @param command
	 */
	public static void runCommand(String command){		
		ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", command);
		Process process;
		try {
			process = processBuilder.start();
			InputStream stderr = process.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null){
				System.out.println(line);
			}
			process.waitFor();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	
	/**
	 * Call bash command
	 * @param command
	 */
	public static void runCommand(String command, String home){		
		ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", command);
		processBuilder.directory(new File(home));
		Process process;
		try {
			process = processBuilder.start();
			InputStream stderr = process.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null){
				System.out.println(line);
			}
			process.waitFor();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}			
	
	public static String buildCommand(String... args){
		String result = "";
		for (int i=0; i < args.length; i++){
			if (i == args.length - 1){
				result = result + args[i];
			}else{
				result = result + args[i] + Constants.SPLIT_REGEX;
			}
		}
		return result;
	}	
	
	public static void appendToFile(String filename, String data){
		try{
	   		File file =new File(filename);
	
			if(!file.exists()){
				file.createNewFile();
			}
	
			FileWriter fileWritter = new FileWriter(filename,true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			
			bufferWritter.write(data + "\n");
			
			bufferWritter.close();		
			fileWritter.close();
		}catch(IOException ex){
			
		}
	}
	
	public static byte[] readFile(String path, long start, long length) throws IOException{
		RandomAccessFile file = new RandomAccessFile(path, "r");
		byte[] data = new byte[(int) length];
		file.seek(start);
		file.read(data);
		file.close();
		
		return data;
	}		
	
	public static String getString(ByteBuffer b) {
		String data = "";
		char c;
		b.position(0);
		while ((c=b.getChar()) != '\0'){
			data += c;
		}
		
		return data;
	}	
	
	public static ByteBuffer putString(ByteBuffer b, String msg){
		for (int i=0; i < msg.length(); i++){
			b.putChar(msg.charAt(i));
		}
		
		return b;
	}
	
	public static String getStringByNumberOfCharacters(ByteBuffer b, int number) {
		b.position(0);
		String data = "";
		
		for (int i=0; i < number; i++){
			data += b.getChar();
		}
		
		return data;
	}
	
	public static int getStringLengthInByte(String s) throws UnsupportedEncodingException{
		return s.getBytes("UTF-16").length - 2;
	}
	
	public static int getUTF_16_Character_Size(){
		return 2;
	}
}
