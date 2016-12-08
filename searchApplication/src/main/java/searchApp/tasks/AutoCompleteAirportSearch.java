package searchApp.tasks;

import com.jfoenix.controls.JFXTextField;
import com.ning.http.client.AsyncHttpClient;
import connector.ServerConnector;
import connector.Service;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import model.Airport;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Mateusz-PC on 25.07.2016.
 */
public class AutoCompleteAirportSearch implements Callback<AutoCompletionBinding.ISuggestionRequest, Collection<Airport>> {

	private final int MIN_WORD_SIZE_FOR_AUTOCOMPLETE = 2;

	private List<Airport> airportList;

	private JFXTextField textField;

	public AutoCompleteAirportSearch(JFXTextField textField, Map<String, String> airports) {
		airportList = FXCollections.observableArrayList();
		this.textField = textField;
		airportList.addAll(airports.entrySet().stream().map(a -> new Airport(a.getKey(), a.getValue())).collect(Collectors.toList()));
	}

	@Override
	public Collection<Airport> call(AutoCompletionBinding.ISuggestionRequest param) {
		if (textField.getText().length() <= MIN_WORD_SIZE_FOR_AUTOCOMPLETE) {
			return FXCollections.observableArrayList();
		}
		return airportList.stream().filter(airport -> airport.getAirportName().toLowerCase().contains(textField.getText().toLowerCase())).collect(Collectors.toList());
	}

	public List<Airport> getAirportList() {
		return airportList;
	}

}
