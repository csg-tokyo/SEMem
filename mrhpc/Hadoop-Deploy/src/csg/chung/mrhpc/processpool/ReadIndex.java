package csg.chung.mrhpc.processpool;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadIndex {

	private long start;
	private long rawLength;
	private long length;
	
	public ReadIndex(String path, int rID) throws IOException {
		RandomAccessFile file = new RandomAccessFile(path, "r");
		int count = -1;
		try {
			while (true) {
				long l1 = file.readLong();
				long l2 = file.readLong();
				long l3 = file.readLong();				
				count++;
				
				if (count == rID){
					this.start = l1;
					this.rawLength = l2;
					this.length = l3;
					break;
				}
			}
		} catch (EOFException ex) {
		} finally {
			file.close();
		}
	}
	
	public long getStart() {
		return start;
	}

	public long getRawLength() {
		return rawLength;
	}

	public long getLength() {
		return length;
	}

	public static void main(String args[]) throws IOException{
		ReadIndex index = new ReadIndex("/Users/chung/Desktop/file.out.index", 17);
		System.out.println(index.getStart() + "-" + index.getLength());
	}
}