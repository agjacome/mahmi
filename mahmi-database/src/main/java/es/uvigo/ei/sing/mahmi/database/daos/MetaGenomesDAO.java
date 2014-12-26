package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import fj.data.Option;

public interface MetaGenomesDAO extends DAO<MetaGenome> {

    public Option<MetaGenome> getWithFasta(
        final Identifier id
    ) throws DAOException;

    public int countByProject(final Project project);

    public Collection<MetaGenome> getByProject(
        final Project project, final int start, final int count
    ) throws DAOException;

    public Collection<MetaGenome> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public void addProtein(
        final MetaGenome metaGenome, final Protein protein, final long counter
    ) throws DAOException;

    public void addProteins(
        final MetaGenome metaGenome, final Map<Protein, Long> proteins
    ) throws DAOException;

    public void removeProtein(
        final MetaGenome metaGenome, final Protein protein
    ) throws DAOException;

    // FIXME: there can be memory-related problems with the following method
    public default void forEachMetaGenomeOf(
        final Project project, final Consumer<MetaGenome> effect
    ) {
        val numMetaGenomes = countByProject(project);
        getByProject(project, 0, numMetaGenomes).forEach(effect);
    }

}
