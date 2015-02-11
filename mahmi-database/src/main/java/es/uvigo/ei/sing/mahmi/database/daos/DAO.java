package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Entity;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

import fj.data.Option;
import fj.data.Set;

public interface DAO<A extends Entity<A>> {

    public long count() throws DAOException;

    public Option<A> get(final Identifier id) throws DAOException;

    public Set<A> getAll(final int start, final int count) throws DAOException;

    public A insert(final A entity) throws DAOException;

    public Set<A> insertAll(final Set<A> entities) throws DAOException;

    public void delete(final Identifier id) throws DAOException;

    public void update(final A entity) throws DAOException;

}
