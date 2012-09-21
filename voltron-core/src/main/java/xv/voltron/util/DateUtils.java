package xv.voltron.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import xv.voltron.core.Config;

public final class DateUtils {

	protected static SimpleDateFormat DEFAULT_DATE_FORMAT;
	protected static SimpleDateFormat DEFAULT_TIME_FORMAT;
	protected static SimpleDateFormat DEFAULT_TIMESTAMP_FORMAT;
	
	{
		String tmp = Config.get("app.format.date");
		DEFAULT_DATE_FORMAT = 
				new SimpleDateFormat(tmp == null ? "yyyy-MM-dd" : tmp);
		
		tmp = Config.get("app.format.time");
		DEFAULT_TIME_FORMAT = 
				new SimpleDateFormat(tmp == null ? "HH:mm:ss" : tmp);
		
		tmp = Config.get("app.format.timestamp");
		DEFAULT_TIMESTAMP_FORMAT =
				new SimpleDateFormat(tmp == null ? "yyyy-MM-dd HH:mm:ss" : tmp);
	}

	public static Date parseDate(String val) throws ParseException {
		return DEFAULT_DATE_FORMAT.parse(val);
	}
	
	public static Time parseTime(String val) throws ParseException {
		return new Time(DEFAULT_TIME_FORMAT.parse(val).getTime());
	}
	
	public static Timestamp parseTimestamp(String val) throws ParseException {
		return new Timestamp(DEFAULT_TIMESTAMP_FORMAT.parse(val).getTime());
	}
	
	public static java.util.Date toDate(java.sql.Date date) {
		return (java.util.Date)date;
	}
	
	public static java.sql.Date toSQLDate(java.util.Date date) {
		long t = date.getTime();
		t = t - (t % 86400000);
		return new java.sql.Date(t);
	}
}
