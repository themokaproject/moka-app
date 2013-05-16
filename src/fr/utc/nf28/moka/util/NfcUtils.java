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
	 * mime type for moka tag
	 */
	public static final String MIME_TYPE_MOKA = "moka";
	/**
	 * Log TAG
	 */
	private static final String TAG = LogUtils.makeLogTag(NfcUtils.class);

	/**
	 * Read tag with URI encoded content
	 *
	 * @param tag tag detected
	 * @return uri store int the payload[0]
	 * @throws UnsupportedEncodingException
	 */
	public static String readTag(Tag tag) {
		final Ndef ndef = Ndef.get(tag);
		if (ndef != null) {
			final NdefMessage message = ndef.getCachedNdefMessage();
			if (message != null) {
				return new String(message.getRecords()[0].getPayload());
			}
			return null;
		}
		return null;
	}

	/**
	 * Write tag with data for moka
	 *
	 * @param tag          tag detected by your device
	 * @param data         data store in your tag
	 * @param makeReadOnly turn your tag in readOnly mode
	 * @return result code
	 */
	public static int writeMokaTag(Tag tag, String data, boolean makeReadOnly) {
		final NdefRecord[] records = {createMokaRecord(data)};
		final NdefMessage message = new NdefMessage(records);
		try {
			// check if tag is already NDEF formatted
			final Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					Log.e(TAG, "write tag failed : readOnly mode");
					ndef.close();
					return TAG_READ_ONLY;
				}

				//calculate our data size
				final int size = message.toByteArray().length;

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
				final NdefFormatable format = NdefFormatable.get(tag);
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
	 * Create record with matching option to moka standard
	 *
	 * @param data your data
	 * @return formatted NdefRecord
	 * @throws UnsupportedEncodingException
	 */
	private static NdefRecord createMokaRecord(String data) {
		final byte[] uriBytes = data.getBytes(Charset.forName("UTF-8"));
		final int textLength = uriBytes.length;
		final byte[] payload = new byte[1 + textLength];

		System.arraycopy(uriBytes, 0, payload, 1, textLength);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_URI,
				new byte[0],
				payload);
	}
}
