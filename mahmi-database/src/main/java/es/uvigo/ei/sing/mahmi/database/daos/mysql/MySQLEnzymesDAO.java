package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;

import fj.control.db.DB;
import fj.data.Option;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.EnzymesDAO;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

public final class MySQLEnzymesDAO extends MySQLAbstractDAO<Enzyme> implements EnzymesDAO {

    private MySQLEnzymesDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static EnzymesDAO mysqlEnzymesDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLEnzymesDAO(connectionPool);
    }


    @Override
    public Option<Enzyme> getByName(final String name) throws DAOException {
        val sql = prepareSelect(enzyme(name)).bind(query).bind(get);
        return read(sql).toOption();
    }

    @Override
    protected Enzyme parse(final ResultSet results) throws SQLException {
        return enzyme(parseIdentifier(results, "enzyme_id"),
        			  parseString(results, "enzyme_name"));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Enzyme enzyme) {
        return sql("SELECT * FROM enzymes WHERE enzyme_name = ? LIMIT 1", enzyme.getName());
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql("SELECT * FROM enzymes WHERE enzyme_id = ? LIMIT 1", id);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT * FROM enzymes ORDER BY enzyme_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(enzyme_id) FROM enzymes");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Enzyme enzyme) {
        val name = enzyme.getName();
        return sql("INSERT INTO enzmyes (enzyme_name) VALUES (?)", name);
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM enzymes WHERE enzyme_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Enzyme enzyme) {
        return sql(
            "UPDATE enzymes SET enzyme_name = ? WHERE enzyme_id = ?", enzyme.getName()
        ).bind(identifier(2, enzyme.getId()));
    }

}
