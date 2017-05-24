package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import lombok.experimental.ExtensionMethod;

import fj.control.db.DB;
import fj.data.HashMap;
import fj.data.List.Buffer;
import fj.data.Option;
import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;

import static fj.Bottom.error;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.*;

@ExtensionMethod(IterableExtensionMethods.class)
public final class MySQLMetaGenomesDAO extends MySQLAbstractDAO<MetaGenome> implements MetaGenomesDAO {

    private MySQLMetaGenomesDAO(final ConnectionPool conectionPool) {
        super(conectionPool);
    }

    public static MetaGenomesDAO mysqlMetaGenomesDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLMetaGenomesDAO(connectionPool);
    }


    @Override
    public Option<MetaGenome> getWithFasta(
        final Identifier id
    ) throws DAOException {
        // this is the only place where metagenome_fasta column is selected, in
        // any other selects present on this file it is explicitly excluded
        // (since it is quite a big column to return always)
        val sql = sql(
            "SELECT * FROM metagenomes NATURAL JOIN projects WHERE metagenome_id = ? LIMIT 1",
            id
        );
        val statement = sql.bind(query).bind(getWith(this::parseWithFasta));
        return read(statement).toOption();
    }

    @Override
    public long countByProject(final Project project) {
        val sql = sql(
            "SELECT COUNT(metagenome_id) FROM metagenomes WHERE project_id = ?",
            project.getId()
        );
        val statement = sql.bind(query).bind(count);
        return read(statement);
    }

    @Override
    public Set<MetaGenome> getByProject(
        final Project project, final int start, final int count
    ) throws DAOException {
        val sql = sql(
            "SELECT metagenome_id, project_id, project_name, project_repository " +
            "FROM projects NATURAL JOIN metagenomes " +
            "WHERE project_id = ? " +
            "ORDER BY metagenome_id LIMIT ? OFFSET ?",
            project.getId()
        ).bind(integer(2, count)).bind(integer(3, start));
        val statement = sql.bind(query).bind(get);
        return read(statement).toSet(ordering);
    }


    @Override
    public void addProteins(
        final MetaGenome metaGenome, final HashMap<Protein, Long> proteins
    ) throws DAOException {
        val statement = Buffer.<DB<ResultSet>>empty();

        for (val protein : proteins) {
            val counter = proteins.get(protein).some();

            val sql = prepareProteinInsert(metaGenome, protein, counter);
            statement.snoc(sql.bind(update));
        }

        write(sequence(statement.toList()));
    }

    @Override
    protected MetaGenome parse(final ResultSet results) throws SQLException {
    	return metagenome(parseIdentifier(results, "metagenome_id"),
				          parseProject(results), 
				          Fasta.empty());
    }


    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT metagenome_id, project_id, project_name, project_repository " +
            "FROM metagenomes NATURAL JOIN projects " +
            "WHERE metagenome_id = ? LIMIT 1",
            id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT metagenome_id, project_id, project_name, project_repository " +
            "FROM metagenomes NATURAL JOIN projects " +
            "ORDER BY metagenome_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(metagenome_id) FROM metagenomes");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final MetaGenome metaGenome) {
        return sql(
            "INSERT INTO metagenomes (project_id, metagenome_fasta) VALUES (?, ?)",
            metaGenome.getProject().getId()
        ).bind(fasta(2, metaGenome.getFasta()));
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM metagenomes WHERE metagenome_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final MetaGenome metaGenome) {
        return prepare(
            "UPDATE metagenomes SET project_id = ?, metagenome_fasta = ? WHERE metagenome_id = ?"
        ).bind(identifier(1, metaGenome.getProject().getId())).bind(fasta(2, metaGenome.getFasta())).bind(identifier(3, metaGenome.getId()));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final MetaGenome metagenome) {
        // used only in MySQLAbstractDAO::getOrInsert, and because we are
        // overriding it (see below), this operation will (and must) never be
        // called
        throw error("Should not be called. Ever.");
    }

    @Override
    protected DB<MetaGenome> getOrInsert(final MetaGenome metaGenome) {
        // do not check for duplicates, always insert
        return prepareInsert(metaGenome)
            .bind(update).bind(key).map(metaGenome::withId);
    }


    private MetaGenome parseWithFasta(
        final ResultSet results
    ) throws SQLException {
        return parse(results).withFasta(parseFasta(results));
    }

    private Project parseProject(final ResultSet results) throws SQLException {
    	return project(parseIdentifier(results, "project_id"),
				       parseString(results, "project_name"),
				       parseString(results, "project_repository"));
    }

    private Fasta<NucleobaseSequence> parseFasta(
        final ResultSet results
    ) throws SQLException {
        try {
            return FastaReader.forNucleobase().fromInput(parseBlob(results, "metagenome_fasta"));
        } catch (final IOException ioe) {
            throw new SQLException(ioe);
        }
    }

    private DB<PreparedStatement> prepareProteinInsert(
        final MetaGenome metaGenome, final Protein protein, final long counter
    ) {
        return sql(
            // ignore duplicates => do not insert if already inserted!!
            "INSERT IGNORE INTO metagenome_proteins (metagenome_id, protein_id, counter) VALUES(?, ?, ?)",
            metaGenome.getId(), protein.getId()
        ).bind(longInt(3, counter));
    }

}
