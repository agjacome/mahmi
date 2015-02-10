package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins.metagenomeProteins;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.integer;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.longInt;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseLong;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.string;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomeProteinsDAO;
import fj.control.db.DB;
import fj.data.Option;

public final class MySQLMetaGenomeProteinsDAO extends MySQLAbstractDAO<MetaGenomeProteins> implements MetaGenomeProteinsDAO {

    private static final String TABLES = "metagenome_proteins NATURAL JOIN projects NATURAL JOIN proteins";

    private MySQLMetaGenomeProteinsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static MetaGenomeProteinsDAO mysqlMetaGenomeProteinsDAO(final ConnectionPool pool) {
        return new MySQLMetaGenomeProteinsDAO(pool);
    }


    @Override
    public Option<MetaGenomeProteins> get(
        final MetaGenome mg, final Protein protein
    ) throws DAOException {
        val metagenomeProteins = metagenomeProteins(mg, protein);
        val statement = prepareSelect(metagenomeProteins).bind(query).bind(get);

        return read(statement).toOption();
    }
    @SuppressWarnings("deprecation")
    @Override
    public Collection<MetaGenomeProteins> search(
    		final Protein protein,final MetaGenome mg,
    		final int start, final int count
    ) throws DAOException{
    	val sql = sql(
    			"select metagenome_proteins_id,"
    		        + " metagenome_id,"
    		        + " project_id, project_name, project_repository,"
    		        + " protein_id, protein_sequence,"
    	            + " counter " +
                "FROM " + TABLES + 
                " WHERE (? = 0 OR protein_id = ?) AND "
                    + "(? = 0 OR metagenome_id = ?) AND "
                    + "(? = 0 OR project_id = ?) AND "
	                + "(? = '' OR project_name = ?) AND "
	                + "(? = '' OR project_repository = ?) AND "
	                + "(? = '' OR protein_hash = ?) "+
                "ORDER BY protein_id LIMIT ? OFFSET ?",
                protein.getId(),protein.getId(),
                mg.getId(),mg.getId(),
                mg.getProject().getId(),mg.getProject().getId()
            ).bind(string(7,mg.getProject().getName()))
             .bind(string(8,mg.getProject().getName()))
             .bind(string(9,mg.getProject().getRepository()))
             .bind(string(10,mg.getProject().getRepository()))
             .bind(string(11,protein.getSequence().toString()))
             .bind(string(12,protein.getSHA1().asHexString()))
             .bind(integer(13, count))
             .bind(integer(14, start));
            
            val statement = sql.bind(query).bind(get);
            return read(statement).toCollection();
    }

    @Override
    public Collection<MetaGenomeProteins> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException {
        return getByForeignIdentifier(
            "protein_id", protein.getId(), start, count
        );
    }

    @Override
    public Collection<MetaGenomeProteins> getByMetaGenome(
        final MetaGenome mg, final int start, final int count
    ) throws DAOException {
        return getByForeignIdentifier(
            "metagenome_id", mg.getId(), start, count
        );
    }


    @Override
    protected MetaGenomeProteins parse(final ResultSet results) throws SQLException {
        val id      = parseIdentifier(results, "metagenome_proteins_id");
        val metagenome = parseMetaGenome(results);        
        val protein = parseProtein(results);
        val counter = parseLong(results, "counter");

        return metagenomeProteins(id,metagenome, protein, counter);
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final MetaGenomeProteins metagenomeProteins) {
        val metagenomeId = metagenomeProteins.getMetagenome().getId();
    	val proteinId = metagenomeProteins.getProtein().getId();

        return sql(
            "SELECT metagenome_proteins_id,"
            + " metagenome_id,"
            + " project_id, project_name, project_repository,"
            + " protein_id, protein_sequence"
            + " counter FROM " + TABLES + " WHERE metagenome_id = ? AND protein_id = ? LIMIT 1",
            metagenomeId,proteinId
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
            "SELECT metagenome_proteins_id,"
            + " metagenome_id,"
            + " project_id, project_name, project_repository,"
            + " protein_id, protein_sequence"
            + " counter"
            + " FROM "+ TABLES +" "
            + "ORDER BY metagenome_proteins_id "
            + "LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(metagenome_proteins_id) FROM metagenome_proteins");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final MetaGenomeProteins metagenomeProteins) {
    	val metagenomeId = metagenomeProteins.getMetagenome().getId();
    	val proteinId = metagenomeProteins.getProtein().getId();
        val counter = metagenomeProteins.getCounter();

        return sql(
            "INSERT INTO metagenome_proteins (metagenome_id, protein_id, counter) VALUES (?, ?, ?)",
            metagenomeId, proteinId
        ).bind(longInt(4, counter));
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM metagenome_proteins WHERE metagenome_proteins_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final MetaGenomeProteins entity) {
        val id      = entity.getId();
        val counter = entity.getCounter();

        return prepare(
            "UPDATE metagenome_proteins SET counter = ? WHERE metagenome_proteins_id = ?"
        ).bind(longInt(1, counter)).bind(identifier(2, id));
    }
   
    protected MetaGenome parseMetaGenome(final ResultSet results) throws SQLException {
        val id      = parseIdentifier(results, "metagenome_id");
        val project = parseProject(results);

        return metagenome(id, project, Fasta.empty());
    }

    private Protein parseProtein(final ResultSet results) throws SQLException {
    	val id  = parseIdentifier(results, "protein_id");
        val seq = parseAASequence(results, "protein_sequence");
        return protein(id, seq);
    }
    
    private Project parseProject(final ResultSet results) throws SQLException {
        val id   = parseIdentifier(results, "project_id");
        val name = parseString(results, "project_name");
        val repo = parseString(results, "project_repository");

        return project(id, name, repo);
    }

    private Collection<MetaGenomeProteins> getByForeignIdentifier(
        final String     columnName,
        final Identifier id,
        final int        start,
        final int        count
    ) throws DAOException {
        val sql = sql(
            "SELECT metagenome_proteins_id,"
                    + " metagenome_id,"
                    + " project_id, project_name, project_repository,"
                    + " protein_id, protein_sequence"
                    + " counter"
            + " FROM " + TABLES + " WHERE " + columnName + " = ? ORDER BY protein_id LIMIT ? OFFSET ?",
            id
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(get);

        return read(statement).toCollection();
    }

}
