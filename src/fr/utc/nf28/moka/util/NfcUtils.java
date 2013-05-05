package fr.utc.nf28.moka.util;

import android.nfc.Tag;

import java.io.UnsupportedEncodingException;

/**
 * Utils to write and read NFC tag
 */
public class NfcUtils {

	/**
	 * Read tag with URI encoded content
	 *
	 * @param tag tag detected
	 * @return uri store int the payload[0]
	 * @throws UnsupportedEncodingException
	 */
	public static String readUriTag(Tag tag) throws UnsupportedEncodingException {
		return new String();
	}

	/**
	 * Write tag with URI encoded content
	 * @param tag tag detected by your device
	 * @param uri uri store in your tag
	 * @return result code
	 * @throws UnsupportedEncodingException
	 */
	public static int writeUriTag(Tag tag, String uri) throws UnsupportedEncodingException {
		return 0;
	}

}
