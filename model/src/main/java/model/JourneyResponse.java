package model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JourneyResponse {
	/**
	 * Lista lotów w tamtą stronę
	 */
	private List<Flight> goingThereFlights;
	/**
	 * Lista lotów powrotnych
	 * Null, jeśli w żądaniu nie było mowy o locie powrotnym
	 */
	private List<Flight> comingBackFlights;
	
	public JourneyResponse(List<Flight> goingThereFlights, List<Flight> comingBackFlights) {
		this.goingThereFlights = goingThereFlights;
		this.comingBackFlights = comingBackFlights;
		//sendFlightsToDB(); //tu wywolanie wysylki lotow do bazy
	}
	
	public List<Flight> getGoingThereFlights() {
		return goingThereFlights;
	}
	
	public List<Flight> getComingBackFlights() {
		return comingBackFlights;
	}
	
	public static Type getType() {
		return new TypeToken<ArrayList<JourneyResponse>>(){}.getType();
	}
	
}
