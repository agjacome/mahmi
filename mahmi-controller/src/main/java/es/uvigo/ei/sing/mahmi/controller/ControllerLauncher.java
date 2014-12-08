package es.uvigo.ei.sing.mahmi.controller;

import static es.uvigo.ei.sing.mahmi.common.services.ServiceServerLauncher.serverLauncher;
import static es.uvigo.ei.sing.mahmi.controller.Configuration.configuration;
import static es.uvigo.ei.sing.mahmi.controller.server.ControllerServer.controllerServer;
import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ControllerLauncher {

    public static void main(final String [ ] args) {
        val controller = controllerServer(configuration(), hikariCP());
        val launcher   = serverLauncher();

        launcher.launch(controller.getServer());
    }

}
