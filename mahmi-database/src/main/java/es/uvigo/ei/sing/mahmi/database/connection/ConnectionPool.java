package es.uvigo.ei.sing.mahmi.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.val;
import fj.control.db.Connector;

public interface ConnectionPool {

    public DataSource getDataSource();

    public default Connection getConnection() throws SQLException {
        val connection = getDataSource().getConnection();

        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        return connection;
    }

    public default Connector getConnector() {
        return new Connector() {
            @Override
            public Connection connect() throws SQLException {
                return getConnection();
            }
        };
    }

}
