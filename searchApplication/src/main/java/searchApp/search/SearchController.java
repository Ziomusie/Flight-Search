package searchApp.search;

import com.jfoenix.controls.*;
import com.jfoenix.skins.JFXDatePickerSkin;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.ValidationFacade;
import com.jfoenix.validation.base.ValidatorBase;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import model.Airport;
import model.JourneyRequest;
import model.SearchLimits;
import model.Time;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import searchApp.MainAppController;
import searchApp.helpers.DialogErrorHandler;
import searchApp.tasks.AutoCompleteAirportSearch;
import searchApp.builders.ResultTableBuilder;
import searchApp.helpers.StringHelpers;
import searchApp.tasks.DatabaseSearchTask;
import searchApp.tasks.ParserSearchTask;
import searchApp.tasks.SearchTask;
import searchApp.validators.DateInputValidator;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Mateusz-PC on 16.07.2016.
 */
public class SearchController implements Initializable {

	@FXML
	private StackPane stackPane;

    @FXML
    private Pane rootPane;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXCheckBox onlyDirectCheckbox;

    @FXML
    private JFXTreeTableView resultTable;

    @FXML
    private JFXDatePicker returnDate;

    @FXML
    private JFXDatePicker departureDate;

    @FXML
    private JFXTextField toInput;

    @FXML
    private JFXTextField fromInput;

	@FXML
	private Label errorLabel;

	@FXML
	private JFXCheckBox maxPriceCheckbox;

	@FXML
	private JFXSlider maxPriceSlider;

	@FXML
	private JFXCheckBox flightDurationCheckbox;

	@FXML
	private JFXSlider flightDurationSlider;

	@FXML
	private HBox upperHbox;

	@FXML
	private HBox lowerHbox;

	@FXML
	private JFXComboBox<String> maxChangesSelector;

	@FXML
	private JFXDialog dialog;

	@FXML
	private JFXButton acceptButton;

	@FXML
	private JFXButton cancelButton;

	private ResultTableBuilder resultTableBuilder;

	private AutoCompleteAirportSearch autoCompleteAirportSearchFrom;

	private AutoCompleteAirportSearch autoCompleteAirportSearchTo;

	private ValidationFacade vf;

	private JourneyRequest request;

	private Map<String, String> airports = new HashMap<>();

	private Map<String, String> airlines = new HashMap<>();

	private DialogErrorHandler dialogErrorHandler;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
		unfocusInputFields();
		addInputFieldValidator(toInput);
		addInputFieldValidator(fromInput);
		resultTableBuilder = new ResultTableBuilder(resultTable);
		resultTableBuilder.buildTable();
		upperHbox.getChildren().remove(maxPriceSlider);
		lowerHbox.getChildren().remove(flightDurationSlider);

		maxChangesSelector.getItems().setAll(StringHelpers.NO_CHANGES, StringHelpers.ONE_CHANGE, StringHelpers.TWO_CHANGES, StringHelpers.ANY_CHANGE);
		maxChangesSelector.setValue(StringHelpers.NO_CHANGES);

