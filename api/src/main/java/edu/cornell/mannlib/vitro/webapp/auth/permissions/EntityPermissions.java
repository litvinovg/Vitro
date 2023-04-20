package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import edu.cornell.mannlib.vitro.webapp.modelaccess.ContextModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;

public class EntityPermissions {
    
    private static final Log log = LogFactory.getLog(EntityPermissions.class);
    /**
     * Static fields for all EntityPermissions
     */
    static final Map<String, EntityPermission> allInstances = new HashMap<>();
    static ContextModelAccess ctxModels = null;

    public static List<EntityPermission> getAllInstances(ContextModelAccess models) {
        if (ctxModels == null && models != null) {
            ctxModels = models;
        }
    
        if (EntityPermissions.allInstances.isEmpty()) {
            if (ctxModels == null) {
                throw new IllegalStateException("ContextModelAccess must be initialized");
            }
    
            getAllInstances(EntityDisplayPermission.class);
            getAllInstances(EntityUpdatePermission.class);
            getAllInstances(EntityPublishPermission.class);
    
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

    private static void getAllInstances(Class<? extends EntityPermission> clazz) {
        OntModel accountsModel = ctxModels.getOntModel(ModelNames.USER_ACCOUNTS);
        try {
            accountsModel.enterCriticalSection(Lock.READ);
    
            StmtIterator typeIter = accountsModel.listStatements(null, RDF.type, accountsModel.getResource("java:" + clazz.getName() + "#Set"));
            while (typeIter.hasNext()) {
                Statement stmt = typeIter.next();
                if (stmt.getSubject().isURIResource()) {
                    String uri = stmt.getSubject().getURI();
    
                    Constructor<? extends EntityPermission> ctor = null;
                    try {
                        ctor = clazz.getConstructor(String.class);
                        EntityPermission permission = ctor.newInstance(new Object[]{uri});
    
                        Resource limitResource = accountsModel.getResource("java:" + clazz.getName() + "#SetLimitToRelatedUser");
                        permission.limitToRelatedUser = accountsModel.contains(stmt.getSubject(), RDF.type, limitResource);
                        allInstances.put(uri, permission);
                    } catch (NoSuchMethodException | InstantiationException |
                            IllegalAccessException | InvocationTargetException e) {
                        log.error("EntityPermission <" + clazz.getName() + "> could not be created", e);
                    }
                }
            }
        } finally {
            accountsModel.leaveCriticalSection();
        }
    }

    private static void updateAllPermissions() {
        if (allInstances.isEmpty()) {
            return;
        }

        WebappDaoFactory wadf = ctxModels.getWebappDaoFactory();

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
