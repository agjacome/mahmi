package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeMIxS;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Option;

public interface MetagenomeMIxSDAO extends DAO<MetagenomeMIxS>{ 
	public Option<MetagenomeMIxS> getByProtein(
	        final Protein protein
	    ) throws DAOException;
}