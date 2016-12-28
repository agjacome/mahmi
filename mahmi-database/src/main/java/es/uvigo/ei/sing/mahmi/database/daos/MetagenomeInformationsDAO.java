package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeInformation;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Option;
import fj.data.Set;

public interface MetagenomeInformationsDAO extends DAO<MetagenomeInformation>{    
	public Option<MetagenomeInformation> getByProtein(
	        final Protein protein
	    ) throws DAOException;
	
	public Set<MetagenomeInformation> getByPeptide(
	        final Peptide peptide
	    ) throws DAOException;
}