package fr.utc.nf28.moka.util;

import java.util.UUID;

import fr.utc.nf28.moka.agent.IAndroidAgent;
import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class JadeUtils {

	public static final String ANDROID_AGENT_NICKNAME = "AndroidAgent_" + UUID.randomUUID().toString();

	public IAndroidAgent getAndroidAgentInterface() {
		try {
			return MicroRuntime.getAgent(ANDROID_AGENT_NICKNAME).getO2AInterface(IAndroidAgent.class);
		} catch (ControllerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
