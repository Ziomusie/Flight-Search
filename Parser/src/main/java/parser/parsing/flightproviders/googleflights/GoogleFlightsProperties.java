package parser.parsing.flightproviders.googleflights;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class GoogleFlightsProperties {
	@Value("${parser.googleflights.queryBase}")
	private String queryBase;
	@Value("${parser.googleflights.findClassQuery}")
	private String findClassQuery;
	
	public String getQueryBase() {
		return queryBase;
	}
	
	public String getFindClassQuery() {
		return findClassQuery;
	}
}
