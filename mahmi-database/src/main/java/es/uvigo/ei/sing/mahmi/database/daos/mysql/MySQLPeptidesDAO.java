package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static fj.Unit.unit;
import static fj.data.Stream.iterableStream;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

public final class MySQLPeptidesDAO extends MySQLAbstractDAO<Peptide> implements PeptidesDAO {

    private final F<Integer, DB<Option<Peptide>>> getPeptide = id -> sql(
        "SELECT * FROM peptides WHERE peptide_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F<String, DB<Option<Peptide>>> getPeptideBySHA = sha -> sql(
        "SELECT * FROM peptides WHERE peptide_hash = ?", sha
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<Peptide>>> getPeptides = (count, start) -> sql(
        "SELECT * FROM peptides LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F2<String, String, DB<Integer>> insertPeptide = (sha, seq) -> sql(
        "INSERT IGNORE INTO peptides (peptide_hash, peptide_sequence) VALUES (?, ?)", sha, seq
    ).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deletePeptide = id -> sql(
        "DELETE FROM peptides WHERE peptide_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F2<String, String, DB<Unit>>> updatePeptide = id -> (sha, seq) -> sql(
        "UPDATE peptides SET peptide_hash = ?, peptide_sequence = ? WHERE peptide_id = ?", sha, seq
    ).bind(integer(3, id)).bind(update).map(r -> unit());


    private MySQLPeptidesDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static PeptidesDAO mysqlPeptidesDAO(final ConnectionPool pool) {
        return new MySQLPeptidesDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<Peptide>> get(final Identifier id) {
        return withIdentifier(getPeptide, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Option<Peptide>> getBySequence(final AminoAcidSequence seq) {
        return read(getPeptideBySHA.f(seq.toSHA1().asHexString()));
    }

    @Override
    public Validation<DAOException, Iterable<Peptide>> getAll(final int start, final int count) {
        return read(withPagination(getPeptides, count, start));
    }

    @Override
    public Validation<DAOException, Peptide> insert(final Peptide peptide) {
        return write(withPeptide(insertPeptide, peptide)).map(peptide::withId);
    }

    @Override
    public Validation<DAOException, Iterable<Peptide>> insertAll(final Iterable<Peptide> peptides) {
        val inserts = iterableStream(peptides).map(
            p -> withPeptide(insertPeptide, p).map(p::withId)
        );

        return write(sequence(inserts));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deletePeptide, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final Peptide peptide) {
        return withIdentifier(updatePeptide, peptide.getId()).map(
            peptideF -> withPeptide(peptideF, peptide)
        ).bind(this::write);
    }

    @Override
    protected Peptide createEntity(final ResultSet resultSet) throws SQLException {
        return peptide(parseInt(resultSet, "peptide_id"), parseAAS(resultSet, "peptide_sequence"));
    }


    private <A> A withPeptide(final F2<String, String, A> f, final Peptide peptide) {
        val aas = peptide.getSequence();
        return f.f(aas.toSHA1().asHexString(), aas.toString());
    }

}
