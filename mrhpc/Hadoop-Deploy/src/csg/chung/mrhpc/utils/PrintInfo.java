package csg.chung.mrhpc.utils;

import mpi.MPI;
import mpi.MPIException;

public class PrintInfo {

	public static void main(String args[]) throws MPIException{
		MPI.Init(args);
		Lib.printNodeInfo(MPI.COMM_WORLD.getRank(), MPI.COMM_WORLD.getSize());
		MPI.Finalize();
	}
}
