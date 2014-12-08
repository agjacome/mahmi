package es.uvigo.ei.sing.mahmi.controller.server;

import static es.uvigo.ei.sing.mahmi.controller.controllers.CutterServiceController.cutterServiceController;
import static es.uvigo.ei.sing.mahmi.controller.controllers.LoaderServiceController.loaderServiceController;
import static es.uvigo.ei.sing.mahmi.controller.services.DigestionService.digestionService;
import static es.uvigo.ei.sing.mahmi.controller.services.EnzymeService.enzymeService;
import static es.uvigo.ei.sing.mahmi.controller.services.MetaGenomeService.metaGenomeService;
import static es.uvigo.ei.sing.mahmi.controller.services.PeptideService.peptideService;
import static es.uvigo.ei.sing.mahmi.controller.services.ProjectService.projectService;
import static es.uvigo.ei.sing.mahmi.controller.services.ProteinService.proteinService;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLDigestionsDAO.mysqlDigestionsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLEnzymesDAO.mysqlEnzymesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomesDAO.mysqlMetaGenomesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLPeptidesDAO.mysqlPeptidesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProjectsDAO.mysqlProjectsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProteinsDAO.mysqlProteinsDAO;

import java.util.Set;

import javax.ws.rs.core.Application;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import es.uvigo.ei.sing.mahmi.controller.Configuration;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class ControllerServerApplication extends Application {

    private final ConnectionPool pool;
    private final Configuration  config;

    public static ControllerServerApplication controllerApplication(
        final ConnectionPool pool, final Configuration config
    ) {
        return new ControllerServerApplication(pool, config);
    }

    @Override
    public Set<Object> getSingletons() {
        return Sets.newHashSet(
            digestionService(mysqlDigestionsDAO(pool), cutterServiceController(config)),
            enzymeService(mysqlEnzymesDAO(pool)),
            metaGenomeService(mysqlMetaGenomesDAO(pool)),
            peptideService(mysqlPeptidesDAO(pool)),
            projectService(mysqlProjectsDAO(pool), loaderServiceController(config)),
            proteinService(mysqlProteinsDAO(pool))
        );
    }

}
