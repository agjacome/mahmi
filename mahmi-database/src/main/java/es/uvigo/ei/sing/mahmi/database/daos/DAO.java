package es.uvigo.ei.sing.mahmi.database.daos;

import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import fj.Unit;
import fj.data.Option;
import fj.data.Validation;

public interface DAO<A> {

    // FIXME: use Sets instead of Iterables
    // FIXME: in insertAll check for already inserted entities, and do not insert them (but do return them)

    public Validation<DAOException, Option<A>> get(final Identifier id);

    public Validation<DAOException, Iterable<A>> getAll(final int start, final int count);

    public Validation<DAOException, A> insert(final A entity);

    public Validation<DAOException, Iterable<A>> insertAll(final Iterable<A> entities);

    public Validation<DAOException, Unit> delete(final Identifier id);

    public Validation<DAOException, Unit> update(final A entity);

}
