package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.SQLException;

import fj.Ord;
import fj.control.db.DB;
import fj.data.Set;
import fj.function.Try0;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ReferencePeptidesDAO;

import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

@Slf4j
@ExtensionMethod(IterableExtensionMethods.class)
@RequiredArgsConstructor(staticName = "mysqlReferencePeptidesDAO")
public class MySQLReferencePeptidesDAO implements ReferencePeptidesDAO {

    private static final Ord<AminoAcidSequence> ordering = SHA1.ord.contramap(AminoAcidSequence::getSHA1);

    private final ConnectionPool connectionPool;

    @Override
    public Set<AminoAcidSequence> getAll() throws DAOException {
        val sql = prepare(
            "SELECT reference_sequence " +
            "FROM reference_peptides "   +
            "ORDER BY reference_id"
        );

        val statement = sql.bind(query).bind(getWith(
            res -> parseAASequence(res, "reference_sequence")
        ));

        return read(statement).toSet(ordering);
    }

    private final <B> B read(final DB<B> db) throws DAOException {
        return action(() -> runReadOnly(db, connectionPool));
    }

    private <B> B action(
        final Try0<B, SQLException> action
    ) throws DAOException {
        try {
            return action.f();
        } catch (final SQLException sqe) {
            if (isMySQLDeadlock(sqe))
                return retryDeadlockedAction(action);
            throw DAOException.withCause(sqe);
        }
    }

    private boolean isMySQLDeadlock(final SQLException exception) {
        val message = exception.getMessage();
        return message.contains("Deadlock found")
            || message.contains("Lock wait timeout exceeded");
    }

    private <B> B retryDeadlockedAction(
        final Try0<B, SQLException> action
    ) throws DAOException {
        log.error("MySQL InnoDB deadlock. Retrying in 200ms.");

        try {
            Thread.sleep(200L);
        } catch (final InterruptedException ie) {
            log.error("Error while waiting to retry: {}", ie.getMessage());
            throw DAOException.withCause(ie);
        }

        return action(action);
    }

}
