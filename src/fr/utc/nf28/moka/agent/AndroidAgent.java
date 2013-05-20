package fr.utc.nf28.moka.agent;

import fr.utc.nf28.moka.util.JadeUtils;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AndroidAgent extends BaseAgent implements IAndroidAgent {

	@Override
	protected void setup() {
		super.setup();
		registerSkill(JadeUtils.JADE_SKILL_NAME_ANDROID);
		registerO2AInterface(IAndroidAgent.class, this);
	}

	@Override
	public void connectPlatform() {
		//TODO construct the content
		sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION),
				"{\"type\":\"connection\",\"request\":\"{\\\"color\\\":-1,\\\"ip\\\"" +
						":\\\"127.0.0.1\\\",\\\"currentItem\\\":null,\\\"lastName\\\":" +
						"\\\"Masciulli\\\",\\\"firstName\\\":\\\"Alexandre\\\"}\"}");
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
