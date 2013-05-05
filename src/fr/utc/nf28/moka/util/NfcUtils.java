package fr.utc.nf28.moka.util;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Utils to write and read NFC tag
 */
public class NfcUtils {
	/**
	 * Log TAG
	 */
	private static final String TAG = LogUtils.makeLogTag(NfcUtils.class);
	/**
	 * result code : tag made read only
	 */
	public static final int TAG_READ_ONLY = 0x00000001;
	/**
	 * result code : not enough space while writing
	 */
	public static final int TAG_NOT_ENOUGHT_FREE_SPACE = 0x00000002;
	/**
	 * result code : writing success
	 */
	public static final int TAG_WRITTEN_SUCCESS = 0x00000003;
	/**
	 * result code : writing failed
	 */
	public static final int TAG_WRITTEN_FAIL = 0x00000004;
	/**
	 * result code : failed to format NDEF
	 */
	public static final int TAG_UNABLE_TO_FORMAT_NDEF = 0x00000005;
	/**
	 * result code : tag not support NDEF format
	 */
	public static final int TAG_NOT_SUPPORT_FORMAT_NDEF = 0x00000006;

	/**
	 * Read tag with URI encoded content
	 *
	 * @param tag tag detected
	 * @return uri store int the payload[0]
	 * @throws UnsupportedEncodingException
	 */
	public static String readUriTag(Tag tag) throws UnsupportedEncodingException {
		final Ndef ndef = Ndef.get(tag);
		final NdefMessage message = ndef.getCachedNdefMessage();
		return new String(message.getRecords()[0].getPayload());
	}

	/**
	 * Write tag with URI encoded content
	 *
	 * @param tag          tag detected by your device
	 * @param uri          uri store in your tag
	 * @param makeReadOnly turn your tag in readOnly mode
	 * @return result code
	 * @throws UnsupportedEncodingException
	 */
	public static int writeUriTag(Tag tag, String uri, boolean makeReadOnly) throws UnsupportedEncodingException {
		NdefRecord[] records = {createUriRecord(uri)};
		NdefMessage message = new NdefMessage(records);
		try {
			// check if tag is already NDEF formatted
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					Log.e(TAG, "write tag failed : readOnly mode");
					ndef.close();
					return TAG_READ_ONLY;
				}

				//calculate our data size
				int size = message.toByteArray().length;

				//check if there is enough place
				if (ndef.getMaxSize() < size) {
					Log.e(TAG, "write tag failed : not enough free space");
					ndef.close();
					return TAG_NOT_ENOUGHT_FREE_SPACE;
				}

				//not a readOnly TAG && enough free place then write
				ndef.writeNdefMessage(message);

				//check is we want to turn this tag in readOnly Tag
				if (makeReadOnly) {
					ndef.makeReadOnly();
				}
				Log.i(TAG, "write tag succeeded");
				ndef.close();
				return TAG_WRITTEN_SUCCESS;


				//tag isn't NDEF formatted
			} else {
				//try to format it
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						Log.i(TAG, "tag ndef formatted and write tag succeeded");
						return TAG_WRITTEN_SUCCESS;
					} catch (IOException e) {
						Log.e(TAG, "unable to format tag to NDEF format");
						return TAG_UNABLE_TO_FORMAT_NDEF;
					}
				} else {
					Log.e(TAG, "tag does'nt support NDEF format");
					return TAG_NOT_SUPPORT_FORMAT_NDEF;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "write tag failed");
		}

		return TAG_WRITTEN_FAIL;
	}

	/**
	 * Create record with matching option to uri standard
	 *
	 * @param uri your uri
	 * @return formatted NdefRecord
	 * @throws UnsupportedEncodingException
	 */
	private static NdefRecord createUriRecord(String uri) throws UnsupportedEncodingException {
		byte[] uriBytes = uri.getBytes(Charset.forName("UTF-8"));
		int uriLength = uriBytes.length;
		byte[] payload = new byte[1 + uriLength];

		// copy uribytes into payload
		System.arraycopy(uriBytes, 0, payload, 1, uriLength);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_URI,
				new byte[0],
				payload);
	}
}
