package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metaGenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IOStreamsExtensionMethods.pipeToInput;
import static fj.Function.curry;
import static fj.Unit.unit;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.List.Buffer;
import fj.data.Option;
import fj.data.Validation;

public final class MySQLMetaGenomesDAO extends MySQLAbstractDAO<MetaGenome> implements MetaGenomesDAO {

    // FIXME: this is quite a mess. Needs serious refactoring.

    private final F<Integer, DB<Option<MetaGenome>>> getMetaGenome = id -> sql(
        "SELECT * FROM metagenomes NATURAL JOIN projects WHERE metagenome_id = ?", id
    ).bind(query).bind(get).map(List::toOption);

    private final F2<Integer, Integer, DB<Iterable<MetaGenome>>> getMetaGenomes = (count, start) -> sql(
        "SELECT * FROM metagenomes NATURAL JOIN projects LIMIT ? OFFSET ?", count, start
    ).bind(query).bind(get).map(List::toCollection);

    private final F<Integer, F2<Integer, Integer, DB<Iterable<Identifier>>>> getByProject =
        pid -> (count, start) ->  sql(
            "SELECT metagenome_id FROM metagenomes NATURAL JOIN projects WHERE project_id = ? LIMIT ? OFFSET ?",
            pid, count, start
        ).bind(query).bind(this::getId);

    private final F<Integer, F2<Integer, Integer, DB<Iterable<Identifier>>>> getByProtein =
        pid -> (count, start) -> sql(
            "SELECT metagenome_id FROM metagenome_proteins WHERE protein_id = ? LIMIT ? OFFSET ?",
            pid, count, start
        ).bind(query).bind(this::getId);

    private final F<Integer, F<InputStream, DB<Integer>>> insertMetaGenome = pid -> fasta -> sql(
        "INSERT INTO metagenomes (project_id, metagenome_fasta) VALUES (?, ?)", pid
    ).bind(blob(2, fasta)).bind(update).bind(getKey);

    private final F<Integer, DB<Unit>> deleteMetaGenome = id -> sql(
        "DELETE FROM metagenomes WHERE metagenome_id = ?", id
    ).bind(update).map(r -> unit());

    private final F<Integer, F<InputStream, DB<Unit>>> updateMetaGenome = id -> fasta -> prepare(
        "UPDATE metagenomes SET metagenome_fasta = ? WHERE metagenome_id = ?"
    ).bind(blob(1, fasta)).bind(integer(2, id)).bind(update).map(r -> unit());

    private final F<Integer, F<Integer, F<Long, DB<Unit>>>> addProtein = mid -> pid -> count -> sql(
        "INSERT INTO metagenome_proteins (metagenome_id, protein_id, counter) VALUES (?, ?, ?)", mid, pid
    ).bind(longinteger(3, count)).bind(update).map(r -> unit());

    private final F<Integer, F<Integer, DB<Unit>>> deleteProtein = mid -> pid -> sql(
        "DELETE FROM metagenome_proteins WHERE metagenome_id = ? AND protein_id = ?", mid, pid
    ).bind(update).map(r -> unit());


    private MySQLMetaGenomesDAO(final ConnectionPool pool) {
        super(pool, pool.getConnector());
    }

    public static MySQLMetaGenomesDAO mysqlMetaGenomesDAO(final ConnectionPool pool) {
        return new MySQLMetaGenomesDAO(pool);
    }


