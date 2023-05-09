/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.objects;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

public abstract class AccessObject {

	public static String ACTION_NAMESPACE = "java:";
    public static String SOME_URI = "?SOME_URI";
    public static Property SOME_PREDICATE = new Property(SOME_URI);
    public static String SOME_LITERAL = "?SOME_LITERAL";
    
    //public abstract PolicyContract getPolicyContract();
    
    /**
     * In its most basic form, a RequestAction needs to have an identifier.
     * Sometimes this will be enough.
     */
    public String getUri() {
    	return ACTION_NAMESPACE + this.getClass().getName();
    }

    @Override
    public String toString() {
    	return this.getClass().getSimpleName();
    }
    
    public AccessObjectType getType() {
        return AccessObjectType.NAMED_OBJECT_URI;
    }

}
