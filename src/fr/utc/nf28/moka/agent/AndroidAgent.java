package fr.utc.nf28.moka.agent;

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
        String json = "{\"type\":\"connection\",\"request\":\"{\\\"color\\\":-1,\\\"ip\\\"" +
                ":\\\"" + ip + "\\\",\\\"currentItem\\\":null,\\\"lastName\\\":\\\"" + lastName + "\\\",\\\"firstName\\\":\\\"" + firstName + "\\\"}\"}";

		sendRequestMessage(getAgentsWithSkill(JadeUtils.JADE_SKILL_NAME_CONNECTION), json);
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
