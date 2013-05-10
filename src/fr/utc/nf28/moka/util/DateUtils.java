package fr.utc.nf28.moka.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter sDateFormatter = DateTimeFormat.forPattern("dd/MM/yy");

	public static String getFormattedDate(DateTime d) {
		return d.toString(sDateFormatter);
	}
}
