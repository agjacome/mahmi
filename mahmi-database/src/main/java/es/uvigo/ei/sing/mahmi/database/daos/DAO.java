package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Set;

import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import fj.data.Option;

public interface DAO<A> {

    public Option<A> get(final Identifier id) throws DAOException;

    public Set<A> getAll(final int start, final int count) throws DAOException;

    public A insert(final A entity) throws DAOException;

    public Set<A> insertAll(final Set<A> entities) throws DAOException;

    public void delete(final Identifier id) throws DAOException;

    public void update(final A entity) throws DAOException;

}
