package es.uvigo.ei.sing.mahmi.cutter;

import static es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher.serverLauncher;
import static es.uvigo.ei.sing.mahmi.cutter.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.cutter.server.CutterServer.cutterServer;
import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CutterLauncher {

    public static void main(final String [ ] args) {
        val cutter = cutterServer(configuration(), hikariCP());
        val launcher = serverLauncher();

        launcher.launch(cutter.getServer());
    }

}
