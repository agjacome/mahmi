package es.uvigo.ei.sing.mahmi.http.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fj.data.Set;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.EnzymesDAO;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import static fj.Ord.stringOrd;
import static fj.data.Set.iterableSet;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

@Path("/enzyme")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class EnzymeService extends DatabaseEntityAbstractService<Enzyme, EnzymesDAO> {

    private EnzymeService(final EnzymesDAO dao) {
        super(dao);
    }

    public static EnzymeService enzymeService(final EnzymesDAO dao) {
        return new EnzymeService(dao);
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") final int id) {
        return buildGet(Identifier.of(id));
    }

    @GET
    public Response get(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return buildGetAll(page, size);
    }

    @POST
    public Response insert(final Enzyme enzyme) {
        return buildInsert(enzyme);
    }

    @POST
    @Path("/all")
    public Response insertAll(final java.util.List<Enzyme> enzymes) {
        val ord = stringOrd.contramap(Enzyme::getName);
        return buildInsertAll(iterableSet(ord, enzymes));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Enzyme enzymes
    ) {
        val toUpdate = enzymes.withId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("name") final String name) {
        return respond(
            () -> dao.getByName(name),
            status(OK)::entity,
            status(NOT_FOUND)
        );
    }

    @Override
    protected GenericEntity<java.util.List<Enzyme>> toGenericEntity(
        final Set<Enzyme> enzymes
    ) {
        return new GenericEntity<java.util.List<Enzyme>>(
            newArrayList(enzymes)
        ) { };
    }

}
