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
import edu.cornell.mannlib.vitro.webapp.auth.objects.DataPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.objects.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleAuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.SimpleAccessRules;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

public class PolicyLoaderTest {
    private static final String USER_ACCOUNTS_HOME = "../home/src/main/resources/rdf/auth/everytime/";
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
    public static final String MENU_ITEMS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_menu_items_editing.n3";
    //Simple permission policies
    public static final String ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_simple_permissions.n3";
    public static final String CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_simple_permissions.n3";
    public static final String EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_simple_permissions.n3";
    public static final String SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_simple_permissions.n3";
    public static final String PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_simple_permissions.n3";
    
    public static final String NOT_MODIFIABLE_STATEMENTS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_not_modifiable_statements.n3";
    //Entity permission policies
    //Display
    public static final String ADMIN_DISPLAY_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_display_object_property.n3";
    public static final String ADMIN_DISPLAY_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_display_data_property.n3";
    public static final String ADMIN_DISPLAY_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_display_class.n3";

    public static final String CURATOR_DISPLAY_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_display_object_property.n3";
    public static final String CURATOR_DISPLAY_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_display_data_property.n3";
    public static final String CURATOR_DISPLAY_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_display_class.n3";

    public static final String PUBLIC_DISPLAY_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_display_object_property.n3";
    public static final String PUBLIC_DISPLAY_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_display_data_property.n3";
    public static final String PUBLIC_DISPLAY_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_display_class.n3";
    
    public static final String SELF_EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_display_object_property.n3";
    public static final String SELF_EDITOR_DISPLAY_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_display_data_property.n3";
    public static final String SELF_EDITOR_DISPLAY_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_display_class.n3";
    
    public static final String EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_display_object_property.n3";
    public static final String EDITOR_DISPLAY_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_display_data_property.n3";
    public static final String EDITOR_DISPLAY_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_display_class.n3";
    //Update
    public static final String ADMIN_UPDATE_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_update_object_property.n3";
    public static final String ADMIN_UPDATE_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_update_data_property.n3";
    public static final String ADMIN_UPDATE_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_update_class.n3";

    public static final String CURATOR_UPDATE_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_update_object_property.n3";
    public static final String CURATOR_UPDATE_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_update_data_property.n3";
    public static final String CURATOR_UPDATE_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_update_class.n3";

    public static final String PUBLIC_UPDATE_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_update_object_property.n3";
    public static final String PUBLIC_UPDATE_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_update_data_property.n3";
    public static final String PUBLIC_UPDATE_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_update_class.n3";
    
    public static final String SELF_EDITOR_UPDATE_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_update_object_property.n3";
    public static final String SELF_EDITOR_UPDATE_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_update_data_property.n3";
    public static final String SELF_EDITOR_UPDATE_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_update_class.n3";
    
    public static final String EDITOR_UPDATE_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_update_object_property.n3";
    public static final String EDITOR_UPDATE_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_update_data_property.n3";
    public static final String EDITOR_UPDATE_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_update_class.n3"; 
    //Publish
    public static final String ADMIN_PUBLISH_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_publish_object_property.n3";
    public static final String ADMIN_PUBLISH_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_publish_data_property.n3";
    public static final String ADMIN_PUBLISH_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_admin_publish_class.n3";

    public static final String CURATOR_PUBLISH_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_publish_object_property.n3";
    public static final String CURATOR_PUBLISH_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_publish_data_property.n3";
    public static final String CURATOR_PUBLISH_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_curator_publish_class.n3";

    public static final String PUBLIC_PUBLISH_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_publish_object_property.n3";
    public static final String PUBLIC_PUBLISH_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_publish_data_property.n3";
    public static final String PUBLIC_PUBLISH_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_public_publish_class.n3";
    
    public static final String SELF_EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_publish_object_property.n3";
    public static final String SELF_EDITOR_PUBLISH_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_publish_data_property.n3";
    public static final String SELF_EDITOR_PUBLISH_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_self_editor_publish_class.n3";
    
    public static final String EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_publish_object_property.n3";
    public static final String EDITOR_PUBLISH_DATA_PROP_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_publish_data_property.n3";
    public static final String EDITOR_PUBLISH_CLASS_POLICY_PATH = USER_ACCOUNTS_HOME + "policy_editor_publish_class.n3";
    
    private Model model;
    private PolicyLoader loader;

    @Before
    public void init() {
        model = ModelFactory.createDefaultModel();
        load(ATTRIBUTES_PATH);
        load(OPERATIONS_PATH);
        load(OPERATION_GROUPS);
        load(SUBJECT_TYPES);
        load(OBJECT_TYPES);
        load(ATTRIBUTE_TYPES_PATH);
        load(TEST_TYPES_PATH);
        load(TEST_DECISIONS);
        
        PolicyLoader.initialize(model);
        loader = PolicyLoader.getInstance();
    }
    
