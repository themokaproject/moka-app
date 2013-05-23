package fr.utc.nf28.moka.agent;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

public class AndroidAgent extends BaseAgent implements IAndroidAgent {
	

	@Override
	protected void setup() {
		super.setup();

		registerSkill(JadeUtils.JADE_SKILL_NAME_ANDROID);
		registerO2AInterface(IAndroidAgent.class, this);
	}

	@Override
	public void connectPlatform(String firstName, String lastName, String ip) {
		final HashMap<String,String> connexion = new HashMap<String, String>();
		connexion.put("ip",ip);
		connexion.put("lastName",lastName);
		connexion.put("firstName",firstName);
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction("connection",connexion));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createItem() {
		//TODO construct the content
		sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CREATION),
				"{\"type\":\"creation\",\"request\":\"{\\\"methods\\\":[],\\\"members\\\"" +
						":[],\\\"className\\\":\\\"MyClass\\\",\\\"id\\\":0,\\\"title\\\":\\\"" +
						"MyClass\\\",\\\"y\\\":100,\\\"x\\\":100}\"}");
	}

	@Override
	public void lockItem() {
	}

	@Override
	public void editItem() {
	}
}
