package fr.utc.nf28.moka.io.agent;


public interface IJadeServerReceiver {

	/**
	 * call when jade server inform Android device that a new item
	 * has been created
	 */
	public void onNewItem(String content);
}
