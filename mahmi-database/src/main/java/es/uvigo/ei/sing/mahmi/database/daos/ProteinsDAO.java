package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;
import java.util.function.Consumer;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.data.Option;

public interface ProteinsDAO extends DAO<Protein> {

    public Option<Protein> getBySequence(
        final AminoAcidSequence sequence
    ) throws DAOException;

    public int countByMetaGenome(final MetaGenome mg);

    public Collection<Protein> getByMetaGenome(
        final MetaGenome mg, final int start, final int count
    ) throws DAOException;

    public default void forEachProteinOf(
        final MetaGenome                    metaGenome,
        final Consumer<Collection<Protein>> consumer
    ) throws DAOException {
        val pageSize = 1000;
        val numPages = countByMetaGenome(metaGenome);

        for (int pageNum = 0; pageNum < numPages; pageNum += pageSize) {
            consumer.accept(getByMetaGenome(metaGenome, pageNum, pageSize));
        }
    }

}
