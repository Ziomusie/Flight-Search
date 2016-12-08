package searchApp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Hyperlink;
import org.joda.money.Money;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * Created by Mateusz-PC on 25.07.2016.
 */
public class FlightRow extends RecursiveTreeObject<FlightRow> {
	private StringProperty airportFrom = new SimpleStringProperty();
	private StringProperty airportTo = new SimpleStringProperty();

	private StringProperty returnDate = new SimpleStringProperty();
	private StringProperty flightDuration = new SimpleStringProperty();
	private StringProperty flightClass = new SimpleStringProperty();
	private StringProperty departureTime = new SimpleStringProperty();
	private StringProperty arrivalTime = new SimpleStringProperty();
	private StringProperty changes = new SimpleStringProperty();
	private StringProperty operator = new SimpleStringProperty();
	private ObjectProperty<Hyperlink> link = new SimpleObjectProperty<>();

	private ObjectProperty<LocalDate> departureDate = new SimpleObjectProperty<>();
	private ObjectProperty<Money> price = new SimpleObjectProperty<>();

	private String operatorUrl;

	public FlightRow(FlightRowBuilder builder) {
		airportFrom.set(builder.airportFrom);
		airportTo.set(builder.airportTo);
		returnDate.set(builder.returnDate);
		flightDuration.set(builder.flightDuration);
		flightClass.set(builder.flightClass);
		departureTime.set(builder.departureTime);
		arrivalTime.set(builder.arrivalTime);
		changes.set(builder.changes);
		operator.set(builder.operator);
		operatorUrl = builder.link;
		departureDate.setValue(builder.departureDate);
		price.setValue(builder.price);
		if (!(builder.operator == null || builder.operator.isEmpty()) && operatorUrl != null) {
			Hyperlink h = prepareHyperLink();
			link.set(h);
		}
	}

	public LocalDate getDepartureDate() {
		return departureDate.get();
	}

	public ObjectProperty<LocalDate> departureDateProperty() {
		return departureDate;
	}

	private Hyperlink prepareHyperLink() {
		Hyperlink h = new Hyperlink();
		h.setOnAction(event -> {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(operatorUrl));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		});
		h.setText("Kup");
		return h;
	}

	public String getAirportFrom() {
		return airportFrom.get();
	}

	public StringProperty airportFromProperty() {
		return airportFrom;
	}

	public String getAirportTo() {
		return airportTo.get();
	}

	public StringProperty airportToProperty() {
		return airportTo;
	}


	public String getReturnDate() {
		return returnDate.get();
	}

	public StringProperty returnDateProperty() {
		return returnDate;
	}


	public String getFlightDuration() {
		return flightDuration.get();
	}

	public StringProperty flightDurationProperty() {
		return flightDuration;
	}

	public String getFlightClass() {
		return flightClass.get();
	}

	public StringProperty flightClassProperty() {
		return flightClass;
	}

	public String getDepartureTime() {
		return departureTime.get();
	}

	public StringProperty departureTimeProperty() {
		return departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime.get();
	}

	public StringProperty arrivalTimeProperty() {
		return arrivalTime;
	}

	public String getChanges() {
		return changes.get();
	}

	public StringProperty changesProperty() {
		return changes;
	}

	public String getOperator() {
		return operator.get();
	}

	public StringProperty operatorProperty() {
		return operator;
	}

	public Hyperlink getLink() {
		return link.get();
	}

	public ObjectProperty<Hyperlink> linkProperty() {
		return link;
	}

	public void setLink(Hyperlink link) {
		this.link.set(link);
	}

	public String getOperatorUrl() {
		return operatorUrl;
	}

	public void setOperatorUrl(String operatorUrl) {
		this.operatorUrl = operatorUrl;
	}

	public Money getPrice() {
		return price.get();
	}

	public ObjectProperty<Money> priceProperty() {
		return price;
	}

	public static class FlightRowBuilder {

		private String airportFrom;
		private String airportTo;
		private String returnDate;
		private String flightDuration;
		private String flightClass;
		private String departureTime;
		private String arrivalTime;
		private String changes;
		private String operator;
		private String link;
		private LocalDate departureDate;
		private Money price;

		public FlightRowBuilder airportFrom(String value) {
			airportFrom = value;
			return this;
		}

		public FlightRowBuilder airportTo(String value) {
			airportTo = value;
			return this;
		}

		public FlightRowBuilder returnDate(String value) {
			returnDate = value;
			return this;
		}

		public FlightRowBuilder flightDuration(String value) {
			flightDuration = value;
			return this;
		}

		public FlightRowBuilder flightClass(String value) {
			flightClass = value;
			return this;
		}

		public FlightRowBuilder departureTime(String value) {
			departureTime = value;
			return this;
		}

		public FlightRowBuilder arrivalTime(String value) {
			arrivalTime = value;
			return this;
		}

		public FlightRowBuilder changes(String value) {
			changes = value;
			return this;
		}

		public FlightRowBuilder operator(String value) {
			operator = value;
			return this;
		}

		public FlightRowBuilder link(String value) {
			link = value;
			return this;
		}

		public FlightRowBuilder departureDate(LocalDate value) {
			departureDate = value;
			return this;
		}

		public FlightRowBuilder price(Money value) {
			price = value;
			return this;
		}


		public FlightRow build() {
			return new FlightRow(this);
		}
	}
}
