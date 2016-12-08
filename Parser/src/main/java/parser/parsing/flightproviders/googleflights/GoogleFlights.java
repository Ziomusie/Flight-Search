package parser.parsing.flightproviders.googleflights;

import model.Flight;
import model.FlightClass;
import model.SearchLimits;
import org.joda.money.Money;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.parsing.configuration.ParserException;
import parser.parsing.configuration.SystemSettings;
import parser.parsing.flightproviders.FlightProvider;
import parser.parsing.flightproviders.googleflights.classcodes.ClassCode;
import parser.parsing.flightproviders.googleflights.classcodes.ClassCodesManager;
import parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter;
import parser.parsing.flightproviders.googleflights.utils.Patterns;
import parser.parsing.flightproviders.googleflights.utils.QueryBuilder;
import parser.parsing.selenium.SeleniumController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

public class GoogleFlights implements FlightProvider {
	private static final Logger log = LoggerFactory.getLogger(GoogleFlights.class);
	
	private static final String NO_ITEMS_TEXT = "Brak wyników spełniających podane kryteria.";
	
	private String query;
	
	private SeleniumController seleniumController;
	private QueryBuilder queryBuilder;
	private ClassCodesManager classCodesManager;
	private SystemSettings systemSettings;
	
	public GoogleFlights(SeleniumController seleniumController, QueryBuilder queryBuilder, ClassCodesManager classCodesManager, SystemSettings systemSettings) {
		this.seleniumController = seleniumController;
		this.queryBuilder = queryBuilder;
		this.classCodesManager = classCodesManager;
		this.systemSettings = systemSettings;
	}
	
	@Override
	public List<Flight> searchFlights(Date date, String airportFrom, String airportTo, SearchLimits limits) {
		query = queryBuilder.build(date, airportFrom, airportTo, limits);
		try {
			return startParser(date, airportFrom, airportTo);
		}
		catch (Exception e) {
			throw new ParserException("error while parsing Google Flights page", e);
		}
	}
	
	private List<Flight> startParser(Date date, String airportFrom, String airportTo) throws Exception {
		seleniumController.openSite(query);
		
		if (seleniumController.doesElementExistsByText(NO_ITEMS_TEXT)) {
			log.info("No flights found");
			return new ArrayList<>();
		}
		
		List<WebElement> flightPrices = getByClassCode(ClassCode.MAIN_PRICE);
		List<WebElement> flightOperators = getByClassCode(ClassCode.MAIN_OPERATOR);
		List<WebElement> flightHours = getByClassCode(ClassCode.MAIN_HOURS);
		List<WebElement> flightDuration = getByClassCode(ClassCode.MAIN_DURATION);
		List<WebElement> flightDirect = getByClassCode(ClassCode.MAIN_DIRECT_TEXT);
		WebElement flightClass = getByClassCode(ClassCode.MAIN_FLIGHT_CLASS).get(1); //sa to wartosci z listy rozwijanej, 0 jest liczba osob
		log.info("Found {} flights", flightPrices.size());
		
		boolean needToReload = false;
		List<Flight> flights = new ArrayList<>();
		for (int i = 0; i < flightPrices.size(); i++) {
			Flight tmp = new Flight();
			
			if (needToReload) {
				needToReload = false;
				flightPrices = getByClassCode(ClassCode.MAIN_PRICE);
				flightOperators = getByClassCode(ClassCode.MAIN_OPERATOR);
				flightHours = getByClassCode(ClassCode.MAIN_HOURS);
				flightDuration = getByClassCode(ClassCode.MAIN_DURATION);
				flightDirect = getByClassCode(ClassCode.MAIN_DIRECT_TEXT);
			}
			tmp.setFlightClass(FlightClass.fullNameToCode(flightClass.getText()));
			tmp.setPrice(FlightDataFormatter.formatFlightPrice(flightPrices.get(i).getText(), systemSettings.getCurrencyUnit()));
			tmp.setFlightDuration(FlightDataFormatter.formatFlightDuration(flightDuration.get(i).getText()));
			tmp.setAirportFrom(airportFrom);
			tmp.setAirportTo(airportTo);
			tmp.setFlightHours(FlightDataFormatter.formatFlightHours(flightHours.get(i).getText()));
			tmp.setDate(date);
			tmp.setAirline(flightOperators.get(i).getText());
			
			int changes = getChangesCount(flightDirect.get(i).getText());
			tmp.setChangesCount(changes);
			if (changes > 0) {
				tmp.setChanges(processChanges(flightPrices.get(i), date));
				needToReload = true;
			}
			
			flights.add(tmp);
		}
		
		return flights;
	}
	
