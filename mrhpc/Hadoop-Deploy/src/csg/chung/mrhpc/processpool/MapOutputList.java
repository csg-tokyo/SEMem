package csg.chung.mrhpc.processpool;

import java.util.ArrayList;
import java.util.List;

import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import csg.chung.mrhpc.utils.MapOutputObj;

public class MapOutputList {
	private List<MapOutputObj> mapOutputList;
	
	public MapOutputList(){
		this.mapOutputList = new ArrayList<MapOutputObj>();		
	}
	
	public synchronized boolean checkFull(){
		return (int)(((double)SendingPool.SLOT_BUFFER_SIZE / Constants.ONE_MB) * mapOutputList.size()) < Configure.MAXIMUM_DIRECT_MEMORY ? false : true;
	}
	
	public synchronized void add(MapOutputObj obj){
		mapOutputList.add(obj);
	}	
	
	public synchronized void remove(String mapID, int rID){
		for (int i=0; i < mapOutputList.size(); i++){
			if (mapOutputList.get(i).getMapID().equals(mapID) && mapOutputList.get(i).getReduceID() == rID){
				mapOutputList.remove(i);
				break;
			}
		}		
	}	
	
	public synchronized MapOutputObj find(String mapID, int rID) {
		for (int i = 0; i < mapOutputList.size(); i++) {
			if (Lib.checkStringEqual(mapOutputList.get(i).getMapID(), mapID) && mapOutputList.get(i).getReduceID() == rID) {
				return mapOutputList.get(i);
			}
		}
		
		return null;
	}
	
	public int getSize(){
		return this.mapOutputList.size();
	}
}