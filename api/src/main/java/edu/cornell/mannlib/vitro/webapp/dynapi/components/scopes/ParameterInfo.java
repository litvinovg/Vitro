package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameters;

public interface ParameterInfo {
  
  public Parameters getRequiredParams();
  
  public Parameters getProvidedParams();
  
}
