package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.database.daos.DAOException.withCause;
import static es.uvigo.ei.sing.mahmi.database.daos.DAOException.withMessage;
import static fj.data.Array.array;
import static fj.data.Validation.fail;
import static fj.data.Validation.success;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import fj.F;
import fj.F2;
import fj.control.db.Connector;
import fj.control.db.DB;
import fj.control.db.DbState;
import fj.data.List;
import fj.data.List.Buffer;
import fj.data.Option;
import fj.data.Validation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MySQLAbstractDAO<A> {

    // TODO: try to fully abstract this functions into fully reusable
    // components. Do not require inheritance to use them.

    protected final ConnectionPool pool;
    protected final Connector      connector;

    protected final <B> Validation<DAOException, B> read(final DB<B> db) {
        return executeWithState(DbState::reader, db);
    }

    protected final <B> Validation<DAOException, B> write(final DB<B> db) {
        return executeWithState(DbState::writer, db);
    }

    protected final <B> Validation<DAOException, B> execute(final DB<B> db) {
        try {
            return success(db.run(pool.getConnection()));
        } catch (final SQLException se) {
            return fail(withCause(se));
        }
    }

    private <B> Validation<DAOException, B> executeWithState(final F<Connector, DbState> state, final DB<B> db) {
        try {
            return success(state.f(connector).run(db));
        } catch (final SQLException se) {
            return fail(withCause(se));
        }
    }

    protected final <B> DB<Iterable<B>> sequence(final Iterable<DB<B>> dbs) {
        return new DB<Iterable<B>>() {
            @Override
            public Iterable<B> run(final Connection c) throws SQLException {
                // cannot map because SQLException could be thrown
                final Buffer<B> buffer = Buffer.empty();
                for (final DB<B> db : dbs) buffer.snoc(db.run(c));
                return buffer.toCollection();
            }
        };
    }

    protected final DB<PreparedStatement> sql(final String sql, final String ... values) {
        return array(values).zipIndex().foldLeft(db -> p -> db.bind(string(p._2() + 1, p._1())), prepare(sql));
    }

    protected final DB<PreparedStatement> sql(final String sql, final Integer ... values) {
        return array(values).zipIndex().foldLeft(db -> p -> db.bind(integer(p._2() + 1, p._1())), prepare(sql));
    }

    protected final DB<PreparedStatement> prepare(final String sql) {
        return new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection connection) throws SQLException {
                return connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
            }
        };
    }

    protected final F<PreparedStatement, DB<PreparedStatement>> string(final int index, final String value) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                statement.setString(index, value);
                return statement;
           }
        };
    }

    protected final F<PreparedStatement, DB<PreparedStatement>> integer(final int index, final int value) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                statement.setInt(index, value);
                return statement;
            }
        };
    }

    protected final F<PreparedStatement, DB<PreparedStatement>> longinteger(final int index, final long value) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                statement.setLong(index, value);
                return statement;
            }
        };
    }

    protected final F<PreparedStatement, DB<PreparedStatement>> blob(final int index, final InputStream blobStream) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                statement.setBlob(index, blobStream);
                return statement;
            }
        };
    }

    protected final F<PreparedStatement, DB<ResultSet>> query = statement -> new DB<ResultSet>() {
        @Override
        public ResultSet run(final Connection c) throws SQLException {
            return statement.executeQuery();
        }
    };

    protected final F<PreparedStatement, DB<ResultSet>> update = statement -> new DB<ResultSet>() {
        @Override
        public ResultSet run(final Connection c) throws SQLException {
            statement.executeUpdate();
            return statement.getGeneratedKeys();
        }
    };

    protected F<ResultSet, DB<List<A>>> get = resultSet -> new DB<List<A>>() {
        @Override
        public List<A> run(final Connection c) throws SQLException {
            try (final ResultSet result = resultSet) {
                // cannot do a List.unfold because SQLException can be thrown
                final Buffer<A> buffer = Buffer.empty();
                while (result.next()) buffer.snoc(createEntity(result));
                return buffer.toList();
            }
        }
    };

    protected abstract A createEntity(final ResultSet resultSet) throws SQLException;

    protected F<ResultSet, DB<Integer>> getKey = resultSet -> new DB<Integer>() {
        @Override
        public Integer run(final Connection c) throws SQLException {
            try (final ResultSet result = resultSet) {
                // FIXME: **TEMPORAL** solution, always return something even
                // when the DB did not produce any key. This removes problems
                // with "INSERT IGNORE" when duplicate column encountered.
                // Previous (causing fails) version:
                // if (!result.next())
                //     throw new SQLException("SQL Update did not generate any key.");
                //
                // return result.getInt(1);
                return result.next() ? result.getInt(1) : 0;
            }
        }
    };

    protected final <B> Validation<DAOException, B> withIdentifier(final F<Integer, B> f, final Identifier id) {
        return id.getValue().toValidation(withMessage("Empty identifier")).map(f);
    }

    protected final <B> B withPagination(final F2<Integer, Integer, B> f, final Integer count, final Integer start) {
        return f.f(count, start);
    }

    protected final int parseInt(final ResultSet results, final String column) throws SQLException {
        return results.getInt(column);
    }

    protected final String parseString(final ResultSet results, final String column) throws SQLException {
        return results.getString(column);
    }

    protected final AminoAcidSequence parseAAS(final ResultSet results, final String column) throws SQLException {
        final Option<AminoAcidSequence> aas = AminoAcidSequence.fromString(parseString(results, column));

        if (aas.isSome())
            return aas.some();
        else
            throw new SQLException("Invalid sequence in " + column + ": " + parseString(results, column));
    }

}
