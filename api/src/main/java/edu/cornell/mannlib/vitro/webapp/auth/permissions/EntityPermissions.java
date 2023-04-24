package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.Lock;
import org.apache.jena.vocabulary.RDF;

import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;
import edu.cornell.mannlib.vitro.webapp.beans.FauxProperty;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.WebappDaoFactory;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;

public class EntityPermissions {
    
    /**
     * Static fields for all EntityPermissions
     */
    static final Map<String, EntityPermission> allInstances = new HashMap<>();

    public static List<EntityPermission> getAllInstances() {
    
        if (EntityPermissions.allInstances.isEmpty()) {
            collectAllInstances();
            updateAllPermissions();
        }
    
        return new ArrayList<>(allInstances.values());
    }
    
    public static void updateAllPermissionsFor(Property p) {
        if (allInstances.isEmpty()) {
            return;
        }
    
        for (EntityPermission instance : allInstances.values()) {
            instance.updateFor(p);
        }
    }

    private static void collectAllInstances() {
        OntModel accountsModel = ModelAccess.getInstance().getOntModel(ModelNames.USER_ACCOUNTS);
        try {
            accountsModel.enterCriticalSection(Lock.READ);
            String[] actions = {"Display", "Update","Publish"};
            for (String action : actions) {
                StmtIterator typeIter = accountsModel.listStatements(null, RDF.type, accountsModel.getResource("java:edu.cornell.mannlib.vitro.webapp.auth.permissions.Entity" + action + "Permission" + "#Set"));
                while (typeIter.hasNext()) {
                    Statement stmt = typeIter.next();
                    if (stmt.getSubject().isURIResource()) {
                        String uri = stmt.getSubject().getURI();
                        EntityPermission permission = createEntityPermission(action, uri);
                        Resource limitResource = accountsModel.getResource("java:edu.cornell.mannlib.vitro.webapp.auth.permissions.Entity" + action + "Permission" + "#SetLimitToRelatedUser");
                        permission.limitToRelatedUser = accountsModel.contains(stmt.getSubject(), RDF.type, limitResource);
                        allInstances.put(uri, permission);
                       
                    }
                }
            }
        } finally {
            accountsModel.leaveCriticalSection();
        }
    }
    
    private static EntityPermission createEntityPermission(String action, String uri) {
        if (action.equals("Update")) {
            return new EntityUpdatePermission(uri);
        }
        if (action.equals("Publish")) {
            return new EntityPublishPermission(uri);
        }
        //Display
        return new EntityDisplayPermission(uri);
    }

    private static void updateAllPermissions() {
        if (allInstances.isEmpty()) {
            return;
        }

        WebappDaoFactory wadf = ModelAccess.getInstance().getWebappDaoFactory();

        Map<String, PropertyDao.FullPropertyKey> propertyKeyMap = new HashMap<>();

        for (ObjectProperty oProp : wadf.getObjectPropertyDao().getAllObjectProperties()) {
            propertyKeyMap.put(oProp.getURI(), new PropertyDao.FullPropertyKey(oProp.getURI()));
            for (FauxProperty fProp : wadf.getFauxPropertyDao().getFauxPropertiesForBaseUri(oProp.getURI())) {
                propertyKeyMap.put(fProp.getConfigUri(), new PropertyDao.FullPropertyKey(fProp.getConfigUri()));
            }
        }
        for (DataProperty dProp : wadf.getDataPropertyDao().getAllDataProperties()) {
            propertyKeyMap.put(dProp.getURI(), new PropertyDao.FullPropertyKey(dProp.getURI()));
            for (FauxProperty fProp : wadf.getFauxPropertyDao().getFauxPropertiesForBaseUri(dProp.getURI())) {
                propertyKeyMap.put(fProp.getConfigUri(), new PropertyDao.FullPropertyKey(fProp.getConfigUri()));
            }
        }

        for (EntityPermission instance : allInstances.values()) {
            instance.update(propertyKeyMap);
        }
    }

}
