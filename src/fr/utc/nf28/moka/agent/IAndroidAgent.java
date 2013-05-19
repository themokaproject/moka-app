package fr.utc.nf28.moka.agent;

/**
 * interface use to communicate with the agent
 */
public interface IAndroidAgent {
	//TODO add methods to communicate with other Jade agents
	public void connectPlatform();
	public void createItem();
	public void lockItem();
	public void editItem();
}
