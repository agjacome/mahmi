package es.uvigo.ei.sing.mahmi.common.services;

import static fj.data.IOFunctions.stdinReadLine;

import java.io.IOException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(staticName = "serverLauncher")
public final class ServiceServerLauncher {

    // FIXME: block execution (or not) in a proper way (not by waiting on stdin)
    public void launch(final ServiceServer server) {
        try (final ServiceServer srv = server.start()) {
            stdinReadLine().run();
        } catch (final IOException ioe) {
            log.error("Error at service grizzlyServer operation:", ioe);
        }
    }

}
