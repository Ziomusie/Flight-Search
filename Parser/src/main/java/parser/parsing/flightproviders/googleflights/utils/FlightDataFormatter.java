package parser.parsing.flightproviders.googleflights.utils;

import model.Time;
import model.TimeInterval;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.parsing.configuration.ParserException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by tswed on 22.07.2016.
 */
public class FlightDataFormatter {
	private static final Logger log = LoggerFactory.getLogger(FlightDataFormatter.class);
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private FlightDataFormatter() {}
	
	public static String formatDate(Date date){
		return dateFormat.format(date);
	}
	
	public static Date formatString(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			log.error("Error while parsing date " + date, e);
		}
		return null;
	}
	
	public static Date getDatePlusDays(Date baseDate, int plusDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DAY_OF_MONTH, plusDays);
		return calendar.getTime();
	}
	
	/**
	 * @param moneyStr cena lotu w formacie 'XXX zł'
	 */
	public static Money formatFlightPrice(String moneyStr, CurrencyUnit currencyUnit) {
		moneyStr = moneyStr.replace("zł", "");
		moneyStr = moneyStr.replaceAll(" ", "");
		moneyStr = moneyStr.trim();
		return Money.ofMajor(currencyUnit, new Long(moneyStr));
	}
	
	/**
	 * @param timeStr czas trwania lotu w formacie 'XXh XXm'
	 */
	public static Time formatFlightDuration(String timeStr) {
		Matcher matcher = Patterns.durationPattern.matcher(timeStr);
		if (matcher.matches()) {
			String hours = matcher.group(1);
			String minutes = matcher.group(2);
			return new Time(new Integer(hours), new Integer(minutes));
		}
		else {
			throw new ParserException("flight duration (" + timeStr + ") doesn't match pattern!");
		}
	}
	
	/**
	 * @param hoursStr godziny lotu w formacie 'HH:mm - HH:mm' lub 'HH:mm - HH:mm+H'
	 */
	public static TimeInterval formatFlightHours(String hoursStr) {
		Matcher matcher = Patterns.flightHoursPattern.matcher(hoursStr);
		Matcher matcherWithShift = Patterns.flightHoursPatternWithShift.matcher(hoursStr);
		Matcher matcherWithDoubleShift = Patterns.flightHoursPatternWithDoubleShift.matcher(hoursStr);
		if (matcher.matches()) {
			Time startTime = formatStartTime(matcher);
			Time endTime = formatEndTime(matcher);
			return new TimeInterval(startTime, endTime);
		}
		else if (matcherWithShift.matches()) {
			Time startTime = formatStartTime(matcherWithShift);
			Time endTime = formatEndTimeWithShift(matcherWithShift);
			return new TimeInterval(startTime, endTime);
		}
		else if (matcherWithDoubleShift.matches()) {
			Time startTime = formatStartTimeWithShift(matcherWithDoubleShift);
			Time endTime = formatEndTimeWithDoubleShift(matcherWithDoubleShift);
			return new TimeInterval(startTime, endTime);
		}
		else {
			throw new ParserException("flight hours (" + hoursStr + ") doesn't match pattern!");
		}
	}
	
	private static Time formatStartTime(Matcher matcher) {
		String hours = matcher.group(1);
		String minutes = matcher.group(2);
		return new Time(new Integer(hours), new Integer(minutes));
	}
	
	private static Time formatStartTimeWithShift(Matcher matcher) {
		String hours = matcher.group(1);
		String minutes = matcher.group(2);
		String shift = matcher.group(3);
		return new Time(new Integer(hours), new Integer(minutes), 0, new Integer(shift));
	}
	
	private static Time formatEndTime(Matcher matcher) {
		String hours = matcher.group(3);
		String minutes = matcher.group(4);
		return new Time(new Integer(hours), new Integer(minutes));
	}
	
	private static Time formatEndTimeWithShift(Matcher matcher) {
		String hours = matcher.group(3);
		String minutes = matcher.group(4);
		String shift = matcher.group(5);
		return new Time(new Integer(hours), new Integer(minutes), 0, new Integer(shift));
	}
	
	private static Time formatEndTimeWithDoubleShift(Matcher matcher) {
		String hours = matcher.group(4);
		String minutes = matcher.group(5);
		String shift = matcher.group(6);
		return new Time(new Integer(hours), new Integer(minutes), 0, new Integer(shift));
	}

}
