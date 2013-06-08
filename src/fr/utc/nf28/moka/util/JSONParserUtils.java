package fr.utc.nf28.moka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utc.nf28.moka.data.*;
import fr.utc.nf28.moka.io.agent.A2ATransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * a JSON serializer/deserializer that uses Jackson
 */
public final class JSONParserUtils {
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
			} catch (ClassNotFoundException ignored) {
			}
			return new A2ATransaction(typeNode.asText(), sMapper.treeToValue(contentNode, contentClass));
		}

		return null;
	}

	public static MokaItem deserializeItemEntry(final String json) throws IOException {
		return deserializeItemEntry(sMapper.readTree(json));
	}

	public static MokaItem deserializeItemEntry(final JsonNode rootNode) throws IOException {
		MokaItem result = null;

		//retrieve common stuff
		final String type = rootNode.path("type").asText();
		final String title = rootNode.path(MokaType.KEY_TITLE).asText();
		final int id = rootNode.path("id").asInt();
		final String creationDate = rootNode.path("creationDate").asText();

		if (ComputerType.UmlType.KEY_TYPE.equals(type)) {
			result = new ComputerItem.UmlItem(title);
			//retrieve uml specific stuff
		} else if (MediaType.ImageType.KEY_TYPE.equals(type)) {
			result = new MediaItem.ImageItem(title);
			result.update(MediaType.KEY_URL, rootNode.path(MediaType.KEY_URL).asText());
			//retrieve image specific stuff
		} else if (MediaType.VideoType.KEY_TYPE.equals(type)) {
			result = new MediaItem.VideoItem(title);
			result.update(MediaType.KEY_URL, rootNode.path(MediaType.KEY_URL).asText());
		} else if (MediaType.WebType.KEY_TYPE.equals(type)) {
			result = new MediaItem.WebItem(title);
			result.update(MediaType.KEY_URL, rootNode.path(MediaType.KEY_URL).asText());
		} else if (TextType.PostItType.KEY_TYPE.equals(type)) {
			result = new TextItem.PostItItem(title);
			result.update(TextType.KEY_CONTENT, rootNode.path(TextType.KEY_CONTENT).asText());
			//retrieve post-it specific stuff
		}

		if (result != null) {
			result.setId(id);
			result.setCreationDate(creationDate);
		}
		return result;
	}

	public static List<MokaItem> deserializeItemEntries(final String json) throws IOException {
		final JsonNode rootNode = sMapper.readTree(json);
		List<MokaItem> result = Collections.emptyList();
		if (rootNode.isArray()) {
			result = new ArrayList<MokaItem>(rootNode.size());
			for (Iterator<JsonNode> iter = rootNode.elements(); iter.hasNext(); ) {
				final MokaItem item = deserializeItemEntry(iter.next());
				if (item != null) {
					result.add(item);
				}
			}
		}
		return result;
	}

}
