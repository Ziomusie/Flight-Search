package parser.addingflights;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import connector.ParametersBuilder;
import connector.ServerConnector;
import model.Flight;
import model.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter;

import java.util.Date;
import java.util.List;

@Service
public class AddFlightsManager {
	private static final Logger log = LoggerFactory.getLogger(AddFlightsManager.class);
	
	public void addFlights(List<Flight> flights) {
		if (flights == null || flights.size() == 0) {
			return;
		}
		log.info("Adding {} flights to database", flights.size());
		
		JsonArray parameters = prepareParams(flights);
		ServerConnector.executePOST(connector.Service.ADD_FLIGHT, parameters);
	}
	
	private JsonArray prepareParams(List<Flight> flights) {
		JsonArray array = new JsonArray();
		
		for (Flight flight : flights) {
			String departureAirport = flight.getAirportFrom();
			String arrivalAirport = flight.getAirportTo();
			String airline = flight.getAirline();
			String departureTime = timestampFromTime(flight.getFlightHours().getStart(), flight.getDate());
			String arrivalTime = timestampFromTime(flight.getFlightHours().getEnd(), flight.getDate());
			String flightTime = flight.getFlightDuration().label(Time.DEFAULT_SEPARATOR);
			String price = "" + flight.getPrice().getAmount();
			String flightClass = flight.getFlightClass();
			
			JsonObject flightJsonObject = new ParametersBuilder()
				.setParameter("departCode", departureAirport)
				.setParameter("destCode", arrivalAirport)
				.setParameter("airlineName", airline)
				.setParameter("departTime", departureTime)
				.setParameter("arTime", arrivalTime)
				.setParameter("flightTime", flightTime)
				.setParameter("price", price)
				.setParameter("flightClass",flightClass)
				.build();
			
			array.add(flightJsonObject);
		}
		
		return array;
	}
	
	/**
	 * @param baseDate klasa 'Time' zawiera jedynie informacje o czasie, musimy więc mieć bazową datę, do której ten czas dokleimy
	 */
	private String timestampFromTime(Time time, Date baseDate) {
		return FlightDataFormatter.formatDate(baseDate) + " " + time.shortLabel(Time.DEFAULT_SEPARATOR);
	}
	
}
