package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleAuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.SimpleAccessRules;

public class SimplePermissionPolicyTest extends PolicyTest {

    public static final String ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_simple_permissions.n3";
    public static final String CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_simple_permissions.n3";
    public static final String EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_simple_permissions.n3";
    public static final String SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_simple_permissions.n3";
    public static final String PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_simple_permissions.n3";
    
    @Test
    public void testAdminSimplePermissionPolicy() {        
        load(ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminSimplePermissionsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1000, policy.getPriority());
        assertEquals(1, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(3, rule.getAttributesCount());
        
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(SimpleAccessRules.NS + "SeeSiteAdminPage");
        ar.setRoleUris(Arrays.asList(ROLE_CURATOR_URI));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar.setRoleUris(Arrays.asList(ROLE_ADMIN_URI));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
    }
    
    @Test
    public void testCuratorSimplePermissionPolicy() {        
        load(CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorSimplePermissionsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1000, policy.getPriority());
        assertEquals(1, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(3, rule.getAttributesCount());
        
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(SimpleAccessRules.NS + "EditOntology");
        ar.setRoleUris(Arrays.asList(ROLE_EDITOR_URI));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar.setRoleUris(Arrays.asList(ROLE_CURATOR_URI));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
    }
    
    @Test
    public void testEditorSimplePermissionPolicy() {        
        load(EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorSimplePermissionsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1000, policy.getPriority());
        assertEquals(1, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(3, rule.getAttributesCount());
        
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(SimpleAccessRules.NS + "DoBackEndEditing");
        ar.setRoleUris(Arrays.asList(ROLE_SELF_EDITOR_URI));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar.setRoleUris(Arrays.asList(ROLE_EDITOR_URI));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
    }

    @Test
    public void testSelfEditorSimplePermissionPolicy() {        
        load(SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorSimplePermissionsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1000, policy.getPriority());
        assertEquals(1, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(3, rule.getAttributesCount());
        
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(SimpleAccessRules.NS + "DoFrontEndEditing");
        ar.setRoleUris(Arrays.asList(PUBLIC_URI));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar.setRoleUris(Arrays.asList(ROLE_SELF_EDITOR_URI));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
    }
    
    @Test
    public void testPublicSimplePermissionPolicy() {        
        load(PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicSimplePermissionsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1000, policy.getPriority());
        assertEquals(1, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(3, rule.getAttributesCount());
        
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(SimpleAccessRules.NS + "QueryFullModel");
        ar.setRoleUris(Arrays.asList(""));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar.setRoleUris(Arrays.asList(PUBLIC_URI));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
    }
   
    
}
