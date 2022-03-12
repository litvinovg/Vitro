package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Action;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameters;

public class Computer {

	private static final Log log = LogFactory.getLog(Computer.class);
	
  public static void computeScopes(Action action) {
  	Parameters result = action.getProvidedParams();
  	OperationGraph graph = new OperationGraph(action);
		List<OperationNode> leafs = graph.getLeafs();
		for (OperationNode node : leafs) {
			node.setWhitelistedOutput(result);
			Parameters leafInput = result.shallowCopy();
			leafInput.remove(node.getProvidedParams());
			leafInput.add(node.getRequiredParams());

			node.setRequiredInput(leafInput);

			/*
			 * if (!leaf.isOptional()) { //we need to provide the same output params even if
			 * the leaf is skipped
			 * 
			 * } else { //output params of this leaf should be removed from the whitelisted
			 * params of input
			 * 
			 * }
			 */
			Parameters providedParams = node.getProvidedParams();
			Parameters requiredParams = node.getRequiredParams();
		}

  	
  }

}
