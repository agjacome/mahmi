package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Option;
import fj.data.Set;

public interface MetaGenomeProteinsDAO extends DAO<MetaGenomeProteins> {

    public Option<MetaGenomeProteins> get(
        final MetaGenome metagenome, final Protein protein
    ) throws DAOException;

    public Set<MetaGenomeProteins> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public Set<MetaGenomeProteins> getByMetaGenome(
        final MetaGenome metagenome, final int start, final int count
    ) throws DAOException;

    public Set<MetaGenomeProteins> search(
        final Protein    protein,
        final MetaGenome metagenome,
        final int        start,
        final int        count
    ) throws DAOException;

}
