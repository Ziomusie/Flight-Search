package searchApp.tasks;

import connector.ServerConnector;
import connector.Service;
import javafx.concurrent.Task;
import model.Flight;
import model.JourneyRequest;
import model.SearchLimits;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import parser.ConnectionFinder;
import searchApp.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz-PC on 05.11.2016.
 */
public class UpdateFlightsTask extends Task<Boolean> {

	private final Integer UPDATE_LIMIT =  4;
	private final Integer UPDATE_AIRPORT_LIMIT =  1;

	@Override
	protected Boolean call() throws Exception {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("limit", UPDATE_LIMIT.toString()));
		params.add(new BasicNameValuePair("airportLimit", UPDATE_AIRPORT_LIMIT.toString()));
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		SearchLimits searchLimits = new SearchLimits();
		searchLimits.setMaxChanges(0);
		List<Flight> flightList = ServerConnector.executeGET(Service.GET_LIST_OF_OLDEST_FLIGHTS, "?" + paramString, Flight.getType());
		for (Flight flight : flightList) {
			JourneyRequest request = new JourneyRequest(flight.getDate(),
														null,
														searchLimits,
														flight.getAirportFrom(),
														flight.getAirportTo());
			try{
				ConnectionFinder.findConnections(request);
			}
			catch (Exception ex){
				System.err.println(ex);
			}
		}
		return true;
	}

	@Override
	protected void failed() {
		System.out.println("BLAD");
	}

	@Override
	protected void succeeded() {
		boolean ret = false;
		try {
			ret = this.get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		System.out.println("OK");
	}
}
