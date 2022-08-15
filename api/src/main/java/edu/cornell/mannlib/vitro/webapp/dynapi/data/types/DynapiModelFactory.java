package edu.cornell.mannlib.vitro.webapp.dynapi.data.types;

import javax.servlet.ServletContext;

import org.apache.jena.rdf.model.Model;

import edu.cornell.mannlib.vitro.webapp.modelaccess.ContextModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;

public class DynapiModelFactory {

    private static DynapiModelFactory INSTANCE = new DynapiModelFactory();
	private static ContextModelAccess modelAccess;

	public static DynapiModelFactory getInstance() {
        return INSTANCE;
	}

	public void init(ServletContext ctx) {
		modelAccess = ModelAccess.on(ctx);		
	}

	public static Model getModel(String modelName) {
		Model model = modelAccess.getOntModel(modelName);
		return model;
	}
}
