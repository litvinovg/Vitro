package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.Lock;
import org.junit.Before;
import org.junit.Test;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.objects.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleAuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.SimpleAccessRules;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

public class PolicyLoaderTest {
    private static final String USER_ACCOUNTS_HOME = "../home/src/main/resources/rdf/auth/everytime/";
    private static final String TEST_RESOURCES_PREFIX = "src/test/resources/edu/cornell/mannlib/vitro/webapp/auth/rules/";
    private static final String RDFS_LABEL_URI = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String ROLE_ADMIN_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#ADMIN";
    private static final String ROLE_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#EDITOR";
    private static final String ROLE_SELF_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#SELF_EDITOR";
    private static final String ROLE_CURATOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#CURATOR";
    private static final String PUBLIC_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#PUBLIC";

    public static final String ONTOLOGY_PATH = USER_ACCOUNTS_HOME + "ontology.n3";
    public static final String ATTRIBUTES_PATH = USER_ACCOUNTS_HOME + "attributes.n3";
    public static final String OPERATIONS_PATH = USER_ACCOUNTS_HOME + "operations.n3";
    public static final String OPERATION_GROUPS = USER_ACCOUNTS_HOME + "operation_groups.n3";
    public static final String SUBJECT_TYPES = USER_ACCOUNTS_HOME + "subject_types.n3";
    public static final String OBJECT_TYPES = USER_ACCOUNTS_HOME + "object_types.n3";
    public static final String ATTRIBUTE_TYPES_PATH = USER_ACCOUNTS_HOME + "attribute_types.n3";
    public static final String TEST_TYPES_PATH = USER_ACCOUNTS_HOME + "test_types.n3";
    public static final String TEST_DECISIONS = USER_ACCOUNTS_HOME + "decisions.n3";

    public static final String ROOT_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_root_user.n3";
    public static final String DISPLAY_OBJ_PROP_PATH = USER_ACCOUNTS_HOME + "policy_display_object_property.n3";
    public static final String MENU_ITEMS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_menu_items_editing.n3";
    public static final String ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_simple_permissions.n3";
    public static final String CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_simple_permissions.n3";
    public static final String EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_simple_permissions.n3";
    public static final String SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_simple_permissions.n3";
    public static final String PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_simple_permissions.n3";

    private Model model;
    private PolicyLoader loader;

    @Before
    public void init() {
        model = ModelFactory.createDefaultModel();
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(ATTRIBUTES_PATH);
            model.read(OPERATIONS_PATH);
            model.read(OPERATION_GROUPS);
            model.read(SUBJECT_TYPES);
            model.read(OBJECT_TYPES);
            model.read(ATTRIBUTE_TYPES_PATH);
            model.read(TEST_TYPES_PATH);
            model.read(TEST_DECISIONS);
        } finally {
            model.leaveCriticalSection();
        }
        PolicyLoader.initialize(model);
        loader = PolicyLoader.getInstance();
    }
    
    @Test
    public void getPolicyUris() {
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(ROOT_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        List<String> uris = loader.getPolicyUris();
        System.out.println(uris);
        assertTrue(!uris.isEmpty());
    }
    
    @Test
    public void testLoadPolicyWithDataSets() {
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(DISPLAY_OBJ_PROP_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/ObjectPropertyDisplayOperationGroupPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testLoadRootUserPolicy() {        
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(ROOT_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/RootUserPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(10000, policy.getPriority());
        assertTrue(policy.getRules().size() > 0 );
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(1, rule.getAttributesCount());
    }
    
    @Test
    public void testLoadHomeMenuItemsRestrictionPolicy() {        
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(MENU_ITEMS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/RestrictHomeMenuItemsEditingPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(9000, policy.getPriority());
        assertTrue(policy.getRules().size() > 0 );
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(false, rule.isAllowMatched());
        assertEquals(4, rule.getAttributesCount());
        
        AccessObject ao = new ObjectPropertyStatementAccessObject(null, null, new Property("http://vitro.mannlib.cornell.edu/ontologies/display/1.1#HomeMenuItem"), "http://vitro.mannlib.cornell.edu/ontologies/display/1.1#hasElement");
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.EDIT);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.ADD);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
    }
    
    @Test
    public void testAdminSimplePermissionPolicy() {        
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminSimplePermissionsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
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
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorSimplePermissionsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
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
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorSimplePermissionsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
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
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorSimplePermissionsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
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
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicSimplePermissionsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
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
