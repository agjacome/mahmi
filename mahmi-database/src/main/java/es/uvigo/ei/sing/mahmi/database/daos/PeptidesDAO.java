package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;

public interface PeptidesDAO extends DAO<Peptide> {

    public Option<Peptide> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException;

    public Collection<Peptide> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;
    
    public Collection<Peptide> getByEnzymeId(
            final int enzymeId, final int start, final int count
        ) throws DAOException;
    
    public Collection<Peptide> getByProteinId(
            final int proteinId, final int start, final int count
        ) throws DAOException;
    
    public Collection<Peptide> getByProjectId(
            final int projectId, final int start, final int count
      ) throws DAOException;
    
    public Collection<Peptide> getByProjectName(
            final String projectName, final int start, final int count
      ) throws DAOException;
    
    public Collection<Peptide> getByProjectRepository(
            final String projectRepository, final int start, final int count
      ) throws DAOException;
    
    public Collection<Peptide> getByMetaGenomeId(
            final int metagenomeId, final int start, final int count
        ) throws DAOException;

}
