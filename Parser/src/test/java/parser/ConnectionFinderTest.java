package parser;

import model.FlightClass;
import model.JourneyRequest;
import model.JourneyResponse;
import model.SearchLimits;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.utils.DateBuilder;
import parser.utils.Repeat;
import parser.utils.RepeatRule;
import parser.utils.TimeUtil;

import java.util.Calendar;
import java.util.Date;

public class ConnectionFinderTest {
	private static final Logger log = LoggerFactory.getLogger(ConnectionFinderTest.class);
	
	// Warszawa - wszystkie lotniska
	private static String airportFrom = "WAW";
	// Londyn - wszystkie lotniska
	private static String airportTo2 = "LHR,LGW,STN,LCY,LTN,SEN,QQS";
	// Nowy Jork - wszystkie lotniska
	private static String airportTo = "GDN";
	
	@Rule
	public RepeatRule repeatRule = new RepeatRule();
	
	@Test
	//@Repeat(10)
	public void testFlights() {
		Date goingThereDate = DateBuilder.build(30, 12, 2016);
		Date comingBackDate = null;
		
		SearchLimits searchLimits = SearchLimits.newSearchLimits().build();
		searchLimits.setFlightClass(FlightClass.ECONOMIC);// do wyswietlenia FlightClass.codeToFullName(flight.flightClass)
		searchLimits.setMaxChanges(0);
		JourneyRequest request = new JourneyRequest(goingThereDate, comingBackDate, searchLimits, airportFrom, airportTo);
		
		TimeUtil timer = TimeUtil.startMeasure();
		ConnectionFinder.findConnections(request);
		log.info("Flights parsing duration: {} ms", timer.getDuration());
		
		swapAirports();
	}
	
	private void swapAirports() {
		String tmp = airportFrom;
		airportFrom = airportTo;
		airportTo = tmp;
	}
	
	private Date getDatePlusDays(Date baseDate, int plusDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(baseDate);
		calendar.add(Calendar.DAY_OF_MONTH, plusDays);
		return calendar.getTime();
	}
	
}
