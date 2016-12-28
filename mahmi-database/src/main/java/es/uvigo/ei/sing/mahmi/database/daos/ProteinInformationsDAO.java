package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.ProteinInformation;
import fj.data.Option;
import fj.data.Set;

public interface ProteinInformationsDAO extends DAO<ProteinInformation>{    
	public Option<ProteinInformation> getByProtein(
	        final Protein protein
	    ) throws DAOException;
	
	public Set<ProteinInformation> getByPeptide(
	        final Peptide peptide
	    ) throws DAOException;
}