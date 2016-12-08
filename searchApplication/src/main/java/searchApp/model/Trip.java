package searchApp.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz-PC on 15.10.2016.
 */
public class Trip {
	@SerializedName("departureCity")
	String departureCity;

	@SerializedName("destinationCity")
	String destinationCity;

	@SerializedName("flights")
	List<FlightTrip> flights;


	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public List<FlightTrip> getFlights() {
		return flights;
	}

	public void setFlights(List<FlightTrip> flights) {
		this.flights = flights;
	}

	public static Type getType() {
		return new TypeToken<ArrayList<Trip>>(){}.getType();
	}
}
