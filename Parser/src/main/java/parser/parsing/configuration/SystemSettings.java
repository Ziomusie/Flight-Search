package parser.parsing.configuration;

import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by tsweda on 2016-07-13.
 */
@Repository
public class SystemSettings {
	@Value("${parser.chosenBrowser}")
    private ParseBrowser chosenBrowser;
	@Value("${parser.flightProvider}")
	private FlightProviderEnum flightProvider;
	@Value("${parser.isExecutable}")
	private boolean isExecutable;
	
	@Value("${parser.showLogs}")
	private boolean showLogs;
	@Value("${parser.printTableToOuput}")
    private boolean printTableToOuput;
	@Value("${parser.closeBrowserOnFinish}")
    private boolean closeBrowserOnFinish;
	@Value("${parser.defaultCurrency}")
	private String defaultCurrency;
	
	@Value("${driver.implicitWait.seconds}")
	private int driverImplicitWait;
	@Value("${driver.staleElRefExc.maxTryNumber}")
	private int driverStaleElRefExcMaxTryNo;
	
	public ParseBrowser getChosenBrowser() {
		return chosenBrowser;
	}
	
	public FlightProviderEnum getFlightProvider() {
		return flightProvider;
	}
	
	public boolean isShowLogs() {
		return showLogs;
	}
	
	public boolean isPrintTableToOuput() {
		return printTableToOuput;
	}
	
	public boolean isCloseBrowserOnFinish() {
		return closeBrowserOnFinish;
	}
	
	public int getDriverImplicitWait() {
		return driverImplicitWait;
	}
	
	public int getDriverStaleElRefExcMaxTryNo() {
		return driverStaleElRefExcMaxTryNo;
	}
	
	public CurrencyUnit getCurrencyUnit() {
		return CurrencyUnit.of(defaultCurrency);
	}
	
	public boolean isExecutable() {
		return isExecutable;
	}
}
