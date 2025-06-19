package edu.cornell.mannlib.vitro.webapp.auth.policy.setup;

import static edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary.AUTH_VOCABULARY_PREFIX;

import java.util.HashMap;
import java.util.Map;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeValueSet;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeValueSetRegistry;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyLoader;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;
import edu.cornell.mannlib.vitro.webapp.utils.sparql.SparqlQueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.util.graph.GraphListenerBase;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

public class DefaultPermissionListener extends GraphListenerBase {

    private static final Log log = LogFactory.getLog(DefaultPermissionListener.class);
    private static Map<String, String> typesMap = new HashMap<>() {
        {
            put(OWL.Class.getURI(), AccessObjectType.CLASS.toString());
            put(OWL.DatatypeProperty.getURI(), AccessObjectType.DATA_PROPERTY.toString());
            put(OWL.ObjectProperty.getURI(), AccessObjectType.OBJECT_PROPERTY.toString());
        }
    };
    private static final String QUERY = "" +
            "PREFIX access: <https://vivoweb.org/ontology/vitro-application/auth/vocabulary/> \n" +
            "SELECT ?valueSet \n" +
            "WHERE {\n" +
            "  ?valueSet a access:ValueSet ;\n" +
            "    access:isDefaultForNewElements true ;\n" +
            "    access:containsElementsOfType ?elementType .\n" +
            "    ?elementType access:id ?type ." +
            "}";
    @Override
    protected void addEvent(Triple triple) {
        if (!triple.getPredicate().hasURI(RDF.type.getURI()) || !triple.getSubject().isURI() || !(triple.getObject()
                .hasURI(OWL.Class.getURI()) || triple.getObject().hasURI(OWL.DatatypeProperty.getURI()) || triple
                        .getObject().hasURI(OWL.ObjectProperty.getURI()))) {
            return;
        }
        OntModel acModel = ModelAccess.getInstance().getOntModel(ModelNames.ACCESS_CONTROL);
        if (acModel.containsResource(ResourceFactory.createResource(triple.getSubject().getURI()))) {
            return;
        }
        addToAttributeValueSets(triple.getSubject().getURI(), typesMap.get(triple.getObject().getURI()));
        //find all ValueSets with applicable type and add new URI to set, update in-memory value sets.
        log.error(String.format("Create permissions for type %s, uri %s", triple.getObject().getURI(), triple
                .getSubject().getURI()));
    }

    private void addToAttributeValueSets(String uri, String type) {
        OntModel acModel = ModelAccess.getInstance().getOntModel(ModelNames.ACCESS_CONTROL);
        ParameterizedSparqlString pss = new ParameterizedSparqlString(QUERY);
        pss.setLiteral("type", type);
        Query query = SparqlQueryUtils.create(pss.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, acModel);
        try {
            ResultSet resultSet = qexec.execSelect();
            while (resultSet.hasNext()) {
                QuerySolution qs = resultSet.nextSolution();
                Resource valueSet = qs.getResource("valueSet");
                log.error("Found target value set " + valueSet.getURI());
                AttributeValueSet avs = AttributeValueSetRegistry.getInstance().get(valueSet.getURI());
                if (avs == null) {
                    updateValueSetGraph(uri, acModel, valueSet);
                    PolicyLoader.getInstance().loadInactivePolicies();
                } else {
                    updateValueSetRepresentations(uri, acModel, valueSet, avs);
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            qexec.close();
        }
    }

    private void updateValueSetRepresentations(String uri, OntModel acModel, Resource valueSet, AttributeValueSet avs) {
        updateValueSetGraph(uri, acModel, valueSet);
        avs.add(uri);
    }

    private void updateValueSetGraph(String uri, OntModel acModel, Resource valueSet) {
        acModel.add(acModel.createStatement(valueSet,
                ResourceFactory.createProperty(AUTH_VOCABULARY_PREFIX + "value"),
                ResourceFactory.createResource(uri)));
    }

    @Override
    protected void deleteEvent(Triple t) {
    }

    @Override
    public void notifyAddGraph(Graph g, Graph added) {
        ExtendedIterator<Triple> it = added.find();
        try {
            for ( ;it.hasNext();) {
                Triple t = it.next();
                addEvent(t);
            }
        } finally {
            if (it != null) {
                it.close();
            }
        }
    }

}
