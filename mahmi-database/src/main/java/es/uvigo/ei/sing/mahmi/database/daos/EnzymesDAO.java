package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import fj.data.Option;

public interface EnzymesDAO extends DAO<Enzyme> {

    public Option<Enzyme> getByName(final String name) throws DAOException;

}
