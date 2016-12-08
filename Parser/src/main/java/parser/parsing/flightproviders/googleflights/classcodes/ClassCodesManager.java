package parser.parsing.flightproviders.googleflights.classcodes;

import com.google.gson.JsonObject;
import connector.ParametersBuilder;
import connector.ServerConnector;
import model.GoogleClass;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.parsing.flightproviders.googleflights.GoogleFlightsProperties;
import parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter;
import parser.parsing.flightproviders.googleflights.utils.Patterns;
import parser.parsing.selenium.SeleniumController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter.getDatePlusDays;

/**
 * Klasa do zarządzania nazwami klas elementów na stronie Google Flights.
 * Mechanizm potrzebny, bo co jakiś czas na stronie Google klasy się zmieniają.
 */
@Service
public class ClassCodesManager {
	private static final Logger log = LoggerFactory.getLogger(ClassCodesManager.class);
	
	@Autowired
	private SeleniumController seleniumController;
	@Autowired
	private GoogleFlightsProperties googleFlightsProperties;
	
	private Map<ClassCode, String> classCodes = new HashMap<>();
	private boolean initialized = false;
	private String currentUrl;
	
	public String getClassForElement(ClassCode code) {
		if (!initialized) {
			initializeClassCodes();
		}
		return classCodes.get(code);
	}
	
	private void initializeClassCodes() {
		if (!initialized) {
			log.info("Initializing class codes");
			List<GoogleClass> classes = ServerConnector.executeGET(connector.Service.GET_LIST_OF_CLASSES, "", GoogleClass.getType());
			
			// jeśli lista klas z serwera nie jest tej samej długości co enum w Javie, resetujemy wartości w bazie
			if (classes.size() != ClassCode.values().length) {
				log.info("Number of class codes from DB isn't equal to number of class codes in Java enum, deleting all from DB");
				deleteAllClasses();
				classes = null;
			}
			
			if (CollectionUtils.isEmpty(classes)) {
				log.info("Updating site class codes");
				performClassFetchAction();
			}
			else {
				fillMapFromDatabase(classes);
				if (needUpdating()) {
					log.info("Class codes need to be updated");
					deleteAllClasses();
					performClassFetchAction();
				}
			}
			
			initialized = true;
		}
	}
	
	private boolean needUpdating() {
		String rootClass = classCodes.get(ClassCode.MAIN_ROOT);
		List<WebElement> elements = seleniumController.getByClassName(rootClass);
		return CollectionUtils.isEmpty(elements);
	}
	
	private void deleteAllClasses() {
		ServerConnector.executeDELETE(connector.Service.DELETE_ALL_CLASSES, new ParametersBuilder().build());
	}
	
	private void performClassFetchAction() {
		goToClassCodesSite();
		fetchClassesFromSite();
		addFetchedClassesToDatabase();
		goBackToPreviousPage();
	}
	
