package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeFactory;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRuleFactory;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFService;
import edu.cornell.mannlib.vitro.webapp.rdfservice.impl.jena.model.RDFServiceModel;

public class PolicyLoader {

    private static final String PRIORITY = "priority";
    private static final String POLICY = "policy";
    private static final Log log = LogFactory.getLog(PolicyLoader.class);
    private static final String POLICY_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?" + POLICY + " ?" + PRIORITY + " \n"
          + "WHERE {\n"
          + "?" + POLICY + " rdf:type ao:Policy .\n"
          + "OPTIONAL {?" + POLICY + " ao:priority ?set_priority" + " . }\n"
          + "BIND(COALESCE(?set_priority, 0 ) as ?" + PRIORITY + " ) .\n"
          + "} ORDER BY ?" + PRIORITY;
    
    private static final String PRIORITY_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?" + PRIORITY + " \n"
          + "WHERE {\n"
          + "?" + POLICY + " rdf:type ao:Policy .\n"
          + "OPTIONAL {?" + POLICY + " ao:priority ?set_priority" + " . }\n"
          + "BIND(COALESCE(?set_priority, 0 ) as ?" + PRIORITY + " ) .\n"
          + "} ORDER BY ?" + PRIORITY;
    
    private static final String POLICY_URI_BY_KEY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?" + POLICY + " \n"
          + "WHERE {\n"
          + "?" + POLICY + " rdf:type ao:Policy .\n"
          + "}";
    
    private static final String DATASET_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?dataSet \n"
          + "WHERE {\n"
          + "       ?policy ao:testDatasets ?dataSets .\n"
          + "       ?policy rdf:type ao:Policy .\n"
          + "       ?dataSets ao:testDataset ?dataSet .\n"
          + "} ORDER BY ?dataSet";
    
    private static final String NO_DATASET_RULES_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?rules ?rule ?attribute ?testId ?typeId ?value ?lit_value ?decision_id \n"
          + "WHERE {\n"
          + "?policy rdf:type ao:Policy .\n"
          + "?policy ao:rules ?rules . \n"
          + "?rules ao:rule ?rule . \n"
          + "?rule ao:attribute ?attribute .\n"
          + "OPTIONAL {\n"
          + "  ?attribute ao:test ?attributeTest .\n"
          + "  OPTIONAL {\n"
          + "    ?attributeTest ao:id ?testId . \n"
          + "  }\n"
          + "}"
          + "OPTIONAL {\n"
          + "  ?attribute ao:type ?attributeType . \n"
          + "  OPTIONAL {\n"
          + "    ?attributeType ao:id ?typeId . \n"
          + "  }\n"
          + "}\n"
          + "OPTIONAL {\n"
          + "   ?rule ao:decision ?decision . \n"
          + "   ?decision ao:id ?decision_id . \n"
          + "}\n"
          + "?attribute ao:value ?value . \n"
          + "OPTIONAL {?value ao:id ?lit_value . }\n"
          + "} ORDER BY ?rule ?attribute";
    
    private static final String DATASET_RULES_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?rules ?rule ?attribute ?testId ?typeId ?value ?lit_value ?decision_id ?dataSetUri \n"
          + "WHERE {\n"
          + "?policy rdf:type ao:Policy .\n"
          + "?policy ao:rules ?rules . \n"
          + "?rules rdf:type ao:Rules . \n"
          + "?rules ao:rule ?rule . \n"
          + "?rule ao:attribute ?attribute . \n"
          + "?attribute rdf:type ao:Attribute .\n"
          + "OPTIONAL {\n"
          + "  ?attribute ao:test ?attributeTest .\n"
          + "  OPTIONAL {\n"
          + "    ?attributeTest ao:id ?testId . \n"
          + "  }\n"
          + "}"
          + "OPTIONAL {\n"
          + "  ?attribute ao:type ?attributeType . \n"
          + "  OPTIONAL {\n"
          + "    ?attributeType ao:id ?typeId . \n"
          + "  }\n"
          + "}\n"
          + "OPTIONAL {\n"
          + "   ?rule ao:decision ?decision . \n"
          + "   ?decision ao:id ?decision_id . \n"
          + "}\n"
          + "OPTIONAL {\n"
          + "   ?attribute ao:setValue ?testData . \n"
          + "   ?dataSet ao:testData ?testData . \n"
          + "   ?testData ao:dataValue ?value . \n"
          + "   OPTIONAL {?value ao:id ?lit_value . }\n"
          + "}\n"
          + "OPTIONAL {\n"
          + "   ?attribute ao:value ?value . \n"
          + "   OPTIONAL {?value ao:id ?lit_value . }\n"
          + "}\n"
          + "BIND(?dataSet as ?dataSetUri)\n"
          + "} ORDER BY ?rule ?attribute";
    
