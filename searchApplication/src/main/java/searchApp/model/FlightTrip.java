package searchApp.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mateusz-PC on 15.10.2016.
 */
public class FlightTrip {
	@SerializedName("ID")
	int id;

	@SerializedName("DepartureAirportName")
	String departureAirport;

	@SerializedName("DestinationAirportName")
	String destinationAirport;

	@SerializedName("DepartureTime")
	String departureTime;

	@SerializedName("FlightTime")
	String flightTime;

	@SerializedName("AirlineName")
	String airlineName;

	@SerializedName("AirlineWebsite")
	String airlineWebsite;

	@SerializedName("Price")
	String price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}

	public String getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(String flightTime) {
		this.flightTime = flightTime;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getAirlineWebsite() {
		return airlineWebsite;
	}

	public void setAirlineWebsite(String airlineWebsite) {
		this.airlineWebsite = airlineWebsite;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
