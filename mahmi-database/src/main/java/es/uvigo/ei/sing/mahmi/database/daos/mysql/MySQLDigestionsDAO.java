package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static fj.Function.curry;
import static fj.Unit.unit;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import fj.F;
import fj.F2;
import fj.F3;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.List.Buffer;
import fj.data.Option;
import fj.data.Validation;

public class MySQLDigestionsDAO extends MySQLAbstractDAO<Digestion> implements DigestionsDAO {

    // FIXME: this is quite a mess. Needs serious refactoring.

    private final String joinedTables = "digestions NATURAL JOIN peptides NATURAL JOIN proteins NATURAL JOIN enzymes";

    private final F<Integer, DB<Option<Digestion>>> getDigestion = id -> sql(
        "SELECT * FROM " + joinedTables + " WHERE digestion_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<Digestion>>> getDigestions = (count, start) -> sql(
        "SELECT * FROM " + joinedTables + " LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F3<Integer, Integer, Integer, DB<Option<Digestion>>> getByFKs = (enzyme, protein, peptide) -> sql(
        "SELECT * FROM " + joinedTables + " WHERE enzyme_id = ? AND protein_id = ? AND peptide_id = ?",
        enzyme, protein, peptide
    ).bind(query).bind(get).map(List::toOption);

    private final F<Integer, F2<Integer, Integer, DB<Iterable<Digestion>>>> getByEnzyme = id -> (count, start) -> sql(
        "SELECT * FROM " + joinedTables + " WHERE enzyme_id = ? LIMIT ? OFFSET ?", id, count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F<Integer, F2<Integer, Integer, DB<Iterable<Digestion>>>> getByProtein = id -> (count, start) -> sql(
        "SELECT * FROM " + joinedTables + " WHERE protein_id = ? LIMIT ? OFFSET ?", id, count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F<Integer, F2<Integer, Integer, DB<Iterable<Digestion>>>> getByPeptide = id -> (count, start) -> sql(
        "SELECT * FROM " + joinedTables + " WHERE peptide_id = ? LIMIT ? OFFSET ?", id, count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F3<Integer, Integer, Integer, F<Long, DB<Integer>>> insertDigestion =
        (enzyme, protein, peptide) -> counter -> sql(
            "INSERT INTO digestions (enzyme_id, protein_id, peptide_id, counter) VALUES (?, ?, ?, ?)",
            enzyme, protein, peptide
        ).bind(longinteger(4, counter)).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deleteDigestion = id -> sql(
        "DELETE FROM digestions WHERE digestion_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F<Long, DB<Unit>>> updateDigestion = id -> counter -> prepare(
        "UPDATE digestions SET counter = ? WHERE digestion_id = ?"
    ).bind(longinteger(1, counter)).bind(integer(2, id)).bind(update).map(r -> unit());


    private MySQLDigestionsDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static DigestionsDAO mysqlDigestionsDAO(final ConnectionPool pool) {
        return new MySQLDigestionsDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<Digestion>> get(final Identifier id) {
        return withIdentifier(getDigestion, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Option<Digestion>> get(
        final Identifier enzymeId, final Identifier proteinId, final Identifier peptideId
    ) {
        return withForeignKeys(getByFKs, enzymeId, proteinId, peptideId).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Digestion>> getAll(final int start, final int count) {
        return read(withPagination(getDigestions, count, start));
    }

    @Override
    public Validation<DAOException, Iterable<Digestion>> getByEnzymeId(
        final Identifier enzymeId, final int start, final int count
    ) {
        return withIdentifier(getByEnzyme, enzymeId).map(f -> withPagination(f, count, start)).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Digestion>> getByProteinId(
        final Identifier proteinId, final int start, final int count
    ) {
        return withIdentifier(getByProtein, proteinId).map(f -> withPagination(f, count, start)).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Digestion>> getByPeptideId(
        final Identifier peptideId, final int start, final int count
    ) {
        return withIdentifier(getByPeptide, peptideId).map(f -> withPagination(f, count, start)).bind(this::read);
    }

    @Override
    public Validation<DAOException, Digestion> insert(final Digestion digestion) {
        return withDigestion(insertDigestion, digestion).bind(this::write).map(digestion::withId);
    }

    @Override
    public Validation<DAOException, Iterable<Digestion>> insertAll(final Iterable<Digestion> digestions) {
        // FIXME: fucking ugly shit in here. Try to accumulate over
        // Validation<DAOException, DB<Digestion>> instead of constructing
        // a Buffer<DB<Digestion>>. There probably is an even better way.

        val buffer = Buffer.<DB<Digestion>>empty();

        for (final Digestion digestion : digestions) {
            val digestionValidation = withDigestion(
                insertDigestion, digestion
            ).map(db -> db.map(digestion::withId));

            if (digestionValidation.isFail())
                return digestionValidation.map(db -> List.nil());
            else
                digestionValidation.map(buffer::snoc);
        }

        return write(sequence(buffer.toCollection()));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deleteDigestion, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final Digestion digestion) {
        return withIdentifier(updateDigestion, digestion.getId()).map(
            f -> f.f(digestion.getCounter())
        ).bind(this::write);
    }

    @Override
    protected Digestion createEntity(final ResultSet results) throws SQLException {
        return digestion(
            protein(parseInt(results, "protein_id"), parseAAS(results, "protein_sequence")),
            peptide(parseInt(results, "peptide_id"), parseAAS(results, "peptide_sequence")),
            enzyme(parseInt(results, "enzyme_id"), parseString(results, "enzyme_name")),
            parseInt(results, "digestion_counter")
        );
    }


    private <A> Validation<DAOException, A> withForeignKeys(
        final F3<Integer, Integer, Integer, A> f,
        final Identifier enzyme,
        final Identifier protein,
        final Identifier peptide
    ) {
        return withIdentifier(curry(f), enzyme)
            .bind(g -> withIdentifier(g, protein))
            .bind(h -> withIdentifier(h, peptide));
    }

    private <A> Validation<DAOException, A> withDigestion(
        final F3<Integer, Integer, Integer, F<Long, A>> f, final Digestion digestion
    ) {
        val p = digestion.toProduct();

        return withForeignKeys(
            f, p._3().getId(), p._1().getId(), p._2().getId()
        ).map(g -> g.f(p._4()));
    }

}
