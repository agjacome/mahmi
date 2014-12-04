package es.uvigo.ei.sing.mahmi.mgloader;

import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import static es.uvigo.ei.sing.mahmi.mgloader.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.mgloader.server.LoaderServer.loaderServer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class LoaderLauncher {

    public static void main(final String [ ] args) {
        val loader   = loaderServer(configuration(), hikariCP());
        val launcher = ServiceServerLauncher.serverLauncher();

        launcher.launch(loader.getServer());
    }

}
