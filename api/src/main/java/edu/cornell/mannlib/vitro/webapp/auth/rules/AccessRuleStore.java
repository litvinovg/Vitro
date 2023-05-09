package edu.cornell.mannlib.vitro.webapp.auth.rules;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.Attribute;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeFactory;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.TestType;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObjectImpl;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleAuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.jena.event.BulkUpdateEvent;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;
import edu.cornell.mannlib.vitro.webapp.rdfservice.ChangeSet;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFService;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFService.ModelSerializationFormat;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFServiceException;
import edu.cornell.mannlib.vitro.webapp.rdfservice.impl.jena.model.RDFServiceModel;

public class AccessRuleStore {
    public static final String ATTR_VALUE = "value";
    public static final String LITERAL_VALUE = "lit_value";
    public static final String TYPE_ID = "typeId";
    public static final String TEST_ID = "testId";
    public static final String ID = "id";
    public static final String URI = "uri";
    public static final String OBJECT_TYPE_ID = "objectTypeId";
    public static final String OBJECT_URI = "objectUri";
    public static final String OPERATION_ID = "operationId";
    public static final String ATTRIBUTE = "attribute";
    public static final String RULE = "rule";

    private static final String RULES_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT ?" + RULE + " ?" + ATTRIBUTE + " ?" + TEST_ID + " ?" + TYPE_ID + " ?" + ATTR_VALUE + " ?" + LITERAL_VALUE + " \n"
          + "WHERE {\n"
          + "?" + RULE + " rdf:type ao:Rule .\n"
          + "?" + RULE + " ao:attribute ?" + ATTRIBUTE + " .\n"
          + "?" + ATTRIBUTE + " ao:test ?test .\n"
          + "?test ao:id ?" + TEST_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:type ?type .\n"
          + "?type ao:id ?" + TYPE_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:value ?" + ATTR_VALUE + " .\n"
          + "OPTIONAL {?" + ATTR_VALUE + " ao:id ?" + LITERAL_VALUE + " . }\n"
          + "} ORDER BY ?rule ?attribute";
    
    private static final String RULE_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT ?" + RULE + " ?" + ATTRIBUTE + " ?" + TEST_ID + " ?" + TYPE_ID + " ?" + ATTR_VALUE + " ?" + LITERAL_VALUE + " \n"
          + "WHERE {\n"
          + "?" + RULE + " rdf:type ao:Rule .\n"
          + "?" + RULE + " ao:attribute ?" + ATTRIBUTE + " .\n"
          + "?" + ATTRIBUTE + " ao:test ?test .\n"
          + "?test ao:id ?" + TEST_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:type ?type .\n"
          + "?type ao:id ?" + TYPE_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:value ?" + ATTR_VALUE + " .\n"
          + "OPTIONAL {?" + ATTR_VALUE + " ao:id ?" + LITERAL_VALUE + " . }\n"
          + "FILTER (?" + RULE + " = IRI(?rule_filter) )\n"
          + "} ORDER BY ?rule ?attribute";

    private static final String DELETE_QUERY = 
              "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
            + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
            + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
            + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
            + "CONSTRUCT { \n"
            + "  ?" + RULE + " rdf:type ao:Rule .\n"
            + "  ?" + RULE + " ao:attribute ?" + ATTRIBUTE + " .\n"
            + "  ?" + ATTRIBUTE + " rdf:type ?rdfType . \n" 
            + "  ?" + ATTRIBUTE + " ao:test ?test .\n"
            + "  ?" + ATTRIBUTE + " ao:type ?type .\n"
            + "  ?" + ATTRIBUTE + " ao:value ?" + ATTR_VALUE + " .\n"
            + "  ?" + ATTR_VALUE + " ao:id ?" + LITERAL_VALUE + " . \n"
            + "}"
            + "WHERE {\n"
            + "  ?" + RULE + " rdf:type ao:Rule .\n"
            + "  ?" + RULE + " ao:attribute ?" + ATTRIBUTE + " .\n"
            + "  OPTIONAL {\n"
            + "    FILTER NOT EXISTS {"
            + "      ?rule2  ao:attribute ?" + ATTRIBUTE + " .\n"
            + "      FILTER (?" + RULE + " != ?rule2)"
            + "    }"
            + "    ?" + ATTRIBUTE + " rdf:type ?rdfType . \n" 
            + "    ?" + ATTRIBUTE + " ao:test ?test . \n"
            + "    ?" + ATTRIBUTE + " ao:type ?type . \n"
            + "    ?" + ATTRIBUTE + " ao:value ?" + ATTR_VALUE + " .\n"
            + "  }"
            + "}";
    
