package fr.utc.nf28.moka.util;

import android.util.Log;
import fr.utc.nf28.moka.BuildConfig;

public final class LogUtils {
	private static final String LOG_PREFIX = "Moka";
	private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
	private static final int MAX_LOG_TAG_LENGTH = 23;

	public static String makeLogTag(String str) {
		if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
			return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
		}

		return LOG_PREFIX + str;
	}

	public static String makeLogTag(Class cls) {
		return makeLogTag(cls.getSimpleName());
	}

	public static void LOGD(String tag, String message) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, message);
		}
	}

	public static void LOGD(String tag, String message, Throwable cause) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, message, cause);
		}
	}

	public static void LOGV(String tag, String message) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, message);
		}
	}

	public static void LOGV(String tag, String message, Throwable cause) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, message, cause);
		}
	}

	public static void LOGI(String tag, String message) {
		Log.i(tag, message);
	}

	public static void LOGI(String tag, String message, Throwable cause) {
		Log.i(tag, message, cause);
	}

	public static void LOGW(String tag, String message) {
		Log.w(tag, message);
	}

	public static void LOGW(String tag, String message, Throwable cause) {
		Log.w(tag, message, cause);
	}

	public static void LOGE(String tag, String message) {
		Log.e(tag, message);
	}

	public static void LOGE(String tag, String message, Throwable cause) {
		Log.e(tag, message, cause);
	}
}
