package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static fj.Unit.unit;
import static fj.data.Stream.iterableStream;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.EnzymesDAO;
import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

public final class MySQLEnzymesDAO extends MySQLAbstractDAO<Enzyme> implements EnzymesDAO {

    private final F<Integer, DB<Option<Enzyme>>> getEnzyme = id -> sql(
        "SELECT * FROM enzymes WHERE enzyme_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<Enzyme>>> getEnzymes = (count, start) -> sql(
        "SELECT * FROM enzymes LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F<String, DB<Integer>> insertEnzyme = name -> sql(
        "INSERT INTO enzymes (enzyme_name) VALUES (?)", name
    ).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deleteEnzyme = id -> sql(
        "DELETE FROM enzymes WHERE enzyme_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F<String, DB<Unit>>> updateEnzyme = id -> name -> sql(
        "UPDATE enzymes SET enzyme_name = ? WHERE enzyme_id = ?", name
    ).bind(integer(2, id)).bind(update).map(r -> unit());


    private MySQLEnzymesDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static MySQLEnzymesDAO mysqlEnzymesDAO(final ConnectionPool pool) {
        return new MySQLEnzymesDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<Enzyme>> get(final Identifier id) {
        return withIdentifier(getEnzyme, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Enzyme>> getAll(final int start, final int count) {
        return read(withPagination(getEnzymes, count, start));
    }

    @Override
    public Validation<DAOException, Enzyme> insert(final Enzyme enzyme) {
        return write(withEnzyme(insertEnzyme, enzyme)).map(enzyme::withId);
    }

    @Override
    public Validation<DAOException, Iterable<Enzyme>> insertAll(final Iterable<Enzyme> enzymes) {
        val inserts = iterableStream(enzymes).map(
            enzyme -> withEnzyme(insertEnzyme, enzyme).map(enzyme::withId)
        );

        return write(sequence(inserts));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deleteEnzyme, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final Enzyme enzyme) {
        return withIdentifier(updateEnzyme, enzyme.getId()).map(f -> withEnzyme(f, enzyme)).bind(this::write);
    }

    @Override
    protected Enzyme createEntity(final ResultSet results) throws SQLException {
        return enzyme(parseInt(results, "enzyme_id"), parseString(results, "enzyme_name"));
    }


    private <A> A withEnzyme(final F<String, A> f, final Enzyme enzyme) {
        return f.f(enzyme.getName());
    }

}
