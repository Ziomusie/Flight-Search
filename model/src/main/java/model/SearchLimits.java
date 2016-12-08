package model;

import org.joda.money.Money;

public class SearchLimits {
	/**
	 * Maksymalna cena
	 */
	private Money price;
	/**
	 * Obsługująca linia lotnicza
	 */
	private String airline;
	/**
	 * Maksymalny czas trwania lotu
	 */
	private Time maxDuration;
	/**
	 * Maksymalna liczba przesiadek
	 * Dla 0 - loty bez przesiadek
	 */
	private Integer maxChanges;
	/**
	 * Klasa lotu (ekonomiczna, biznesowa, itd.)
	 */
	private String flightClass;
	/**
	 * Dopuszczalne godziny wylotu (lot w tamtą stronę)
	 */
	private TimeInterval flightHours;


	
	public SearchLimits() {}
	
	private SearchLimits(Builder builder) {
		this.price = builder.price;
		this.airline = builder.airline;
		this.maxDuration = builder.maxDuration;
		this.maxChanges = builder.maxChanges;
		this.flightClass = builder.flightClass;
		this.flightHours = builder.flightHours;
	}
	
	public static Builder newSearchLimits() {
		return new Builder();
	}
	
	public Money getPrice() {
		return price;
	}
	
	public void setPrice(Money price) {
		this.price = price;
	}
	
	public String getAirline() {
		return airline;
	}
	
	public void setAirline(String airline) {
		this.airline = airline;
	}
	
	public Time getMaxDuration() {
		return maxDuration;
	}
	
	public void setMaxDuration(Time maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	public Integer getMaxChanges() {
		return maxChanges;
	}
	
	public void setMaxChanges(Integer maxChanges) {
		this.maxChanges = maxChanges;
	}
	
	public String getFlightClass() {
		return flightClass;
	}
	
	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}
	
	public TimeInterval getFlightHours() {
		return flightHours;
	}
	
	public void setFlightHours(TimeInterval flightHours) {
		this.flightHours = flightHours;
	}
	
	public static final class Builder {
		private Money price;
		private String airline;
		private Time maxDuration;
		private Integer maxChanges;
		private String flightClass;
		private TimeInterval flightHours;
		
		private Builder() {}
		
		public SearchLimits build() {
			return new SearchLimits(this);
		}
		
		public Builder price(Money price) {
			this.price = price;
			return this;
		}
		
		public Builder airline(String airline) {
			this.airline = airline;
			return this;
		}
		
		public Builder maxDuration(Time maxDuration) {
			this.maxDuration = maxDuration;
			return this;
		}
		
		public Builder maxChanges(Integer maxChanges) {
			this.maxChanges = maxChanges;
			return this;
		}
		
		public Builder flightClass(String flightClass) {
			this.flightClass = flightClass;
			return this;
		}
		
		public Builder flightHours(TimeInterval flightHours) {
			this.flightHours = flightHours;
			return this;
		}
	}
}
