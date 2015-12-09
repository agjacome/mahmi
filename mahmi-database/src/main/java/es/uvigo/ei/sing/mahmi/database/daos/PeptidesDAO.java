package es.uvigo.ei.sing.mahmi.database.daos;

import fj.data.Option;
import fj.data.Set;
import fj.function.Effect1;
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

    public default void forEachPeptideOfMetagenome(
        final MetaGenome metagenome, final Effect1<Set<Peptide>> effect
    ) throws DAOException {
        val pageSize = 2000; // TODO: configurable pageSize
        val numPages = countByMetagenome(metagenome);

        for (int pageNum = 0; pageNum < numPages; pageNum += pageSize)
            effect.f(getByMetagenome(metagenome, pageNum, pageSize));
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
