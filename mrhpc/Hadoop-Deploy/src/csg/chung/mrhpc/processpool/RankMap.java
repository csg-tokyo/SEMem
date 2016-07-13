package csg.chung.mrhpc.processpool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankMap {	
	public static String echoString(int rank, String host){
		String line = "rank " + rank + "=" + host + " slot=0,1,2,3,4,5,6,7,8,9,10,11";
		return line;
	}
	
	public static void main(String args[]) throws IOException{
		Configure.setTsubame();
		String RANK_MAP_FILE = Configure.DEPLOY_FOLDER + "/rankfile";		
		
		List<String> listHost = new ArrayList<String>();
		
		FileReader fr = new FileReader(args[0]);
		BufferedReader in = new BufferedReader(fr);
		
		String line;
		while ((line = in.readLine()) != null){
			listHost.add(line);
		}
		
		in.close();
		fr.close();
		
		int NUMBER_OF_NODE  = listHost.size();
		
		System.out.println("Writing to file:" + RANK_MAP_FILE);

		FileWriter fw = new FileWriter(RANK_MAP_FILE);
		BufferedWriter out = new BufferedWriter(fw);
		
		int rank = 0;
		
		for (int i=0; i < NUMBER_OF_NODE; i++){
			if (i < NUMBER_OF_NODE - Configure.NUMBER_OF_EXTRA_NODE){
				for (int j=0; j < Configure.NUMBER_PROCESS_EACH_NODE; j++){
					out.write(echoString(rank, listHost.get(i)) + "\n");
					rank++;
				}
			}else{
				out.write(echoString(rank, listHost.get(i)) + "\n");
				rank++;
			}
		}
		
		out.close();
		fw.close();
	}
}
