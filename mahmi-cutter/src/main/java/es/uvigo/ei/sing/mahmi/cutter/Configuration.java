package es.uvigo.ei.sing.mahmi.cutter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;

import static javax.ws.rs.core.UriBuilder.fromUri;

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
