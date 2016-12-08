package searchApp.tasks;

import com.google.common.reflect.TypeToken;
import connector.ServerConnector;
import connector.Service;
import javafx.scene.layout.Pane;
import model.JourneyRequest;
import model.JourneyResponse;
import parser.ConnectionFinder;
import searchApp.builders.ResultTableBuilder;
import searchApp.builders.rowBuilders.ParserTableFiller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz-PC on 29.09.2016.
 */
public class ParserSearchTask extends SearchTask {

	private JourneyResponse response;

	private Map<String, String> airports = new HashMap<>();

	private Map<String, String> airlines = new HashMap<>();

	public ParserSearchTask(Pane rootPane, ResultTableBuilder resultTableBuilder, JourneyRequest request) {
		super(rootPane, resultTableBuilder, request);
	}

	@Override
	protected Boolean call() throws Exception {
		try {
			response = ConnectionFinder.findConnections(getJourneyRequest());
		} catch (Exception ex) {
			System.err.print(ex);
		}
		return true;
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

		if (ret && response != null) {
			getResultTableBuilder().fillTable(new ParserTableFiller(response, airports, airlines));
		}
	}

	public void setAirports(Map<String, String> airports) {
		this.airports = airports;
	}

	public void setAirlines(Map<String, String> airlines) {
		this.airlines = airlines;
	}
}
