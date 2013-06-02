package fr.utc.nf28.moka.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");

	public static String getFormattedDate(Date d) {
		return sDateFormat.format(d);
	}
}
