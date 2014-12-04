package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static fj.Unit.unit;
import static fj.data.Stream.iterableStream;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

public final class MySQLProteinsDAO extends MySQLAbstractDAO<Protein> implements ProteinsDAO {

    private final F<Integer, DB<Option<Protein>>> getProtein = id -> sql(
        "SELECT * FROM proteins WHERE protein_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F<String, DB<Option<Protein>>> getProteinBySHA = sha -> sql(
        "SELECT * FROM peptides WHERE protein_hash = ?", sha
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<Protein>>> getProteins = (count, start) -> sql(
        "SELECT * FROM proteins LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F2<String, String, DB<Integer>> insertProtein = (sha, seq) -> sql(
        "INSERT IGNORE INTO proteins (protein_hash, protein_sequence) VALUES (?, ?)", sha, seq
    ).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deleteProtein = id -> sql(
        "DELETE FROM proteins WHERE protein_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F2<String, String, DB<Unit>>> updateProtein = id -> (sha, seq) -> sql(
        "UPDATE proteins SET protein_hash = ?, protein_sequence = ? WHERE protein_id = ?", sha, seq
    ).bind(integer(3, id)).bind(update).map(r -> unit());


    private MySQLProteinsDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static ProteinsDAO mysqlProteinsDAO(final ConnectionPool pool) {
        return new MySQLProteinsDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<Protein>> get(final Identifier id) {
        return withIdentifier(getProtein, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Option<Protein>> getBySequence(final AminoAcidSequence seq) {
        return read(getProteinBySHA.f(seq.toSHA1().asHexString()));
    }

    @Override
    public Validation<DAOException, Iterable<Protein>> getAll(final int start, final int count) {
        return read(withPagination(getProteins, count, start));
    }

    @Override
    public Validation<DAOException, Protein> insert(final Protein protein) {
        return write(withProtein(insertProtein, protein)).map(protein::withId);
    }

    @Override
    public Validation<DAOException, Iterable<Protein>> insertAll(final Iterable<Protein> proteins) {
        val inserts = iterableStream(proteins).map(
            p -> withProtein(insertProtein, p).map(p::withId)
        );

        return write(sequence(inserts));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deleteProtein, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final Protein protein) {
        return withIdentifier(updateProtein, protein.getId()).map(
            proteinF -> withProtein(proteinF, protein)
        ).bind(this::write);
    }

    @Override
    protected Protein createEntity(final ResultSet resultSet) throws SQLException {
        return protein(parseInt(resultSet, "protein_id"), parseAAS(resultSet, "protein_sequence"));
    }


    private <A> A withProtein(final F2<String, String, A> f, final Protein protein) {
        val aas = protein.getSequence();
        return f.f(aas.toSHA1().asHexString(), aas.toString());
    }

}
