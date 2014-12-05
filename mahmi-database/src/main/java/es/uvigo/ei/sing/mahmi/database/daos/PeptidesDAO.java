package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;

public interface PeptidesDAO extends DAO<Peptide> {

    public Option<Peptide> getBySequence(final AminoAcidSequence sequence) throws DAOException;

}
