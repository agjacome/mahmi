package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import fj.data.Option;

public interface MetaGenomeProteinsDAO extends DAO<MetaGenomeProteins> {

    public Option<MetaGenomeProteins> get(
        final MetaGenome metagenome, final Protein protein
    ) throws DAOException;

    public Collection<MetaGenomeProteins> getByProtein(
        final Protein protein, final int start, final int count
    ) throws DAOException;

    public Collection<MetaGenomeProteins> getByMetaGenome(
        final MetaGenome metagenome, final int start, final int count
    ) throws DAOException;

    public Collection<MetaGenomeProteins> search(
        final Protein    protein,
        final MetaGenome metagenome,
        final int        start,
        final int        count
    ) throws DAOException;

}
