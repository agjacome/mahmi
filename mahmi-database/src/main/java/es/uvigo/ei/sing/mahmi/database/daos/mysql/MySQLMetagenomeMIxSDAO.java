package es.uvigo.ei.sing.mahmi.database.daos.mysql;

import static es.uvigo.ei.sing.mahmi.common.entities.MetagenomeMIxS.metagenomeMIxS;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseIdentifier;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.parseString;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.query;
import static es.uvigo.ei.sing.mahmi.database.utils.FunctionalJDBC.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeMIxS;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetagenomeMIxSDAO;
import fj.control.db.DB;
import fj.data.Option;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(IterableExtensionMethods.class)
public class MySQLMetagenomeMIxSDAO extends MySQLAbstractDAO<MetagenomeMIxS> implements MetagenomeMIxSDAO{
    
    private MySQLMetagenomeMIxSDAO(final ConnectionPool connectionPool) {
        super(connectionPool);
    }

    public static MetagenomeMIxSDAO mysqlMetagenomeMIxSDAO(
        final ConnectionPool connectionPool
    ) {
        return new MySQLMetagenomeMIxSDAO(connectionPool);
    }
    
    @Override
    protected DB<PreparedStatement> prepareUpdate(final MetagenomeMIxS metagenomeInformation) {
    	throw new UnsupportedOperationException("Not valid operation");        
    }   
  
  
    @Override
    protected MetagenomeMIxS parse(ResultSet resultSet) throws SQLException {    	
        final Identifier id = parseIdentifier(resultSet, "metagenome_mixs_id");
        final String     name = parseString(resultSet, "metagenome_mixs_name");
	    final String     investigation_type = parseString(resultSet, "metagenome_mixs_investigation_type");
	    final String 	 project_name = parseString(resultSet, "metagenome_mixs_project_name");
	    final String 	 sequencing_method = parseString(resultSet, "metagenome_mixs_sequencing_method");
	    final String 	 collection_date = parseString(resultSet, "metagenome_mixs_collection_date");
	    final String 	 enviromental_package = parseString(resultSet, "metagenome_mixs_enviromental_package");
	    final String 	 latitude = parseString(resultSet, "metagenome_mixs_latitude");
	    final String 	 longitude = parseString(resultSet, "metagenome_mixs_longitude");
	    final String 	 location = parseString(resultSet, "metagenome_mixs_location");
	    final String 	 biome = parseString(resultSet, "metagenome_mixs_biome");
	    final String 	 feature = parseString(resultSet, "metagenome_mixs_feature");
	    final String 	 material = parseString(resultSet, "metagenome_mixs_material");
        return metagenomeMIxS(id, name, investigation_type, project_name, 
				  sequencing_method, collection_date, enviromental_package,
				  latitude, longitude, location, biome, feature, material );
    }
    
    @Override
    protected DB<PreparedStatement> prepareSelect(MetagenomeMIxS proteinInformation) {
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
    protected DB<PreparedStatement> prepareInsert(MetagenomeMIxS proteinInformation) {
        throw new UnsupportedOperationException("Not valid operation");
    }
    
    @Override
    protected DB<PreparedStatement> prepareDelete(Identifier id) {
        throw new UnsupportedOperationException("Not valid operation");
    }

	@Override
	public Option<MetagenomeMIxS> getByProtein(final Protein protein)
			throws DAOException {
		val sql = sql(
                "SELECT * FROM metagenome_mixs WHERE metagenome_mixs_name = ?",
                protein.getName().split("_")[0]
            ).bind(query).bind(get);
        return read(sql).toOption();
	}
   
}
