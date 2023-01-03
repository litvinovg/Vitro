package edu.cornell.mannlib.vitro.webapp.dynapi.data.implementation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import edu.cornell.mannlib.vitro.webapp.dynapi.data.Data;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion.ConversionException;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion.JSONConverter;

public class JsonContainer {

	public static final String PATH_ROOT_PREFIX = "$";
	public static final String EMPTY_OBJECT = "{}";
	public static final String EMPTY_ARRAY = "[]";
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Type rootType;

	public static enum Type { ARRAY, OBJECT };

	private Map<String, Data> dataMap = new HashMap<>();
	private static final Configuration jsonPathConfig = prepareJsonPathConfig();

	private DocumentContext ctx;

	public JsonContainer(String jsonString) {
		init(jsonString);
	}

    private void init(String jsonString) {
        setRootType(jsonString);
        ctx =  JsonPath.using(jsonPathConfig).parse(jsonString);
    }

    private void setRootType(String jsonString) {
        long cbIndex = jsonString.indexOf("{");
        if (cbIndex == 0) {
            rootType = Type.OBJECT;
            return;
        }
        long sbIndex = jsonString.indexOf("[");
        if (sbIndex == 0) {
            rootType = Type.ARRAY;
            return;
        }
        if (cbIndex < 0 && sbIndex > 0) {
            rootType = Type.ARRAY;
            return;
        }
        if (sbIndex < 0 && sbIndex > 0) {
            rootType = Type.OBJECT;
            return;
        }
        if (sbIndex < cbIndex) {
            rootType = Type.ARRAY;
            return;
        }
        if (sbIndex > cbIndex) {
            rootType = Type.OBJECT;
        }
    }

	public JsonContainer(Type type) {
		if (type.equals(Type.ARRAY)) {
			init(EMPTY_ARRAY);
		} else {
			init(EMPTY_OBJECT);
		}
	}

	public List<String> getDataAsStringList() {
	    List<String> result = new LinkedList<>();
	    for (Data data : dataMap.values()) {
	        result.add(data.getSerializedValue());
	    }
	    return result;
	}
	
	private String getRandomKey() {
		return UUID.randomUUID().toString();
	}

	public static String serialize(JsonContainer object) {
	    //TODO: Copy object and replace keys with values from data map
	    return object.asJsonNode().toString();
	}

	public JsonNode asJsonNode() {
		JsonNode parsedModel = ctx.json();
		JsonNode deepCopy = parsedModel.deepCopy();
		replaceKeys(deepCopy);
		return deepCopy;
	}

	private void replaceKeys(JsonNode node) {
		if (node.isObject()) {
			ObjectNode objectNode = (ObjectNode) node;
			Iterator<Entry<String, JsonNode>> it = objectNode.fields();
			while (it.hasNext()) {
				Map.Entry<String, JsonNode> entry = it.next();
				replaceKeys(entry.getKey(), entry.getValue(), objectNode);
			}
		} else if (node.isArray()) {
			ArrayNode array = (ArrayNode) node;
			Iterator<JsonNode> it = array.elements();
			int i = 0;
			while (it.hasNext()) {
				JsonNode child = it.next();
				replaceKeys(child, array, i);
				i++;
			}
		}
	}

	private void replaceKeys(String key, JsonNode node, ObjectNode parent) {
		if (node.isContainerNode()) {
			replaceKeys(node);
		} else if (node.isValueNode() && dataMap.containsKey(node.asText())) {
			Data data = dataMap.get(node.asText());
			JsonNode dataValue = JSONConverter.convertDataValue(data);
            parent.set(key, dataValue);
		}
	}

	private void replaceKeys(JsonNode node, ArrayNode parent, int i) {
		if (node.isContainerNode()) {
			replaceKeys(node);
		} else if (node.isValueNode() && dataMap.containsKey(node.asText())) {
			Data data = dataMap.get(node.asText());
			parent.set(i, JSONConverter.convertDataValue(data));
		}
	}

	public static JsonContainer deserialize(String input) throws ConversionException {
		JsonContainer object = new JsonContainer(input);
		return object;
	}

	private static Configuration prepareJsonPathConfig() {
		return Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider(mapper))
				.mappingProvider(new JacksonMappingProvider(mapper))
				.options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS).build();
	}

	public void addRow(String jsonPath, JsonContainer row) {
		dataMap.putAll(row.dataMap);
		ctx.add(jsonPath, row.ctx.json());
	}

	public void addKeyValue(String var, Data data) {
		String id = getRandomKey();
		if (dataMap.containsKey(id)) {
			throw new RuntimeException();
		}
		if (rootType.equals(Type.OBJECT)) {
		    ctx.put(PATH_ROOT_PREFIX, var, id);    
		} else {
		    ObjectNode objectNode = mapper.createObjectNode();
		    objectNode.put(var, id);
            ctx.add(PATH_ROOT_PREFIX, objectNode);
		}
		dataMap.put(id, data);
	}
	
    public void addValue(Data data) {
        String id = getRandomKey();
        if (dataMap.containsKey(id)) {
            throw new RuntimeException();
        }
        if (rootType.equals(Type.ARRAY)) {
            ctx.add(PATH_ROOT_PREFIX, id);
        } else {
            Object values = ctx.read(PATH_ROOT_PREFIX + "." + "values");
            if (NullNode.getInstance().equals(values)) {
                ArrayNode arrayNode = mapper.createArrayNode();
                ctx.put(PATH_ROOT_PREFIX, "values", arrayNode);    
            }
            ctx.add(PATH_ROOT_PREFIX + ".values", id);
        }
        dataMap.put(id, data);
    }
}