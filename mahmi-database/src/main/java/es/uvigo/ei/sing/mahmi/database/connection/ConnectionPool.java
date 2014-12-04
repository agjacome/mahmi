package es.uvigo.ei.sing.mahmi.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import fj.control.db.Connector;

public interface ConnectionPool {

    public DataSource getDataSource();

    public default Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
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
