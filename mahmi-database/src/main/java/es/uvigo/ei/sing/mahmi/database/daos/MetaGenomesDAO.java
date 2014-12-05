package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Map;
import java.util.Set;

import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;

public interface MetaGenomesDAO extends DAO<MetaGenome> {

    // TODO: must be refined, maybe not all these operations should be
    // resposibility of MetaGenomesDAO

    public Set<Identifier> getIdsByProjectId(
        final Identifier projectId, final int start, final int count
    ) throws DAOException;

    public Set<Identifier> getIdsByProteinId(
        final Identifier proteinId, final int start, final int count
    ) throws DAOException;

    public void addProteinToMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId, final long counter
    ) throws DAOException;

    public void addAllProteinsToMetaGenome(
        final Identifier metaGenomeId, final Map<Identifier, Long> proteinCounters
    ) throws DAOException;

    public void deleteProteinFromMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId
    ) throws DAOException;

}
