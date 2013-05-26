package fr.utc.nf28.moka.agent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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

	/**
	 * Application context to send broadcast
	 */
	private Context mContext;

	@Override
	protected void setup() {
		super.setup();

		mContext = (Context) getArguments()[0];

		registerSkill(JadeUtils.JADE_SKILL_NAME_ANDROID);
		registerO2AInterface(IAndroidAgent.class, this);

		addBehaviour(new ReceptionBehaviour());
	}

	@Override
	public void connectPlatform(String firstName, String lastName, String ip) {
		final HashMap<String, String> connection = new HashMap<String, String>();
		connection.put("ip", ip);
		connection.put("lastName", lastName);
		connection.put("firstName", firstName);
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_CONNECTION, connection));
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
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_ADD_ITEM, "umlClass"));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CREATION), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteItem(int itemId) {
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_DELETE_ITEM, itemId));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CREATION), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveItem(int itemId, int direction, int velocity) {
		try {
			final HashMap<String, Integer> movement = new HashMap<String, Integer>();
			movement.put("itemId", itemId);
			movement.put("direction", direction);
			movement.put("velocity", velocity);
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_MOVE_ITEM, movement));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_ITEM_MOVEMENT), json);
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

	/**
	 * use to send message to the activity which register a JadeServerReceiver
	 *
	 * @param content message content
	 */
	public void sendBroadcastMessage(String content) {
		final Intent i = new Intent(JadeServerReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
		i.putExtra(JadeServerReceiver.EXTRA_JADE_SERVER_MESSAGE, content);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
	}
}
