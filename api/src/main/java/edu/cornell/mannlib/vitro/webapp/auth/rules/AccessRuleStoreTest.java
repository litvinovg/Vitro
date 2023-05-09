package edu.cornell.mannlib.vitro.webapp.auth.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.Lock;
import org.junit.Before;
import org.junit.Test;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;

public class AccessRuleStoreTest {

    private static final String RDFS_LABEL_URI = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String ROLE_ADMIN_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#ADMIN";
    private static final String ROLE_EDITOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#EDITOR";
    private static final String ROLE_CURATOR_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/authorization#CURATOR";

    public static final String RULES_PATH = "src/main/resources/edu/cornell/mannlib/vitro/webapp/auth/rules/rules.n3";
    private Model model;
    private AccessRuleStore store;

    @Before
    public void init() {
        model = ModelFactory.createDefaultModel();
        try {
            model.enterCriticalSection(Lock.WRITE);
            model.read(RULES_PATH);    
        } finally {
            model.leaveCriticalSection();
        }
        AccessRuleStore.initialize(model);
        store = AccessRuleStore.getInstance();
    }
    
    @Test
    public void testInitilization() {
        assertEquals(2, store.getRulesCount());
    }
    
    @Test
    public void testGetGrantedRoleUris() {
        List<String> grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.DISPLAY);
        assertEquals(1, grantedRoles.size());
        assertTrue(grantedRoles.contains(ROLE_ADMIN_URI));
        grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.PUBLISH);
        assertEquals(1, grantedRoles.size());
    }
    
    @Test
    public void testDeleteRule() {
        long initialSize = store.getModelSize();
        long initialRulesCount = store.getRulesCount();
        store.removeEntityRule(RDFS_LABEL_URI, AccessObjectType.OBJECT_PROPERTY, AccessOperation.PUBLISH, ROLE_ADMIN_URI);
        assertTrue(store.getModelSize() < initialSize);
        assertTrue(store.getRulesCount() < initialRulesCount);
    }
    
    @Test
    public void testCreateRule() {
        long initialSize = store.getModelSize();
        long initialRulesCount = store.getRulesCount();
        store.createEntityRule(RDFS_LABEL_URI, AccessObjectType.OBJECT_PROPERTY, AccessOperation.PUBLISH, ROLE_EDITOR_URI);
        assertTrue(store.getModelSize() > initialSize);
        assertTrue(store.getRulesCount() > initialRulesCount);
    }
    
    @Test
    public void testGrantedRolesModification() {
        List<String> grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.PUBLISH);
        int prevRolesCount = grantedRoles.size();
        store.createEntityRule(RDFS_LABEL_URI, AccessObjectType.OBJECT_PROPERTY, AccessOperation.PUBLISH, ROLE_EDITOR_URI);
        grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.PUBLISH);
        assertTrue(grantedRoles.size() > prevRolesCount);
        prevRolesCount = grantedRoles.size();
        store.createEntityRule(RDFS_LABEL_URI, AccessObjectType.OBJECT_PROPERTY, AccessOperation.PUBLISH, ROLE_CURATOR_URI);
        grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.PUBLISH);
        assertTrue(grantedRoles.size() > prevRolesCount);
        prevRolesCount = grantedRoles.size();
        store.removeEntityRule(RDFS_LABEL_URI, AccessObjectType.OBJECT_PROPERTY, AccessOperation.PUBLISH, ROLE_EDITOR_URI);
        grantedRoles = store.getGrantedRoleUris(RDFS_LABEL_URI, AccessOperation.PUBLISH);
        assertTrue(grantedRoles.size() < prevRolesCount);
    }
    
}
