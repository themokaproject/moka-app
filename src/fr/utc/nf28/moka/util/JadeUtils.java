package fr.utc.nf28.moka.util;

import java.util.UUID;

import fr.utc.nf28.moka.io.agent.IAndroidAgent;
import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class JadeUtils {
	/**
	 * Agent name for AndroidAgent. Unique name based on UUID
	 */
	public static final String ANDROID_AGENT_NICKNAME = "AndroidAgent_" + UUID.randomUUID().toString();
	/**
	 * default type for Jade skill registering
	 */
	public static final String JADE_SKILL_TYPE_DEFAULT = "MokaDefaultSkillType";
	/**
	 * name for Jade skill registering of AndroidAgent
	 */
	public static final String JADE_SKILL_NAME_ANDROID = "AndroidAgentSkillName";
	/**
	 * name for Jade skill registering of ConnectionAgent
	 */
	public static final String JADE_SKILL_NAME_CONNECTION = "ConnectionAgentSkillName";
	/**
	 * name for Jade item movement skill
	 */
	public static final String JADE_SKILL_NAME_ITEM_MOVEMENT = "ItemMovementSkillName";
	/**
	 * name for Jade skill registering of CreationAgent
	 */
	public static final String JADE_SKILL_NAME_CREATION = "CreationAgentSkillName";
	private static final IAndroidAgent sDummyAndroidAgentInterface = new IAndroidAgent() {
		@Override
		public void connectPlatform(String firstName, String lastName, String ip) {
		}

		@Override
		public void createItem(String type) {
		}

		@Override
		public void deleteItem(int itemId) {
		}

		@Override
		public void moveItem(int itemId, int direction, int velocity) {
		}

		@Override
		public void lockItem() {
		}

		@Override
		public void editItem() {
		}
	};

	/**
	 * use to call method of AndroidAgent from Activity
	 *
	 * @return interface reference of AndroidAgent running on device
	 */
	public static IAndroidAgent getAndroidAgentInterface() {
		try {
			return MicroRuntime.getAgent(ANDROID_AGENT_NICKNAME).getO2AInterface(IAndroidAgent.class);
		} catch (ControllerException e) {
			e.printStackTrace();
			return sDummyAndroidAgentInterface;
		}
	}

	/**
	 * connection transaction
	 */
	public static final String TRANSACTION_TYPE_CONNECTION = "connection";

	/**
	 * addItem transaction
	 */
	public static final String TRANSACTION_TYPE_ADD_ITEM = "addItem";

	/**
	 * deleteItem transaction
	 */
	public static final String TRANSACTION_TYPE_DELETE_ITEM = "deleteItem";

	/**
	 * moveItem transaction
	 */
	public static final String TRANSACTION_TYPE_MOVE_ITEM = "moveItem";

	/**
	 * creation success transaction
	 */
	public static final String TRANSACTION_TYPE_ITEM_CREATION_SUCCESS = "creationSuccess";

	/**
	 * refresh current items transaction
	 */
	public static final String TRANSACTION_TYPE_REFRESH_CURRENT_ITEMS = "refreshCurrentItems";
}
