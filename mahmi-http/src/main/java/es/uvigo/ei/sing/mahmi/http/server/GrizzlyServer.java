package es.uvigo.ei.sing.mahmi.http.server;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Application;

import lombok.extern.slf4j.Slf4j;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

@Slf4j
public final class GrizzlyServer implements Server {

    private final HttpServer server;

    private GrizzlyServer(final URI uri, final Application app) {
        this.server = GrizzlyHttpServerFactory.createHttpServer(
            uri, ResourceConfig.forApplication(app)
        );
    }

    public static Server grizzlyServer(final URI uri, final Application app) {
        return new GrizzlyServer(uri, app);
    }

    @Override
    public void start() throws IOException {
        log.info("Starting Grizzly server...");
        server.start();
    }

    @Override
    public void stop() {
        log.info("Stoping Grizzly server...");
        server.shutdownNow();
    }

}
