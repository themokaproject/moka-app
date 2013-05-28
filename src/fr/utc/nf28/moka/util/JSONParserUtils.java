package fr.utc.nf28.moka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import fr.utc.nf28.moka.io.agent.A2ATransaction;

/**
 * a JSON serializer/deserializer that uses Jackson
 */
public class JSONParserUtils {
	private static final ObjectMapper sMapper = new ObjectMapper();

	public static String serializeA2ATransaction(final A2ATransaction transaction) throws IOException {
		return sMapper.writeValueAsString(transaction);
	}

	public static A2ATransaction deserializeA2ATransaction(final String json) throws IOException {
		final JsonNode rootNode = sMapper.readTree(json);
		final JsonNode typeNode = rootNode.get("type");
		final JsonNode contentNode = rootNode.get("content");
		final JsonNode contentClassNode = rootNode.get("contentClass");

		if (typeNode != null && contentNode != null && contentClassNode != null) {
			Class contentClass = Object.class;
			try {
				contentClass = Class.forName(contentClassNode.asText());
			} catch (ClassNotFoundException e) {
				System.out.println("deserializeA2ATransaction:ClassNotFound : " + contentClassNode.asText());
				System.out.println("use " + contentClass.toString() + " instead");
			}
			Object content = sMapper.treeToValue(contentNode, contentClass);
			return new A2ATransaction(typeNode.asText(), content);
		}

		return null;
	}

}
