package parser;

import model.Flight;
import model.JourneyRequest;
import model.JourneyResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import parser.addingflights.AddFlightsManager;
import parser.parsing.flightproviders.FlightProvider;

import java.util.List;

public class ConnectionFinder {
	
	private ConnectionFinder() {}
	
	private static ApplicationContext applicationContext;
	
	static {
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
		((ClassPathXmlApplicationContext) applicationContext).registerShutdownHook();
	}
	
	public static JourneyResponse findConnections(JourneyRequest request) {
		FlightProvider flightProvider = applicationContext.getBean(FlightProvider.class);
		AddFlightsManager addFlightsManager = applicationContext.getBean(AddFlightsManager.class);
		
		List<Flight> goingThereFlights = flightProvider.searchFlights(request.getGoingThereDate(), request.getAirportFrom(),
		                                                      request.getAirportTo(), request.getSearchLimits());
		addFlightsManager.addFlights(goingThereFlights);
		
		List<Flight> comingBackFlights = null;
		if (request.getComingBackDate() != null) {
			comingBackFlights = flightProvider.searchFlights(request.getComingBackDate(), request.getAirportTo(),
			                                         request.getAirportFrom(), request.getSearchLimits());
			addFlightsManager.addFlights(comingBackFlights);
		}
		
		return new JourneyResponse(goingThereFlights, comingBackFlights);
	}

}
