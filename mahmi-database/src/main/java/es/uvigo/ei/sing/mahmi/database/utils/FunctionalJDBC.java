package es.uvigo.ei.sing.mahmi.database.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fj.F;
import fj.control.db.Connector;
import fj.control.db.DB;
import fj.control.db.DbState;
import fj.data.List;
import fj.data.List.Buffer;
import fj.function.Try1;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.text.MessageFormat.format;

import static fj.data.Array.array;

import static es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter.fastaWriter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FunctionalJDBC {

    public static <A> A runReadOnly(
        final DB<A> db, final ConnectionPool pool
    ) throws SQLException {
        return withState(DbState::reader, db, pool);
    }

    public static <A> A runReadWrite(
        final DB<A> db, final ConnectionPool pool
    ) throws SQLException {
        return withState(DbState::writer, db, pool);
    }

    public static <A> DB<Iterable<A>> sequence(final Iterable<DB<A>> dbs) {
        return db(connection -> {
            final java.util.List<A> buffer = new java.util.LinkedList<A>();
            for (final DB<A> db : dbs)
                buffer.add(db.run(connection));
            return buffer;
        });
    }

    public static DB<PreparedStatement> prepare(final String sql) {
        return db(connection ->
            connection.prepareStatement(sql, RETURN_GENERATED_KEYS)
        );
    }

    public static F<PreparedStatement, DB<PreparedStatement>> string(
        final int index, final String value
    ) {
        return statement -> db(connection -> {
            statement.setString(index, value);
            return statement;
        });
    }

    public static F<PreparedStatement, DB<PreparedStatement>> integer(
        final int index, final int value
    ) {
        return statement -> db(connection -> {
            statement.setInt(index, value);
            return statement;
        });
    }

    public static F<PreparedStatement, DB<PreparedStatement>> longInt(
        final int index, final long value
    ) {
        return statement -> db(connection -> {
            statement.setLong(index, value);
            return statement;
        });
    }

    public static F<PreparedStatement, DB<PreparedStatement>> identifier(
        final int index, final Identifier id
    ) {
        return statement -> db(connection -> {
            final long value = OptionExtensionMethods.orThrow(
                id.getValue(),
                () -> error("Empty Identifier on index {0}", index)
            ).longValue();

            statement.setLong(index, value);
            return statement;
        });
    }

    public static F<PreparedStatement, DB<PreparedStatement>> blob(
        final int index, final InputStream stream
    ) {
        return statement -> db(connection -> {
            statement.setBlob(index, stream);
            return statement;
        });
    }

    public static <A extends CompoundSequence<?>> F<PreparedStatement, DB<PreparedStatement>> fasta(
        final int index, final Fasta<A> fasta
    ) {
        final FastaWriter<A> writer = fastaWriter();

        return statement -> db(connection -> {
            try (final StringWriter stringWriter = new StringWriter()) {
                writer.toWriter(fasta, stringWriter);
                statement.setString(index, stringWriter.toString());
                return statement;
            } catch (final RuntimeException | IOException e) {
                throw error("Error while binding fasta to DB");
            }
        });
    }

    public static String parseString(
        final ResultSet results, final String column
    ) throws SQLException {
        return results.getString(column);
    }

    public static int parseInt(
        final ResultSet results, final String column
    ) throws SQLException {
        return results.getInt(column);
    }

    public static long parseLong(
        final ResultSet results, final String column
    ) throws SQLException {
        return results.getLong(column);
    }

    public static Identifier parseIdentifier(
        final ResultSet results, final String column
    ) throws SQLException {
        return Identifier.of(parseInt(results, column));
    }

    public static InputStream parseBlob(
        final ResultSet results, final String column
    ) throws SQLException {
        return results.getBlob(column).getBinaryStream();
    }

    public static AminoAcidSequence parseAASequence(
        final ResultSet results, final String column
    ) throws SQLException {
        val str = parseString(results, column);
        return OptionExtensionMethods.orThrow(
            AminoAcidSequence.fromString(str),
            () -> error("Invalid AminoAcid sequence at {0}: {1}", column, str)
        );
    }

    public static DB<PreparedStatement> sql(
        final String sql, final String ... values
    ) {
        return array(values).zipIndex().foldLeft(
            db -> p -> db.bind(string(p._2() + 1, p._1())), prepare(sql)
        );
    }

    public static DB<PreparedStatement> sql(
        final String sql, final Integer ... values
    ) {
        return array(values).zipIndex().foldLeft(
            db -> p -> db.bind(integer(p._2() + 1, p._1())), prepare(sql)
        );
    }

    public static DB<PreparedStatement> sql(
        final String sql, final Identifier ... values
    ) {
        return array(values).zipIndex().foldLeft(
            db -> p -> db.bind(identifier(p._2() + 1, p._1())), prepare(sql)
        );
    }

    public static final F<PreparedStatement, DB<ResultSet>> query =
        statement -> db(connection -> statement.executeQuery());

    public static final F<PreparedStatement, DB<ResultSet>> update =
        statement -> db(connection -> {
            statement.executeUpdate();
            return statement.getGeneratedKeys();
        });

    public static <A> F<ResultSet, DB<List<A>>> getWith(
        final Try1<ResultSet, A, SQLException> parser
    ) {
        return resultSet -> db(connection -> {
            try (final ResultSet results = resultSet) {
                final Buffer<A> buffer = Buffer.<A>empty();
                while (results.next()) buffer.snoc(parser.f(results));

                return buffer.toList();
            }
        });
    }

    public static F<ResultSet, DB<Identifier>> key =
        resultSet -> db(connection -> {
            try (final ResultSet result = resultSet) {
                 if (!result.next())
                     throw error("SQL Update did not generate any key.");
                 else
                     return Identifier.of(result.getInt(1));
            }
        });

    public static F<ResultSet, DB<Long>> count =
        resultSet -> db(connection -> {
            try (final ResultSet result = resultSet) {
                return result.next() ? result.getLong(1) : 0L;
            }
        });

    public static F<ResultSet, DB<Boolean>> exists =
        resultSet -> db(connection -> {
            try (final ResultSet result = resultSet) {
                return result.next() && result.getInt(1) > 0;
            }
        });


    private static <A> A withState(
        final F<Connector, DbState> stateF,
        final DB<A>                 db,
        final ConnectionPool        pool
    ) throws SQLException {
        return stateF.f(pool.getConnector()).run(db);
    }

    private static SQLException error(
        final String message, final Object ... args
    ) {
        return new SQLException(format(message, args));
    }

    @FunctionalInterface
    private static interface DBAction<A> {
        public A apply(final Connection c) throws SQLException;
    }

    private static <A> DB<A> db(final DBAction<A> action) {
        return new DB<A>() {
            @Override
            public A run(final Connection c) throws SQLException {
                return action.apply(c);
            }
        };
    }

}
