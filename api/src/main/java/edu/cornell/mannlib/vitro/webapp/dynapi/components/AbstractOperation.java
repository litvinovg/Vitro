package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes.LocalScope;

public abstract class AbstractOperation implements LocalScope{
	
	private Parameters whitelistedOutput;
	private Parameters whitelistedInput;
	
  public void setWhitelistedOutput(Parameters whitelistedOutput) {
  	this.whitelistedInput = whitelistedOutput;
  }
  
  public Parameters getWhitelistedOutput() {
  	return whitelistedOutput;
  }
  
  public void setRequiredInput(Parameters whitelistedInput) {
  	this.whitelistedInput = whitelistedInput;
  }
  
  public Parameters getRequiredInput() {
  	return whitelistedInput;
  }
}
