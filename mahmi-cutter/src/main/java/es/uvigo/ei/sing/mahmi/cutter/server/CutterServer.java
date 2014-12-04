package es.uvigo.ei.sing.mahmi.cutter.server;

import es.uvigo.ei.sing.mahmi.common.services.ServiceServer;
import es.uvigo.ei.sing.mahmi.common.utils.Location;
import es.uvigo.ei.sing.mahmi.cutter.Configuration;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import lombok.Getter;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;

import static es.uvigo.ei.sing.mahmi.common.services.GrizzlyServer.grizzlyServer;
import static es.uvigo.ei.sing.mahmi.cutter.server.CutterServerApplication.cutterApplication;
import static javax.ws.rs.client.ClientBuilder.newClient;

@Getter
public final class CutterServer {

    private final ServiceServer server;

    private CutterServer(final Configuration config, final ConnectionPool pool) {
        this.server = grizzlyServer(loadServiceURI(config), cutterApplication(pool));
    }

    public static CutterServer cutterServer(final Configuration config, final ConnectionPool pool) {
        return new CutterServer(config, pool);
    }

    private URI loadServiceURI(final Configuration config) {
        final WebTarget target   = newClient().target(config.getLocatorURI()).path("/cutter");
        final Location  location = target.request(MediaType.APPLICATION_XML).get(Location.class);

        return location.getUri();
    }

}
