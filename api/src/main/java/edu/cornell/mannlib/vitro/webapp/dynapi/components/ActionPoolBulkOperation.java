package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import edu.cornell.mannlib.vitro.webapp.dynapi.ProcedurePool;

public class ActionPoolBulkOperation extends PoolBulkOperation {

	public ActionPoolBulkOperation(){
		this.pool = ProcedurePool.getInstance();
	}
}
