package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import java.util.Set;

public interface OperationNode extends ParameterInfo, LocalScope {
  
  public Set<OperationNode> getNextNodes();
  
  public boolean isRoot();
  
  public boolean isOptional();
  
}
