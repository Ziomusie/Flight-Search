package searchApp.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXSpinner;
import connector.ConnectorException;
import connector.ParametersBuilder;
import connector.ServerConnector;
import connector.Service;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import searchApp.builders.ResultTableBuilder;

import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz-PC on 16.07.2016.
 */
public class AdvancedSearchTask extends Task<Boolean> {

    private JFXSpinner spinner;

    private Pane rootPane;

	private ResultTableBuilder resultTableBuilder;

	private JsonArray resultArray;

	private String query;

	private Label errorLabel;

	public AdvancedSearchTask(Pane rootPane, ResultTableBuilder resultTableBuilder, String query, Label errorLabel) {
        this.rootPane = rootPane;
        this.resultTableBuilder = resultTableBuilder;
		this.query = query;
		this.errorLabel = errorLabel;
		spinner = initSpinner(rootPane);
    }

	public static JFXSpinner initSpinner(Pane rootPane) {
		JFXSpinner spinner = new JFXSpinner();
		spinner.setStartingAngle(-40);
		spinner.setLayoutX(375);
		spinner.setLayoutY(400);
		spinner.setVisible(false);
		rootPane.getChildren().add(spinner);
		return spinner;
	}

	@Override
    protected Boolean call() throws Exception {
    	try {
    		JsonObject parameters = new ParametersBuilder().setParameter("query", query).build();
    		String response = ServerConnector.executePOST(Service.EXECUTE_QUERY, parameters);
		    JsonElement rootElement = new JsonParser().parse(response);
		    JsonObject rootObject = rootElement.getAsJsonObject();
		    JsonElement results = rootObject.get("results");
		    resultArray = results.getAsJsonArray();
		    errorLabel.setVisible(false);
		    resultTableBuilder.buildTable(resultArray);
	    }
	    catch (ConnectorException e) {
	    	changeErrorLabel(e.getServerMessage());
	    }
        return true;
    }

	private void changeErrorLabel(String errorMessage) {
		Platform.runLater(() -> {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        });
	}

	@Override
    protected void running() {
        spinner.setVisible(true);
        resultTableBuilder.getResultTable().setVisible(false);
    }

    @Override
    protected void succeeded() {
        spinner.setVisible(false);
        rootPane.getChildren().remove(spinner);
		boolean ret = false;
        try {
            ret = this.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
    }

	@Override
	protected void failed() {
		spinner.setVisible(false);
		rootPane.getChildren().remove(spinner);
		System.out.println("Wystąpił błąd");
	}
}
