package es.uvigo.ei.sing.mahmi.common.services;

import static org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory.createHttpServer;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Application;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

public final class GrizzlyServer implements ServiceServer {

    private final HttpServer server;

    private GrizzlyServer(final URI uri, final String ... packages) {
        this.server = createHttpServer(uri, new ResourceConfig().packages(true, packages), false);
    }

    private GrizzlyServer(final URI uri, final Application app) {
        this.server = createHttpServer(uri, ResourceConfig.forApplication(app));
    }

    public static GrizzlyServer server(final URI uri, final String ... packages) {
        return new GrizzlyServer(uri, packages);
    }

    public static GrizzlyServer grizzlyServer(final URI uri, final Application app) {
        return new GrizzlyServer(uri, app);
    }

    @Override
    public boolean isStarted() {
        return server.isStarted();
    }

    @Override
    public ServiceServer start() throws IOException {
        server.start();
        return this;
    }

    @Override
    public ServiceServer stop() {
        server.shutdown();
        return this;
    }

}
