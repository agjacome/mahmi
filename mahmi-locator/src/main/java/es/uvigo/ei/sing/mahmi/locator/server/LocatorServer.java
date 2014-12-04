package es.uvigo.ei.sing.mahmi.locator.server;

import static es.uvigo.ei.sing.mahmi.common.services.GrizzlyServer.grizzlyServer;
import static es.uvigo.ei.sing.mahmi.locator.server.LocatorServerApplication.locatorApplication;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.services.ServiceServer;
import es.uvigo.ei.sing.mahmi.locator.Configuration;

@Getter
public final class LocatorServer {

    private final ServiceServer server;

    private LocatorServer(final Configuration config) {
        this.server = grizzlyServer(config.getLocatorURI(), locatorApplication(config));
    }

    public static LocatorServer locatorServer(final Configuration config) {
        return new LocatorServer(config);
    }

}
