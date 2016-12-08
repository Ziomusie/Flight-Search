package searchApp.search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import searchApp.builders.ResultTableBuilder;
import searchApp.tasks.AdvancedSearchTask;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by Mateusz-PC on 22.07.2016.
 */
public class AdvancedSearchController implements Initializable {

	@FXML
	private Pane rootPane;

	@FXML
	private JFXTreeTableView<?> resultTable;

	@FXML
	private JFXButton searchButton;

	@FXML
	private JFXButton infoButton;

	@FXML
	private Region region;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextArea inputTextArea;

	@FXML
	private Label errorLabel;

	@FXML
	private JFXDialog dialog;

	@FXML
	private JFXButton acceptButton;

	@FXML
	private ImageView dialogImage;

	private ResultTableBuilder resultTableBuilder;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inputTextArea.setStyle("-fx-font-size: 16px;");
		resultTableBuilder = new ResultTableBuilder(resultTable);
		errorLabel.setVisible(false);

		acceptButton.setOnMouseClicked((e)->{
			dialog.close();
		});

		File image = new File("searchApplication/src/main/resources/db.PNG");
		Image i = new Image(image.toURI().toString());
		dialogImage.setImage(i);
	}

	@FXML
	void infoAction(ActionEvent event) {
		dialog.show(stackPane);
	}

	@FXML
	void searchAction(ActionEvent event) {
		AdvancedSearchTask task = new AdvancedSearchTask(rootPane, resultTableBuilder, inputTextArea.getText(), errorLabel);
		new Thread(task).start();
	}

	@FXML
	void sampleQuery1Action(ActionEvent event) {
		String query = "SELECT a1.airportname as Z, a2.airportname as Do, f.departuretime as Wylot,\n" +
			" f.arrivaltime as Przylot, f.price as Cena FROM flight f\n" +
			"\tINNER JOIN airport a1 On f.departureairportid = a1.id\n" +
			"\tINNER JOIN airport a2 On f.destinationairportid = a2.id\n" +
			"\tWHERE a1.airportcode = 'WAW' AND a2.airportcode = 'GDN'\n" +
			"\tORDER BY f.price;";
		inputTextArea.setText(query);
	}

	@FXML
	void sampleQuery2Action(ActionEvent event) {
		String query = "SELECT a1.airportname as Z, a2.airportname as Do, f.departuretime as Wylot,\n" +
			" f.arrivaltime as Przylot, f.price as Cena FROM flight f\n" +
			"\tINNER JOIN airport a1 On f.departureairportid = a1.id\n" +
			"\tINNER JOIN airport a2 On f.destinationairportid = a2.id\n" +
			"\tWHERE date_trunc('day', f.departuretime) = '"+ LocalDate.now() + "'\n" +
			"\tORDER BY f.price;\n" +
			"\n";
		inputTextArea.setText(query);
	}

}
