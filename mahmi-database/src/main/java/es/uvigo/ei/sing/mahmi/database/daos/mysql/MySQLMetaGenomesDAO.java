package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metaGenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static fj.Unit.unit;
import static jersey.repackaged.com.google.common.collect.Sets.newLinkedHashSet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import fj.F;
import fj.Unit;
import fj.control.db.DB;
import fj.data.Option;

@Slf4j
public final class MySQLMetaGenomesDAO extends MySQLAbstractDAO<MetaGenome> implements MetaGenomesDAO {

    private MySQLMetaGenomesDAO(final ConnectionPool pool) {
        super(pool);
    }

    public static MySQLMetaGenomesDAO mysqlMetaGenomesDAO(final ConnectionPool pool) {
        return new MySQLMetaGenomesDAO(pool);
    }


    @Override
    public Option<MetaGenome> get(final Identifier id) throws DAOException {
        val sql = sql("SELECT * FROM metagenomes NATURAL JOIN projects WHERE metagenome_id = ? LIMIT 1", id)
                 .bind(query).bind(get);

        return read(sql).toOption();
    }

    @Override
    public Set<MetaGenome> getAll(final int start, final int count) throws DAOException {
        val sql = sql("SELECT * FROM metagenomes NATURAL JOIN projects LIMIT ? OFFSET ?", count, start)
                 .bind(query).bind(get);

        return newLinkedHashSet(read(sql).toCollection());
    }

    @Override
    public Set<Identifier> getIdsByProjectId(final Identifier project, final int start, final int count) throws DAOException {
        val sql = sql(
            "SELECT metagenome_id FROM metagenomes NATURAL JOIN projects WHERE project_id = ? LIMIT ? OFFSET ?", project
        ).bind(integer(2, count)).bind(integer(3, start)).bind(query).bind(idSet);

        return read(sql);
    }

    @Override
    public Set<Identifier> getIdsByProteinId(final Identifier protein, final int start, final int count) throws DAOException {
        val sql = sql(
            "SELECT metagenome_id FROM metagenome_proteins WHERE protein_id = ? LIMIT ? OFFSET ?", protein
        ).bind(integer(2, count)).bind(integer(3, start)).bind(query).bind(idSet);

        return read(sql);
    }

    @Override
    public MetaGenome insert(final MetaGenome metaGenome) throws DAOException {
        val sql = prepareInsert(metaGenome);

        return write(sql);
    }

    @Override
    public Set<MetaGenome> insertAll(final Set<MetaGenome> metaGenomes) throws DAOException {
        val sqlBuffer = new LinkedList<DB<MetaGenome>>();
        for (val metaGenome : metaGenomes) {
            sqlBuffer.add(prepareInsert(metaGenome));
        }

        return newLinkedHashSet(write(sequence(sqlBuffer)));
    }

    @Override
    public void delete(final Identifier id) throws DAOException {
        val sql = sql("DELETE FROM metagenomes WHERE metagenome_id = ?", id).bind(update);

        write(sql);
    }

    @Override
    public void update(final MetaGenome metaGenome) throws DAOException {
        val sql = prepare("UPDATE metagenomes SET metagenome_fasta = ? WHERE metagenome_id = ?")
                 .bind(fasta(1, FastaWriter.forDNA(), metaGenome.getGenomeFasta()))
                 .bind(identifier(2, metaGenome.getId()))
                 .bind(update);

        write(sql);
    }

    @Override
    public void addProteinToMetaGenome(final Identifier metaGenome, final Identifier protein, final long counter) throws DAOException {
        val sql = prepareProteinInsert(metaGenome, protein, counter);

        write(sql);
    }

    @Override
    public void addAllProteinsToMetaGenome(final Identifier metaGenome, final Map<Identifier, Long> proteins) throws DAOException {
        val sqlBuffer = new LinkedList<DB<Unit>>();
        for (val entry : proteins.entrySet()) {
            val protein = entry.getKey();
            val counter = entry.getValue();

            val sql = prepareProteinInsert(metaGenome, protein, counter);
            sqlBuffer.add(sql);
        }

        write(sequence(sqlBuffer));
    }

    @Override
    public void deleteProteinFromMetaGenome(final Identifier metaGenome, final Identifier protein) throws DAOException {
        val sql = sql(
            "DELETE FROM metagenome_proteins WHERE metagenome_id = ? AND protein_id = ?", metaGenome, protein
        ).bind(update);

        write(sql);
    }

    @Override
    protected MetaGenome createEntity(final ResultSet results) throws SQLException {
        val id      = parseInt(results, "metagenome_id");
        val project = parseProject(results);
        val fasta   = parseFasta(results, "metagenome_fasta");

        return metaGenome(id, project, fasta);
    }


    private DB<MetaGenome> prepareInsert(final MetaGenome metaGenome) throws DAOException {
        val project = metaGenome.getProject().getId();
        val fasta   = metaGenome.getGenomeFasta();

        return sql("INSERT INTO metagenomes (project_id, metagenome_fasta) VALUES (?, ?)", project)
               .bind(fasta(2, FastaWriter.forDNA(), fasta))
               .bind(update).bind(getKey).map(metaGenome::withId);
    }

    private DB<Unit> prepareProteinInsert(final Identifier metaGenome, final Identifier protein, final long counter) throws DAOException {
        val exists = sql(
            "SELECT COUNT(*) FROM metagenome_proteins WHERE protein_id = ? AND metagenome_id = ? LIMIT 1",
            protein, metaGenome
        ).bind(query).bind(exists);

        val insert = sql(
            "INSERT INTO metagenome_proteins (metagenome_id, protein_id, counter) VALUES(?, ?, ?)",
            metaGenome, protein
        ).bind(longInt(3, counter)).bind(update).map(r -> unit());

        val nothing = DB.unit(unit());

        return exists.bind(alreadyInserted -> alreadyInserted ? nothing : insert);
    }

    private Project parseProject(final ResultSet results) throws SQLException {
        val id   = parseInt(results, "project_id");
        val name = parseString(results, "project_name");
        val repo = parseString(results, "project_repository");

        return project(id, name, repo);
    }

    private GenomeFasta parseFasta(final ResultSet results, final String columnName) throws SQLException {
        try {
            val reader = FastaReader.forDNA();
            val fasta  = reader.fromInput(results.getBlob(columnName).getBinaryStream());

            return (GenomeFasta) fasta;
        } catch (final IOException ioe) {
            log.error("Error while parsing MetaGenome Fasta from DB", ioe);
            throw new SQLException(ioe);
        }
    }

    private final F<ResultSet, DB<Set<Identifier>>> idSet = resultSet -> new DB<Set<Identifier>>() {
        @Override
        public Set<Identifier> run(final Connection c) throws SQLException {
            try (final ResultSet result = resultSet) {
                val buffer = new LinkedHashSet<Identifier>();
                while (result.next()) {
                    val id = Identifier.of(parseInt(result, "metagenome_id"));
                    buffer.add(id);
                }

                return buffer;
            }
        }
    };

}
