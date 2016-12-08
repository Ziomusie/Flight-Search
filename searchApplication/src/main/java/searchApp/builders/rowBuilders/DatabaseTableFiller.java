package searchApp.builders.rowBuilders;

import javafx.scene.control.TreeItem;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import parser.parsing.flightproviders.googleflights.utils.FlightDataFormatter;
import searchApp.builders.ResultTableBuilder;
import searchApp.helpers.DateUtill;
import searchApp.model.FlightRow;
import searchApp.model.FlightTrip;
import searchApp.model.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Mateusz-PC on 15.10.2016.
 */
public class DatabaseTableFiller implements TableFiller {

	private List<Trip> trips;

	private List<Trip> returnTrips;

	public DatabaseTableFiller(List<Trip> trips, List<Trip> returnTrips) {
		this.trips = trips;
		this.returnTrips = returnTrips;
	}

	@Override
	public void fill(TreeItem<FlightRow> parent) {
		if (!returnTrips.isEmpty()) {
			TreeItem<FlightRow> goingThereParent = getFlightParent(parent, ResultTableBuilder.FLIGHTS);
			TreeItem<FlightRow> returnParent = getFlightParent(parent, ResultTableBuilder.RETURN_FLIGHTS);
			trips.forEach(trip -> buildTrip(goingThereParent, trip));
			returnTrips.forEach(trip -> buildTrip(returnParent, trip));
		} else {
			trips.forEach(trip -> buildTrip(parent, trip));
		}
	}


	private void buildTrip(TreeItem<FlightRow> parent, Trip trip) {
		if (trip.getFlights() != null && trip.getFlights().size() == 1) {
			buildTripOneRow(parent,trip.getFlights().get(0));
		} else if (trip.getFlights() != null){
			buildTripMultipleRows(parent,trip);
		}
	}

	private void buildTripMultipleRows(TreeItem<FlightRow> parent, Trip trip) {
		int lastFlightIndex = trip.getFlights().size() - 1;
		FlightTrip flight = trip.getFlights().get(0);
		FlightTrip firstFlight = trip.getFlights().get(0);
		FlightTrip lastFlight = trip.getFlights().get(lastFlightIndex);
		LocalDate depDate = DateUtill.getLocalDate(flight.getDepartureTime());
		String depTime = DateUtill.getTimeFromDateTime(flight.getDepartureTime());
		String arrivalTime = calculateArrivalTime(trip.getFlights().get(lastFlightIndex));
		Money price = calculatePrice(trip.getFlights());
		FlightRow row = new FlightRow.FlightRowBuilder().airportFrom(firstFlight.getDepartureAirport())
			.airportTo(lastFlight.getDestinationAirport())
			.departureDate(depDate)
			.departureTime(depTime)
			.arrivalTime(arrivalTime)
			.price(price)
			.build();
		TreeItem<FlightRow> mainRow = new TreeItem<>(row);

		trip.getFlights().forEach(f -> buildTripOneRow(mainRow, f));

		parent.getChildren().add(mainRow);

	}

	private Money calculatePrice(List<FlightTrip> flightTrips) {
		Double sum = 0.0;
		for (FlightTrip ft : flightTrips) {
			Double price = Double.valueOf(ft.getPrice());
			sum += price;
		}
		return FlightDataFormatter.formatFlightPrice(sum.intValue()  + "", CurrencyUnit.of("PLN"));
	}

	private String calculateArrivalTime(FlightTrip flightTrip) {
		return getArrivalDate(flightTrip.getDepartureTime(), flightTrip.getFlightTime());
	}

	private void buildTripOneRow(TreeItem<FlightRow> parent,FlightTrip flight) {
		LocalDate date = DateUtill.getLocalDate(flight.getDepartureTime());
		String depTime = DateUtill.getTimeFromDateTime(flight.getDepartureTime());

		String arrivalTime = getArrivalDate(flight.getDepartureTime(), flight.getFlightTime());
		FlightRow row = new FlightRow.FlightRowBuilder().airportFrom(flight.getDepartureAirport())
			.airportTo(flight.getDestinationAirport())
			.departureTime(depTime)
			.arrivalTime(arrivalTime)
			.departureDate(date)
			.operator(flight.getAirlineName())
			.link(flight.getAirlineWebsite())
			.price(FlightDataFormatter.formatFlightPrice(flight.getPrice(), CurrencyUnit.of("PLN")))
			.build();
		parent.getChildren().add(new TreeItem<>(row));

	}

	private String getArrivalDate(String date, String flightTime) {
		LocalDateTime depDate = DateUtill.getLocalDateTime(date);
		LocalTime time = DateUtill.getTimeStringFromTime(flightTime);
		LocalDateTime arrivalTime = DateUtill.addTimeToDate(depDate, time);
		return DateUtill.getTimeFromDateTime(arrivalTime);
	}
}
