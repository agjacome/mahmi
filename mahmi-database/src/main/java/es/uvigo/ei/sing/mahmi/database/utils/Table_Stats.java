package es.uvigo.ei.sing.mahmi.database.utils;

import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.integer;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.longInt;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.runReadWrite;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import fj.control.db.DB;
import fj.function.Try0;

@Slf4j
@AllArgsConstructor(staticName = "tableStats")
public class Table_Stats {
    

    protected final ConnectionPool connectionPool;
    
    public void updateStats (final int table_id, final long count){
          val sql= prepareUpdate(table_id, count);
          val statement = sql.bind(update);
          write(statement);          
    }
    
    private DB<PreparedStatement> prepareUpdate(final int table_id, final long count) {
        return prepare(
            "UPDATE table_stats (table_counter) VALUES(?) WHERE table_stats_id=?"
                ).bind(longInt(1, count)).bind(integer(2,table_id));
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
