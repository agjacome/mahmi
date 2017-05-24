package es.uvigo.ei.sing.mahmi.database.daos;

import fj.data.Option;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;

public interface EnzymesDAO extends DAO<Enzyme> {
    public Option<Enzyme> getByName(final String name) throws DAOException;

}