    private Model userAccountsModel;
    private RDFService rdfService;
    private static PolicyLoader INSTANCE;

    public static PolicyLoader getInstance() {
        return INSTANCE;
    }
    
    private PolicyLoader(Model model) {
        if (model != null) {
            this.userAccountsModel = model;
        } else {
            this.userAccountsModel = ModelAccess.getInstance().getOntModelSelector().getUserAccountsModel();
            //this.rdfService = ModelAccess.getInstance().getRDFService(WhichService.CONFIGURATION);
            this.rdfService = new RDFServiceModel(userAccountsModel);
            loadPolicies();
        }
    }

    private Model getUserAccountsModel() {
        return userAccountsModel;
    }

    public static void initialize(Model model) {
        INSTANCE = new PolicyLoader(model);
    }
    
    private void loadPolicies() {
        List<String> policyUris = getPolicyUris();
        for (String uri : policyUris) {
            debug("Loading policy %s", uri);
            DynamicPolicy policy = loadPolicy(uri);
            if (policy != null) {
                debug("Loaded policy %s", uri);
                //take policy priority into account
                PolicyStore.getInstance().add(policy);
            }
        }
    }

    public List<String> getPolicyUris() {
        debug("SPARQL Query to get policy uris from the graph:\n %s", POLICY_QUERY);
        Query query = QueryFactory.create(POLICY_QUERY);
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        List<String> policyUris = new LinkedList<String>();
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (!qs.contains(POLICY) || !qs.get(POLICY).isResource()) {
                    //debug("Policy solution doesn't contain policy resource");
                    continue;
                }
                policyUris.add(qs.getResource(POLICY).getURI());
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        return policyUris;
    }
    
    public DynamicPolicy loadPolicy(String uri) {
        List<String> dataSetNames = getDataSetNames(uri);
        Set<AccessRule> rules = new HashSet<>();
        long priority = getPriority(uri);
        try {
            if (dataSetNames.isEmpty()) {
                loadRulesWithoutDataSet(uri, rules);
            } else {
                for (String dataSetName : dataSetNames) {
                    loadRulesForDataSet(uri, rules, dataSetName);
                }
            }
        } catch (Exception e) {
            return null;
        }
        if (rules.isEmpty()) {
            return null;
        }
        DynamicPolicy policy = new DynamicPolicy(uri, priority);
        policy.addRules(rules);
        return policy;
    }

    private long getPriority(String uri) {
        //debug("SPARQL Query to get policy uris from the graph:\n %s", POLICY_URIS_QUERY);
        ParameterizedSparqlString pss = new ParameterizedSparqlString(PRIORITY_QUERY);
        pss.setIri(POLICY, uri);
        //debug("Get priority query:\n %s", pss.toString());
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (!qs.contains(PRIORITY) || !qs.get(PRIORITY).isLiteral()) {
                    return 0;
                }
                return qs.getLiteral(PRIORITY).getLong();
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        return 0;
    }

