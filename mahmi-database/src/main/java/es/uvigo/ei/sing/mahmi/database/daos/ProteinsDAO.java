package es.uvigo.ei.sing.mahmi.database.daos;

import lombok.val;

import fj.data.Option;
import fj.data.Set;
import fj.function.Effect1;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public interface ProteinsDAO extends DAO<Protein> {

    public Option<Protein> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException;

    public long countByMetaGenome(final MetaGenome mg);

    public Set<Protein> getByMetaGenome(
        final MetaGenome mg, final int start, final int count
    ) throws DAOException;

    public Set<Protein> search(
        final MetaGenome metagenome,
        final AminoAcidSequence sequence,
        final int start,
        final int count
    ) throws DAOException;

    public default void forEachProteinOf(
        final MetaGenome            metaGenome,
        final Effect1<Set<Protein>> effect
    ) throws DAOException {
        val pageSize = 1000; // TODO: configurable pageSize
        val numPages = countByMetaGenome(metaGenome);

        for (int pageNum = 0; pageNum < numPages; pageNum += pageSize) {
            effect.f(getByMetaGenome(metaGenome, pageNum, pageSize));
        }
    }

}