    private static final String ATTRIBUTE_URI_QUERY = 
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT ?" + ATTRIBUTE + " \n"
          + "WHERE {\n"
          + "?" + ATTRIBUTE + " rdf:type ao:Attribute .\n"
          + "?" + ATTRIBUTE + " ao:test ?test .\n"
          + "?test ao:id ?" + TEST_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:type ?type .\n"
          + "?type ao:id ?" + TYPE_ID + " .\n"
          + "?" + ATTRIBUTE + " ao:value ?" + ATTR_VALUE + " .\n"
          + "OPTIONAL {?" + ATTR_VALUE + " ao:id ?" + LITERAL_VALUE + " . }\n"
          + "}";
    
    private static final String URI_BY_ID_QUERY = ""
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT ?" + URI + " \n"
          + "WHERE {\n"
          + "?" + URI + " ao:id ?" + ID + " .\n"
          + "}";
    
    private static final Log log = LogFactory.getLog(AccessRuleStore.class);
    
    private static AccessRuleStore INSTANCE;

    public static AccessRuleStore getInstance() {
        return INSTANCE;
    }

    private Set<AccessRule> allRules = Collections.synchronizedSet(new HashSet<>());
    private Map<AccessOperation,Set<AccessRule>> ruleSetsByOperation;
    private Map<AccessObjectType,Set<AccessRule>> ruleSetsByObjectType;
    private Map<String,Set<AccessRule>> ruleSetsByObjectUri;
    
    private Model userAccountsModel;
    private RDFService rdfService;
    
    
    private AccessRuleStore(Model userAccountsModel){
        if (userAccountsModel != null) {
            this.userAccountsModel = userAccountsModel;
            this.rdfService = new RDFServiceModel(userAccountsModel);
        } else {
            userAccountsModel = ModelAccess.getInstance().getOntModelSelector().getUserAccountsModel();
            rdfService = ModelAccess.getInstance().getRDFService();
        }
        initilizeRuleSetsByOperation();
        initilizeRuleSetsByObjectType();
        initilizeRuleSetsByObjectUri();
        loadRules();
    }

    private void initilizeRuleSetsByObjectUri() {
        ruleSetsByObjectUri = Collections.synchronizedMap(new HashMap<>());
        Set<AccessRule> set = Collections.synchronizedSet(new HashSet<>());
        ruleSetsByObjectUri.put("", set);
    }
    
    private void initilizeRuleSetsByObjectType() {
        ruleSetsByObjectType = Collections.synchronizedMap(new HashMap<>());
        for (AccessObjectType aot : AccessObjectType.values()) {
            Set<AccessRule> set = Collections.synchronizedSet(new HashSet<>());
            ruleSetsByObjectType.put(aot, set);
        }        
    }

    private void initilizeRuleSetsByOperation() {
        ruleSetsByOperation = Collections.synchronizedMap(new HashMap<>());
        for (AccessOperation ao : AccessOperation.values()) {
            Set<AccessRule> set = Collections.synchronizedSet(new HashSet<>());
            ruleSetsByOperation.put(ao, set);
        }
    }

    public static void initialize(Model model) {
        if (INSTANCE != null) {
            return;
        }
        INSTANCE = new AccessRuleStore(model);
    }

