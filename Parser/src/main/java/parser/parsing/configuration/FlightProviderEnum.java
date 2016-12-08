package parser.parsing.configuration;

public enum FlightProviderEnum {
	GOOGLE_FLIGHTS("Google Flights");
	
	String label;
	
	FlightProviderEnum(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
