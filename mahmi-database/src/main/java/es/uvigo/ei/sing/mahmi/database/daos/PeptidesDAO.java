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

}
