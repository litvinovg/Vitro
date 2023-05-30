package edu.cornell.mannlib.vitro.webapp.auth.policy;

import org.junit.Test;

public class EntityPolicyTests extends PolicyTest {
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
    
    @Test
    public void testAdminDisplayObjectPropertyPolicy() {
        load(ADMIN_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testAdminDisplayDataPropertyPolicy() {
        load(ADMIN_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testAdminDisplayClassPolicy() {
        load(ADMIN_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testCuratorDisplayObjectPropertyPolicy() {
        load(CURATOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorDisplayDataPropertyPolicy() {
        load(CURATOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorDisplayClassPolicy() {
        load(CURATOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testPublicDisplayObjectPropertyPolicy() {
        load(PUBLIC_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testPublicDisplayDataPropertyPolicy() {
        load(PUBLIC_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testPublicDisplayClassPolicy() {
        load(PUBLIC_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testSelfEditorDisplayObjectPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorDisplayDataPropertyPolicy() {
        load(SELF_EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorDisplayClassPolicy() {
        load(SELF_EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testEditorDisplayObjectPropertyPolicy() {
        load(EDITOR_DISPLAY_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testEditorDisplayDataPropertyPolicy() {
        load(EDITOR_DISPLAY_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);

    }
    
    @Test
    public void testEditorDisplayClassPolicy() {
        load(EDITOR_DISPLAY_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorDisplayClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
   
    @Test
    public void testAdminUpdateObjectPropertyPolicy() {
        load(ADMIN_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testAdminUpdateDataPropertyPolicy() {
        load(ADMIN_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testAdminUpdateClassPolicy() {
        load(ADMIN_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testCuratorUpdateObjectPropertyPolicy() {
        load(CURATOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorUpdateDataPropertyPolicy() {
        load(CURATOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorUpdateClassPolicy() {
        load(CURATOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testPublicUpdateObjectPropertyPolicy() {
        load(PUBLIC_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testPublicUpdateDataPropertyPolicy() {
        load(PUBLIC_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testPublicUpdateClassPolicy() {
        load(PUBLIC_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/PublicUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testSelfEditorUpdateObjectPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorUpdateDataPropertyPolicy() {
        load(SELF_EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorUpdateClassPolicy() {
        load(SELF_EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testEditorUpdateObjectPropertyPolicy() {
        load(EDITOR_UPDATE_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testEditorUpdateDataPropertyPolicy() {
        load(EDITOR_UPDATE_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testEditorUpdateClassPolicy() {
        load(EDITOR_UPDATE_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorUpdateClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }

    @Test
    public void testAdminPublishObjectPropertyPolicy() {
        load(ADMIN_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testAdminPublishDataPropertyPolicy() {
        load(ADMIN_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testAdminPublishClassPolicy() {
        load(ADMIN_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/AdminPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testCuratorPublishObjectPropertyPolicy() {
        load(CURATOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorPublishDataPropertyPolicy() {
        load(CURATOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testCuratorPublishClassPolicy() {
        load(CURATOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/CuratorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testSelfEditorPublishObjectPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorPublishDataPropertyPolicy() {
        load(SELF_EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testSelfEditorPublishClassPolicy() {
        load(SELF_EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/SelfEditorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
    @Test
    public void testEditorPublishObjectPropertyPolicy() {
        load(EDITOR_PUBLISH_OBJ_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishObjectPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testEditorPublishDataPropertyPolicy() {
        load(EDITOR_PUBLISH_DATA_PROP_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishDataPropertyPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 2, 4);
    }
    
    @Test
    public void testEditorPublishClassPolicy() {
        load(EDITOR_PUBLISH_CLASS_POLICY_PATH);
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/EditorPublishClassPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        countRulesAndAttributes(policy, 1, 4);
    }
    
}
