
package model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by tswed on 22.07.2016.
 */
public class FlightClass {
	public static final String ECONOMIC = "e";
	public static final String ECONOMICPREMIUM = "p";
	public static final String BIZNES = "b";
	public static final String FIRST = "f";

	public static Type getType() {
		return new TypeToken<ArrayList<FlightClass>>() {
		}.getType();
	}

	public static String codeToFullName(String code) {
		if (code.equals(ECONOMIC))
			return "Klasa ekonomiczna";
		else if (code.equals(ECONOMICPREMIUM))
			return "Klasa ekonomiczna premium";
		else if (code.equals(BIZNES))
			return "Klasa biznesowa";
		else if (code.equals(FIRST))
			return "Pierwsza klasa";
		else
			return "";
	}
	public static String fullNameToCode(String fullName) {
		if (fullName.equals("Klasa ekonomiczna"))
			return ECONOMIC;
		else if (fullName.equals("Klasa ekonomiczna premium"))
			return ECONOMICPREMIUM;
		else if (fullName.equals("Klasa biznesowa"))
			return BIZNES;
		else if (fullName.equals("Pierwsza klasa"))
			return FIRST;
		else
			return "";
	}
}