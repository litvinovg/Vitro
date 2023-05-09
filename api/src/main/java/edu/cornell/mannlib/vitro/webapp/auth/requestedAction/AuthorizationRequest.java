package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import java.util.Collections;
import java.util.List;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public abstract class AuthorizationRequest {

    IdentifierBundle ids;
    List<String> roleUris = Collections.emptyList();
    
    public void setRoleUris(List<String> roleUris) {
        this.roleUris = roleUris;
    }

    public boolean isContainer() {
        return false;
    }

    public DecisionResult getPredefinedDecision(){
        return DecisionResult.INCONCLUSIVE;
    }
    
    public List<AuthorizationRequest> getItems() {
        return Collections.emptyList();
    }
    
    public abstract AccessObject getAccessObject();
    
    public abstract AccessOperation getAccessOperation();

    public IdentifierBundle getIds() {
        return ids;
    }
    
    public List<String> getRoleUris() {
        return roleUris;
    }
    
    public void setIds(IdentifierBundle ids) {
        this.ids = ids;
    }
    
}