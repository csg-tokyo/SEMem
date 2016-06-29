package csg.chung.mrhpc.processpool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RankMap {
	public static String RANK_MAP_FILE = Configure.DEPLOY_FOLDER + "/rankmap";
	
	public static void main(String args[]) throws IOException{
		int NUMBER_OF_NODE  = Integer.parseInt(args[0]);
		
		System.out.println("Writing to file:" + RANK_MAP_FILE);
		FileWriter fw = new FileWriter(RANK_MAP_FILE);
		BufferedWriter out = new BufferedWriter(fw);
		
		for (int i=0; i < NUMBER_OF_NODE; i++){
			if (i < NUMBER_OF_NODE - Configure.NUMBER_OF_EXTRA_NODE){
				for (int j=0; j < Configure.NUMBER_PROCESS_EACH_NODE; j++){
					out.write("(" + i + ")" + "\n");
				}
			}else{
				out.write("(" + i + ")" + "\n");
			}
		}
		
		out.close();
		fw.close();
	}
}
