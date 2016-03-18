package es.uvigo.ei.sing.mahmi.http;

import static javax.ws.rs.core.UriBuilder.fromUri;

import java.net.URI;

import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "configuration")
public final class Configuration {

//    private final Config config = ConfigFactory.load("config");

    public URI getServerURI() {
        return fromUri("http://{host}:{port}/{path}").build(
            "localhost",
            8087,
            "mahmi"
        );
    }

}
