package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.Lock;
import org.junit.Before;
import org.junit.Test;

public class PolicyLoaderTest {
    private static final String USER_ACCOUNTS_HOME = "../home/src/main/resources/rdf/auth/everytime/";
    private static final String TEST_RESOURCES_PREFIX = "src/test/resources/edu/cornell/mannlib/vitro/webapp/auth/rules/";
    private static final String RDFS_LABEL_URI = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String ROLE_ADMIN_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#ADMIN";
    private static final String ROLE_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#EDITOR";
    private static final String ROLE_CURATOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#CURATOR";

    public static final String ONTOLOGY_PATH = USER_ACCOUNTS_HOME + "ontology.n3";
    public static final String ATTRIBUTES_PATH = USER_ACCOUNTS_HOME + "attributes.n3";
    public static final String OPERATIONS_PATH = USER_ACCOUNTS_HOME + "operations.n3";
    public static final String OPERATION_GROUPS = USER_ACCOUNTS_HOME + "operation_groups.n3";
    public static final String SUBJECT_TYPES = USER_ACCOUNTS_HOME + "subject_types.n3";
    public static final String OBJECT_TYPES = USER_ACCOUNTS_HOME + "object_types.n3";
    public static final String ATTRIBUTE_TYPES_PATH = USER_ACCOUNTS_HOME + "attribute_types.n3";
    public static final String TEST_TYPES_PATH = USER_ACCOUNTS_HOME + "test_types.n3";

    public static final String ROOT_POLICY_PATH = USER_ACCOUNTS_HOME + "root_user_policy.n3";
    public static final String DISPLAY_OBJ_PROP_PATH = USER_ACCOUNTS_HOME + "display_object_property_policy.n3";

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
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
    
    @Test
    public void testLoadPolicyWithoutDataSets() {        
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(ROOT_POLICY_PATH);
        } finally {
            model.leaveCriticalSection();
        }
        String policyUri = "https://vivoweb.org/ontology/vitro-application/auth/individual/RootUserPolicy";
        DynamicPolicy policy = loader.loadPolicy(policyUri);
        assertTrue(policy != null);
    }
}
