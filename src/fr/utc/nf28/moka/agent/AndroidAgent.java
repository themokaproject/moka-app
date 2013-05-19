package fr.utc.nf28.moka.agent;

import fr.utc.nf28.moka.util.JadeUtils;
import jade.core.Agent;

public class AndroidAgent extends BaseAgent implements IAndroidAgent {

	@Override
	protected void setup() {
		super.setup();
		registerSkill(JadeUtils.JADE_SKILL_NAME_ANDROID);
	}

	@Override
	public void connectPlatform() {

	}

	@Override
	public void createItem() {

	}

	@Override
	public void lockItem() {

	}

	@Override
	public void editItem() {

	}
}
