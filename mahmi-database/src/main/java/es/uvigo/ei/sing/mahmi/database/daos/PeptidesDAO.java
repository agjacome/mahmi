package es.uvigo.ei.sing.mahmi.database.daos;

import fj.data.Option;
import fj.data.Set;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public interface PeptidesDAO extends DAO<Peptide> {

    public Option<Peptide> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException;

    public Set<Peptide> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public long countByMetagenome(
        final MetaGenome metagenome
    ) throws DAOException;

    public Set<Peptide> getByMetagenome(
        final MetaGenome metagenome, final int start, final int count
    ) throws DAOException;

    public default Set<Peptide> getAllPeptidesFromMetagenome(
        final MetaGenome metagenome
    ) throws DAOException {
        val count = 2000;
        val total = countByMetagenome(metagenome);

        Set<Peptide> ps = Set.empty(getByMetagenome(metagenome, 0, 1).ord());

        for (int start = 0; start < total; start += count)
            ps = ps.union(getByMetagenome(metagenome, start, count));

        return ps;
    }

    public Set<Peptide> search(
        final Protein           protein,
        final MetaGenome        metagenome,
        final AminoAcidSequence sequence,
        final Enzyme            enzyme,
        final int               start,
        final int               count
    ) throws DAOException;

}
