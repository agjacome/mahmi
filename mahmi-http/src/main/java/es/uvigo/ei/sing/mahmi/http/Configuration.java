package es.uvigo.ei.sing.mahmi.http;

import static javax.ws.rs.core.UriBuilder.fromUri;

import java.net.URI;

import lombok.NoArgsConstructor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@NoArgsConstructor(staticName = "configuration")
public final class Configuration {

    private final Config config = ConfigFactory.load("config");

    public URI getServerURI() {
        return fromUri("http://{host}:{port}/{path}").build(
            config.getConfig("server").getString("host"),
            config.getConfig("server").getInt("port"),
            config.getConfig("server").getString("path")
        );
    }

}
