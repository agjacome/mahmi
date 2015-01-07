package es.uvigo.ei.sing.mahmi.database.daos;

import java.util.Collection;
import java.util.Set;

import es.uvigo.ei.sing.mahmi.common.entities.Entity;
import es.uvigo.ei.sing.mahmi.common.utils.Count;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import fj.data.Option;

public interface DAO<A extends Entity<A>> {

    public Option<A> get(final Identifier id) throws DAOException;

    public Collection<A> getAll(
        final int start, final int count
    ) throws DAOException;
    
    public Option<Count> getCount () throws DAOException;

    public A insert(final A entity) throws DAOException;

    public Collection<A> insertAll(final Set<A> entities) throws DAOException;

    public void delete(final Identifier id) throws DAOException;

    public void update(final A entity) throws DAOException;

}
