package es.uvigo.ei.sing.mahmi.database.daos;

import lombok.val;

import fj.data.HashMap;
import fj.data.Option;
import fj.data.Set;
import fj.function.Effect1;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

public interface MetaGenomesDAO extends DAO<MetaGenome> {

    public Option<MetaGenome> getWithFasta(
        final Identifier id
    ) throws DAOException;
    
    public long countByProject(final Project project);

    public Set<MetaGenome> getByProject(
        final Project project, final int start, final int count
    ) throws DAOException;

    public void addProteins(
        final MetaGenome metaGenome, final HashMap<Protein, Long> proteins
    ) throws DAOException;

    // FIXME: there can be memory-related problems with this method
    public default void forEachMetaGenomeOf(
        final Project project, final Effect1<MetaGenome> effect
    ) {
        val numMetaGenomes = countByProject(project);
        getByProject(project, 0, (int) numMetaGenomes).forEach(
            metagenome -> effect.f(metagenome)
        );
    }

}
