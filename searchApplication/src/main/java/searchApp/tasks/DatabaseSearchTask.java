package searchApp.tasks;

import com.jfoenix.controls.JFXDialog;
import connector.ServerConnector;
import connector.Service;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.JourneyRequest;
import searchApp.builders.ResultTableBuilder;
import searchApp.helpers.StringHelpers;
import searchApp.builders.rowBuilders.DatabaseTableFiller;
import searchApp.model.Trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz-PC on 16.07.2016.
 */
public class DatabaseSearchTask extends SearchTask {
	private StackPane stackPane;
	private JFXDialog dialog;
	private List<Trip> flightList;
	private List<Trip> returnFlightsList = new ArrayList<>();
	private Date comingBackDate;

	public DatabaseSearchTask(Pane rootPane, ResultTableBuilder resultTableBuilder, JourneyRequest request, StackPane stackPane, JFXDialog dialog) {
		super(rootPane, resultTableBuilder, request);
		this.stackPane = stackPane;
		this.dialog = dialog;
    }

	@Override
    protected Boolean call() throws Exception {
		comingBackDate = getJourneyRequest().getComingBackDate();
		getJourneyRequest().setComingBackDate(null);
		String params = StringHelpers.getJourneyRequestParams(getJourneyRequest());
		flightList = ServerConnector.executeGET(Service.GET_FLIGHTS, params, Trip.getType());
		if (comingBackDate != null) {
			switchDestinations();
			getJourneyRequest().setGoingThereDate(comingBackDate);
			String returnParams = StringHelpers.getJourneyRequestParams(getJourneyRequest());
			returnFlightsList = ServerConnector.executeGET(Service.GET_FLIGHTS, returnParams, Trip.getType());
		}

        return true;
    }

	private void switchDestinations() {
		String airportFrom = getJourneyRequest().getAirportFrom();
		String airportTo = getJourneyRequest().getAirportTo();
		getJourneyRequest().setAirportFrom(airportTo);
		getJourneyRequest().setAirportTo(airportFrom);
	}
	
	public void showDialog(boolean isResponseEmpty) {
		if (isResponseEmpty) {
			dialog.show(stackPane);
		}
	}
	@Override
	protected void succeeded() {
		getSpinner().setVisible(false);
		getResultTableBuilder().getResultTable().setVisible(true);
		getRootPane().getChildren().remove(getSpinner());
		boolean ret = false;
		try {
			ret = this.get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		if (ret && flightList != null) {
			getResultTableBuilder().fillTable(new DatabaseTableFiller(flightList, returnFlightsList));
			showDialog(flightList.isEmpty());
			if (comingBackDate != null && returnFlightsList != null) {
				showDialog(returnFlightsList.isEmpty());
			}
		}
	}
}