    @Test
    public void getPolicyUris() {
        load(ROOT_POLICY_PATH);
        List<String> uris = loader.getPolicyUris();
        System.out.println(uris);
        assertTrue(!uris.isEmpty());
    }
    
    @Test
    public void testAdminDisplayObjectPropertyPolicy() {
        load(ADMIN_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminDisplayDataPropertyPolicy() {
        load(ADMIN_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminDisplayClassPolicy() {
        load(ADMIN_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorDisplayObjectPropertyPolicy() {
        load(CURATOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorDisplayDataPropertyPolicy() {
        load(CURATOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorDisplayClassPolicy() {
        load(CURATOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicDisplayObjectPropertyPolicy() {
        load(PUBLIC_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicDisplayDataPropertyPolicy() {
        load(PUBLIC_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicDisplayClassPolicy() {
        load(PUBLIC_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorDisplayObjectPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorDisplayDataPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorDisplayClassPolicy() {
        load(SELF_EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorDisplayObjectPropertyPolicy() {
        load(EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorDisplayDataPropertyPolicy() {
        load(EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorDisplayClassPolicy() {
        load(EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
   
    @Test
    public void testAdminUpdateObjectPropertyPolicy() {
        load(ADMIN_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminUpdateDataPropertyPolicy() {
        load(ADMIN_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminUpdateClassPolicy() {
        load(ADMIN_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorUpdateObjectPropertyPolicy() {
        load(CURATOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorUpdateDataPropertyPolicy() {
        load(CURATOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorUpdateClassPolicy() {
        load(CURATOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicUpdateObjectPropertyPolicy() {
        load(PUBLIC_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicUpdateDataPropertyPolicy() {
        load(PUBLIC_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testPublicUpdateClassPolicy() {
        load(PUBLIC_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorUpdateObjectPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorUpdateDataPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorUpdateClassPolicy() {
        load(SELF_EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorUpdateObjectPropertyPolicy() {
        load(EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorUpdateDataPropertyPolicy() {
        load(EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorUpdateClassPolicy() {
        load(EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }

    @Test
    public void testAdminPublishObjectPropertyPolicy() {
        load(ADMIN_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminPublishDataPropertyPolicy() {
        load(ADMIN_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testAdminPublishClassPolicy() {
        load(ADMIN_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorPublishObjectPropertyPolicy() {
        load(CURATOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorPublishDataPropertyPolicy() {
        load(CURATOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testCuratorPublishClassPolicy() {
        load(CURATOR_PUBLISH_CLASS_POLICY_PATH);
        
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorPublishObjectPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorPublishDataPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testSelfEditorPublishClassPolicy() {
        load(SELF_EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorPublishObjectPropertyPolicy() {
        load(EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishObjectPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorPublishDataPropertyPolicy() {
        load(EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishDataPropertyPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testEditorPublishClassPolicy() {
        load(EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishClassPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    @Test
    public void testLoadRootUserPolicy() {        
        load(ROOT_POLICY_PATH);
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
        load(MENU_ITEMS_POLICY_PATH);
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
        load(ADMIN_SIMPLE_PERMISSIONS_POLICY_PATH);
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
        load(CURATOR_SIMPLE_PERMISSIONS_POLICY_PATH);
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
        load(EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
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
        load(SELF_EDITOR_SIMPLE_PERMISSIONS_POLICY_PATH);
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
        load(PUBLIC_SIMPLE_PERMISSIONS_POLICY_PATH);
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
    
    @Test
    public void testNonModifiableStatementsPolicy() {        
        load(NOT_MODIFIABLE_STATEMENTS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/NotModifiableStatementsPolicy";
        Policy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(8000, policy.getPriority());
        assertEquals(5, policy.getRules().size());
        final AccessRule rule = policy.getRules().iterator().next();
        assertEquals(false, rule.isAllowMatched());
        for (AccessRule irule : policy.getRules()) {
            assertEquals(3, irule.getAttributesCount());
        }
        
        AccessObject ao = new ObjectPropertyStatementAccessObject(null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Valid", null, null);
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ao = new ObjectPropertyStatementAccessObject(null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime", null, null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        
        ao = new ObjectPropertyStatementAccessObject(null, null, new Property("http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Valid"), null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ao = new ObjectPropertyStatementAccessObject(null, null, new Property("http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime"), null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        
        ao = new ObjectPropertyStatementAccessObject(null, null, null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Valid");
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ao = new ObjectPropertyStatementAccessObject(null, null, null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime");
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        
        //Data property statement
        ao = new DataPropertyStatementAccessObject(null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Valid", null, null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ao = new DataPropertyStatementAccessObject(null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime", null, null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        
        ao = new DataPropertyStatementAccessObject(null, null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Valid", null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.UNAUTHORIZED, policy.decide(ar).getDecisionResult());
        ao = new DataPropertyStatementAccessObject(null, null, "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#modTime", null);
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.DROP);
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
    }
    
    private void load(String filePath) {
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(filePath);
        } finally {
            model.leaveCriticalSection();
        }
    }
}
