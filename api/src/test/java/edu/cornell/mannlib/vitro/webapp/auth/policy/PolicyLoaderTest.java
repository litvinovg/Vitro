package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PolicyLoaderTest extends PolicyTest {

    public static final String BROKEN_POLICY_BROKEN_TEST = TEST_RESOURCES_PREFIX + "test_policy_broken1.n3";
    public static final String BROKEN_POLICY_BROKEN_TYPE = TEST_RESOURCES_PREFIX + "test_policy_broken2.n3";
    public static final String BROKEN_POLICY_BROKEN_TEST_ID = TEST_RESOURCES_PREFIX + "test_policy_broken3.n3";
    public static final String BROKEN_POLICY_BROKEN_TYPE_ID = TEST_RESOURCES_PREFIX + "test_policy_broken4.n3";
    public static final String VALID_POLICY = TEST_RESOURCES_PREFIX + "test_policy_valid.n3";
    public static final String VALID_POLICY_WITH_SET = TEST_RESOURCES_PREFIX + "test_policy_valid_set.n3";
    public static final String BROKEN_POLICY_WITH_SET = TEST_RESOURCES_PREFIX + "test_policy_broken_set.n3";

    @Test
    public void testGetPolicyUris() {
        load(VALID_POLICY);
        List<String> uris = loader.getPolicyUris();
        assertTrue(!uris.isEmpty());
    }
    
    @Test
    public void testValidPolicy() {
        load(VALID_POLICY);
        List<String> uris = loader.getPolicyUris();
        assertTrue(!uris.isEmpty());
    }
    
    @Test
    public void testValidPolicyWithSet() {
        load(VALID_POLICY_WITH_SET);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/ValidTestSetPolicy");
        countRulesAndAttributes(policy, 1, 2);
    }
    
    @Test
    public void testBrokenTestId() {
        load(BROKEN_POLICY_BROKEN_TEST_ID);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/BrokenPolicyBrokenTestTypeId");
        assertTrue(policy == null);
    }
    
    @Test
    public void testBrokenTypeId() {
        load(BROKEN_POLICY_BROKEN_TYPE_ID);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/BrokenPolicyTypeId");
        assertTrue(policy == null);
    }
    
    @Test
    public void testBrokenTest() {
        load(BROKEN_POLICY_BROKEN_TEST);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/BrokenPolicyBrokenTestType");
        assertTrue(policy == null);
    }
    
    @Test
    public void testBrokenType() {
        load(BROKEN_POLICY_BROKEN_TYPE);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/BrokenPolicyType");
        assertTrue(policy == null);
    }
    
    @Test
    public void testBrokenSet() {
        load(BROKEN_POLICY_WITH_SET);
        DynamicPolicy policy = loader.loadPolicy("https://vivoweb.org/ontology/vitro-application/auth/individual/BrokenTestSetPolicy");
        assertTrue(policy == null);
    }
}
