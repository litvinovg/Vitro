package edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpHeaders;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Action;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.OperationResult;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.Parameters;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.DataStore;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.Data;

public class Converter {

	private static final Log log = LogFactory.getLog(Converter.class.getName());
	private static final String SPLIT_BY_COMMA_AND_TRIM_REGEX = "\\s*,\\s*";
	private static final String SPLIT_CONTENT_TYPE_AND_WEIGHT_REGEX = "\\s*;\\s*q=";
	private static Set<String> supportedContentTypes = new HashSet<>(
			Arrays.asList(ContentType.APPLICATION_JSON.toString(), 
			ContentType.MULTIPART_FORM_DATA.toString(), 
			ContentType.WILDCARD.toString()));

	public static void convert(HttpServletRequest request, Action action, DataStore dataStore)
			throws ConversionException {
		ContentType contentType = getContentType(request);
		ContentType responseType = getResponseType(request.getHeader(HttpHeaders.ACCEPT), contentType);
		dataStore.setResponseType(responseType);
		Set<LangTag> acceptLangs = getAcceptLanguages(request);
		dataStore.setAcceptLangs(acceptLangs);
		if (isJson(contentType)) {
			JSONConverter.convert(request, action, dataStore);
		} else if (isForm(contentType)) {
			FormDataConverter.convert(request, action, dataStore);
		} else {
			String message = inputContentTypeExceptionMessage(contentType);
			throw new ConversionException(message);
		}
		convertInternalParams(action, dataStore);
	}

	private static void convertInternalParams(Action action, DataStore dataStore) throws ConversionException {
		Parameters params = action.getInternalParams();
		for (String name : params.getNames()) {
			Data data = new Data(params.get(name));
			data.earlyInitialization();
			dataStore.addData(name, data);
		}
	}

	protected static ContentType getResponseType(String header, ContentType requestType)
			throws ConversionException {
		TreeMap<Double, ContentType> types = new TreeMap<>(Collections.reverseOrder());
		if (StringUtils.isBlank(header)) {
			if (supportedContentTypes.contains(requestType.toString())) {
				return requestType;
			} else {
				String message = outputContentTypeExceptionMessage(requestType);
				throw new ConversionException(message);
			}
		}
		String[] weightedTypes = header.trim().split(SPLIT_BY_COMMA_AND_TRIM_REGEX);
		for (String wType : weightedTypes) {
			String[] typeInfo = wType.split(SPLIT_CONTENT_TYPE_AND_WEIGHT_REGEX);
			final String typeName = typeInfo[0];
			if (StringUtils.isBlank(typeName)) {
				continue;
			}
			ContentType contentType = null;
			try {
				contentType = ContentType.parse(typeName);
			} catch (Exception e) {
				log.error(e, e);
			}
			if (contentType == null) {
				continue;
			}
			if (!supportedContentTypes.contains(typeName)) {
				continue;
			}
			Double weight = 1.0;
			if (typeInfo.length == 2) {
				final String qfactor = typeInfo[1];
				if (qfactor.length() < 6 && qfactor.matches("^[0-1](\\.[0-9]{1,3})?$")) {
					try {
						weight = Double.parseDouble(qfactor);
					} catch (Exception e) {
						log.error(e, e);
					}
				}
			}
			types.put(weight, contentType);
		}
		if (types.isEmpty()) {
			String message = outputContentTypeExceptionMessage(requestType);
			throw new ConversionException(message);
		}
		final ContentType mostAppropriateType = types.entrySet().iterator().next().getValue();
		if (mostAppropriateType.toString().equals(ContentType.WILDCARD.toString())) {
			return ContentType.APPLICATION_JSON;
		}
		return mostAppropriateType;
	}

	public static void convert(HttpServletResponse response, Action action, OperationResult operationResult,
			DataStore dataStore) throws ConversionException {
		if (!operationResult.hasSuccess()) {
			operationResult.prepareResponse(response);
			return;
		}
		if (!action.isOutputValid(dataStore)) {
			response.setStatus(500);
			return;
		}
		operationResult.prepareResponse(response);
		// TODO: test accepted content types and prepare response according to it
		ContentType responseType = dataStore.getResponseType();
		if (isJson(responseType)) {
			JSONConverter.convert(response, action, dataStore);
		} else if (isForm(responseType)) {
			FormDataConverter.convert(response, action, dataStore);
		} else {
			String message = String.format("No suitable converter found for output content type %s", responseType);
			throw new ConversionException(message);
		}
	}

	private static Set<LangTag> getAcceptLanguages(HttpServletRequest request) {
		Set<LangTag> result = new HashSet<>();
		String header = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if (StringUtils.isBlank(header)) {
			// Default
			result.add(new LangTag("*"));
			return result;
		}
		String[] rawTags = header.trim().split(SPLIT_BY_COMMA_AND_TRIM_REGEX);
		for (String rawTag : rawTags) {
			result.add(new LangTag(rawTag));
		}
		return result;
	}

	private static ContentType getContentType(HttpServletRequest request) {
		String header = request.getContentType();
		if (StringUtils.isBlank(header)) {
			return ContentType.MULTIPART_FORM_DATA;
		}
		return ContentType.parse(header);
	}

	private static boolean isForm(ContentType type) {
		if (ContentType.MULTIPART_FORM_DATA.getMimeType().equals(type.getMimeType())) {
			return true;
		}
		return false;
	}

	private static boolean isJson(ContentType type) {
		if (ContentType.APPLICATION_JSON.getMimeType().equals(type.getMimeType())) {
			return true;
		}
		return false;
	}

	private static String inputContentTypeExceptionMessage(ContentType contentType) {
		return String.format("No suitable converter found for request content type %s", contentType);
	}

	private static String outputContentTypeExceptionMessage(ContentType contentType) {
		return String.format("No suitable converter found for response content type %s", contentType);
	}

}
