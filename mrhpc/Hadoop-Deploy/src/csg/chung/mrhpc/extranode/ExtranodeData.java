package csg.chung.mrhpc.extranode;

import csg.chung.mrhpc.processpool.ShuffleManager;

public class ExtranodeData extends ShuffleManager{

	public ExtranodeData(int rank) {
		super(rank);
		this.ID = "ExtranodeData";
	}

}
