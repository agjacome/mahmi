package es.uvigo.ei.sing.mahmi.http;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Application;

import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import static es.uvigo.ei.sing.mahmi.http.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.http.HttpApplication.httpApplication;
import static es.uvigo.ei.sing.mahmi.http.server.GrizzlyServer.grizzlyServer;

@Slf4j
@NoArgsConstructor(staticName = "launcher")
final class Launcher {

    private final Application app = httpApplication(hikariCP());
    private final URI         uri = configuration().getServerURI();

    public void launch() {
        val server = grizzlyServer(uri, app);
        server.addStopOnShutdownHook();

        try {
            server.start();
            System.out.println("Press Ctrl+C to exit.");
            Thread.currentThread().join();
        } catch (final IOException ioe) {
            log.error("Error while starting HTTP Server", ioe);
        } catch (final InterruptedException ie) {
            log.error("Unexpected HTTP Server interruption", ie);
        }
    }

    public static void main(final String [ ] args) {
        launcher().launch();
    }

}
