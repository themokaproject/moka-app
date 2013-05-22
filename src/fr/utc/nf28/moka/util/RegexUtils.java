package fr.utc.nf28.moka.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	private static final String REGEX_IP_ADDRESS =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static Pattern mPattern;
	private static Matcher mMatcher;

	public static boolean validateIpAddress(String ip){
		mPattern = Pattern.compile(REGEX_IP_ADDRESS);
		mMatcher = mPattern.matcher(ip);
		return mMatcher.matches();
	}
}
