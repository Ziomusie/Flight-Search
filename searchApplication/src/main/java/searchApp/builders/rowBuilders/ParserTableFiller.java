package searchApp.builders.rowBuilders;

import javafx.scene.control.TreeItem;
import model.Flight;
import model.JourneyResponse;
import model.Time;
import org.joda.money.Money;
import searchApp.builders.ResultTableBuilder;
import searchApp.model.FlightRow;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

/**
 * Created by Mateusz-PC on 15.10.2016.
 */
public class ParserTableFiller implements TableFiller {

	private JourneyResponse response;

	private Map<String, String> airports;
	private Map<String, String> airlines;


	public ParserTableFiller(JourneyResponse response, Map<String, String> airports, Map<String, String> airlines) {
		this.response = response;
		this.airports = airports;
		this.airlines = airlines;
	}

	@Override
	public void fill(TreeItem<FlightRow> parent) {
		if (response.getComingBackFlights() != null && !response.getComingBackFlights().isEmpty()) {
			TreeItem<FlightRow> goingThereParent = getFlightParent(parent, ResultTableBuilder.FLIGHTS);
			TreeItem<FlightRow> returnParent = getFlightParent(parent, ResultTableBuilder.RETURN_FLIGHTS);
			response.getComingBackFlights().forEach(flight -> buildRow(returnParent, flight));
			response.getGoingThereFlights().forEach(flight -> buildRow(goingThereParent, flight));
		} else {
			response.getGoingThereFlights().stream().forEach(flight -> buildRow(parent, flight));
		}
	}

	private void buildRow(TreeItem<FlightRow> parent, Flight flight) {
		Money price = flight.getPrice();
		String airportTo = airports.get(flight.getAirportTo());
		String airportFrom = airports.get(flight.getAirportFrom());
		LocalDate date = flight.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		FlightRow row = new FlightRow.FlightRowBuilder().airportFrom(airportFrom)
			.airportTo(airportTo)
			.departureDate(date)
			.departureTime(flight.getFlightHours().getStart().shortLabel(Time.DEFAULT_SEPARATOR))
			.arrivalTime(flight.getFlightHours().getEnd().shortLabel(Time.DEFAULT_SEPARATOR))
			.operator(flight.getAirline())
			.link(airlines.get(flight.getAirline()))
			.price(price)
			.build();

		parent.getChildren().add(new TreeItem<>(row));
	}
}
