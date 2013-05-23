package fr.utc.nf28.moka.agent;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class AndroidAgent extends BaseAgent implements IAndroidAgent {

	/**
	 * Tag for logcat
	 */
	private static final String TAG = makeLogTag(AndroidAgent.class);

	@Override
	protected void setup() {
		super.setup();

		registerSkill(JadeUtils.JADE_SKILL_NAME_ANDROID);
		registerO2AInterface(IAndroidAgent.class, this);
	}

	@Override
	public void connectPlatform(String firstName, String lastName, String ip) {
		final HashMap<String, String> connexion = new HashMap<String, String>();
		connexion.put("ip", ip);
		connexion.put("lastName", lastName);
		connexion.put("firstName", firstName);
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction("connection", connexion));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION), json);
		} catch (JsonProcessingException e) {
			Log.e(TAG, "connectPlatrform failed : JsonProcessingException");
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
