package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.io.ByteArrayOutputStream;
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

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeFactory;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.TestType;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRuleFactory;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFService;
import edu.cornell.mannlib.vitro.webapp.rdfservice.impl.jena.model.RDFServiceModel;

public class PolicyLoader {

    private static final String PRIORITY = "priority";
    private static final String POLICY = "policy";
    private static final Log log = LogFactory.getLog(PolicyLoader.class);
    private static final String POLICY_URIS_QUERY =
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
    
    private static final String POLICY_QUERY =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
          + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
          + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
          + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
          + "prefix auth: <http://vitro.mannlib.cornell.edu/ns/vitro/authorization#>\n"
          + "prefix ai: <https://vivoweb.org/ontology/vitro-application/auth/individual/>\n"
          + "prefix ao: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/>\n"
          + "SELECT DISTINCT ?priority ?rules ?rule ?attribute ?testId ?typeId ?value ?lit_value ?dataSet ?decision_id \n"
          + "WHERE {\n"
          + "?policy rdf:type ao:Policy .\n"
          + "OPTIONAL {?" + POLICY + " ao:priority ?set_priority" + " . }\n"
          + "BIND(COALESCE(?set_priority, 0 ) as ?" + PRIORITY + " ) .\n"
          + "?policy ao:rules ?rules . \n"
          + "?rules ao:rule ?rule . \n"
          + "?rule ao:attribute ?attribute .\n"
          + "?attribute ao:test ?attributeTest .\n"
          + "?attributeTest ao:id ?testId . \n"
          + "?attribute ao:type ?attributeType . \n"
          + "?attributeType ao:id ?typeId . \n"
          + "OPTIONAL {"
          + "   ?rule ao:decision ?decision . \n"
          + "   ?decision ao:id ?decision_id"
          + "}"
          + "OPTIONAL { "
          + "   SELECT ?dataSet WHERE \n"
          + "   {\n"
          + "       ?policy ao:testDatasets ?dataSets .\n"
          + "       ?policy rdf:type ao:Policy .\n"
          + "       ?dataSets ao:testDataset ?dataSet .\n"
          + "   }\n"
          + "}\n"
          + "BIND(EXISTS{ \n"
          + "   ?attribute ao:setValue ?testData . \n"
          + "   ?dataSet ao:testData ?testData . \n"
          + "   ?testData ai:dataValue ?value . \n"
          + "} AS ?cond1)\n"
          + "BIND(EXISTS{ \n"
          + "   ?attribute ao:value ?value . \n"
          + "} AS ?cond2)\n"
          + "{\n"
          + "   ?attribute ao:setValue ?testData . \n"
          + "   ?dataSet ao:testData ?testData . \n"
          + "   ?testData ai:dataValue ?value . \n"
          + "}\n"
          + "UNION \n"
          + "{\n"
          + "   ?attribute ao:value ?value . \n"
          + "}\n"
          + "   OPTIONAL {?value ao:id ?lit_value . }\n"
          + "FILTER( (?cond1 || ?cond2) && ! (?cond1 && ?cond2) )\n"
          + "} ORDER BY ?priority ?rule ?attribute";
    
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
        //debug("SPARQL Query to get policy uris from the graph:\n %s", POLICY_URIS_QUERY);
        Query query = QueryFactory.create(POLICY_URIS_QUERY);
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        List<String> policyUris = new LinkedList<String>();
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (!qs.contains(POLICY) || !qs.get(POLICY).isResource()) {
                    debug("Policy solution doesn't contain policy resource");
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
        ParameterizedSparqlString pss = new ParameterizedSparqlString(POLICY_QUERY);
        pss.setIri(POLICY, uri);
        //debug("SPARQL Query to get policy %s from the graph:\n %s", uri, pss.toString());
        Query query = QueryFactory.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, getUserAccountsModel());
        Set<AccessRule> rules = new HashSet<>();
        long priority = 0;
        try {
            ResultSet rs = qexec.execSelect();
            AccessRule rule = null;
            //debugSelectQueryResults(rs);
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                if (isInvalidPolicySolution(qs)) {
                    return null;
                }
                priority = qs.getLiteral("priority").getLong();
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
                debug("\nLoaded %s rules for %s policy", rules.size(), uri);
            } else {
                debug("\nNo rules loaded from the user accounts model for %s policy.", uri);
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
        if (rules.isEmpty()) {
            return null;
        }
        DynamicPolicy policy = new DynamicPolicy(uri, priority);
        policy.addRules(rules);
        return policy;
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
        return rule.getRuleUri().equals(qs.getResource("rule").getURI());
    }
    
    private static void populateRule(AccessRule ar, QuerySolution qs) {
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
    
    private static boolean isInvalidPolicySolution(QuerySolution qs) {
        if (!qs.contains(PRIORITY) || !qs.get(PRIORITY).isLiteral()) {
            debug("Policy solution doesn't contain priority literal");
            return true;
        }
        if (!qs.contains("rules") || !qs.get("rules").isResource()) {
            debug("Policy solution doesn't contain rules uri");
            return true;
        }
        if (!qs.contains("rule") || !qs.get("rule").isResource()) {
            debug("Policy solution doesn't contain rule uri");
            return true;
        }
        if (!qs.contains("value")) {
            debug("Policy solution doesn't contain value");
            return true;
        }
        if (!qs.contains("typeId") || !qs.get("typeId").isLiteral()) {
            debug("Policy solution doesn't contain attribute type id");
            return true;
        }
        if (!qs.contains("testId") || !qs.get("testId").isLiteral()) {
            debug("Policy solution doesn't contain attribute test id");
            return true;
        }
        return false;
    }
    
    private static void debug(String template, Object... objects) {
        if (true) {
            log.error(String.format(template, objects ));
        }
    }
}
