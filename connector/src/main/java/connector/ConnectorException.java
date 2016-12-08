package connector;

public class ConnectorException extends RuntimeException {
	private String serverMessage;
	
	public ConnectorException(String serverMessage) {
		super(serverMessage);
		this.serverMessage = serverMessage;
	}
	
	public ConnectorException(String serverMessage, String message) {
		super(message);
		this.serverMessage = serverMessage;
	}
	
	public ConnectorException(String serverMessage, String message, Throwable cause) {
		super(message, cause);
		this.serverMessage = serverMessage;
	}
	
	public ConnectorException(String serverMessage, Throwable cause) {
		super(cause);
		this.serverMessage = serverMessage;
	}
	
	public ConnectorException(String serverMessage, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.serverMessage = serverMessage;
	}
	
	public String getServerMessage() {
		return serverMessage;
	}
}
