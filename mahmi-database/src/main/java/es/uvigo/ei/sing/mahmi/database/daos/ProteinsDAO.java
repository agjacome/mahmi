package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;

public interface ProteinsDAO extends DAO<Protein> {

    public Option<Protein> getBySequence(final AminoAcidSequence sequence) throws DAOException;

}
