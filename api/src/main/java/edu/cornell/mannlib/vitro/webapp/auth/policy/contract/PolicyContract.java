package edu.cornell.mannlib.vitro.webapp.auth.policy.contract;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.NamedAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DataPropertyAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.ObjectPropertyAccessObject;

public class PolicyContract<K extends AccessObject> {
    
    public static final PolicyContract NAMED_ACCESS_OBJECT_CONTRACT = new PolicyContract<NamedAccessObject>();
    public static final PolicyContract DATA_PROPERTY_CONTRACT = new PolicyContract<DataPropertyAccessObject>();
    public static final PolicyContract OBJECT_PROPERTY_CONTRACT = new PolicyContract<ObjectPropertyAccessObject>();

    private PolicyContract() {}
}
