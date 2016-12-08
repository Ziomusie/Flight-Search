package connector;

import model.Airport;
import org.junit.Test;

import java.util.List;

/**
 * Created by tswed on 14.08.2016.
 */
public class ConnectorTest {
	@Test
	public void testFlights() {
		List<Airport> objects = ServerConnector.executeGET(Service.GET_AIRPORT, "?name=A&limit=10", Airport.getType());
		for (Airport a: objects) {
			System.out.println(a.toString());
		}
	}
}
