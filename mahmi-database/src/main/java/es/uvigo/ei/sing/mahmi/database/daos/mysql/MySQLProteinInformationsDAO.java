package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.common.entities.ProteinInformation.proteinInformation;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseAASequence;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.ProteinInformation;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinInformationsDAO;
import fj.control.db.DB;
import fj.data.Option;
import fj.data.Set;

@ExtensionMethod(IterableExtensionMethods.class)
public class MySQLProteinInformationsDAO extends MySQLAbstractDAO<ProteinInformation> implements ProteinInformationsDAO{
    
    private MySQLProteinInformationsDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static ProteinInformationsDAO mysqlProteinInformationsDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLProteinInformationsDAO(connectionPool);
    }
    
    @Override
    protected DB<PreparedStatement> prepareUpdate(final ProteinInformation proteinInformation) {
    	throw new UnsupportedOperationException("Not valid operation");        
    }   
  
  
    @Override
    protected ProteinInformation parse(ResultSet resultSet) throws SQLException {
    	return proteinInformation(parseIdentifier(resultSet, "protein_information_id"),
						          parseProtein(resultSet),
						          parseString(resultSet, "uniprot_id"),
						          parseString(resultSet, "uniprot_organism"),
						          parseString(resultSet, "uniprot_protein"),
						          parseString(resultSet, "uniprot_gene"));
    }
    
    private Protein parseProtein(ResultSet resultSet) throws SQLException {
    	return protein(parseIdentifier(resultSet, "protein_id"),
			           parseAASequence(resultSet, "protein_sequence"),
			           parseString(resultSet, "protein_name"));
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(ProteinInformation proteinInformation) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareCount() {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(Identifier id) {
        return sql(
            "SELECT * FROM protein_information NATURAL JOIN digestions NATURAL JOIN proteins WHERE protein_information_id=?",
            id
        );
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(int limit, int offset) {
        return sql(
            "SELECT * FROM protein_information NATURAL JOIN digestions NATURAL JOIN proteins LIMIT ? OFFSET ?",
        limit, offset
        );
    }
    
    @Override
    protected DB<PreparedStatement> prepareInsert(ProteinInformation proteinInformation) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareDelete(Identifier id) {
        throw new UnsupportedOperationException("Not valid operation");
    }

	@Override
	public Option<ProteinInformation> getByProtein(Protein protein)
			throws DAOException {
		val sql = sql(
            "SELECT * FROM proteins NATURAL JOIN digestions NATURAL JOIN protein_information WHERE protein_id=?",
            protein.getId()
        ).bind(query).bind(get);
        return read(sql).toOption();
	}

	@Override
	public Set<ProteinInformation> getByPeptide(Peptide peptide)
			throws DAOException {
		val sql = sql(
            "SELECT * FROM peptides NATURAL JOIN digestions NATURAL JOIN proteins NATURAL JOIN protein_information WHERE peptide_id=?",
            peptide.getId()
        ).bind(query).bind(get);
        return read(sql).toSet(ordering);
	}
   
}
