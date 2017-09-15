package ru.insagent.util;

public class TimeUtils {
	/**
	 * Приватный конструктор, чтобы класс нельзя было инициализировать.
	 */
	private TimeUtils() {
	}

	public static java.sql.Date convertDateToSqlDate(java.util.Date date) {
		if(date == null) {
			return null;
		}

		return new java.sql.Date(date.getTime());
	}

	public static java.util.Date convertSqlDateToDate(java.sql.Date date) {
		if(date == null) {
			return null;
		}

		return new java.util.Date(date.getTime());
	}

	public static long convertDateToTimestamp(java.util.Date date) {
		if(date == null) {
			return 0;
		}

		return date.getTime()/1000;
	}

	public static java.util.Date convertTimestampToDate(long timestamp) {
		if(timestamp == 0) {
			return null;
		}

		return new java.util.Date(1000*timestamp);
	}
}
