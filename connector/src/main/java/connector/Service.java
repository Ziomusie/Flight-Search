package connector;

public enum Service {
	GET_AIRPORT("getAirport"),
	ADD_FLIGHT("addFlight"),
	GET_FLIGHTS("getFlights"),
	GET_LIST_OF_AIRLINES("getListOfAirlines"),
	GET_LIST_OF_AIRPORTS("getListOfAirports"),
	GET_LIST_OF_CLASSES("getListOfClasses"),
	DELETE_ALL_CLASSES("deleteAllClasses"),
	UPDATE_CLASS("updateClass"),
	ADD_CLASS("addClass"),
	EXECUTE_QUERY("executeQuery"),
	GET_LIST_OF_OLDEST_FLIGHTS("getTheOldestFlights");
	
	private String urlPart;
	
	Service(String urlPart) {
		this.urlPart = urlPart;
	}
	
	public String getUrlPart() {
		return urlPart;
	}
}
