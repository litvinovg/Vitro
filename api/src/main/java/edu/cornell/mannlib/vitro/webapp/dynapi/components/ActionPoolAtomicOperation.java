package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import edu.cornell.mannlib.vitro.webapp.dynapi.ProcedurePool;

public class ActionPoolAtomicOperation extends PoolAtomicOperation {

	public ActionPoolAtomicOperation(){
		this.pool = ProcedurePool.getInstance();
	}
}
