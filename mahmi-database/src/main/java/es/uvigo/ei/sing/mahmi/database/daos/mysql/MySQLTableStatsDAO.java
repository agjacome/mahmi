package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.longInt;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.update;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.TableStat;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.TableStatsDAO;
import fj.control.db.DB;

@ExtensionMethod(IterableExtensionMethods.class)
public class MySQLTableStatsDAO extends MySQLAbstractDAO<TableStat> implements TableStatsDAO{
    
    private MySQLTableStatsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static TableStatsDAO mysqlTableStatsDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLTableStatsDAO(connectionPool);
    }
    
    @Override
    public void update (final TableStat tableStat)throws DAOException{
        val sql= prepareUpdate(tableStat);
        val statement = sql.bind(update);
        write(statement);          
  }
  /*
    @Override
    public Option<TableStat> get() throws DAOException{
        val sql = prepare("SELECT * FROM table_stats");
        return read(sql).toOption();
    }*/
    
    @Override
    protected DB<PreparedStatement> prepareUpdate(final TableStat tableStat) {
        return prepare(
                "UPDATE table_stats (table_counter) VALUES(?) WHERE table_stats_id=?"
            ).bind(longInt(1,tableStat.getCounter())).bind(identifier(2, tableStat.getId()));        
    }   
  
  
    @Override
    protected TableStat parse(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(TableStat entity) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareCount() {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(Identifier id) {
        return sql(
                "SELECT * FROM table_stats WHERE table_stats_id=?",
                id
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(int limit, int offset) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareInsert(TableStat entity) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareDelete(Identifier id) {
        throw new UnsupportedOperationException("Not valid operation");
    }
   
}
