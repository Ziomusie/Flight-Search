package parser.parsing.flightproviders.googleflights.utils;

import java.util.regex.Pattern;

public class Patterns {
	
	private Patterns() {}
	
	/**
	 * Trasa lotu, np. 'GDN-LTN'
	 */
	public static final Pattern routePattern = Pattern.compile(".*?\\(([A-Z]{3})\\).*?\\(([A-Z]{3})\\)");
	
	/**
	 * Nazwa linii lotniczej (po wejściu na szczegóły lotu)
	 */
	public static final Pattern airlinePattern = Pattern.compile("(.*?) \\d+.*?");
	
	/**
	 * Czas lotu (na potrzeby ClassCodesManager)
	 */
	public static final Pattern hoursSimplePattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
	
	/**
	 * Czas trwania lotu
	 */
	public static final Pattern durationPattern = Pattern.compile("([0123456]?[0-9]|7[0-2])h ([0-5][0-9])min");
	
	/**
	 * Godziny odlotu i przylotu
	 * Format: HH:mm - HH:mm
	 */
	public static final Pattern flightHoursPattern = Pattern.compile("([01]?[0-9]|2[0-3]):([0-5][0-9])\\s\\W\\s([01]?[0-9]|2[0-3]):([0-5][0-9])");
	
	/**
	 * Godziny odlotu i przylotu z przesunięciem
	 * Format: HH:mm - HH:mm+H
	 */
	public static final Pattern flightHoursPatternWithShift = Pattern.compile("([01]?[0-9]|2[0-3]):([0-5][0-9])\\s\\W\\s([01]?[0-9]|2[0-3]):([0-5][0-9])\\+(\\d+)");
	
	/**
	 * Godziny odlotu i przylotu z przesunięciem obydwu godzin
	 * Format: HH:mm+H - HH:mm+H
	 */
	public static final Pattern flightHoursPatternWithDoubleShift = Pattern.compile("([01]?[0-9]|2[0-3]):([0-5][0-9])\\+(\\d+)\\s\\W\\s([01]?[0-9]|2[0-3]):([0-5][0-9])\\+(\\d+)");
	
}
