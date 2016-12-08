package connector;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerConnector {
	private static final Logger log = LoggerFactory.getLogger(ServerConnector.class);
	private static Properties properties;
	private static Gson gson;
	
	private static final String EXECUTION_LOG_TEMPLATE = "Sending request to {} using {} method";
	
	static {
		properties = ConfigFactory.create(Properties.class);
		gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	}
	
	private ServerConnector() {
	}
	
	public static <T> List<T> executeGET(Service service, String urlParams, Type type) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String url = prepareUrl(service, urlParams);
		AsyncHttpClient.BoundRequestBuilder boundRequestBuilder = asyncHttpClient.prepareGet(url);
		
		log.info(EXECUTION_LOG_TEMPLATE, url, "GET");
		Future<List<T>> collectionFuture = boundRequestBuilder.execute(new AsyncCompletionHandler<List<T>>() {
			@Override
			public List<T> onCompleted(Response response) throws Exception {
				try {
					return gson.fromJson(response.getResponseBody(), type);
				}
				catch (Exception e) {
					return Collections.emptyList();
				}
			}
		});
		
		return executeFutureTask(collectionFuture, asyncHttpClient);
	}

	public static <T> Map<T, T> executeGETToMap(Service service, String urlParams, Type type) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String url = prepareUrl(service, urlParams);
		AsyncHttpClient.BoundRequestBuilder boundRequestBuilder = asyncHttpClient.prepareGet(url);

		log.info(EXECUTION_LOG_TEMPLATE, url, "GET");
		Future<Map<T, T>> collectionFuture = boundRequestBuilder.execute(new AsyncCompletionHandler<Map<T, T>>() {
			@Override
			public Map<T, T> onCompleted(Response response) throws Exception {
				try {
					return gson.fromJson(response.getResponseBody(), type);
				}
				catch (Exception e) {
					return Collections.emptyMap();
				}
			}
		});

		return executeFutureTask(collectionFuture, asyncHttpClient);
	}

	public static String executePOST(Service service, JsonElement parameters) {
		return executeRequest(ReqestMethod.POST, service, parameters);
	}
	
	public static String executePUT(Service service, JsonElement parameters) {
		return executeRequest(ReqestMethod.PUT, service, parameters);
	}
	
	public static String executeDELETE(Service service, JsonElement parameters) {
		return executeRequest(ReqestMethod.DELETE, service, parameters);
	}
	
	private static String prepareUrl(Service service, String urlParams) {
		String url = properties.urlBase() + service.getUrlPart();
		if (StringUtils.isNotBlank(urlParams)) {
			url += urlParams;
		}
		return url;
	}
	
	private static <T> T executeFutureTask(Future<T> execution, AsyncHttpClient client) {
		T result = null;
		try {
			result = execution.get();
		}
		catch (InterruptedException | ExecutionException e) {
			log.error("Error while sending request", e);
		}
		finally {
			client.closeAsynchronously();
		}
		return result;
	}
	
	private static String executeRequest(ReqestMethod reqestMethod, Service service, JsonElement parameters) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String url = prepareUrl(service, "");
		AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
		
		try {
			Method requestMethodReflection = asyncHttpClient.getClass().getMethod(reqestMethod.getMethodJavaName(), String.class);
			boundRequestBuilder = (AsyncHttpClient.BoundRequestBuilder) requestMethodReflection.invoke(asyncHttpClient, url);
		}
		catch (Exception e) {
			throw new IllegalStateException("Error while executing '" + reqestMethod.getMethodJavaName() + "' with reflection", e);
		}
		
		log.info(EXECUTION_LOG_TEMPLATE, url, reqestMethod);
		Future<Response> execution = boundRequestBuilder.setHeader("Content-Type", "application/json")
			.setBody(parameters != null ? parameters.toString() : null)
			.execute(new AsyncCompletionHandler<Response>() {
				@Override
				public Response onCompleted(Response response) throws Exception {
					return response;
				}
			});
		
		Response response = executeFutureTask(execution, asyncHttpClient);
		try {
			validateResponse(response, service);
			return response.getResponseBody();
		}
		catch (IOException e) {
			throw new ConnectorException("Błąd podczas pobierania zwrotki z serwera", e);
		}
	}
	
	/**
	 * Waliduje odpowiedź z serwera - jeśli kod response nie zaczyna się od '2', oznacza to błąd
	 */
	private static void validateResponse(Response response, Service service) throws IOException {
		String responseCodeStr = "" + response.getStatusCode();
		if (responseCodeStr.charAt(0) != '2') {
			int responseCode = response.getStatusCode();
			String responseBody = response.getResponseBody();
			JsonReader jsonReader = new JsonReader(new StringReader(responseBody));
			jsonReader.setLenient(true);
			JsonElement rootElement = new JsonParser().parse(jsonReader);
			
			String msgPrefix = service + " service execution failed (" + responseCode + ")";
			if (rootElement.isJsonPrimitive()) {
				throw new ConnectorException(rootElement.getAsJsonPrimitive().toString(), msgPrefix + ": " + rootElement.getAsJsonPrimitive().toString());
			}
			else if (rootElement.isJsonObject()) {
				JsonObject rootObject = rootElement.getAsJsonObject();
				JsonElement error = rootObject.get("error");
				if (error != null) {
					throw new ConnectorException(error.getAsString(), msgPrefix + ": " + error.getAsString());
				}
				else {
					throw new ConnectorException("Nieznany błąd", msgPrefix);
				}
			}
		}
	}
	
	private enum ReqestMethod {
		GET("prepareGet"),
		POST("preparePost"),
		PUT("preparePut"),
		DELETE("prepareDelete");
		
		String methodJavaName;
		
		ReqestMethod(String methodJavaName) {
			this.methodJavaName = methodJavaName;
		}
		
		public String getMethodJavaName() {
			return methodJavaName;
		}
	}
	
}
