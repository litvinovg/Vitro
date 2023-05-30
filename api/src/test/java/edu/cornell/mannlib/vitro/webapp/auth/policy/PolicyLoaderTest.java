package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static final String TEST_VALUES_PATH = USER_ACCOUNTS_HOME + "test_values.n3";
    public static final String TEST_DECISIONS = USER_ACCOUNTS_HOME + "decisions.n3";
    
    private static final String TEST_RESOURCES_PREFIX = "src/test/resources/edu/cornell/mannlib/vitro/webapp/auth/rules/";

    private static final String PROXIMITY_POLICY_PATH = TEST_RESOURCES_PREFIX + "proximity_test_policy.n3";
    private static final String PROXIMITY_DATA_PATH = TEST_RESOURCES_PREFIX + "proximity_test_data.n3";

    
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
        load(TEST_VALUES_PATH);
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
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        Set<AccessRule> rules = policy.getRules();
        Map<String, AccessRule> ruleMap = rules.stream().collect(Collectors.toMap( r -> r.getRuleUri(), r -> r));
        assertEquals(2, ruleMap.size());
        AccessRule ar = ruleMap.get("https://vivoweb.org/ontology/vitro-application/auth/individual/AllowAdminDisplayObjectPropertyRule");
        assertEquals(4, ar.getAttributes().size());
        ar = ruleMap.get("https://vivoweb.org/ontology/vitro-application/auth/individual/AllowAdminDisplayObjectStatementPropertyRule");
        for (String att : ar.getAttributes().keySet()) {
            System.out.println(att);
        }
        assertEquals(4, ar.getAttributes().size());
    }
    
    @Test
    public void testAdminDisplayDataPropertyPolicy() {
        load(ADMIN_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testAdminDisplayClassPolicy() {
        load(ADMIN_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testCuratorDisplayObjectPropertyPolicy() {
        load(CURATOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorDisplayDataPropertyPolicy() {
        load(CURATOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorDisplayClassPolicy() {
        load(CURATOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testPublicDisplayObjectPropertyPolicy() {
        load(PUBLIC_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testPublicDisplayDataPropertyPolicy() {
        load(PUBLIC_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testPublicDisplayClassPolicy() {
        load(PUBLIC_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorDisplayObjectPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorDisplayDataPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorDisplayClassPolicy() {
        load(SELF_EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testEditorDisplayObjectPropertyPolicy() {
        load(EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorDisplayDataPropertyPolicy() {
        load(EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorDisplayClassPolicy() {
        load(EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
   
    @Test
    public void testAdminUpdateObjectPropertyPolicy() {
        load(ADMIN_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testAdminUpdateDataPropertyPolicy() {
        load(ADMIN_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testAdminUpdateClassPolicy() {
        load(ADMIN_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testCuratorUpdateObjectPropertyPolicy() {
        load(CURATOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorUpdateDataPropertyPolicy() {
        load(CURATOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorUpdateClassPolicy() {
        load(CURATOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testPublicUpdateObjectPropertyPolicy() {
        load(PUBLIC_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testPublicUpdateDataPropertyPolicy() {
        load(PUBLIC_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testPublicUpdateClassPolicy() {
        load(PUBLIC_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorUpdateObjectPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorUpdateDataPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorUpdateClassPolicy() {
        load(SELF_EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testEditorUpdateObjectPropertyPolicy() {
        load(EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorUpdateDataPropertyPolicy() {
        load(EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorUpdateClassPolicy() {
        load(EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }

    @Test
    public void testAdminPublishObjectPropertyPolicy() {
        load(ADMIN_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testAdminPublishDataPropertyPolicy() {
        load(ADMIN_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testAdminPublishClassPolicy() {
        load(ADMIN_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testCuratorPublishObjectPropertyPolicy() {
        load(CURATOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorPublishDataPropertyPolicy() {
        load(CURATOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testCuratorPublishClassPolicy() {
        load(CURATOR_PUBLISH_CLASS_POLICY_PATH);
        
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorPublishObjectPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorPublishDataPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testSelfEditorPublishClassPolicy() {
        load(SELF_EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
        
    }
    
    @Test
    public void testEditorPublishObjectPropertyPolicy() {
        load(EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorPublishDataPropertyPolicy() {
        load(EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(2, policy.getRules().size());
    }
    
    @Test
    public void testEditorPublishClassPolicy() {
        load(EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertEquals(1, policy.getRules().size());
    }
    
    @Test
    public void testLoadRootUserPolicy() {        
        load(ROOT_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/RootUserPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
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
        DynamicPolicy policy = loader.loadPolicy(policyUri);
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
    
    @Test
    public void testNonModifiableStatementsPolicy() {        
        load(NOT_MODIFIABLE_STATEMENTS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/NotModifiableStatementsPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
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
    
    @Test
    public void testProximityPolicy() {        
        load(PROXIMITY_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/ProximityTestPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
        assertTrue(policy.getRules().size() == 1 );
        AccessRule rule = policy.getRules().iterator().next();
        assertEquals(true, rule.isAllowMatched());
        assertEquals(1, rule.getAttributesCount());
        
        Model targetModel = ModelFactory.createDefaultModel();
        try {
            targetModel.enterCriticalSection(Lock.WRITE);
            targetModel.read(PROXIMITY_DATA_PATH);
        } finally {
            targetModel.leaveCriticalSection();
        }
        AccessObject ao = new ObjectPropertyStatementAccessObject(targetModel, "test:publication", null, null);
        SimpleAuthorizationRequest ar = new SimpleAuthorizationRequest(ao, AccessOperation.EDIT);
        ar.setEditorUris(Arrays.asList("test:bob"));
        assertEquals(DecisionResult.INCONCLUSIVE, policy.decide(ar).getDecisionResult());
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.EDIT);
        ar.setEditorUris(Arrays.asList("test:alice"));
        assertEquals(DecisionResult.AUTHORIZED, policy.decide(ar).getDecisionResult());
        ar = new SimpleAuthorizationRequest(ao, AccessOperation.EDIT);
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
