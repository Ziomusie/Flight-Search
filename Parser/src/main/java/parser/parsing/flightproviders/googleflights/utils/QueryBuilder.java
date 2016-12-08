package parser.parsing.flightproviders.googleflights.utils;

import model.SearchLimits;
import model.Time;
import model.TimeInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.parsing.flightproviders.googleflights.GoogleFlightsProperties;

import java.util.Date;

@Service
public class QueryBuilder {
	
	@Autowired
	private GoogleFlightsProperties googleFlightsProperties;
	
	public String build(Date date, String from, String to, SearchLimits limits) {
		String query = googleFlightsProperties.getQueryBase();
		String dateStr = FlightDataFormatter.formatDate(date);
		
		query = appendParameter(query, "v", "f");
		query = appendParameter(query, "tt", "o");
		query = appendParameter(query, "d", dateStr);
		query = appendParameter(query, "f", from);
		query = appendParameter(query, "t", to);
		
		if (limits.getAirline() != null) {
			query = appendParameter(query, "a", limits.getAirline());
		}
		if (limits.getFlightClass() != null) {
			query = appendParameter(query, "sc", limits.getFlightClass());
		}
		if (limits.getMaxChanges() != null) {
			query = appendParameter(query, "s", "" + limits.getMaxChanges());
		}
		if (limits.getMaxDuration() != null) {
			query = appendParameter(query, "md", "" + limits.getMaxDuration().getTotalMinutes());
		}
		if (limits.getPrice() != null) {
			query = appendParameter(query, "mp", "" + limits.getPrice().getAmountMajorInt());
		}
		if (limits.getFlightHours() != null) {
			query = processFlightHours(query, limits.getFlightHours());
		}
		
		return query;
	}
	
	private String appendParameter(String query, String key, String value) {
		return appendParameter(query, key, value, "");
	}
	private String appendParameter(String query, String key, String value, String valuePrefix) {
		return query + ";" + key + "=" + valuePrefix + value;
	}
	
	private String processFlightHours(String query, TimeInterval flightHours) {
		Time minTime = flightHours.getStart();
		Time maxTime = flightHours.getEnd();
		
		if (minTime == null || maxTime == null) {
			return query;
		}
		
		String value = minTime.shortLabel("");
		value += "-";
		value += maxTime.shortLabel("");
		return appendParameter(query, "ti", value, "t");
	}
	
}
