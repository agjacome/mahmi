package es.uvigo.ei.sing.mahmi.http;

import static es.uvigo.ei.sing.mahmi.cutter.ProteinCutter.proteinCutter;
import static es.uvigo.ei.sing.mahmi.cutter.ProteinCutterController.proteinCutterCtrl;
import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLDigestionsDAO.mysqlDigestionsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLEnzymesDAO.mysqlEnzymesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomeProteinsDAO.mysqlMetaGenomeProteinsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomesDAO.mysqlMetaGenomesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLPeptidesDAO.mysqlPeptidesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProjectsDAO.mysqlProjectsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProteinsDAO.mysqlProteinsDAO;
import static es.uvigo.ei.sing.mahmi.database.utils.Table_Stats.tableStats;
import static es.uvigo.ei.sing.mahmi.http.services.DigestionService.digestionService;
import static es.uvigo.ei.sing.mahmi.http.services.EnzymeService.enzymeService;
import static es.uvigo.ei.sing.mahmi.http.services.MetaGenomeProteinsService.metaGenomeProteinsService;
import static es.uvigo.ei.sing.mahmi.http.services.MetaGenomeService.metaGenomeService;
import static es.uvigo.ei.sing.mahmi.http.services.PeptideService.peptideService;
import static es.uvigo.ei.sing.mahmi.http.services.ProjectService.projectService;
import static es.uvigo.ei.sing.mahmi.http.services.ProteinService.proteinService;
import static es.uvigo.ei.sing.mahmi.loader.MGRastProjectLoader.mgRastLoader;
import static es.uvigo.ei.sing.mahmi.loader.ProjectLoaderController.projectLoaderCtrl;

import java.util.Set;

import javax.ws.rs.core.Application;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;

@RequiredArgsConstructor(staticName = "httpApplication")
public final class HttpApplication extends Application {

    private final ConnectionPool connectionPool = hikariCP();

    @Override
    public Set<Object> getSingletons() {
        // FIXME: crazy as hell stuff here, just solve it. Idea: create
        // factories in each module (DAOFactory, LoaderFactory and
        // CutterFactory) that ease the creation of all this stuff. Or if we are
        // nuts enough to do it: use a dependency injection framework (but a
        // small and lightweight one, not fucking Spring).

        val digestionsDAO         = mysqlDigestionsDAO(connectionPool);
        val enzymesDAO            = mysqlEnzymesDAO(connectionPool);
        val metaGenomesDAO        = mysqlMetaGenomesDAO(connectionPool);
        val metaGenomeProteinsDAO = mysqlMetaGenomeProteinsDAO(connectionPool);
        val peptidesDAO           = mysqlPeptidesDAO(connectionPool);
        val projectsDAO           = mysqlProjectsDAO(connectionPool);
        val proteinsDAO           = mysqlProteinsDAO(connectionPool);
        val tableStats            = tableStats(connectionPool);

        val loaderController = projectLoaderCtrl(
            mgRastLoader(),
            projectsDAO,
            metaGenomesDAO,
            proteinsDAO,
            tableStats
        );

        val cutterController = proteinCutterCtrl(
            proteinCutter(),
            metaGenomesDAO,
            proteinsDAO,
            peptidesDAO,
            digestionsDAO,
            tableStats
        );

        return Sets.newHashSet(
            digestionService(digestionsDAO, cutterController),
            enzymeService(enzymesDAO),
            metaGenomeService(metaGenomesDAO),
            metaGenomeProteinsService(metaGenomeProteinsDAO),
            peptideService(peptidesDAO),
            projectService(projectsDAO, loaderController),
            proteinService(proteinsDAO)
        );
    }

}
