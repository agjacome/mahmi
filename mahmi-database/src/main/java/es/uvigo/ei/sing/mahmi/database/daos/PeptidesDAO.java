package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;
import fj.data.Set;

public interface PeptidesDAO extends DAO<Peptide> {

    public Option<Peptide> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException;

    public Set<Peptide> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public Set<Peptide> search(
        final Protein           protein,
        final MetaGenome        metagenome,
        final AminoAcidSequence sequence,
        final Enzyme            enzyme,
        final int               start,
        final int               count
    ) throws DAOException;

}
