package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.Lock;
import org.junit.Before;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.Attribute;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;

public class PolicyTest {
    public static final String USER_ACCOUNTS_HOME = "../home/src/main/resources/rdf/auth/everytime/";
    protected static final String ROLE_ADMIN_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#ADMIN";
    protected static final String ROLE_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#EDITOR";
    protected static final String ROLE_SELF_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#SELF_EDITOR";
    protected static final String ROLE_CURATOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#CURATOR";
    protected static final String PUBLIC_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#PUBLIC";

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
    
    protected static final String TEST_RESOURCES_PREFIX = "src/test/resources/edu/cornell/mannlib/vitro/webapp/auth/rules/";
    
    private Model model;
    protected PolicyLoader loader;

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
    
    protected void countRulesAndAttributes(DynamicPolicy policy, int ruleCount, int attCount) {
        assertTrue(policy != null);
        Set<AccessRule> rules = policy.getRules();
        Map<String, AccessRule> ruleMap = rules.stream().collect(Collectors.toMap( r -> r.getRuleUri(), r -> r));
        assertEquals(ruleCount, ruleMap.size());
        for (AccessRule ar : ruleMap.values()) {
            assertEquals(attCount, ar.getAttributes().size());
            for (Attribute att : ar.getAttributes().values()) {
                assertTrue(att.getValues().size() > 0); 
            }
        }
    }
    
    protected void load(String filePath) {
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(filePath);
        } finally {
            model.leaveCriticalSection();
        }
    }
}
