package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Set;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import fj.data.Option;

public interface DigestionsDAO extends DAO<Digestion> {

    public Option<Digestion> get(
        final Identifier enzymeId, final Identifier proteinId, final Identifier peptideId
    ) throws DAOException;

    public Set<Digestion> getByEnzymeId(
        final Identifier enzymeId, final int start, final int count
    ) throws DAOException;

    public Set<Digestion> getByProteinId(
        final Identifier proteinId, final int start, final int count
    ) throws DAOException;

    public Set<Digestion> getByPeptideId(
        final Identifier peptideId, final int start, final int count
    ) throws DAOException;

}