    private void loadRules() {
        loadRulesFromModel(RULES_QUERY);
    }
    
    private void loadRule(String uri) {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(RULE_QUERY);
        pss.setLiteral("rule_filter", uri);
        loadRulesFromModel(pss.toString());
    }

    private void loadRulesFromModel(String sparqlQuery) {
        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qexec = QueryExecutionFactory.create(query, userAccountsModel);
        try {
            ResultSet rs = qexec.execSelect();
            AccessRule rule = null;
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (isInvalidRulesData(qs)) {
                    return;
                }
                if (isRuleContinues(rule, qs)){
                    populateRule(rule, qs);
                } else {
                    if (rule != null) {
                        addRule(rule);    
                    }
                    rule = AccessRuleFactory.createRule(qs);
                }
            }
            if (rule != null) {
                addRule(rule);    
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
    }
    
    private String getAttributeUri(String testId, String typeId, String value, boolean valueIsLiteral) throws Exception {
        String uri = getAttributeUriFromModel(testId, typeId, value, valueIsLiteral);
        if (StringUtils.isBlank(uri)) {
            final String attributeUri = generateAttributeUri();
            String ttl = ""
                    + "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n"
                    + "@prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/> .\n"
                    + "@prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/> .\n"
                    + "<" + attributeUri + "> rdf:type ao:Attribute . \n"
                    + "<" + attributeUri + "> ao:test <" + getUriById(testId) + "> .\n"
                    + "<" + attributeUri + "> ao:type <" + getUriById(typeId) + "> .\n"
                    + "";
            if (AttributeType.OBJECT_URI.toString().equals(typeId) || 
                AttributeType.SUBJECT_ROLE_URI.toString().equals(typeId)) {
                ttl += "<" + attributeUri + "> ao:value <" + value + "> . \n";
            } else {
                System.out.println(typeId);
                ttl += "<" + attributeUri + "> ao:value <" + getUriById(value) + "> . \n";
            }
            try {
                Model ruleData = ModelFactory.createDefaultModel();
                ruleData.read(IOUtils.toInputStream(ttl, "UTF-8"), null, "TTL");
                updateUserAccountsModel(ruleData, false);
            } catch (IOException e) {
                log.error(e, e);
            }
            return attributeUri;
        } else {
            return uri;
        }
    }
    
    private String getUriById(String id) throws Exception {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(URI_BY_ID_QUERY);
        pss.setLiteral(ID, id);    
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, userAccountsModel);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                RDFNode uri = qs.get(URI);
                if (uri.isResource()) {
                    return uri.asResource().toString();
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        throw new Exception(String.format("Uri by id '%s' not found.", id ));
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String getAttributeUriFromModel(String testId, String typeId, String value, boolean valueIsLiteral) {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(ATTRIBUTE_URI_QUERY);
        if (testId != null) {
            pss.setLiteral(TEST_ID,testId);    
        }
        if (typeId != null) {
            pss.setLiteral(TYPE_ID, typeId);    
        }
        if (value != null) {
            if (valueIsLiteral) {
                pss.setLiteral(LITERAL_VALUE, value);    
            } else {
                pss.setIri(ATTR_VALUE, value);    
            }
        }
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, userAccountsModel);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                RDFNode uri = qs.get(ATTRIBUTE);
                if (uri.isResource()) {
                    return uri.asResource().toString();
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        return "";
    }
    
    private void removeRuleFromModel(String ruleUri) {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(DELETE_QUERY);
        pss.setIri(RULE, ruleUri);
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, userAccountsModel);
        Model ruleData = null;
        try {
              ruleData = qexec.execConstruct();
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        if (ruleData != null) {
            updateUserAccountsModel(ruleData, true);
        }
    }
    
    private void updateUserAccountsModel(Model ruleData, boolean remove) {
        try {
            ChangeSet changeSet = makeChangeSet();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ruleData.write(baos, "N3");
            InputStream in = new ByteArrayInputStream(baos.toByteArray());
            if (remove) {
                changeSet.addRemoval(in, ModelSerializationFormat.N3, ModelNames.USER_ACCOUNTS);    
            } else {
                changeSet.addAddition(in, ModelSerializationFormat.N3, ModelNames.USER_ACCOUNTS);    
            }
            rdfService.changeSetUpdate(changeSet);
        } catch (RDFServiceException e) {
            StringWriter sw = new StringWriter();
            ruleData.write(sw, "TTL");
            log.error("Got " + e.getClass().getSimpleName() + " while updating (remove? " + remove + ") user accounts model \n" + sw.toString());
            log.error(e,e);
        }
    }

    private void addRule(AccessRule rule) {
        rule.addToSet(allRules);
        addRuleToOperationSets(rule);
        addRuleToObjectTypeSets(rule);
        addRuleToObjectUriSets(rule);
    }

    private synchronized void addRuleToObjectUriSets(AccessRule rule) {
        String uri = rule.getObjectUri();
        Set<AccessRule> set;
        if (ruleSetsByObjectUri.containsKey(uri)) {
            set = ruleSetsByObjectUri.get(uri);
        } else {
            set = Collections.synchronizedSet(new HashSet<>());
            ruleSetsByObjectUri.put(uri, set);
        }
        rule.addToSet(set);
    }

    private void addRuleToObjectTypeSets(AccessRule rule) {
        AccessObjectType aot = rule.getObjectType();
        Set<AccessRule> set = ruleSetsByObjectType.get(aot);
        rule.addToSet(set);
    }

    private void addRuleToOperationSets(AccessRule rule) {
        AccessOperation operation = rule.getOperation();
        Set<AccessRule> set = ruleSetsByOperation.get(operation);
        rule.addToSet(set);
    }

    private static void populateRule(AccessRule ar, QuerySolution qs) {
        if (ar == null) {
            return;
        }
        String attributeUri = qs.getResource(ATTRIBUTE).getURI();
        if (ar.getAttributeUris().contains(attributeUri)) {
            log.error("Attribute has already been processed. Shouldn't be here.");
            return;
        }
        try {
            ar.addAttribute(AttributeFactory.createAttribute(qs));
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private static boolean isRuleContinues(AccessRule rule, QuerySolution qs) {
        if (rule == null) {
            return false;
        }
        return rule.getRuleUri().equals(qs.getResource(RULE).getURI());
    }

    private static boolean isInvalidRulesData(QuerySolution qs) {
        if (!qs.contains(RULE) || !qs.get(RULE).isResource()) {
            log.error("Rules data doesn't contain rule resource");
            return true;
        }
        if (!qs.contains(ATTRIBUTE) || !qs.get(ATTRIBUTE).isResource()) {
            log.error("Rules data doesn't contain attribute resource");
            return true;
        }
        return false;
    }

    public Set<AccessRule> getFilteredRules(AuthorizationRequest ar) {
        Set<Set<AccessRule>> ruleSets = new HashSet<>();
        getFilteredByOperation(ar.getAccessOperation(), ruleSets);
        AccessObject accessObject = ar.getAccessObject();
        if (accessObject != null) {
            getFilteredByObjectType(accessObject.getType(), ruleSets);
            getFilteredByObjectUri(accessObject.getUri(), ruleSets);
        }
        return getIntersection(ruleSets);
    }


    private Set<AccessRule> getGrantedRoleRulesForEntityUri(String entityURI, AccessOperation operation) {
        Set<Set<AccessRule>> ruleSets = new HashSet<>();
        Set<AccessRule> filteredByObjectUri = ruleSetsByObjectUri.get(entityURI);
        if (filteredByObjectUri != null) {
            ruleSets.add(filteredByObjectUri);
        }
        Set<AccessRule> filteredByOperation = ruleSetsByOperation.get(operation);
        if (filteredByOperation != null) {
            ruleSets.add(filteredByOperation);
        }
        Set<AccessRule> ruleset = getIntersection(ruleSets);
        return ruleset;
    }
    
    private void getFilteredByObjectUri(String uri, Set<Set<AccessRule>> ruleSets) {
        if (uri == null) {
            return;
        }
        Set<AccessRule> filteredByObjectUri = ruleSetsByObjectUri.get(uri);
        if (filteredByObjectUri != null) {
            ruleSets.add(filteredByObjectUri);
        }
    }

    private void getFilteredByObjectType(AccessObjectType aot, Set<Set<AccessRule>> ruleSets) {
        if (aot == AccessObjectType.ANY) {
            return;
        }
        Set<AccessRule> filteredByObjectType = ruleSetsByObjectType.get(aot);
        if (filteredByObjectType != null) {
            ruleSets.add(filteredByObjectType);
        }
    }

    private void getFilteredByOperation(AccessOperation aop, Set<Set<AccessRule>> ruleSets) {
        if (aop == null) {
            log.error("Access operation is null in request " + aop);
            return;
        }
        Set<AccessRule> filteredByOperation = ruleSetsByOperation.get(aop);
        if (filteredByOperation != null) {
            ruleSets.add(filteredByOperation);
        }
    }

    private Set<AccessRule> getIntersection(Set<Set<AccessRule>> ruleSets) {
        if (ruleSets.isEmpty()) {
            return Collections.emptySet();
        }
        Set<AccessRule> intersection = new HashSet<>(findMinimalSet(ruleSets));
        if (intersection.isEmpty()) {
            return intersection;
        }
        for (Set<AccessRule> set : ruleSets) {
            intersection.retainAll(set);
            if (intersection.isEmpty()) {
                return intersection;
            }
        }
        return intersection;
    }

    private Set<AccessRule> findMinimalSet(Set<Set<AccessRule>> ruleSets) {
        Set<AccessRule> min = ruleSets.iterator().next();
        for (Set<AccessRule> set : ruleSets) {
            if (set.size() < min.size()) {
                min = set;
            }
        }
        return min;
    }

    public void updateEntityRules(String entityUri, AccessObjectType aot, AccessOperation operation, Set<String> newRoleUris) {
        Set<String> currentRoleUris =  new HashSet<>(getGrantedRoleUris(entityUri, operation));
        //remove already existing roles, now list contains roles to allow access.
        Set<String> roleUrisToCreate = new HashSet<String>(newRoleUris);
        roleUrisToCreate.removeAll(currentRoleUris);
        for (String roleUri : roleUrisToCreate) {
            createEntityRule(entityUri, aot, operation, roleUri);
        }
        Set<String> rolesUrisToRemove = new HashSet<String>(currentRoleUris);
        rolesUrisToRemove.removeAll(newRoleUris);
        for (String roleUri : rolesUrisToRemove) {
            removeEntityRule(entityUri, aot, operation, roleUri);
        }
    }
    
    protected void removeEntityRule(String entityUri, AccessObjectType aot, AccessOperation operation, String roleUri) {
        AccessObjectImpl ao = new AccessObjectImpl(entityUri, aot);
        AuthorizationRequest ar = new SimpleAuthorizationRequest(ao, operation);
        ar.setRoleUris(Collections.singletonList(roleUri));
        Set<AccessRule> rules = getFilteredRules(ar);
        for (AccessRule rule : rules) {
            //Check number of attributes to avoid removing custom rules
            if (rule.match(ar) && rule.getAttributes().size() == 4) {
                rule.removeFromSets();
                removeRuleFromModel(rule.getRuleUri()); 
            }
        }
    }

    protected void createEntityRule(String objectUri, AccessObjectType accessObjectType, AccessOperation operation, String roleUri) {
        //create 4 attribute uris or get uris 
        String equals = TestType.EQUALS.toString();
        String ruleUri = generateRuleUri();
        try {
            String subjectRoleAtt = getAttributeUri(equals, AttributeType.SUBJECT_ROLE_URI.toString(), roleUri, false);
            String operationAtt = getAttributeUri(equals, AttributeType.OPERATION.toString(), operation.toString(), true);
            String objectUriAtt = getAttributeUri(equals,  AttributeType.OBJECT_URI.toString(), objectUri, true);
            String objectTypeAtt = getAttributeUri(equals, AttributeType.OBJECT_TYPE.toString(), accessObjectType.toString(), true);
            String ttl = generateEntityRuleData(ruleUri, subjectRoleAtt, operationAtt, objectUriAtt, objectTypeAtt);
            Model ruleData = ModelFactory.createDefaultModel();
            ruleData.read(IOUtils.toInputStream(ttl, "UTF-8"), null, "TTL");
            updateUserAccountsModel(ruleData, false);
            loadRule(ruleUri);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private String generateRuleUri() {
        return "https://vivoweb.org/ontology/vitro-application/auth/individual/rule/" + generateUUID();
    }
    
    private String generateAttributeUri() {
        return "https://vivoweb.org/ontology/vitro-application/auth/individual/attribute/" + generateUUID();
    }
    

    private String generateEntityRuleData(String ruleUri, String subjectRoleAttribute, String operationAttribute,
            String objectUriAttribute, String objectTypeAttribute) {
        String ttl = ""
                + "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n"
                + "@prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/> .\n"
                + "@prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/> .\n"
                + "<" + ruleUri + "> rdf:type ao:Rule . \n"
                + "<" + ruleUri + "> ao:attribute <" + subjectRoleAttribute + "> .\n"
                + "<" + ruleUri + "> ao:attribute <" + operationAttribute + "> .\n"
                + "<" + ruleUri + "> ao:attribute <" + objectUriAttribute + "> .\n"
                + "<" + ruleUri + "> ao:attribute <" + objectTypeAttribute + "> .";
        return ttl;
    }

    public static void deletedEntityEvent(Property oldObj) {
        log.error("delete entity event for property " + oldObj );
        // TODO Auto-generated method stub
    }

    public static void updatedEntityEvent(Object oldObj, Object newObj) {
        // TODO Auto-generated method stub
        if (oldObj instanceof Property || newObj instanceof Property)
        log.error("update entity event old " + oldObj + " new object " + newObj );
    }

    public static void insertedEntityEvent(Property newObj) {
        // TODO Auto-generated method stub
        log.error("insert entity event for property " + newObj );
    }

    public List<String> getGrantedRoleUris(String entityUri, AccessOperation operation) {
        if (StringUtils.isBlank(entityUri)) {
            return Collections.emptyList();
        }
        Set<AccessRule> rulesWithGrantedRoles = getGrantedRoleRulesForEntityUri(entityUri, operation);
        return getGrantedUrisFromRules(rulesWithGrantedRoles);
    }

    private List<String> getGrantedUrisFromRules(Set<AccessRule> ruleset) {
        List<String> roleUris = Collections.emptyList();
        for (AccessRule ar : ruleset) {
            Set<Attribute> atts = ar.getAttributesByType(AttributeType.SUBJECT_ROLE_URI);
            if (!atts.isEmpty()) {
                String uri = atts.iterator().next().getValue();
                if (uri != null) {
                    if(roleUris.isEmpty()) {
                        roleUris = new LinkedList<>();
                    }
                    roleUris.add(uri);
                }
            }
        }
        return roleUris;
    }

    
    public long getRulesCount() {
        return allRules.size();
    }
    
    public long getModelSize() {
        return userAccountsModel.size();
    }
    
    private ChangeSet makeChangeSet() {
        ChangeSet cs = rdfService.manufactureChangeSet();
        cs.addPreChangeEvent(new BulkUpdateEvent(null, true));
        cs.addPostChangeEvent(new BulkUpdateEvent(null, false));
        return cs;
    }

}
