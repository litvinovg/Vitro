package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import java.util.Set;

public interface OperationNode extends ParameterInfo {
  
  public Set<OperationNode> getNextLinks();
  
  public boolean isRoot();
  
}
