package searchApp.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.joda.money.Money;
import searchApp.builders.rowBuilders.TableFiller;
import searchApp.model.FlightRow;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Created by Mateusz-PC on 22.07.2016.
 */
public class ResultTableBuilder {
	private JFXTreeTableView resultTable;

	public static String FLIGHTS = "Loty";

	public static String RETURN_FLIGHTS = "Loty powrotne";

	public ResultTableBuilder(JFXTreeTableView resultTable) {
		this.resultTable = resultTable;

	}

	public void buildTable(JsonArray array) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				resultTable.getColumns().clear();
				resultTable.setVisible(true);
				if (array.size() <= 0 ) {
					return;
				}
				array.get(0).getAsJsonObject().entrySet().forEach(entry -> {
					JFXTreeTableColumn<JsonObject, String> col = new JFXTreeTableColumn<>(entry.getKey());
					col.setPrefWidth(120);
					col.setCellValueFactory((TreeTableColumn.CellDataFeatures<JsonObject, String> param) ->
						new ReadOnlyStringWrapper(param.getValue().getValue().get(entry.getKey()).getAsString())
					);
					resultTable.getColumns().add(col);

				});
				TreeItem<JsonObject> r = new TreeItem<JsonObject>();
				resultTable.setRoot(r);
				resultTable.setShowRoot(false);
				array.forEach(jsonElement -> resultTable.getRoot().getChildren().add(new TreeItem<>(jsonElement.getAsJsonObject()))	);
			}
		});
	}

	public void buildTable() {
		resultTable.setPlaceholder(new Label("Brak wyników"));
		resultTable.setFixedCellSize(24d);
		JFXTreeTableColumn<FlightRow, String> fromColumn = new JFXTreeTableColumn<>("Z");
		fromColumn.setPrefWidth(175);
		fromColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, String> param) ->
			new ReadOnlyStringWrapper(param.getValue().getValue().getAirportFrom())
		);


		JFXTreeTableColumn<FlightRow, String> toColumn = new JFXTreeTableColumn<>("Do");
		toColumn.setPrefWidth(175);
		toColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, String> param) ->
			new ReadOnlyStringWrapper(param.getValue().getValue().getAirportTo())
		);

		JFXTreeTableColumn<FlightRow, LocalDate> dateColumn = new JFXTreeTableColumn<>("Data");
		dateColumn.setPrefWidth(100);
		dateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, LocalDate> param) ->
			new ReadOnlyObjectWrapper<LocalDate>(param.getValue().getValue().getDepartureDate())
		);

		dateColumn.setResizable(false);


		JFXTreeTableColumn<FlightRow, String> departureTimeColumn = new JFXTreeTableColumn<>("Odlot");
		departureTimeColumn.setPrefWidth(120);
		departureTimeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, String> param) ->
			new ReadOnlyStringWrapper(param.getValue().getValue().getDepartureTime())
		);
		departureTimeColumn.setResizable(false);

		JFXTreeTableColumn<FlightRow, String> arrivalTimeColumn = new JFXTreeTableColumn<>("Przylot");
		arrivalTimeColumn.setPrefWidth(120);
		arrivalTimeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, String> param) ->
			new ReadOnlyStringWrapper(param.getValue().getValue().getArrivalTime())
		);
		arrivalTimeColumn.setResizable(false);

		JFXTreeTableColumn<FlightRow, String> operatorColumn = new JFXTreeTableColumn<>("Linia");
		operatorColumn.setPrefWidth(100);
		operatorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, String> param) ->
			new ReadOnlyStringWrapper(param.getValue().getValue().getOperator())
		);
		operatorColumn.setResizable(false);

		JFXTreeTableColumn<FlightRow, Money> priceColumn = new JFXTreeTableColumn<>("Cena");
		priceColumn.setPrefWidth(98);
		priceColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, Money> param) ->
			new ReadOnlyObjectWrapper(param.getValue().getValue().getPrice())
		);
		priceColumn.setResizable(false);

		JFXTreeTableColumn<FlightRow, Hyperlink> buyLink = new JFXTreeTableColumn<>("Zakup");
		buyLink.setPrefWidth(85);
		buyLink.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightRow, Hyperlink> param) ->
			new ReadOnlyObjectWrapper<Hyperlink>(param.getValue().getValue().getLink())
		);
		buyLink.setResizable(false);
		buyLink.setSortable(false);

		prepareRowFactory();

		resultTable.setEditable(false);
		resultTable.setShowRoot(false);
		FlightRow f = new FlightRow.FlightRowBuilder().build();
		final TreeItem<FlightRow> root = new RecursiveTreeItem<FlightRow>(f, RecursiveTreeObject::getChildren);
		resultTable.setRoot(root);
		resultTable.getColumns().setAll(fromColumn, toColumn,
			dateColumn,departureTimeColumn,
			arrivalTimeColumn, operatorColumn,
			priceColumn, buyLink
			);
	}

	private void prepareRowFactory() {
		resultTable.setRowFactory(new Callback<TreeTableView<FlightRow>, TreeTableRow<FlightRow>>() {
			@Override
			public TreeTableRow<FlightRow> call(TreeTableView<FlightRow> tableView) {
				final TreeTableRow<FlightRow> row = new TreeTableRow<FlightRow>() {
					@Override
					protected void updateItem(FlightRow flightRow, boolean empty){
						super.updateItem(flightRow, empty);
						if (flightRow == null) {
							getStyleClass().removeAll(Collections.singleton("highlightedRow"));
							return;
						}
						if (FLIGHTS.equals(flightRow.getAirportFrom()) || RETURN_FLIGHTS.equals(flightRow.getAirportFrom())) {
							if (! getStyleClass().contains("highlightedRow")) {
								getStyleClass().add("highlightedRow");
							}
						} else {
							getStyleClass().removeAll(Collections.singleton("highlightedRow"));
						}
					}
				};
				return row;
			}
		});
	}

	public void fillTable(TableFiller tableFiller){
		resultTable.getRoot().getChildren().clear();
		tableFiller.fill(resultTable.getRoot());
	}

	public JFXTreeTableView getResultTable() {
		return resultTable;
	}

}
