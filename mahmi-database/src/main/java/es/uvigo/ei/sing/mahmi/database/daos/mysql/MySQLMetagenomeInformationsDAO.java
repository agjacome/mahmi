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
    	return metagenomeInformation(parseIdentifier(resultSet, "metagenome_information_id"),
    								 parseProtein(resultSet),
	    							 parseString(resultSet, "gene_length"),
							    	 parseString(resultSet, "gene_completeness"),
							    	 parseString(resultSet, "gene_origin"),
							    	 parseString(resultSet, "species_annotation_phylum"),
							    	 parseString(resultSet, "species_annotation_genus"),
							    	 parseString(resultSet, "kegg_annotation_phylum"),
							    	 parseString(resultSet, "sample_occurrence_frequency"),
							    	 parseString(resultSet, "individual_occurrence_frequency"),
							    	 parseString(resultSet, "kegg_functional_category"));
    }
    
    private Protein parseProtein(ResultSet resultSet) throws SQLException {
    	return protein(parseIdentifier(resultSet, "protein_id"),
        			   parseAASequence(resultSet, "protein_sequence"),
        			   parseString(resultSet, "protein_name"));
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
                "SELECT * FROM proteins NATURAL JOIN metagenome_information WHERE protein_id=?",
                protein.getId()
            ).bind(query).bind(get);
        return read(sql).toOption();
	}

	@Override
	public Set<MetagenomeInformation> getByPeptide(Peptide peptide)
			throws DAOException {
		val sql = sql(
                "SELECT * FROM peptides natural join proteins natural join digestions natural join metagenome_information WHERE peptide_id=?",
                peptide.getId()
            ).bind(query).bind(get);
        return read(sql).toSet(ordering);
	}
   
}
