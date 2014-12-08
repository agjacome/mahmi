package es.uvigo.ei.sing.mahmi.locator;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Configuration {

    private final Config     config  = ConfigFactory.load("services");
    private final UriBuilder builder = UriBuilder.fromUri("http://{host}:{port}/{path}");

    public static Configuration configuration() {
        return new Configuration();
    }

    public URI getLocatorURI() {
        return build(config.getConfig("locator"));
    }

    public URI getControllerURI() {
        return build(config.getConfig("controller"));
    }

    public URI getLoaderURI() {
        return build(config.getConfig("loader"));
    }

    public URI getCutterURI() {
        return build(config.getConfig("cutter"));
    }

    private URI build(final Config cfg) {
        return builder.build(cfg.getString("host"), cfg.getInt("port"), cfg.getString("path"));
    }

}
