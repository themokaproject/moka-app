package fr.utc.nf28.moka.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.utc.nf28.moka.agent.A2ATransaction;

/**
 * a JSON serializer/deserializer that uses Jackson
 */
public class JSONParserUtils {
	private static final ObjectMapper sMapper = new ObjectMapper();

	public static String serializeA2ATransaction(A2ATransaction transaction) throws JsonProcessingException {
		return sMapper.writeValueAsString(transaction);
	}

}
