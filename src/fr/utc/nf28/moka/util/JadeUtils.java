package fr.utc.nf28.moka.util;

import java.util.UUID;

import fr.utc.nf28.moka.agent.IAndroiAgent;
import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class JadeUtils {

	private static final String ANDROID_AGENT_NICKNAME = "AndroidAgent_"+ UUID.randomUUID().toString();

	public IAndroiAgent getAndroidAgentInterface() {
		try {
			return MicroRuntime.getAgent(ANDROID_AGENT_NICKNAME).getO2AInterface(IAndroiAgent.class);
		} catch (ControllerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
