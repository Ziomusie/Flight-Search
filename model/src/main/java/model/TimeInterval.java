package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeInterval {
	private Time start;
	private Time end;
	
	public TimeInterval(Time start, Time end) {
		validate(start, end);
		this.start = start;
		this.end = end;
	}
	
	public Time getStart() {
		return start;
	}
	
	public Time getEnd() {
		return end;
	}
	
	private void validate(Time start, Time end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("start (" + start + ") or end (" + end + ") time is null!");
		}
		if (start.getShift() != 0 || end.getShift() != 0) {
			return;
		}
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			Date startDate = timeFormat.parse(start.label(":"));
			Date endDate = timeFormat.parse(end.label(":"));
			if (startDate.after(endDate)) {
				throw new IllegalArgumentException("end time (" + end + ") is before start time (" + start + ")!");
			}
		}
		catch (ParseException e) {
			// ignore
		}
	}
	
	@Override
	public String toString() {
		return start.toString() + " - " + end.toString();
	}
}
