package parser.utils;

import java.util.Calendar;
import java.util.Date;

public class DateBuilder {
	
	private DateBuilder() {}
	
	public static Date build(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}
	
}
