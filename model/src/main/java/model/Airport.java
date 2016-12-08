package model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Mateusz-PC on 30.07.2016.
 */
public class Airport {
	private String AirportCode;
	private String AirportName;

	public Airport(String airportCode, String airportName) {
		AirportCode = airportCode;
		AirportName = airportName;
	}

	public String getAirportCode() {
		return AirportCode;
	}

	public void setAirportCode(String airportCode) {
		AirportCode = airportCode;
	}

	public String getAirportName() {
		return AirportName;
	}

	public void setAirportName(String airportName) {
		AirportName = airportName;
	}

	public static Type getType() {
		return new TypeToken<ArrayList<Airport>>(){}.getType();
	}

	@Override
	public String toString() {
		return getAirportName() + " (" + getAirportCode() + ")";
	}
}
