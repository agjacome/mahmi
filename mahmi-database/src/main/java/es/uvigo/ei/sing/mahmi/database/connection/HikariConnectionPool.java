package es.uvigo.ei.sing.mahmi.database.connection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Getter
@NoArgsConstructor(staticName = "hikariCP")
public final class HikariConnectionPool implements ConnectionPool {

    private final HikariDataSource dataSource = new HikariDataSource(configure());

    private HikariConfig configure() {
        val hikari = new HikariConfig();
        val config = ConfigFactory.load("database");

        hikari.setMaximumPoolSize(config.getInt("maxPoolSize"));
        hikari.setDataSourceClassName(config.getString("datasource"));

        hikari.addDataSourceProperty("serverName"  , config.getString("connection.host"));
        hikari.addDataSourceProperty("port"        , config.getString("connection.port"));
        hikari.addDataSourceProperty("databaseName", config.getString("connection.database"));
        hikari.addDataSourceProperty("user"        , config.getString("connection.user"));
        hikari.addDataSourceProperty("password"    , config.getString("connection.password"));

        return hikari;
    }

}
