package es.uvigo.ei.sing.mahmi.locator;

import static es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher.serverLauncher;
import static es.uvigo.ei.sing.mahmi.locator.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.locator.server.LocatorServer.locatorServer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class LocatorLauncher {

    public static void main(final String [ ] args) {
        val locator  = locatorServer(configuration());
        val launcher = serverLauncher();

        launcher.launch(locator.getServer());
    }

}
