package parser.parsing.flightproviders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.parsing.configuration.FlightProviderEnum;
import parser.parsing.configuration.SystemSettings;
import parser.parsing.flightproviders.googleflights.GoogleFlights;
import parser.parsing.flightproviders.googleflights.classcodes.ClassCodesManager;
import parser.parsing.flightproviders.googleflights.utils.QueryBuilder;
import parser.parsing.selenium.SeleniumController;

@Service("flightProvider")
public class FlightProviderFactoryBean implements FactoryBean<FlightProvider> {
	private static final Logger log = LoggerFactory.getLogger(FlightProviderFactoryBean.class);
	
	@Autowired
	private SystemSettings systemSettings;
	@Autowired
	private SeleniumController seleniumController;
	@Autowired
	private QueryBuilder queryBuilder;
	@Autowired
	private ClassCodesManager classCodesManager;
	
	private FlightProvider flightProvider;
	
	@Override
	public FlightProvider getObject() throws Exception {
		if (flightProvider == null) {
			FlightProviderEnum flightProviderEnum = systemSettings.getFlightProvider();
			log.info("Used flight provider: {}", flightProviderEnum.getLabel());
			
			switch (flightProviderEnum) {
				case GOOGLE_FLIGHTS:
					flightProvider = new GoogleFlights(seleniumController, queryBuilder, classCodesManager, systemSettings);
					break;
			}
		}
		return flightProvider;
	}
	
	@Override
	public Class<?> getObjectType() {
		return FlightProvider.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
}
