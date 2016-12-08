package searchApp.helpers;

import model.JourneyRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz-PC on 03.09.2016.
 */
public class StringHelpers {
	public static final String NO_CHANGES  = "bez przesiadek";
	public static final String ONE_CHANGE  = "maks. 1 przesiadka";
	public static final String TWO_CHANGES  = "maks. 2 przesiadki";
	public static final String ANY_CHANGE  = "bez znaczenia";

	public static int getNumberOfChanges(String changesString) {
		switch (changesString) {
			case NO_CHANGES:
				return 0;
			case ONE_CHANGE:
				return 1;
			case TWO_CHANGES:
			case ANY_CHANGE:
				return 2;
		}
		return 0;
	}

	public static String getJourneyRequestParams(JourneyRequest request) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("depCode", request.getAirportFrom()));
		params.add(new BasicNameValuePair("destCode", request.getAirportTo()));
		LocalDate startDate = request.getGoingThereDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		params.add(new BasicNameValuePair("startTime", startDate.toString()));
		params.add(new BasicNameValuePair("endTime", startDate.plusDays(10).toString()));
		if (request.getComingBackDate() != null) {
			LocalDate endDate = request.getComingBackDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			params.add(new BasicNameValuePair("endTime", endDate.toString()));
		}
		if (request.getSearchLimits().getMaxChanges() != null) {
			String changeLimit = "" + request.getSearchLimits().getMaxChanges();
			params.add(new BasicNameValuePair("maxChanges", changeLimit));
		}
		if (request.getSearchLimits().getPrice() != null) {
			String price = "" + request.getSearchLimits().getPrice().getAmountMajorInt();
			params.add(new BasicNameValuePair("maxPrice", price));
		}
		if (request.getSearchLimits().getMaxDuration() != null) {
			String duration = "" + request.getSearchLimits().getMaxDuration().getTotalMinutes();
			params.add(new BasicNameValuePair("maxTime", duration));
		}
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		return "?" + paramString;
	}
}
