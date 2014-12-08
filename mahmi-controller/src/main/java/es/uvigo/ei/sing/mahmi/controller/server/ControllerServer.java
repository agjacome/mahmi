package es.uvigo.ei.sing.mahmi.controller.server;

import static es.uvigo.ei.sing.mahmi.common.services.GrizzlyServer.grizzlyServer;
import static es.uvigo.ei.sing.mahmi.controller.server.ControllerServerApplication.controllerApplication;
import static javax.ws.rs.client.ClientBuilder.newClient;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import lombok.Getter;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.services.ServiceServer;
import es.uvigo.ei.sing.mahmi.common.utils.Location;
import es.uvigo.ei.sing.mahmi.controller.Configuration;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;

@Getter
public final class ControllerServer {

    private final ServiceServer server;

    public ControllerServer(final Configuration config, final ConnectionPool pool) {
        this.server = grizzlyServer(loadServiceURI(config), controllerApplication(pool, config));
    }

    public static ControllerServer controllerServer(final Configuration config, final ConnectionPool pool) {
        return new ControllerServer(config, pool);
    }

    private URI loadServiceURI(final Configuration config) {
        val target   = newClient().target(config.getLocatorURI()).path("/controller");
        val location = target.request(MediaType.APPLICATION_XML).get(Location.class);

        return location.getUri();
    }

}
