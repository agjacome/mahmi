package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;
import fj.data.Validation;

public interface ProteinsDAO extends DAO<Protein> {

    public Validation<DAOException, Option<Protein>> getBySequence(final AminoAcidSequence seq);

}
