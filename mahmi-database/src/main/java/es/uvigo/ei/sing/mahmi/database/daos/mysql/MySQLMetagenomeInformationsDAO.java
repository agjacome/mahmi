package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetagenomeInformation.metagenomeInformation;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeInformation;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetagenomeInformationsDAO;
import fj.control.db.DB;
import fj.data.Option;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public class MySQLMetagenomeInformationsDAO extends MySQLAbstractDAO<MetagenomeInformation> implements MetagenomeInformationsDAO{
    
    private MySQLMetagenomeInformationsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static MetagenomeInformationsDAO mysqlMetagenomeInformationsDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLMetagenomeInformationsDAO(connectionPool);
    }
    
    @Override
    protected DB<PreparedStatement> prepareUpdate(final MetagenomeInformation metagenomeInformation) {
    	throw new UnsupportedOperationException("Not valid operation");        
    }   
  
  
    @Override
    protected MetagenomeInformation parse(ResultSet resultSet) throws SQLException {    	
        final Identifier id = parseIdentifier(resultSet, "metagenome_information_id");
	    final Protein protein = parseProtein(resultSet);
	    final String  geneLength = parseString(resultSet, "gene_length");
	    final String  geneCompleteness = parseString(resultSet, "gene_completeness");
	    final String  geneOrigin = parseString(resultSet, "gene_origin");
	    final String  speciesAnnotationPhylum = parseString(resultSet, "species_annotation_phylum");
	    final String  speciesAnnotationGenus = parseString(resultSet, "species_annotation_genus");
	    final String  keggAnnotationPhylum = parseString(resultSet, "kegg_annotation_phylum");
	    final String  sampleOcurrenceFrequency = parseString(resultSet, "sample_occurrence_frequency");
	    final String  individualOcurrenceFrequency = parseString(resultSet, "individual_occurrence_frequency");
	    final String  keggFunctionalCategory  = parseString(resultSet, "kegg_functional_category");
        return metagenomeInformation(id, protein, geneLength, 
					        		 geneCompleteness, geneOrigin, speciesAnnotationPhylum, speciesAnnotationGenus, 
					        		 keggAnnotationPhylum, sampleOcurrenceFrequency,  individualOcurrenceFrequency,
					        		 keggFunctionalCategory  );
    }
    
    private Protein parseProtein(ResultSet resultSet) throws SQLException {
    	val id  = parseIdentifier(resultSet, "protein_id");
        val seq = parseAASequence(resultSet, "protein_sequence");
    	val name = parseString(resultSet, "protein_name");
    	return protein(id, seq, name);
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(MetagenomeInformation proteinInformation) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareCount() {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(Identifier id) {
        return sql(
                "SELECT * FROM metagenome_information NATURAL JOIN digestions NATURAL JOIN proteins WHERE metagenome_information_id=?",
                id
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(int limit, int offset) {
        return sql(
                "SELECT * FROM metagenome_information NATURAL JOIN digestions NATURAL JOIN proteins LIMIT ? OFFSET ?",
            limit, offset
            );
    }
    
    @Override
    protected DB<PreparedStatement> prepareInsert(MetagenomeInformation proteinInformation) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareDelete(Identifier id) {
        throw new UnsupportedOperationException("Not valid operation");
    }

	@Override
	public Option<MetagenomeInformation> getByProtein(Protein protein)
			throws DAOException {
		val sql = sql(
                "SELECT * FROM metagenome_information NATURAL JOIN proteins WHERE protein_id=?",
                protein.getId()
            ).bind(query).bind(get);
        return read(sql).toOption();
	}

	@Override
	public Set<MetagenomeInformation> getByPeptide(Peptide peptide)
			throws DAOException {
		val sql = sql(
                "SELECT * FROM metagenome_information NATURAL JOIN digestions NATURAL JOIN proteins NATURAL JOIN peptides WHERE peptide_id=?",
                peptide.getId()
            ).bind(query).bind(get);
        return read(sql).toSet(ordering);
	}
   
}
