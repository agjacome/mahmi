package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.BioactivePeptide;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.ReferencePeptide;
import fj.data.Set;

public interface PeptidesDAO extends DAO<Peptide> {;
    
    public Set<BioactivePeptide> getBioactives (final int start, final int count
    ) throws DAOException;
    
    public Set<BioactivePeptide> getBioactivesByOrganism (final int start, final int count, final String organism
    ) throws DAOException;
    
    public Set<BioactivePeptide> getBioactivesByProtein (final int start, final int count, final String protein
    ) throws DAOException;
    
    public Set<BioactivePeptide> getBioactivesByGene (final int start, final int count, final String gene
    ) throws DAOException;
    
    public Set<BioactivePeptide> getBioactivesByLength (final int start, final int count, final String length
    ) throws DAOException;
    
    public Set<ReferencePeptide> getReferences() throws DAOException;

}
