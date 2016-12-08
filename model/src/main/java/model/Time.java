package model;

public class Time {
	public static final String DEFAULT_SEPARATOR = ":";
	
	private final int hours;
	private final int minutes;
	private final int seconds;
	private final int shift;
	
	public Time(int hours, int minutes) {
		this(hours, minutes, 0);
	}
	
	public Time(int hours, int minutes, int seconds) {
		this(hours, minutes, seconds, 0);
	}
	
	public Time(int hours, int minutes, int seconds, int shift) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.shift = shift;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public int getShift() {
		return shift;
	}
	
	public String label(String separator) {
		
		return withLeadindZero(hours) + separator + withLeadindZero(minutes) + separator + withLeadindZero(seconds) + getShiftStr();
	}
	
	public String shortLabel(String separator) {
		return withLeadindZero(hours) + separator + withLeadindZero(minutes) + getShiftStr();
	}
	
	/**
	 * Oblicza łączną liczbę minut: 60*hours + minutes
	 */
	public int getTotalMinutes() {
		return 60*hours + minutes;
	}
	
	/**
	 * Oblicza łączną liczbę sekund: 3600*hours + 60*minutes + seconds
	 */
	public int getTotalSeconds() {
		return 60*getTotalMinutes() + seconds;
	}
	
	private String getShiftStr() {
		if (shift != 0) {
			return "+" + shift;
		}
		return "";
	}
	
	public static String withLeadindZero(int value) {
		String str = "" + value;
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}
	
	@Override
	public String toString() {
		return shortLabel(DEFAULT_SEPARATOR);
	}
}
