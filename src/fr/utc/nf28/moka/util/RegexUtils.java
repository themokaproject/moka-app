package fr.utc.nf28.moka.util;

import java.util.regex.Pattern;

public class RegexUtils {
	private static final String REGEX_IP_ADDRESS =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static final Pattern sIpPattern = Pattern.compile(REGEX_IP_ADDRESS);

	public static boolean validateIpAddress(String ip) {
		return sIpPattern.matcher(ip).matches();
	}
}
