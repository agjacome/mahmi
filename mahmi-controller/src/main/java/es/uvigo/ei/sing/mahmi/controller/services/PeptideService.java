package es.uvigo.ei.sing.mahmi.controller.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Set;

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
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

@Path("/peptide")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
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
        return buildGet(id);
    }

    @GET
    public Response get(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return buildGetAll(page, size);
    }

    @POST
    public Response insert(final Peptide peptide) {
        return buildInsert(peptide);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<Peptide> peptides) {
        return buildInsertAll(peptides);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(id);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final int id, final Peptide peptide) {
        return buildUpdate(peptide.withId(id));
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("sequence") final String sequence) {
        return respond(
            () -> dao.getBySequence(AminoAcidSequence.fromString(sequence)),
            op -> op.option(status(NOT_FOUND), status(OK)::entity)
        );
    }

    @Override
    protected GenericEntity<Set<Peptide>> mapSet(final Set<Peptide> peptides) {
        return new GenericEntity<Set<Peptide>>(peptides) { };
    }

}
