package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Map;

import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import fj.Unit;
import fj.data.Validation;

public interface MetaGenomesDAO extends DAO<MetaGenome> {

    // TODO: must be refined, maybe not all these operations should be
    // resposibility of MetaGenomesDAO

    public Validation<DAOException, Iterable<Identifier>> getIdsByProjectId(
        final Identifier projectId, final int start, final int count
    );

    public Validation<DAOException, Iterable<Identifier>> getIdsByProteinId(
        final Identifier proteinId, final int start, final int count
    );

    public Validation<DAOException, Unit> addProteinToMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId, final long counter
    );

    public Validation<DAOException, Unit> addAllProteinsToMetaGenome(
        final Identifier metaGenomeId, final Map<Identifier, Long> proteinCounters
    );

    public Validation<DAOException, Unit> deleteProteinFromMetaGenome(
        final Identifier metaGenomeId, final Identifier proteinId
    );

}
