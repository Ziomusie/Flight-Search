package searchApp;

import com.google.common.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import connector.ServerConnector;
import connector.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import searchApp.helpers.DialogErrorHandler;
import searchApp.helpers.FXMLResources;
import searchApp.search.SearchController;
import searchApp.tasks.UpdateFlightsTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainAppController implements Initializable {

    @FXML
    private AnchorPane contentPane;

	@FXML
	private JFXDialog errorDialog;

	@FXML
	private StackPane stackPane;
	@FXML
	private JFXButton cancelButton;

	private DialogErrorHandler dialogErrorHandler;

    private String loadedScene;

	private Map<String, String> airports = new HashMap<>();

	private Map<String, String> airlines = new HashMap<>();


	@Override
    public void initialize(URL location, ResourceBundle resources) {
		dialogErrorHandler = new DialogErrorHandler(errorDialog, stackPane, cancelButton);
		Type t = new TypeToken<HashMap<String, String>>(){}.getType();
		try{
			airports = ServerConnector.executeGETToMap(Service.GET_LIST_OF_AIRPORTS, "", t);
			if (airports == null) {
				airports = new HashMap<>();
			}
		} catch (Exception ex) {
			airports = new HashMap<>();
		}
		try{
			airlines = ServerConnector.executeGETToMap(Service.GET_LIST_OF_AIRLINES, "", t);
			if (airlines == null) {
				airlines = new HashMap<>();
			}
		} catch (Exception ex) {
			airlines = new HashMap<>();
		}
		if (airlines.isEmpty() && airports.isEmpty()) {
			dialogErrorHandler.showErrorDialog();
		} else {
			loadSearchController();
		}

		loadedScene = FXMLResources.SEARCH_SCENE_NAME;
		UpdateFlightsTask task = new UpdateFlightsTask();
		Thread updateThread = new Thread(task);
		updateThread.start();
	}

    @FXML
    private void searchButtonClick(ActionEvent actionEvent) {
        if (loadedScene.equals(FXMLResources.SEARCH_SCENE_NAME)) {
            return;
        }
        contentPane.getChildren().clear();
		loadSearchController();
		loadedScene = FXMLResources.SEARCH_SCENE_NAME;
    }

	private void loadSearchController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(FXMLResources.SEARCH_SCENE));
			contentPane.getChildren().add(fxmlLoader.load());
			SearchController controller = fxmlLoader.<SearchController>getController();
			controller.setAirports(airports);
			controller.setAirlines(airlines);
			controller.setDialogErrorHandler(dialogErrorHandler);
			controller.initAutoCompleteSearch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSearchContent(String scene) {
        try {
			contentPane.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource(scene)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void advancedSearchButtonClick(ActionEvent actionEvent) {
		if (loadedScene.equals(FXMLResources.ADVANCED_SEARCH_SCENE_NAME)) {
			return;
		}
		contentPane.getChildren().clear();
		loadSearchContent(FXMLResources.ADVANCED_SEARCH_SCENE);
		loadedScene = FXMLResources.ADVANCED_SEARCH_SCENE_NAME;
    }


}