    @Override
    public Validation<DAOException, Option<MetaGenome>> get(final Identifier id) {
        return withIdentifier(getMetaGenome, id).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<MetaGenome>> getAll(final int start, final int count) {
        return read(withPagination(getMetaGenomes, count, start));
    }

    @Override
    public Validation<DAOException, Iterable<Identifier>> getIdsByProjectId(
        final Identifier projectId, final int start, final int count
    ) {
        return withIdentifier(getByProject, projectId).map(f -> withPagination(f, count, start)).bind(this::read);
    }

    @Override
    public Validation<DAOException, Iterable<Identifier>> getIdsByProteinId(
        final Identifier proteinId, final int start, final int count
    ) {
        return withIdentifier(getByProtein, proteinId).map(f -> withPagination(f, count, start)).bind(this::read);
    }

    @Override
    public Validation<DAOException, MetaGenome> insert(final MetaGenome metaGenome) {
        return withMetaGenome(insertMetaGenome, metaGenome).bind(this::write).map(metaGenome::withId);
    }

    @Override
    public Validation<DAOException, Iterable<MetaGenome>> insertAll(final Iterable<MetaGenome> metaGenomes) {
        // FIXME: fucking ugly shit in here. Try to accumulate over
        // Validation<DAOException, DB<MetaGenome>> instead of constructing
        // a Buffer<DB<MetaGenome>>. There probably is an even better way.

        val buffer = Buffer.<DB<MetaGenome>>empty();

        for (final MetaGenome metaGenome : metaGenomes) {
            val mgValidation = withMetaGenome(
                insertMetaGenome, metaGenome
            ).map(db -> db.map(metaGenome::withId));

            if (mgValidation.isFail())
                return mgValidation.map(db -> List.nil());
            else
                mgValidation.map(buffer::snoc);
        }

        return write(sequence(buffer.toCollection()));
    }

    @Override
    public Validation<DAOException, Unit> delete(final Identifier id) {
        return withIdentifier(deleteMetaGenome, id).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> update(final MetaGenome metaGenome) {
        return withIdentifier(updateMetaGenome, metaGenome.getId()).bind(
            fastaF -> withFasta(fastaF, metaGenome.getGenomeFasta())
        ).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> addProteinToMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId, final long counter
    ) {
        return withIdentifier(addProtein, metaGenomeId).bind(
            proteinIdF -> withIdentifier(proteinIdF, proteinId)
        ).map(counterF -> counterF.f(counter)).bind(this::write);
    }

    @Override
    public Validation<DAOException, Unit> addAllProteinsToMetaGenome(
        final Identifier metaGenomeId, final Map<Identifier, Long> proteinCounters
    ) {
        // FIXME: fucking ugly shit in here. Try to accumulate over
        // Validation<DAOException, DB<Unit>> instead of constructing a
        // Buffer<DB<Unit>>. There probably is an even better way.

        val buffer = Buffer.<DB<Unit>>empty();
        val mgV    = withIdentifier(addProtein, metaGenomeId);

        for (final Entry<Identifier, Long> entry : proteinCounters.entrySet()) {
            val addValidation = mgV.bind(
                pidF -> withIdentifier(pidF, entry.getKey())
            ).map(ctrF -> ctrF.f(entry.getValue()));

            if (addValidation.isFail())
                return addValidation.map(db -> unit());
            else
                addValidation.map(buffer::snoc);
        }

        return write(sequence(buffer.toCollection())).map(i -> unit());
    }

    @Override
    public Validation<DAOException, Unit> deleteProteinFromMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId
    ) {
        return withIdentifier(deleteProtein, metaGenomeId).bind(
            pidF -> withIdentifier(pidF, proteinId)
        ).bind(this::write);
    }

    @Override
    protected MetaGenome createEntity(final ResultSet results) throws SQLException {
        val project = project(
            parseInt(results, "project_id"),
            parseString(results, "project_name"),
            parseString(results, "project_repository")
        );

        return metaGenome(
            parseInt(results, "metagenome_id"), project, parseFasta(results, "metagenome_fasta")
        );
    }


    private <A> Validation<DAOException, A> withMetaGenome(
        final F<Integer, F<InputStream, A>> f, final MetaGenome metaGenome
    ) {
        return withIdentifier(f, metaGenome.getProject().getId()).bind(
            fastaF -> withFasta(fastaF, metaGenome.getGenomeFasta())
        );
    }

    private <A> Validation<DAOException, A> withFasta(final F<InputStream, A> f, final GenomeFasta fasta) {
        val writer    = FastaWriter.forDNA();
        val validated = pipeToInput(curry(writer::toOutput).f(fasta)).map(f);

        return validated.f().map(DAOException::withCause);
    }

    private GenomeFasta parseFasta(final ResultSet results, final String columnName) throws SQLException {
        // FIXME: ugly
        val reader = FastaReader.forDNA();
        val fastaV = reader.fromInput(results.getBlob(columnName).getBinaryStream());

        if (fastaV.isSuccess())
            return GenomeFasta.of(fastaV.success().getSequences());
        else
            throw new SQLException("Could not parse DNA-Fasta at " + columnName, fastaV.fail());
    }

    private DB<Iterable<Identifier>> getId(final ResultSet resultSet) {
        return new DB<Iterable<Identifier>>() {
            @Override
            public Iterable<Identifier> run(final Connection c) throws SQLException {
                try (final ResultSet result = resultSet) {
                    val buffer = Buffer.<Identifier>empty();
                    while (result.next()) {
                        buffer.snoc(Identifier.of(parseInt(result, "metagenome_id")));
                    }
                    return buffer.toList();
                }
            }
        };
    }

}
