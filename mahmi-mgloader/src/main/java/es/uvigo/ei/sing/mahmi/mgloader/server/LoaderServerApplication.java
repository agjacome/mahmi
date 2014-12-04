package es.uvigo.ei.sing.mahmi.mgloader.server;

import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomesDAO.mysqlMetaGenomesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProjectsDAO.mysqlProjectsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProteinsDAO.mysqlProteinsDAO;
import static es.uvigo.ei.sing.mahmi.mgloader.loaders.MGRastProjectLoader.mgRastProjectLoader;
import static es.uvigo.ei.sing.mahmi.mgloader.loaders.ProjectLoaderController.projectLoaderCtrl;
import static es.uvigo.ei.sing.mahmi.mgloader.service.LoaderService.loaderService;

import java.util.Set;

import javax.ws.rs.core.Application;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class LoaderServerApplication extends Application {

    private final ConnectionPool pool;

    public static LoaderServerApplication loaderApplication(final ConnectionPool pool) {
        return new LoaderServerApplication(pool);
    }

    @Override
    public Set<Object> getSingletons() {
        return Sets.newHashSet(
            loaderService(projectLoaderCtrl(
                mgRastProjectLoader(),
                mysqlProjectsDAO(pool),
                mysqlMetaGenomesDAO(pool),
                mysqlProteinsDAO(pool)
            ))
        );
    }

}
