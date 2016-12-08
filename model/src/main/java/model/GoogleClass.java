package model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Encja odzwierciedlająca nazwy klas na stronie Google Flights
 */
public class GoogleClass {
	
	private Integer id;
	private String name;
	@SerializedName("Class")
	private String className;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public static Type getType() {
		return new TypeToken<ArrayList<GoogleClass>>(){}.getType();
	}
	
}
