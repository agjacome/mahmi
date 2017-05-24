package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Set;
import fj.function.Effect1;
import lombok.val;

public interface ProteinsDAO extends DAO<Protein> {

    public long countByMetaGenome(final MetaGenome mg);

    public Set<Protein> getByMetaGenome(
        final MetaGenome mg, final int start, final int count
    ) throws DAOException;

    public default void forEachProteinOf(
        final MetaGenome            metaGenome,
        final Effect1<Set<Protein>> effect
    ) throws DAOException {
        val pageSize = 250; // TODO: configurable pageSize
        val numPages = countByMetaGenome(metaGenome);

        for (int pageNum = 0; pageNum < numPages; pageNum += pageSize)
            effect.f(getByMetaGenome(metaGenome, pageNum, pageSize));
    }
    
    public Set<Protein> getByPeptide(
    		final Peptide peptide
    ) throws DAOException;

}
