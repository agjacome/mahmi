package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.utils.SHA1;

public interface BioactivePeptidesDAO {

    public void insert(
        final SHA1   referenceHash,
        final SHA1   peptideHash,
        final double percentage
    ) throws DAOException;

}
