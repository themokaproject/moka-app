package fr.utc.nf28.moka.io.agent;


public interface IJadeServerReceiver {

	/**
	 * call when jade server inform Android device that a new item
	 * has been successfully created
	 *
	 * @param id item server id
	 */
	public void onItemCreationSuccess(int id);

	/**
	 * call when jade server request Android device to refresh
	 * current items list due to a new item creation
	 */
	public void onNewItemCreated();
}
