package es.uvigo.ei.sing.mahmi.http.services;

import static fj.Unit.unit;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Entity;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.DAO;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import fj.F;
import fj.data.Option;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class DatabaseEntityAbstractService<A extends Entity<A>, B extends DAO<A>> {

    protected final B dao;

    protected final Response buildGet(final Identifier id) {
        return respond(
            () -> dao.get(id),
            status(OK)::entity,
            status(NOT_FOUND)
        );
    }
    
    protected final Response buildGetCount(){
    	return respond(
    			() -> dao.getCount(),
                status(OK)::entity,
                status(NOT_FOUND)
            );
    }

    protected final Response buildGetAll(final int page, final int size) {
        return respond(
            () -> dao.getAll((page - 1) * size, size),
            as -> status(OK).entity(toGenericEntity(as))
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
            as -> status(CREATED).entity(toGenericEntity(as))
        );
    }

    protected final Response buildDelete(final Identifier id) {
        return respond(
            () -> { dao.delete(id); return unit(); },
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
        final Supplier<C>           dataSupplier,
        final F<C, ResponseBuilder> dataMapper
    ) {
        try {
            return dataMapper.f(dataSupplier.get()).build();
        } catch (final DAOException daoe) {
            log.error("Error while accessing database", daoe);
            // FIXME: it won't always be a InternalServerError, sometimes it can
            // simply be a BadRequest. "Inspect" DAOException in some way (maybe
            // add info to the class like isDBError()? or isClientError()?, or
            // even make it an enum with different exception types à la
            // Algebraic Data Type).
            return status(INTERNAL_SERVER_ERROR).entity(
                daoe.getMessage()
            ).build();
        } catch (final Exception e) {
            log.error("Unexpected error on response building", e);
            return status(BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    protected final <C> Response respond(
        final Supplier<Option<C>>   optSupplier,
        final F<C, ResponseBuilder> someMapper,
        final ResponseBuilder       noneResponse
    ) {
        return respond(optSupplier, op -> op.option(noneResponse, someMapper));
    }  

    protected abstract GenericEntity<List<A>> toGenericEntity(
        final Collection<A> entities
    );

}
