package model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.joda.money.Money;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tsweda on 2016-07-12.
 */
public class Flight {
	/**
	 * Cena lotu
	 */
	@SerializedName("Price")
	private Money price;
	
	/**
	 * Czas trwania lotu
	 */
	@SerializedName("FlightTime")
	private Time flightDuration;
	
	/**
	 * Kod IATA lotniska skąd lecimy
	 */
	@SerializedName("DepartureAirportCode")
	private String airportFrom;
	
	/**
	 * Kod IATA lotniska docelowego
	 */
	@SerializedName("DestinationAirportCode")
	private String airportTo;
	
	/**
	 * Data wylotu
	 */
	@SerializedName("DepartureTime")
	private Date date;
	
	/**
	 * Godziny odlotu i przylotu
	 */
	private TimeInterval flightHours;
	
	/**
	 * Nazwa linii lotniczej
	 */
	private String airline;
	
	/**
	 * Liczba przesiadek
	 */
	private int changesCount;
	
	/**
	 * Przesiadki
	 */
	private List<Flight> changes = new ArrayList<Flight>();
	
	/**
	 * Data ostatniej aktualizacji lotu
	 */
	private Date lastUpdate;

	@SerializedName("FlightTime")
	private String flightClass;

	
	public static Type getType() {
		return new TypeToken<ArrayList<Flight>>(){}.getType();
	}
	
	public Money getPrice() {
		return price;
	}
	
	public void setPrice(Money price) {
		this.price = price;
	}
	
	public Time getFlightDuration() {
		return flightDuration;
	}
	
	public void setFlightDuration(Time flightDuration) {
		this.flightDuration = flightDuration;
	}
	
	public String getAirportFrom() {
		return airportFrom;
	}
	
	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}
	
	public String getAirportTo() {
		return airportTo;
	}
	
	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public TimeInterval getFlightHours() {
		return flightHours;
	}
	
	public void setFlightHours(TimeInterval flightHours) {
		this.flightHours = flightHours;
	}
	
	public String getAirline() {
		return airline;
	}
	
	public void setAirline(String airline) {
		this.airline = airline;
	}
	
	public int getChangesCount() {
		return changesCount;
	}
	
	public void setChangesCount(int changesCount) {
		this.changesCount = changesCount;
	}
	
	public List<Flight> getChanges() {
		return changes;
	}
	
	public void setChanges(List<Flight> changes) {
		this.changes = changes;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}
}

