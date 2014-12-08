package es.uvigo.ei.sing.mahmi.loader.server;

import static es.uvigo.ei.sing.mahmi.loader.server.LoaderServerApplication.loaderApplication;
import static javax.ws.rs.client.ClientBuilder.newClient;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import lombok.Getter;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.services.GrizzlyServer;
import es.uvigo.ei.sing.mahmi.common.services.ServiceServer;
import es.uvigo.ei.sing.mahmi.common.utils.Location;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.loader.Configuration;

@Getter
public final class LoaderServer {

    private final ServiceServer server;

    private LoaderServer(final Configuration config, final ConnectionPool pool) {
        this.server = GrizzlyServer.grizzlyServer(loadServiceURI(config), loaderApplication(pool));
    }

    public static LoaderServer loaderServer(final Configuration config, final ConnectionPool pool) {
        return new LoaderServer(config, pool);
    }

    private URI loadServiceURI(final Configuration config) {
        val target   = newClient().target(config.getLocatorURI()).path("/loader");
        val location = target.request(MediaType.APPLICATION_XML).get(Location.class);

        return location.getUri();
    }

}
