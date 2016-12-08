package parser.parsing.flightproviders;

import model.Flight;
import model.SearchLimits;

import java.util.Date;
import java.util.List;

public interface FlightProvider {
	
	List<Flight> searchFlights(Date date, String airportFrom, String airportTo, SearchLimits limits);
	
}
