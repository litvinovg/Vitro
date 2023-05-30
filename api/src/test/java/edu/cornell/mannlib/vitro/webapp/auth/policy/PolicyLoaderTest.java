package edu.cornell.mannlib.vitro.webapp.auth.policy;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PolicyLoaderTest extends PolicyTest {

    public static final String BROKEN_POLICY = TEST_RESOURCES_PREFIX + "test_policy_broken1.n3";
    public static final String VALID_POLICY = TEST_RESOURCES_PREFIX + "test_policy_valid.n3";

    @Test
    public void testGetPolicyUris() {
        load(VALID_POLICY);
        List<String> uris = loader.getPolicyUris();
        assertTrue(!uris.isEmpty());
    }
    
    @Test
    public void testBrokenPolicy() {
        load(BROKEN_POLICY);
        List<String> uris = loader.getPolicyUris();
        assertTrue(uris.isEmpty());
    }
}
