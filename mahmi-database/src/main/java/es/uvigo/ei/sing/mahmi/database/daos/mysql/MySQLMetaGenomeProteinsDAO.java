package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins.metagenomeProteins;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.longInt;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseLong;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomeProteinsDAO;
import fj.control.db.DB;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public final class MySQLMetaGenomeProteinsDAO extends MySQLAbstractDAO<MetaGenomeProteins> implements MetaGenomeProteinsDAO {

    private static final String TABLES = "metagenome_proteins NATURAL JOIN projects NATURAL JOIN proteins ";

    private MySQLMetaGenomeProteinsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static MetaGenomeProteinsDAO mysqlMetaGenomeProteinsDAO(final ConnectionPool pool) {
        return new MySQLMetaGenomeProteinsDAO(pool);
    }

    @Override
    protected MetaGenomeProteins parse(final ResultSet results) throws SQLException {
    	return metagenomeProteins(parseIdentifier(results, "metagenome_proteins_id"),
					        	  parseMetaGenome(results),
					        	  parseProtein(results),
					        	  parseLong(results, "counter"));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final MetaGenomeProteins metagenomeProteins) {
        return sql(
            "SELECT metagenome_proteins_id,"
            + " metagenome_id,"
            + " project_id, project_name, project_repository,"
            + " protein_id, protein_sequence"
            + " counter FROM " + TABLES + " WHERE metagenome_id = ? AND protein_id = ? LIMIT 1",
            metagenomeProteins.getMetagenome().getId(), metagenomeProteins.getProtein().getId()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT metagenome_proteins_id,"
            + " metagenome_id,"
            + " project_id, project_name, project_repository,"
            + " protein_id, protein_sequence"
            + " counter FROM " + TABLES + " WHERE metagenome_proteins_id = ? LIMIT 1", id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final int limit, final int offset) {
        return sql(
            "SELECT * FROM " + TABLES +
            "ORDER BY metagenome_proteins_id " +
            "LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(metagenome_proteins_id) FROM metagenome_proteins");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final MetaGenomeProteins metagenomeProteins) {
        return sql(
            "INSERT INTO metagenome_proteins (metagenome_id, protein_id, counter) VALUES (?, ?, ?)",
            metagenomeProteins.getMetagenome().getId(), metagenomeProteins.getProtein().getId()
        ).bind(longInt(4, metagenomeProteins.getCounter()));
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM metagenome_proteins WHERE metagenome_proteins_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final MetaGenomeProteins metagenomeProteins) {
        return prepare(
            "UPDATE metagenome_proteins SET counter = ? WHERE metagenome_proteins_id = ?"
        ).bind(longInt(1, metagenomeProteins.getCounter())).bind(identifier(2, metagenomeProteins.getId()));
    }

    protected MetaGenome parseMetaGenome(final ResultSet results) throws SQLException {
    	return metagenome(parseIdentifier(results, "metagenome_id"),
    					 parseProject(results), 
    					 Fasta.empty());
    }

    private Protein parseProtein(final ResultSet results) throws SQLException {
    	return protein(parseIdentifier(results, "protein_id"),
			           parseAASequence(results, "protein_sequence"),
			           parseString(results, "protein_name"));
    }

    private Project parseProject(final ResultSet results) throws SQLException {
    	return project(parseIdentifier(results, "project_id"),
    				   parseString(results, "project_name"),
    				   parseString(results, "project_repository"));
    }
}
