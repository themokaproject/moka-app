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
		final HashMap<String, String> connection = new HashMap<String, String>();
		connection.put("ip", ip);
		connection.put("lastName", lastName);
		connection.put("firstName", firstName);
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction("connection", connection));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION), json);
		} catch (JsonProcessingException e) {
			Log.e(TAG, "connectPlatform failed : JsonProcessingException");
			e.printStackTrace();
		}
	}

	@Override
	public void createItem() {
		//TODO construct the content
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction("creation", "umlClass"));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CREATION), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void lockItem() {
	}

	@Override
	public void editItem() {
	}
}
