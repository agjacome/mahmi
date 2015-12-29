package es.uvigo.ei.sing.mahmi.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

@Getter
@NoArgsConstructor(staticName = "hikariCP")
public final class HikariConnectionPool implements ConnectionPool {

    private final HikariDataSource dataSource = new HikariDataSource(configure());

    private HikariConfig configure() {
        val hikari = new HikariConfig();
        // val config = ConfigFactory.load("database");

        hikari.setMaximumPoolSize(100);                                                 // config.getInt("maxPoolSize");
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"); // config.getString("datasource");

        hikari.addDataSourceProperty("serverName"  , "localhost"); // config.getString("connection.host");
        hikari.addDataSourceProperty("port"        , "3306");      // config.getString("connection.port");
        hikari.addDataSourceProperty("databaseName", "mahmi");     // config.getString("connection.database");
        hikari.addDataSourceProperty("user"        , "mahmiuser"); // config.getString("connection.user");
        hikari.addDataSourceProperty("password"    , "mahmipass"); // config.getString("connection.password");

        return hikari;
    }

}
