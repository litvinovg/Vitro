package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class PolicyListImpl implements PolicyList, Cloneable {

    private static final Log log = LogFactory.getLog(PolicyListImpl.class.getName());

    protected ArrayList<PolicyIface> policies = new ArrayList<PolicyIface>(); 

    @Override
    public PolicyDecision decide(AuthorizationRequest ar) {
        PolicyDecision pd = null;
        PolicyDecisionLogger logger = new PolicyDecisionLogger(ar.getIds(), ar.getAccessObject());
        for(PolicyIface policy : policies){
            try{
                pd = policy.decide(ar);
                logger.log(policy, pd);
                if( pd != null ){
                    if(  pd.getDecisionResult() == DecisionResult.AUTHORIZED )
                        return pd;
                    if( pd.getDecisionResult() == DecisionResult.UNAUTHORIZED )
                        return pd;
                    if( pd.getDecisionResult() == DecisionResult.INCONCLUSIVE )
                        continue;
                } else{
                    log.debug("policy " + policy.toString() + " returned a null PolicyDecision");
                }
            }catch(Throwable th){
                log.error("ignoring exception in policy " + policy.toString(), th );
            }
        }

        pd = new BasicPolicyDecision(DecisionResult.INCONCLUSIVE,
                "No policy returned a conclusive decision on " + ar.getAccessObject());
        logger.logNoDecision(pd);
        return pd;
    }

    @Override
    public boolean contains(PolicyIface policy) {
        return policies.contains(policy);
    }

    @Override
    public void add(PolicyIface policy) {
        policies.add(policy);
    }

    @Override
    public void add(int i, PolicyIface policy) {
        policies.add(i, policy);
        
    }
    
    @Override
    public void clear() {
        policies.clear();
    }

    @Override
    public ListIterator<PolicyIface> listIterator() {
        return policies.listIterator();
    }
    
    public PolicyListImpl copy() {
        PolicyListImpl copy = new PolicyListImpl();
        copy.policies = new ArrayList<>(this.policies);
        return copy;
    }
    
}
