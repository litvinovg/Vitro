package edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameters;

public interface LocalScope {
	
  public void setWhitelistedOutput(Parameters params);
  
  public Parameters getWhitelistedOutput();
  
  public void setRequiredInput(Parameters params);
  
  public Parameters getRequiredInput();
  
}
