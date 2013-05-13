package fr.utc.nf28.moka.util;

/**
 * all utils for managing jade runtime and agents life cycle
 */
public class JadeUtils {

	/**
	 * Log TAG
	 */
	private static final String TAG = LogUtils.makeLogTag(JadeUtils.class);

	/**
	 * Default port
	 */
	private static final int DEFAULT_PORT = 1099;

	/**
	 * create jade container and connect to the main container
	 * @param ip ip address of computer which host the main container
	 * @return true onSuccess, false onError
	 */
	public boolean createContainer(String ip) {
		return true;
	}

	/**
	 * create jade container and connect to the main container
	 * @param ip ip address of computer which host the main container
	 * @param port port for network communication
	 * @return true onSuccess, false onError
	 */
	public boolean createContainer(String ip, int port) {
		return true;
	}
}
