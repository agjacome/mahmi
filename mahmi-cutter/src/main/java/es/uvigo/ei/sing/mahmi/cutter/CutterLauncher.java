package es.uvigo.ei.sing.mahmi.cutter;

import es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher;
import es.uvigo.ei.sing.mahmi.cutter.server.CutterServer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher.serverLauncher;
import static es.uvigo.ei.sing.mahmi.cutter.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.cutter.server.CutterServer.cutterServer;
import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CutterLauncher {

    public static void main(final String [ ] args) {
        final CutterServer cutter = cutterServer(configuration(), hikariCP());
        final ServiceServerLauncher launcher = serverLauncher();

        launcher.launch(cutter.getServer());
    }

}
