package connector;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:connector.properties"})
public interface Properties extends Config {
	
	@Key("url.base")
	String urlBase();
	
}
