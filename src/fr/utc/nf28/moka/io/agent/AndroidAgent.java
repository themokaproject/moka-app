package fr.utc.nf28.moka.io.agent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.HashMap;

import fr.utc.nf28.moka.util.JSONParserUtils;
import fr.utc.nf28.moka.util.JadeUtils;

import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

public class AndroidAgent extends BaseAgent implements IAndroidAgent {
	/**
	 * Tag for logcat
	 */
	private static final String TAG = makeLogTag(AndroidAgent.class);
	private final HashMap<String, Object> mRequest = new HashMap<String, Object>();
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
		mRequest.clear();
		mRequest.put("ip", ip);
		mRequest.put("lastName", lastName);
		mRequest.put("firstName", firstName);
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_CONNECTION, mRequest));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION), json);
		} catch (JsonProcessingException e) {
			Log.e(TAG, "connectPlatform failed : JsonProcessingException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createItem(String type) {
		try {
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_ADD_ITEM, type));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CREATION), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveItem(int itemId, int direction, int velocity) {
		mRequest.clear();
		try {
			mRequest.put("itemId", itemId);
			mRequest.put("direction", direction);
			mRequest.put("velocity", velocity);
			final String json = JSONParserUtils.serializeA2ATransaction(new A2ATransaction(JadeUtils.TRANSACTION_TYPE_MOVE_ITEM, mRequest));
			sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_ITEM_MOVEMENT), json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	 * @param request request from SMA
	 */
	public void sendBroadcastMessage(final String request) {
		final Intent i = new Intent(JadeServerReceiver.INTENT_FILTER_JADE_SERVER_RECEIVER);
		i.putExtra(JadeServerReceiver.EXTRA_JADE_REQUEST, request);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
	}
}
