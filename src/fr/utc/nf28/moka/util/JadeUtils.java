package fr.utc.nf28.moka.util;

import java.util.UUID;

import fr.utc.nf28.moka.agent.IAndroidAgent;
import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class JadeUtils {

	/**
	 * Agent name for AndroidAgent. Unique name based on UUID
	 */
	public static final String ANDROID_AGENT_NICKNAME = "AndroidAgent_" + UUID.randomUUID().toString();

	/**
	 * use to call method of AndroidAgent from Activity
	 *
	 * @return interface reference of AndroidAgent running on device
	 */
	public IAndroidAgent getAndroidAgentInterface() {
		try {
			return MicroRuntime.getAgent(ANDROID_AGENT_NICKNAME).getO2AInterface(IAndroidAgent.class);
		} catch (ControllerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * default type for Jade skill registering
	 */
	public static final String JADE_SKILL_TYPE_DEFAULT = "MokaDefaultSkillType";

	/**
	 * name for Jade skill registering of AndroidAgent
	 */
	public static final String JADE_SKILL_NAME_ANDROID = "AndroidAgentSkillName";
}
