package parser.utils;

import model.Flight;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class TableBuilder {
	
	private TableBuilder() {}
	
	private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
    private static List<String[]> rows = new LinkedList<String[]>();

	public static void printFlights(List<Flight> flights) {
		addRow("Price", "FlightTime", "AirportTo", "AirportFrom", "TimeDepart", "TimeArrive", "Date", "Airline", "Changes");
		addRow("-----", "----------", "---------", "-----------", "----------", "----------", "----", "--------", "-------");
		for (Flight f : flights) {
			addRow(f.getPrice().toString(), f.getFlightDuration().toString(), f.getAirportTo(), f.getAirportFrom(), f.getFlightHours().getStart().toString(), f.getFlightHours().getEnd().toString(), dateFormat.format(f.getDate()), f.getAirline(), "" + f.getChangesCount());
		}
		System.out.println(asString().trim());
		rows.clear();
	}
	
    private static void addRow(String... cols) {
        rows.add(cols);
    }

    private static int[] colWidths() {
        int cols = -1;
        for (String[] row : rows)
            cols = Math.max(cols, row.length);
        int[] widths = new int[cols];
        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] =
                        Math.max(widths[colNum], StringUtils.length(row[colNum]));
            }
        }
        return widths;
    }
	
    private static String asString() {
        StringBuilder buf = new StringBuilder("\n");
        int[] colWidths = colWidths();
        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                buf.append(
                        StringUtils.rightPad(
                                StringUtils.defaultString(
                                        row[colNum]), colWidths[colNum]));
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

}