package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.F;
import fj.Ord;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;
import fj.data.Set;
import fj.function.Try0;

import es.uvigo.ei.sing.mahmi.common.entities.Entity;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAO;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;

import static fj.data.List.list;

import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ExtensionMethod(IterableExtensionMethods.class)
abstract class MySQLAbstractDAO<A extends Entity<A>> implements DAO<A> {

    protected final ConnectionPool connectionPool;

    protected final Ord<A> ordering = Identifier.ord.comap(A::getId);

    protected final F<ResultSet, DB<List<A>>> get = getWith(this::parse);

    @Override
    public Option<A> get(final Identifier id) throws DAOException {
        val sql = prepareSelect(id).bind(query).bind(get);
        return read(sql).toOption();
    }

    @Override
    public long count() throws DAOException {
        val sql = prepareCount().bind(query).bind(count);
        return read(sql);
    }

    @Override
    public Set<A> getAll(
        final int start, final int count
    ) throws DAOException {
        val sql = prepareSelect(count, start).bind(query).bind(get);
        return read(sql).toSet(ordering);
    }

    @Override
    public A insert(final A entity) throws DAOException {
        return write(getOrInsert(entity));
    }

    @Override
    public Set<A> insertAll(final Set<A> entities) throws DAOException {
        val sql = sequence(list(entities).map(this::getOrInsert));
        return write(sql).toSet(ordering);
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = prepareDelete(id).bind(update);
        write(sql);
    }

    @Override
    public void update(final A entity) throws DAOException {
        val sql = prepareUpdate(entity).bind(update);
        write(sql);
    }


    protected final <B> B read(final DB<B> db) throws DAOException {
        return action(() -> runReadOnly(db, connectionPool));
    }

    protected final <B> B write(final DB<B> db) throws DAOException {
        return action(() -> runReadWrite(db, connectionPool));
    }

    protected DB<A> getOrInsert(final A entity) {
        val exists = prepareSelect(entity)
            .bind(query).bind(get).map(List::toOption);

        val insert = prepareInsert(entity)
            .bind(update).bind(key).map(entity::withId);

        return exists.bind(a -> a.option(insert, DB::unit));
    }

    protected abstract A parse(final ResultSet resultSet) throws SQLException;

    protected abstract DB<PreparedStatement> prepareSelect(final A entity);

    protected abstract DB<PreparedStatement> prepareCount();

    protected abstract DB<PreparedStatement> prepareSelect(final Identifier id);

    protected abstract DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    );

    protected abstract DB<PreparedStatement> prepareInsert(final A entity);

    protected abstract DB<PreparedStatement> prepareDelete(final Identifier id);

    protected abstract DB<PreparedStatement> prepareUpdate(final A entity);


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
