package searchApp.helpers;

import parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Mateusz-PC on 26.07.2016.
 */
public class DateUtill {
	public static Date inputDateToDate(String date){
		return FlightDataFormatter.formatString(date, "dd.MM.yyyy");
	}

	public static String getDateFromDateTime(String date) {
		LocalDateTime dateTime = getLocalDateTime(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String formattedDate = dateTime.format(formatter);
		return formattedDate;
	}

	public static LocalDate getLocalDate(String date) {
		LocalDateTime dateTime = getLocalDateTime(date);
		return dateTime.toLocalDate();
	}

	public static String getTimeFromDateTime(String date) {
		LocalDateTime dateTime = getLocalDateTime(date);
		String formattedTime = getTimeFromDateTime(dateTime);
		return formattedTime;
	}

	public static String getTimeFromDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return dateTime.format(formatter);
	}

	public static LocalDateTime getLocalDateTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(date, formatter);
	}

	public static LocalTime getTimeStringFromTime(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return LocalTime.parse(time, formatter);
	}
	public static LocalDateTime addTimeToDate(LocalDateTime date, LocalTime time) {
		date = date.plusHours(time.getHour());
		date = date.plusMinutes(time.getMinute());
		return date;
	}


}
