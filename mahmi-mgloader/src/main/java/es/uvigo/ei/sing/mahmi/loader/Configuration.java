package es.uvigo.ei.sing.mahmi.loader;

import static javax.ws.rs.core.UriBuilder.fromUri;

import java.net.URI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Configuration {

    private final Config locatorConfig = ConfigFactory.load("locator").getConfig("locator");

    public static Configuration configuration() {
        return new Configuration();
    }

    public URI getLocatorURI() {
        return fromUri("http://{host}:{port}/{path}").build(
            locatorConfig.getString("host"),
            locatorConfig.getInt("port"),
            locatorConfig.getString("path")
        );
    }

}