    private void loadRulesWithoutDataSet(String policyUri, Set<AccessRule> rules) throws Exception {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(NO_DATASET_RULES_QUERY);
        pss.setIri(POLICY, policyUri);
        debug(pss.toString());
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        try {
            ResultSet rs = qexec.execSelect();
            AccessRule rule = null;
            //debugSelectQueryResults(rs);
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (isInvalidPolicySolution(policyUri, qs)) {
                    throw new Exception();
                }
                if (isRuleContinues(rule, qs)){
                    populateRule(rule, qs);
                } else {
                    if (rule != null) {
                        rules.add(rule);    
                    }
                    rule = AccessRuleFactory.createRule(qs);
                }
            }
            if (rule != null) {
                rules.add(rule);
                debug("\nLoaded %s rules for %s policy", rules.size(), policyUri);
            } else {
                debug("\nNo rules loaded from the user accounts model for %s policy.", policyUri);
            }
        } finally {
            qexec.close();
        }
    }
    
    private void loadRulesForDataSet(String policyUri, Set<AccessRule> rules, String dataSetName) throws Exception {
        ParameterizedSparqlString pss = new ParameterizedSparqlString(DATASET_RULES_QUERY);
        pss.setIri(POLICY, policyUri);
        pss.setIri("dataSet", dataSetName);
        debug(pss.toString());
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        try {
            ResultSet rs = qexec.execSelect();
            AccessRule rule = null;
            //debugSelectQueryResults(rs);
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (isInvalidPolicySolution(policyUri, qs)) {
                    throw new Exception();
                }
                if (isRuleContinues(rule, qs)){
                    populateRule(rule, qs);
                } else {
                    if (rule != null) {
                        rules.add(rule);    
                    }
                    rule = AccessRuleFactory.createRule(qs);
                }
            }
            if (rule != null) {
                rules.add(rule);
                debug("\nLoaded %s rules for %s policy", rules.size(), policyUri);
            } else {
                debug("\nNo rules loaded from the user accounts model for %s policy.", policyUri);
            }
        } finally {
            qexec.close();
        }
    }

    private List<String> getDataSetNames(String uri) {
        List<String> result = new ArrayList<>();
        ParameterizedSparqlString pss = new ParameterizedSparqlString(DATASET_QUERY);
        pss.setIri(POLICY, uri);
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (qs.contains("dataSet")) {
                    RDFNode dataSet = qs.get("dataSet");
                    if (dataSet.isResource()) {
                        result.add(dataSet.asResource().getURI());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        return result;
    }

    private void debugSelectQueryResults(ResultSet rs) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(baos, rs);
        String json = new String(baos.toByteArray());
        debug(json);
    }
    
    private static boolean isRuleContinues(AccessRule rule, QuerySolution qs) {
        if (rule == null) {
            return false;
        }
        String ruleUri = qs.getResource("rule").getURI();
        if (qs.contains("dataSetUri")) {
            ruleUri += "." + qs.getResource("dataSetUri").getURI();    
        }
        return rule.getRuleUri().equals(ruleUri);
    }
    
    private static void populateRule(AccessRule ar, QuerySolution qs) throws Exception {
        if (ar == null) {
            return;
        }
        String attributeUri = qs.getResource("attribute").getURI();
        if (ar.containsAttributeUri(attributeUri)) {
            AttributeFactory.extendAttribute(ar.getAttribute(attributeUri), qs);
            return;
        } 
        try {
            ar.addAttribute(AttributeFactory.createAttribute(qs));
        } catch (Exception e) {
            log.error(e, e);
        }
    }
    
    private static boolean isInvalidPolicySolution(String uri, QuerySolution qs) {
        if (!qs.contains("rules") || !qs.get("rules").isResource()) {
            debug("Policy <%s> solution doesn't contain rules uri", uri);
            return true;
        }
        if (!qs.contains("rule") || !qs.get("rule").isResource()) {
            debug("Policy <%s> solution doesn't contain rule uri", uri);
            return true;
        }
        if (!qs.contains("value")) {
            debug("Policy <%s> solution doesn't contain value", uri);
            return true;
        }
        if (!qs.contains("typeId") || !qs.get("typeId").isLiteral()) {
            debug("Policy <%s> solution doesn't contain attribute type id", uri);
            return true;
        }
        if (!qs.contains("testId") || !qs.get("testId").isLiteral()) {
            debug("Policy <%s> solution doesn't contain attribute test id", uri);
            return true;
        }
        return false;
    }
    
    private static void debug(String template, Object... objects) {
        if (log.isDebugEnabled()) {
            log.error(String.format(template, objects ));
        }
    }
}