	private List<Flight> processChanges(WebElement flightElement, Date date) {
		seleniumController.click(flightElement);
		seleniumController.waitForElement(getClassForCode(ClassCode.CHANGES_SHARE_BUTTON));
		
		List<Flight> changes = new ArrayList<>();
		List<WebElement> changeRows = seleniumController.getByClassName(getClassForCode(ClassCode.CHANGES_CHANGE_ROWS));
		for (int i = 0; i < changeRows.size(); i += 2) {
			WebElement changeRow = changeRows.get(i);
			
			List<WebElement> flightAirports = seleniumController.getByClassName(changeRow, getClassForCode(ClassCode.CHANGES_AIRPORTS));
			List<WebElement> flightHours = seleniumController.getByClassName(changeRow, getClassForCode(ClassCode.CHANGES_HOURS));
			List<WebElement> flightDuration = seleniumController.getByClassName(changeRow, getClassForCode(ClassCode.CHANGES_DURATION));
			List<WebElement> flightOperators = seleniumController.getByClassName(changeRow, getClassForCode(ClassCode.CHANGES_OPERATOR));
			
			Flight tmp = new Flight();
			
			tmp.setPrice(Money.zero(systemSettings.getCurrencyUnit()));
			tmp.setFlightDuration(FlightDataFormatter.formatFlightDuration(seleniumController.getElementText(flightDuration.get(0))));
			tmp.setAirportFrom(getDepartureAirportCode(seleniumController.getElementText(flightAirports.get(0))));
			tmp.setAirportTo(getArrivalAirportCode(seleniumController.getElementText(flightAirports.get(0))));
			tmp.setFlightHours(FlightDataFormatter.formatFlightHours(seleniumController.getElementText(flightHours.get(0))));
			tmp.setDate(date);
			tmp.setAirline(getAirline(seleniumController.getElementText(flightOperators.get(0))));
			tmp.setChangesCount(0);
			
			changes.add(tmp);
		}
		
		List<WebElement> closeButton = getByClassCode(ClassCode.CHANGES_CLOSE_BUTTON);
		seleniumController.click(closeButton.get(0));
		seleniumController.waitForElement(getClassForCode(ClassCode.MAIN_CHOOSE_FLIGHT));
		
		return changes;
	}
	
	private int getChangesCount(String changesText) {
		String textWithoutNumbers = changesText.replaceAll("[^0-9]", "");
		if ("".equals(textWithoutNumbers)) {
			return 0;
		} else {
			return Integer.parseInt(textWithoutNumbers);
		}
	}
	
	private String getDepartureAirportCode(String routeText) {
		Matcher matcher = Patterns.routePattern.matcher(routeText);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return "";
	}
	
	private String getArrivalAirportCode(String routeText) {
		Matcher matcher = Patterns.routePattern.matcher(routeText);
		if (matcher.matches()) {
			return matcher.group(2);
		}
		return "";
	}
	
	private String getAirline(String detailsText) {
		Matcher matcher = Patterns.airlinePattern.matcher(detailsText);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return "";
	}
	
	private List<WebElement> getByClassCode(ClassCode code) {
		return seleniumController.getByClassName(getClassForCode(code));
	}
	
	private String getClassForCode(ClassCode code) {
		return classCodesManager.getClassForElement(code);
	}
	
}
