package es.uvigo.ei.sing.mahmi.controller.services;

import static fj.Unit.unit;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Set;
import java.util.function.Supplier;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.DAO;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import fj.F;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class DatabaseEntityAbstractService<A, B extends DAO<A>> {

    protected final B dao;

    protected final Response buildGet(final int id) {
        return respond(
            () -> dao.get(Identifier.of(id)),
            op -> op.option(status(NOT_FOUND), status(OK)::entity)
        );
    }

    protected final Response buildGetAll(final int page, final int size) {
        return respond(
            () -> dao.getAll((page - 1) * size, size),
            as -> status(OK).entity(mapSet(as))
        );
    }

    protected final Response buildInsert(final A entity) {
        return respond(
            () -> dao.insert(entity),
            status(CREATED)::entity
        );
    }

    protected final Response buildInsertAll(final Set<A> entities) {
        return respond(
            () -> dao.insertAll(entities),
            as -> status(CREATED).entity(mapSet(as))
        );
    }

    protected final Response buildDelete(final int id) {
        return respond(
            () -> { dao.delete(Identifier.of(id)); return unit(); },
            u  -> status(ACCEPTED)
        );
    }

    protected final Response buildUpdate(final A entity) {
        return respond(
            () -> { dao.update(entity); return unit(); },
            u  -> status(ACCEPTED)
        );
    }

    protected final <C> Response respond(
        final Supplier<C>               dataSupplier,
        final F<C, ResponseBuilder>     dataMapper
    ) {
        // FIXME: it won't always be a InternalServerError, sometimes it can
        // simply be a BadRequest. "Inspect" DAOException in some way (maybe add
        // info to the class like isDBError()? or isClientError()?, or even make
        // it an enum with different exception types à la Algebraic Data Type).
        try {
            return dataMapper.f(dataSupplier.get()).build();
        } catch (final DAOException daoe) {
            return status(INTERNAL_SERVER_ERROR).entity(daoe.getMessage()).build();
        }
    }

    protected abstract GenericEntity<Set<A>> mapSet(final Set<A> entities);

}
