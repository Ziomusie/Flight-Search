package connector;

import com.google.gson.JsonObject;

public class ParametersBuilder {
	
	private JsonObject parameters;
	
	public ParametersBuilder() {
		parameters = new JsonObject();
	}
	
	public ParametersBuilder setParameter(String name, String value) {
		parameters.addProperty(name, value);
		return this;
	}
	
	public ParametersBuilder setParameter(String name, Boolean value) {
		parameters.addProperty(name, value);
		return this;
	}
	
	public ParametersBuilder setParameter(String name, Number value) {
		parameters.addProperty(name, value);
		return this;
	}
	
	public ParametersBuilder setParameter(String name, Character value) {
		parameters.addProperty(name, value);
		return this;
	}
	
	public JsonObject build() {
		return parameters;
	}
	
}
