package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Action;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameters;

public class Computer {

	private static final Log log = LogFactory.getLog(Computer.class);
	
  public static Parameters computeRequirements(Action action) {
  	OperationGraph graph = new OperationGraph(action);
  	log.debug("TAILS... " + graph.getTailsOf(null).size());

		/*
		 * Parameters actionResult = action.getProvidedParams(); Set<OperationNode>
		 * links = action.getNextLinks(); Set<Parameters> requirements = new
		 * HashSet<Parameters>();
		 * 
		 * for (OperationNode link : links) { computeRequirements(link, actionResult); }
		 */
    return new Parameters();
  }
}
