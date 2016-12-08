package searchApp.builders.rowBuilders;

import javafx.scene.control.TreeItem;
import searchApp.model.FlightRow;

/**
 * Created by Mateusz-PC on 15.10.2016.
 */
public interface TableFiller {

	void fill(TreeItem<FlightRow> parent);

	default TreeItem<FlightRow> getFlightParent(TreeItem<FlightRow> parent, String name) {
		TreeItem<FlightRow> treeItem;
		FlightRow returnRow = new FlightRow.FlightRowBuilder().airportFrom(name)
			.build();
		treeItem = new TreeItem<>(returnRow);
		treeItem.setExpanded(true);
		parent.getChildren().add(treeItem);
		return treeItem;
	}
}
