package csg.chung.mrhpc.deploy.test;
import java.net.InetAddress;
import java.net.UnknownHostException;

import mpi.MPI;
import mpi.MPIException;
import mpi.Info;

public class Parent {

	public static void main(String args[]){
		try {
			MPI.Init(args);
			int rank = MPI.COMM_WORLD.getRank();
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Parent " + rank + ": " + ip.getHostName() + " - " + ip.getHostAddress());
			
			// Spawn
			if (rank == 0){
				Info master = new Info();
				master.set("host", "master");
				master.set("soft", "soft_limits");
				
				Info slave1 = new Info();
				slave1.set("host", "slave1");
				slave1.set("soft", "soft_limits");	
				
				String commands[] = {"nodemanager.sh"};
				String params[][] = {{}};
				int procs[] = {1};
				Info infos[] = {slave1};
				
				MPI.COMM_WORLD.spawnMultiple(commands, params, procs, infos, 0, null);			
			}
			
			MPI.Finalize();
		} catch (MPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}