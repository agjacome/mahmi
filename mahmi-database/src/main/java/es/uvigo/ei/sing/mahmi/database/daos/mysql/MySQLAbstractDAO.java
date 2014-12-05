package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IOStreamsExtensionMethods.pipeToInput;
import static fj.data.Array.array;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import fj.F;
import fj.control.db.Connector;
import fj.control.db.DB;
import fj.control.db.DbState;
import fj.data.List;
import fj.data.List.Buffer;

@Slf4j
public abstract class MySQLAbstractDAO<A> {

    // TODO: try to abstract this functions into fully reusable components. Do
    // not require inheritance to use them.

    protected final ConnectionPool pool;
    protected final Connector      connector;

    protected MySQLAbstractDAO(final ConnectionPool pool) {
        this.pool      = pool;
        this.connector = pool.getConnector();
    }

    protected final <B> B execute(final DB<B> db) throws DAOException {
        try {
            return db.run(pool.getConnection());
        } catch (final SQLException sqe) {
            log.error("Database operation execution error", sqe);
            throw DAOException.withCause(sqe);
        }
    }

    private <B> B executeWithState(final F<Connector, DbState> state, final DB<B> db) throws DAOException {
        try {
            return state.f(connector).run(db);
        } catch (final SQLException sqe) {
            log.error("Database operation execution error", sqe);
            throw DAOException.withCause(sqe);
        }
    }

    protected final <B> B read(final DB<B> db) throws DAOException {
        return executeWithState(DbState::reader, db);
    }

    protected final <B> B write(final DB<B> db) throws DAOException {
        return executeWithState(DbState::writer, db);
    }

    protected final <B> DB<Iterable<B>> sequence(final Iterable<DB<B>> dbs) {
        return new DB<Iterable<B>>() {
            @Override
            public Iterable<B> run(final Connection c) throws SQLException {
                val buffer = new LinkedList<B>();
                for (final DB<B> db : dbs) {
                    buffer.add(db.run(c));
                }

                return buffer;
            }
        };
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

    protected final F<PreparedStatement, DB<PreparedStatement>> longInt(final int index, final long value) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                statement.setLong(index, value);
                return statement;
            }
        };
    }

    protected final F<PreparedStatement, DB<PreparedStatement>> identifier(final int index, final Identifier value) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                if (value.isEmpty()) throw new SQLException("Empty identifier on " + value.toString());

                statement.setInt(index, value.get());
                return statement;
            }
        };
    }

    protected final <B extends ChemicalCompoundSequence<?>> F<PreparedStatement, DB<PreparedStatement>> fasta(
        final int index, final FastaWriter<B> writer, final Fasta<B> fasta
    ) {
        return statement -> new DB<PreparedStatement>() {
            @Override
            public PreparedStatement run(final Connection c) throws SQLException {
                // FIXME: so exceptions, such try, wow
                try {
                    val input = pipeToInput(os -> {
                        try {
                            writer.toOutput(fasta, os);
                        } catch (final IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });

                    statement.setBlob(index, input);
                    return statement;
                } catch (final RuntimeException | IOException e) {
                    log.error("Error while inserting MetaGenome Fasta into DB", e);
                    throw new SQLException(e);
                }
            }
        };
    }

    protected final DB<PreparedStatement> sql(final String sql, final String ... values) {
        return array(values).zipIndex().foldLeft(db -> p -> db.bind(string(p._2() + 1, p._1())), prepare(sql));
    }

    protected final DB<PreparedStatement> sql(final String sql, final Integer ... values) {
        return array(values).zipIndex().foldLeft(db -> p -> db.bind(integer(p._2() + 1, p._1())), prepare(sql));
    }

    protected final DB<PreparedStatement> sql(final String sql, final Identifier ... values) {
        return array(values).zipIndex().foldLeft(db -> p -> db.bind(identifier(p._2() + 1, p._1())), prepare(sql));
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
                val buffer = Buffer.<A>empty();
                while (result.next()) {
                    buffer.snoc(createEntity(result));
                }

                return buffer.toList();
            }
        }
    };

    protected F<ResultSet, DB<Integer>> getKey = resultSet -> new DB<Integer>() {
        @Override
        public Integer run(final Connection c) throws SQLException {
            try (final ResultSet result = resultSet) {
                 if (!result.next())
                     throw new SQLException("SQL Update did not generate any key.");

                 return result.getInt(1);
            }
        }
    };

    protected F<ResultSet, DB<Boolean>> exists = resultSet -> new DB<Boolean>() {
        @Override
        public Boolean run(final Connection c) throws SQLException {
            try (final ResultSet result = resultSet) {
                return result.next() && result.getInt(1) > 0;
            }
        }
    };

    protected final int parseInt(final ResultSet results, final String column) throws SQLException {
        return results.getInt(column);
    }

    protected final long parseLong(final ResultSet results, final String column) throws SQLException {
        return results.getLong(column);
    }

    protected final String parseString(final ResultSet results, final String column) throws SQLException {
        return results.getString(column);
    }

    protected final AminoAcidSequence parseAAS(final ResultSet results, final String column) throws SQLException {
        return AminoAcidSequence.fromString(parseString(results, column));
    }

    protected abstract A createEntity(final ResultSet resultSet) throws SQLException;

}