	private void fetchClassesFromSite() {
		WebElement root = seleniumController.getById("root").get(0);
		classCodes.put(ClassCode.MAIN_ROOT, getElementClass(root));
		
		WebElement chooseFlightText = seleniumController.getByText("Wybierz lot").get(0);
		classCodes.put(ClassCode.MAIN_CHOOSE_FLIGHT, getElementClass(chooseFlightText));
		
		WebElement directTextMain = seleniumController.getByText("Lot bezpośredni").get(0);
		classCodes.put(ClassCode.MAIN_DIRECT_TEXT, getElementClass(directTextMain));
		
		WebElement main = seleniumController.getParent(directTextMain).get(0);
		main = seleniumController.getParent(main).get(0);
		WebElement priceMain = seleniumController.getByText("zł", main).get(0);
		classCodes.put(ClassCode.MAIN_PRICE, getElementClass(priceMain));
		
		WebElement operatorMain = seleniumController.getByText("Wizz", main).get(0);
		operatorMain = seleniumController.getParent(operatorMain).get(0);
		classCodes.put(ClassCode.MAIN_OPERATOR, getElementClass(operatorMain));
		
		WebElement hoursMain = seleniumController.getByPattern(Patterns.hoursSimplePattern, main, ":").get(0);
		hoursMain = seleniumController.getParent(hoursMain).get(0);
		classCodes.put(ClassCode.MAIN_HOURS, getElementClass(hoursMain));
		
		WebElement durationMain = seleniumController.getByPattern(Patterns.durationPattern, main, "min").get(0);
		classCodes.put(ClassCode.MAIN_DURATION, getElementClass(durationMain));
		
		seleniumController.click(priceMain);
		seleniumController.waitForElementName("Udostępnij");
		
		WebElement shareButtonChanges = seleniumController.getByText("Udostępnij").get(0);
		classCodes.put(ClassCode.CHANGES_SHARE_BUTTON, getElementClass(shareButtonChanges));
		
		WebElement airportsChanges = seleniumController.getByText("Warszawa (WAW) – Londyn (LTN)").get(0);
		classCodes.put(ClassCode.CHANGES_AIRPORTS, getElementClass(airportsChanges));
		airportsChanges = seleniumController.getParent(airportsChanges).get(0);
		airportsChanges = seleniumController.getParent(airportsChanges).get(0);
		classCodes.put(ClassCode.CHANGES_CHANGE_ROWS, getElementClass(airportsChanges));
		
		WebElement durationChanges = seleniumController.getByPattern(Patterns.durationPattern, airportsChanges, "min").get(0);
		classCodes.put(ClassCode.CHANGES_DURATION, getElementClass(durationChanges));
		
		WebElement hoursChanges = seleniumController.getByPattern(Patterns.hoursSimplePattern, airportsChanges, ":").get(0);
		hoursChanges = seleniumController.getParent(hoursChanges).get(0);
		classCodes.put(ClassCode.CHANGES_HOURS, getElementClass(hoursChanges));
		
		WebElement operatorChanges = seleniumController.getByText("Wizz Air").get(0);
		classCodes.put(ClassCode.CHANGES_OPERATOR, getElementClass(operatorChanges));
		
		WebElement closeButtonChanges = seleniumController.getByText("Lot w jedną stronę").get(0);
		closeButtonChanges = seleniumController.getParent(closeButtonChanges).get(0);
		for (WebElement e : seleniumController.getChild(closeButtonChanges)) {
			if (e.getCssValue("display").equals("block")) {
				classCodes.put(ClassCode.CHANGES_CLOSE_BUTTON, getElementClass(e));
				break;
			}
		}
		WebElement flightClass = seleniumController.getByText("Klasa ekonomiczna", main).get(0);
		classCodes.put(ClassCode.MAIN_FLIGHT_CLASS, getElementClass(flightClass));
	}
	
	private void goToClassCodesSite() {
		String findClassQuery = googleFlightsProperties.getFindClassQuery();
		Date goingThereDate = new Date();
		goingThereDate = getDatePlusDays(goingThereDate, 30);
		String dateStr = FlightDataFormatter.formatDate(goingThereDate);
		findClassQuery += dateStr + ";tt=o;v=f;s=0";
		currentUrl = seleniumController.getSiteUrl();
		log.info("Going to specific address to load classes");
		seleniumController.openSite(findClassQuery);
	}
	
	private void goBackToPreviousPage() {
		if (currentUrl == null) {
			throw new NullPointerException("currentUrl is null!");
		}
		log.info("Going back on previous site after getting class codes");
		seleniumController.openSite(currentUrl);
		currentUrl = null;
	}
	
	private void addFetchedClassesToDatabase() {
		for (Map.Entry<ClassCode, String> entry : classCodes.entrySet()) {
			JsonObject parameters = new ParametersBuilder()
				.setParameter("name", entry.getKey().name())
				.setParameter("class", entry.getValue())
				.build();
			
			ServerConnector.executePOST(connector.Service.ADD_CLASS, parameters);
		}
	}
	
	private void fillMapFromDatabase(List<GoogleClass> classes) {
		for (GoogleClass clazz : classes) {
			classCodes.put(ClassCode.valueOf(clazz.getName()), clazz.getClassName());
		}
	}
	
	private String getElementClass(WebElement element) {
		return element.getAttribute("class");
	}
	
}
