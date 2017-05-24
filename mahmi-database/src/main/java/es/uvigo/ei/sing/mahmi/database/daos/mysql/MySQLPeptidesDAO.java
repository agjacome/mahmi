package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.BioactivePeptide.bioactivePeptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.ReferencePeptide.referencePeptide;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.getWith;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.identifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.integer;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseDouble;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.prepare;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.BioactivePeptide;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.ReferencePeptide;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.Ord;
import fj.control.db.DB;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public final class MySQLPeptidesDAO extends MySQLAbstractDAO<Peptide> implements PeptidesDAO {

	private final Ord<ReferencePeptide> orderRef = Identifier.ord.contramap(a -> a.getId());
	private final Ord<BioactivePeptide> orderBio = Identifier.ord.contramap(a -> a.getId());
	
    private MySQLPeptidesDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static PeptidesDAO mysqlPeptidesDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLPeptidesDAO(connectionPool);
    }
        
    @Override
    public Set<BioactivePeptide> getBioactives (final int start, final int count
    		) throws DAOException {
    	val sql = sql(
            "SELECT peptide_id, peptide_sequence, reference_id, reference_sequence, reference_type, " +
            "bioactive_similarity FROM (SELECT DISTINCT peptide_hash FROM bioactive_peptides LIMIT ? "+
            "OFFSET ?) temp natural join peptides natural join bioactive_peptides natural join " +
            "reference_peptides",count, start
        );

        val statement = sql.bind(query).bind(getWith(this::parseBioactive));
        return read(statement).toSet(orderBio);
    }
    
    @Override
    public Set<BioactivePeptide> getBioactivesByOrganism (final int start, final int count, final String organism
    		) throws DAOException {
    	val sql = sql(
			"SELECT peptide_id, peptide_sequence, reference_id, reference_sequence, reference_type, " +
    	    "bioactive_similarity FROM (SELECT DISTINCT peptide_id FROM protein_information " +
    	    "STRAIGHT_JOIN digestions ON (protein_information.protein_id = digestions.protein_id " +
    	    "AND uniprot_organism LIKE ?) NATURAL JOIN peptides NATURAL JOIN bioactive_peptides " +
    	    "LIMIT ? OFFSET ?) temp natural join peptides natural join bioactive_peptides natural join " +
    	    "reference_peptides",organism
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(getWith(this::parseBioactive));
        return read(statement).toSet(orderBio);
    }
    
    @Override
    public Set<BioactivePeptide> getBioactivesByGene (final int start, final int count, final String gene
    		) throws DAOException {
    	val sql = sql(
            "SELECT peptide_id, peptide_sequence, reference_id, reference_sequence, reference_type, " +
    	    "bioactive_similarity FROM (SELECT DISTINCT peptide_id FROM protein_information " +
    	    "STRAIGHT_JOIN digestions ON (protein_information.protein_id = digestions.protein_id " +
    	    "AND uniprot_gene LIKE ?) NATURAL JOIN peptides NATURAL JOIN bioactive_peptides " +
    	    "LIMIT ? OFFSET ?) temp natural join peptides natural join bioactive_peptides natural join " +
    	    "reference_peptides",gene
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(getWith(this::parseBioactive));
        return read(statement).toSet(orderBio);
    }
    
    @Override
    public Set<BioactivePeptide> getBioactivesByProtein (final int start, final int count, final String protein
    		) throws DAOException {
    	val sql = sql(
			"SELECT peptide_id, peptide_sequence, reference_id, reference_sequence, reference_type, " +
    	    "bioactive_similarity FROM (SELECT DISTINCT peptide_id FROM protein_information " +
    	    "STRAIGHT_JOIN digestions ON (protein_information.protein_id = digestions.protein_id " +
    	    "AND uniprot_protein LIKE ?) NATURAL JOIN peptides NATURAL JOIN bioactive_peptides " +
    	    "LIMIT ? OFFSET ?) temp natural join peptides natural join bioactive_peptides natural join " +
    	    "reference_peptides",protein
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(getWith(this::parseBioactive));
        return read(statement).toSet(orderBio);
    }
    
    @Override
    public Set<BioactivePeptide> getBioactivesByLength (final int start, final int count, final String length
    		) throws DAOException {
    	val sql = sql(
            "SELECT peptide_id, peptide_sequence, bioactive_similarity, " + 
            "reference_id, reference_sequence, reference_type "   +
            "FROM (SELECT DISTINCT peptides.peptide_id, peptides.peptide_sequence " +
            "FROM protein_information STRAIGHT_JOIN digestions ON " +
            "(protein_information.protein_id = digestions.protein_id) "+
            "STRAIGHT_JOIN peptides ON (digestions.peptide_id = peptides.peptide_id AND " +
            "LENGTH(CONVERT(peptide_sequence USING utf8)) = ?) " +
            "NATURAL JOIN bioactive_peptides LIMIT ? OFFSET ?) temp natural join peptides natural " +
            "join bioactive_peptides natural join reference_peptides",length
        ).bind(integer(2, count)).bind(integer(3, start));

        val statement = sql.bind(query).bind(getWith(this::parseBioactive));
        return read(statement).toSet(orderBio);
    }
    
    @Override
    public Set<ReferencePeptide> getReferences() throws DAOException{
    	val sql = prepare(
            "SELECT reference_id, reference_sequence, reference_type "   +
            "FROM reference_peptides"
        );

        val statement = sql.bind(query).bind(getWith(this::parseReference));
        return read(statement).toSet(orderRef);
    }

    @Override
    protected Peptide parse(final ResultSet results) throws SQLException {
    	return peptide(parseIdentifier(results, "peptide_id"),
    				   parseAASequence(results, "peptide_sequence"));
    }
    
    private ReferencePeptide parseReference(final ResultSet results) throws SQLException {
    	return referencePeptide(parseIdentifier(results, "reference_id"),
        						parseAASequence(results, "reference_sequence"),
        						parseString(results, "reference_type"));
    }
    
    private BioactivePeptide parseBioactive(final ResultSet results) throws SQLException {
    	return bioactivePeptide(parseIdentifier(results, "peptide_id"),
								parseAASequence(results, "peptide_sequence"),
								parseDouble(results, "bioactive_similarity"),
								parseReference(results));
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Peptide peptide) {
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides WHERE peptide_hash = ? LIMIT 1",
            peptide.getSHA1().asHexString()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(final Identifier id) {
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides WHERE peptide_id = ? LIMIT 1",
            id
        );
    }

    @Override
    protected DB<PreparedStatement> prepareSelect(
        final int limit, final int offset
    ) {
        return sql(
            "SELECT peptide_id, peptide_sequence FROM peptides ORDER BY peptide_id LIMIT ? OFFSET ?",
            limit, offset
        );
    }

    @Override
    public DB<PreparedStatement> prepareCount() {
        return prepare("SELECT COUNT(peptide_id) FROM peptides");
    }

    @Override
    protected DB<PreparedStatement> prepareInsert(final Peptide peptide) {
        return sql(
            "INSERT INTO peptides (peptide_hash, peptide_sequence) VALUES (?, ?)",
            peptide.getSHA1().asHexString(), peptide.getSequence().asString()
        );
    }

    @Override
    protected DB<PreparedStatement> prepareDelete(final Identifier id) {
        return sql("DELETE FROM peptides WHERE peptide_id = ?", id);
    }

    @Override
    protected DB<PreparedStatement> prepareUpdate(final Peptide peptide) {
      return sql(
          "UPDATE peptides SET peptide_hash = ?, peptide_sequence = ? WHERE peptide_id = ?",
          peptide.getSHA1().asHexString(), peptide.getSequence().asString()
      ).bind(identifier(3, peptide.getId()));
    }
}