		vf = new ValidationFacade();
		DateInputValidator fromDateValidator = new DateInputValidator(departureDate, returnDate);
		fromDateValidator.setSrcControl(departureDate);
		fromDateValidator.setErrorStyleClass("departureDate-error");
		DateInputValidator toDateValidator = new DateInputValidator(departureDate, returnDate);
		toDateValidator.setSrcControl(returnDate);
		toDateValidator.setErrorStyleClass("departureDate-error");
		vf.setValidators(fromDateValidator, toDateValidator);
		cancelButton.setOnMouseClicked((e)->{
			dialog.close();
		});
		disablePastDateSelection();
	}

	private void disablePastDateSelection() {
		Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);

				if(item.isBefore(LocalDate.now()))
				{
					Platform.runLater(() -> setDisable(true));
				}
			}
		};
		returnDate.setDayCellFactory(dayCellFactory);
		departureDate.setDayCellFactory(dayCellFactory);
	}

	public void initAutoCompleteSearch() {
		autoCompleteAirportSearchFrom =  new AutoCompleteAirportSearch(fromInput, airports);
		autoCompleteAirportSearchTo =  new AutoCompleteAirportSearch(toInput, airports);
		AutoCompletionTextFieldBinding autoCompleteFromAirport = new AutoCompletionTextFieldBinding(fromInput, autoCompleteAirportSearchFrom);
		AutoCompletionTextFieldBinding autoCompleteToAirport = new AutoCompletionTextFieldBinding(toInput, autoCompleteAirportSearchTo);
	}

	private void addInputFieldValidator(JFXTextField inputField) {
		RequiredFieldValidator toValidator = new RequiredFieldValidator();
		toValidator.setMessage("Pole nie może być puste");
		toValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
			.size("2em")
			.style("-fx-fill: red;")
			.build());
		inputField.getValidators().add(toValidator);
		inputField.focusedProperty().addListener((o,oldVal,newVal)->{
			if(!newVal) inputField.validate();
		});
	}

	private void unfocusInputFields() {
		toInput.setFocusTraversable(false);
		fromInput.setFocusTraversable(false);
	}
	@FXML
	public void searchFromParser(ActionEvent actionEvent) {
		dialog.close();
		request = getCurrentJourneyRequest();
		ParserSearchTask task = new ParserSearchTask(rootPane, resultTableBuilder, request);
		task.setAirports(airports);
		task.setAirlines(airlines);

		Thread parserThread = new Thread(task);
		parserThread.start();
	}


	@FXML
    public void searchAction(ActionEvent actionEvent) {
		forceValidation();
		boolean hasErrors = false;
		for (ValidatorBase validatorBase : vf.getValidators()) {
			validatorBase.validate();
			if (validatorBase.getHasErrors()){
				errorLabel.setVisible(true);
				errorLabel.setText(validatorBase.getMessage());
				hasErrors = true;
			}
		}

		if (hasValidationErrors() || hasErrors) {
			return;
		} else {
			errorLabel.setText("");
			errorLabel.setVisible(false);
		}
		request = getCurrentJourneyRequest();
        SearchTask task = new DatabaseSearchTask(rootPane, resultTableBuilder, request, stackPane, dialog);
        Thread parserThread = new Thread(task);
		parserThread.start();

	}

	private JourneyRequest getCurrentJourneyRequest() {
		SearchLimits searchLimits = addSearchLimits();
		LocalDate goingThereLocalDate = departureDate.getValue();
		LocalDate comingBackLocalDate = returnDate.getValue();
		String airportFrom = getAirportByName(autoCompleteAirportSearchFrom, fromInput.getText()).getAirportCode();
		String airportTo = getAirportByName(autoCompleteAirportSearchTo, toInput.getText()).getAirportCode();

		Date goingThereDate =
			Date.from(goingThereLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date comingBackDate= null;
		if (comingBackLocalDate!= null) {
			comingBackDate =
				Date.from(comingBackLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}

		return new JourneyRequest(goingThereDate, comingBackDate, searchLimits, airportFrom, airportTo);
	}

	private SearchLimits addSearchLimits() {
		SearchLimits searchLimits = new SearchLimits();
		
		searchLimits.setMaxChanges(StringHelpers.getNumberOfChanges(maxChangesSelector.getValue()));
		
		if (maxPriceCheckbox.isSelected()) {
			searchLimits.setPrice(moneyFromSliderValue(maxPriceSlider.getValue()));
		}
		if (flightDurationCheckbox.isSelected()) {
			searchLimits.setMaxDuration(timeFromSliderValue(flightDurationSlider.getValue()));
		}
		return searchLimits;
	}
	
	private Time timeFromSliderValue(Double sliderValue) {
		int hours = sliderValue.intValue();
		return new Time(hours, 0);
	}
	
	private Money moneyFromSliderValue(Double sliderValue) {
		int amount = sliderValue.intValue();
		return Money.of(CurrencyUnit.of("PLN"), amount);
	}

	private boolean hasValidationErrors() {
		return !isInputValid(fromInput) || !isInputValid(toInput) || departureDate.getValue() == null
			|| !isAirportNameValid(fromInput, autoCompleteAirportSearchFrom) || !isAirportNameValid(toInput, autoCompleteAirportSearchTo);
	}

	private Airport getAirportByName(AutoCompleteAirportSearch autoCompleteAirportSearch, String airportName) {
		return autoCompleteAirportSearch.getAirportList().stream().filter( a -> a.getAirportName().toLowerCase().contains(airportName.toLowerCase() )
			|| a.toString().equals(airportName)).findFirst().orElse(null);
	}

	private void forceValidation() {
		fromInput.requestFocus();
		toInput.requestFocus();
		searchButton.requestFocus();
	}

	private boolean isInputValid(JFXTextField textField) {
		return !textField.getValidators().stream().anyMatch(ValidatorBase::getHasErrors);
	}

	private boolean isAirportNameValid(JFXTextField textField, AutoCompleteAirportSearch autoCompleteAirportSearch) {
		return getAirportByName(autoCompleteAirportSearch, textField.getText()) != null;
	}

	@FXML
	public void toggleFlightDurationSlider(ActionEvent event) {
		if (flightDurationCheckbox.isSelected()) {
			lowerHbox.getChildren().add(1, flightDurationSlider);
		} else {
			lowerHbox.getChildren().remove(flightDurationSlider);
		}
	}

	@FXML
	public void toggleMaxPriceSlider(ActionEvent event) {
		if (maxPriceCheckbox.isSelected()) {
			upperHbox.getChildren().add(maxPriceSlider);
		} else {
			upperHbox.getChildren().remove(maxPriceSlider);
		}
	}

	public void setAirports(Map<String, String> airports) {
		this.airports = airports;
	}

	public void setAirlines(Map<String, String> airlines) {
		this.airlines = airlines;
	}

	public void setDialogErrorHandler(DialogErrorHandler dialogErrorHandler) {
		this.dialogErrorHandler = dialogErrorHandler;
	}
}
