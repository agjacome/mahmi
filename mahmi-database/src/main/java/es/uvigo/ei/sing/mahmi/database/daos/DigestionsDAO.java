package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import fj.data.Option;
import fj.data.Validation;

public interface DigestionsDAO extends DAO<Digestion> {

    public Validation<DAOException, Option<Digestion>> get(
        final Identifier enzymeId, final Identifier proteinId, final Identifier peptideId
    );

    public Validation<DAOException, Iterable<Digestion>> getByEnzymeId(
        final Identifier enzymeId, final int start, final int count
    );

    public Validation<DAOException, Iterable<Digestion>> getByProteinId(
        final Identifier proteinId, final int start, final int count
    );

    public Validation<DAOException, Iterable<Digestion>> getByPeptideId(
        final Identifier peptideId, final int start, final int count
    );

}
