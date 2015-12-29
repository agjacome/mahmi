package es.uvigo.ei.sing.mahmi.database.daos;

import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public interface ReferencePeptidesDAO {

    public Set<AminoAcidSequence> getAll() throws DAOException;

}
