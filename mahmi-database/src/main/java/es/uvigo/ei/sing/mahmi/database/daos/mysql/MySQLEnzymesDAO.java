package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.EnzymesDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLEnzymesDAO extends MySQLAbstractDAO<Enzyme> implements EnzymesDAO {

    private MySQLEnzymesDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static MySQLEnzymesDAO mysqlEnzymesDAO(final ConnectionPool pool) {
        return new MySQLEnzymesDAO(pool);
    }


    @Override
    public Option<Enzyme> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM enzymes WHERE enzyme_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Option<Enzyme> getByName(final String name) throws DAOException {
        val sql = sql("SELECT * FROM enzymes WHERE enzyme_name = ? LIMIT 1", name)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<Enzyme> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM enzymes LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return newLinkedHashSet(read(sql).toCollection());
    }

    @Override
    public Enzyme insert(final Enzyme enzyme) throws DAOException {
        val sql = getOrPrepareInsert(enzyme);

        return write(sql);
    }

    @Override
    public Set<Enzyme> insertAll(final Set<Enzyme> enzymes) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Enzyme>>();
        for (val enzyme : enzymes) {
            sqlBuffer.add(getOrPrepareInsert(enzyme));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM enzymes WHERE enzyme_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final Enzyme enzyme) throws DAOException {
        val sql = sql("UPDATE enzymes SET enzyme_name = ? WHERE enzyme_id = ?", enzyme.getName())
                 .bind(identifier(2, enzyme.getId()))
                 .bind(update);

        write(sql);
    }

    @Override
    protected Enzyme createEntity(final ResultSet results) throws SQLException {
        val id   = parseInt(results, "enzyme_id");
        val name = parseString(results, "enzyme_name");

        return enzyme(id, name);
    }

    private DB<Enzyme> getOrPrepareInsert(final Enzyme enzyme) throws DAOException {
        val name = enzyme.getName();

        val maybeInserted = getByName(name).map(DB::unit);
        val prepareInsert = sql(
            "INSERT INTO enzymes (enzyme_name) VALUES (?)", name
        ).bind(update).bind(getKey).map(enzyme::withId);

        return maybeInserted.orSome(prepareInsert);
    }

}
