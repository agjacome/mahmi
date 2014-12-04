package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;
import fj.data.Validation;

public interface PeptidesDAO extends DAO<Peptide> {

    public Validation<DAOException, Option<Peptide>> getBySequence(final AminoAcidSequence seq);

}
