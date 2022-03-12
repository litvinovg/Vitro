package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import java.util.HashSet;
import java.util.Set;

import edu.cornell.mannlib.vitro.webapp.dynapi.OperationData;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes.OperationNode;

public class ConditionalStep extends AbstractOperation implements Step {

	private Condition condition;
	private Step nextStepTrue;
	private Step nextStepFalse;
	
  @Override
	public void dereference() {
		// TODO Auto-generated method stub

	}
	
	public void setNextStepTrue(Step nextStepTrue) {
		this.nextStepTrue = nextStepTrue;
	}
	
	public void setNextStepFalse(Step nextStepFalse) {
		this.nextStepFalse = nextStepFalse;
	}

	@Override
	public OperationResult run(OperationData input) {
		return null;
	}

  @Override
  public Set<OperationNode> getNextNodes() {
    HashSet<OperationNode> nextNodes = new HashSet<OperationNode>();
    nextNodes.add(nextStepFalse);
    nextNodes.add(nextStepTrue);
    return nextNodes;
  }

  @Override
  public Parameters getRequiredParams() {
    return condition.getRequiredParams();
  }

  @Override
  public Parameters getProvidedParams() {
    return new Parameters();
  }

  @Override
  public boolean isRoot() {
    return false;
  }
  
  public void setCondition(Condition condition) {
    this.condition = condition;
  }

	@Override
	public boolean isOptional() {
		return false;
	}

}
