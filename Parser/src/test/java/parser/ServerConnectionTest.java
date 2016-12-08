package parser;

import com.google.gson.JsonObject;
import connector.ParametersBuilder;
import connector.ServerConnector;
import connector.Service;
import model.Airport;
import model.GoogleClass;
import org.junit.Test;

import java.util.List;

public class ServerConnectionTest {
	
	@Test
	public void getListOfAirports() {
		List<Airport> airports = ServerConnector.executeGET(Service.GET_LIST_OF_AIRPORTS, "", Airport.getType());
		for (Airport airport : airports) {
			System.out.println(airport.toString());
		}
	}
	
	@Test
	public void getListOfClasses() {
		List<GoogleClass> classes = ServerConnector.executeGET(Service.GET_LIST_OF_CLASSES, "", GoogleClass.getType());
		for (GoogleClass googleClass : classes) {
			System.out.println(googleClass.getName() + ": " + googleClass.getClassName());
		}
	}
	
	@Test
	public void putNewClass() {
		JsonObject parameters = new ParametersBuilder()
			.setParameter("name", "DUPA")
			.setParameter("class", "KLASA-DUPA")
			.build();
		
		ServerConnector.executePOST(Service.ADD_CLASS, parameters);
	}
	
}
