/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.audit.storage;

/**
 * Factory for Audit DAOs
 */
public class AuditDAOFactory {
    // Available storage engines - plain file syatem, or Jena TDB
    public enum Storage { AUDIT_FS, AUDIT_TDB };

    // Configured storage engine.
    private static Storage storage;

    /**
     * Initialise the factory
     *
     * @param storage
     */
    public static void initialize(Storage storage) {
        if (AuditDAOFactory.storage != null) {
            throw new IllegalStateException("AduitDAOFactory already initialized");
        }
        AuditDAOFactory.storage = storage;
    }

    /**
     * Clean up for the factory
     */
    public static void shutdown() {
    }

    /**
     * Get an Audit DAO instance
     *
     * @param req
     * @return
     */
    public static AuditDAO getAuditDAO() {
        if (storage == null) {
            throw new IllegalStateException("AduitDAOFactory not initialized");
        }

        switch (storage) {
            case AUDIT_FS:
                return new AuditDAOFS();

            case AUDIT_TDB:
                return new AuditDAOTDB();
        }

        throw new UnsupportedOperationException("Unsupported Audit storage");
    }
}
