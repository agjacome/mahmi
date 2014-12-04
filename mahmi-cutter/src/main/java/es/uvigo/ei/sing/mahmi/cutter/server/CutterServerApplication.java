package es.uvigo.ei.sing.mahmi.cutter.server;

import com.google.common.collect.Sets;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.ws.rs.core.Application;
import java.util.Set;

import static es.uvigo.ei.sing.mahmi.cutter.cutters.ProteinCutter.proteinCutter;
import static es.uvigo.ei.sing.mahmi.cutter.cutters.ProteinCutterController.proteinCutterCtrl;
import static es.uvigo.ei.sing.mahmi.cutter.service.CutterService.cutterService;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLDigestionsDAO.mysqlDigestionsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLPeptidesDAO.mysqlPeptidesDAO;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CutterServerApplication extends Application {

    private final ConnectionPool pool;

    public static CutterServerApplication cutterApplication(final ConnectionPool pool) {
        return new CutterServerApplication(pool);
    }

    @Override
    public Set<Object> getSingletons() {
        return Sets.newHashSet(
            cutterService(proteinCutterCtrl(proteinCutter(), mysqlPeptidesDAO(pool), mysqlDigestionsDAO(pool)))
        );
    }

}
