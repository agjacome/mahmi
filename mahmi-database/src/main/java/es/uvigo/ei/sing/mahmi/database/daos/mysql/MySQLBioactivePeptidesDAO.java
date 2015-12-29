package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.SQLException;

import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.function.Try0;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.BioactivePeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;

import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

@Slf4j
@RequiredArgsConstructor(staticName = "mysqlBioactivePeptidesDAO")
public class MySQLBioactivePeptidesDAO implements BioactivePeptidesDAO {

    private final ConnectionPool connectionPool;

    @Override
    public void insert(
        final SHA1   referenceHash,
        final SHA1   peptideHash,
        final double percentage
    ) throws DAOException {
        val ref = referenceHash.asHexString();
        val cmp = peptideHash.asHexString();

        val select = sql(
            "SELECT bioactive_similarity FROM bioactive_peptides " +
            "WHERE reference_hash = ? AND peptide_hash = ? ",
            ref, cmp
        ).bind(query).bind(getWith(
            res -> res.getDouble("bioactive_similarity")
        )).map(List::toOption);

        val insert = sql(
            "INSERT INTO bioactive_peptides(reference_hash, peptide_hash, bioactive_similarity) VALUES(?, ?, ?)",
            ref, cmp
        ).bind(doubleFloat(3, percentage)).bind(updateNoKeys);

        write(select.bind(opt -> opt.option(
            insert, d -> DB.unit(Unit.unit())
        )));
    }

    protected final <B> B write(final DB<B> db) throws DAOException {
        return action(() -> runReadWrite(db, connectionPool));
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
