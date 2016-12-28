package es.uvigo.ei.sing.mahmi.http.services;

import static fj.data.Set.iterableSet;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

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

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@Path("/peptide")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(OptionExtensionMethods.class)
public final class PeptideService extends DatabaseEntityAbstractService<Peptide, PeptidesDAO> {

    private PeptideService(final PeptidesDAO dao) {
        super(dao);
    }

    public static PeptideService peptideService(final PeptidesDAO dao) {
        return new PeptideService(dao);
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

    @GET
    @Path("/count")
    public Response count() {
        return buildCount();
    }

    @POST
    public Response insert(final Peptide peptide) {
        return buildInsert(peptide);
    }

    @POST
    @Path("/all")
    public Response insertAll(final java.util.List<Peptide> peptides) {
        return buildInsertAll(iterableSet(Peptide.ord, peptides));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Peptide peptide
    ) {
        val toUpdate = peptide.withId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @Override
    protected GenericEntity<java.util.List<Peptide>> toGenericEntity(
        final Set<Peptide> peptides
    ) {
        return new GenericEntity<java.util.List<Peptide>>(
            newArrayList(peptides)
        ) { };
    }

}
