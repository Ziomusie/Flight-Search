package model;

import java.util.Date;

public class JourneyRequest {
	/**
	 * Data wylotu
	 */
	private Date goingThereDate;
	/**
	 * Data powrotu.
	 * Jeśli null - oznacza, że nie szukamy lotów powrotnych
	 */
	private Date comingBackDate;
	/**
	 * Limity wyszukiwania
	 */
	private SearchLimits searchLimits;
	/**
	 * Kod lotniska wylotu
	 */
	private String airportFrom;
	/**
	 * Kod lotniska docelowego
	 */
	private String airportTo;
	
	public JourneyRequest(Date goingThereDate, Date comingBackDate, SearchLimits searchLimits, String airportFrom, String airportTo) {
		this.goingThereDate = goingThereDate;
		this.comingBackDate = comingBackDate;
		this.searchLimits = searchLimits;
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
	}
	
	public Date getGoingThereDate() {
		return goingThereDate;
	}
	
	public Date getComingBackDate() {
		return comingBackDate;
	}
	
	public SearchLimits getSearchLimits() {
		return searchLimits;
	}
	
	public String getAirportFrom() {
		return airportFrom;
	}
	
	public String getAirportTo() {
		return airportTo;
	}

	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}

	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}

	public void setGoingThereDate(Date goingThereDate) {
		this.goingThereDate = goingThereDate;
	}

	public void setComingBackDate(Date comingBackDate) {
		this.comingBackDate = comingBackDate;
	}
}
