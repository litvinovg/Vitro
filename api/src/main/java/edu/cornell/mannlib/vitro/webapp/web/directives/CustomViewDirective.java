/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.web.directives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.utils.dataGetter.DataGetter;
import edu.cornell.mannlib.vitro.webapp.utils.dataGetter.DataGetterUtils;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.ontology.OntModel;

import static edu.cornell.mannlib.vitro.webapp.freemarker.config.FreemarkerConfigurationImpl.DISABLE_DATA_GETTERS_RUN;

/**
 * Find custom template. Make substitutions in data getters, get required data,
 * and render the template.
 */
public class CustomViewDirective extends BaseTemplateDirectiveModel {
    private static final Log log = LogFactory.getLog(CustomViewDirective.class);

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String templateName = getRequiredSimpleScalarParameter(params, "template");
        HttpServletRequest req = (HttpServletRequest) env.getCustomAttribute("request");
        VitroRequest vreq = new VitroRequest(req);
        try {
            env.setCustomAttribute(DISABLE_DATA_GETTERS_RUN, true);
            Template template = env.getTemplateForInclusion(templateName, null, true);
            OntModel model = vreq.getDisplayModel();
            List<DataGetter> dataGetters = DataGetterUtils.getDataGettersForTemplate(vreq, model, templateName);
            debug(String.format("Retrieved %d data getters for template '%s'", dataGetters.size(), templateName));
            Map<String, Object> parameters = getOptionalHashModelParameter(params, "parameters");
            for (DataGetter dataGetter : dataGetters) {
                applyDataGetter(dataGetter, env, parameters);
            }
            env.include(template);
        } catch (IOException e) {
            handleException(templateName, "Could not load template '%s'", e);
        } catch (Exception e) {
            handleException(templateName, "Could not process template '%s'", e);
        } finally {
            env.setCustomAttribute(DISABLE_DATA_GETTERS_RUN, false);
        }
    }

    /**
     * Get the data from a DataGetter, provide variable values for substitution and
     * store results in global variables in the Freemarker environment.
     */
    private static void applyDataGetter(DataGetter dataGetter, Environment env, Map<String, Object> parameters)
            throws TemplateModelException {
        Map<String, Object> moreData = dataGetter.getData(parameters);
        ObjectWrapper wrapper = env.getObjectWrapper();
        if (moreData != null) {
            for (String key : moreData.keySet()) {
                Object value = moreData.get(key);
                env.setVariable(key, wrapper.wrap(value));
                debug(String.format("Stored in environment: '%s' = '%s'", key, value));
            }
        }
    }

    /**
     * Handle exceptions that could happen in template processing
     */
    private void handleException(String templateName, String messageTemplate, Exception e) {
        log.error(String.format(messageTemplate, templateName));
        log.error(e, e);
        renderErrorMessage(templateName);
    }

    /**
     * If there is a problem rendering the template, render error instead.
     */
    private void renderErrorMessage(String template) {
        Environment env = Environment.getCurrentEnvironment();
        try {
            env.getOut().append(String.format("<span>Can't process the custom view for %s</span>", template));
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    @Override
    public Map<String, Object> help(String name) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("effect", "Find the freemarker template and optional DataGetters. "
                + "Apply parameter substitutions in DataGetters." + "Execute the DataGetters and render the template.");
        map.put("comments", "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("template", "Freemarker template file name");
        params.put("parameters", "Map of parameters and values");
        map.put("parameters", params);

        List<String> examples = new ArrayList<String>();
        examples.add("<@customView template=customView.ftl \n"
                + "parameters = { \"object\": \"http://objUri\", \"property\": \"http://propUri\" } />");
        map.put("examples", examples);

        return map;
    }
    
    private static void debug(String message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }

}
