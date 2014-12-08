package es.uvigo.ei.sing.mahmi.controller.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Collections;
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

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.CutProteinsWrapper;
import es.uvigo.ei.sing.mahmi.controller.controllers.CutterServiceController;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;

@Path("/digestion")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class DigestionService extends DatabaseEntityAbstractService<Digestion, DigestionsDAO> {

    private final CutterServiceController cutter;

    private DigestionService(final DigestionsDAO dao, final CutterServiceController cutter) {
        super(dao);
        this.cutter = cutter;
    }

    public static DigestionService digestionService(final DigestionsDAO dao, final CutterServiceController cutter) {
        return new DigestionService(dao, cutter);
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
    public Response insert(final Digestion digestion) {
        return buildInsert(digestion);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<Digestion> digestions) {
        return buildInsertAll(digestions);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(id);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final int id, final Digestion digestion) {
        return buildUpdate(digestion.withId(id));
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("enzyme")  @DefaultValue("-1") final int enzymeId,
        @QueryParam("protein") @DefaultValue("-1") final int proteinId,
        @QueryParam("peptide") @DefaultValue("-1") final int peptideId,
        @QueryParam("page")    @DefaultValue( "1") final int page,
        @QueryParam("size")    @DefaultValue("50") final int size
    ) {
        val enzyme  = enzymeId  != -1 ? Identifier.of(enzymeId)  : Identifier.empty();
        val protein = proteinId != -1 ? Identifier.of(proteinId) : Identifier.empty();
        val peptide = peptideId != -1 ? Identifier.of(peptideId) : Identifier.empty();

        if (!enzyme.isEmpty() && !protein.isEmpty() && !peptide.isEmpty())
            return getByFKs(enzyme, protein, peptide);
        if (enzyme.isEmpty() && protein.isEmpty() && peptide.isEmpty())
            return status(BAD_REQUEST).build();

        // FIXME: does not respect page/size, since we are adding up all results
        // in a common Set
        val start = (page - 1) * size;
        val found = Collections.<Digestion>emptySet();
        if (!enzyme.isEmpty())  found.addAll(dao.getByEnzymeId (enzyme,  start, size));
        if (!protein.isEmpty()) found.addAll(dao.getByProteinId(protein, start, size));
        if (!peptide.isEmpty()) found.addAll(dao.getByPeptideId(peptide, start, size));

        return status(OK).entity(mapSet(found)).build();
    }

    @POST
    @Path("/digest")
    public Response digest(final CutProteinsWrapper toDigest) {
        try {
            val digestions = cutter.cut(toDigest);
            return status(CREATED).entity(mapSet(digestions)).build();
        } catch (final Exception e) {
            return status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Override
    protected GenericEntity<Set<Digestion>> mapSet(final Set<Digestion> digestions) {
        return new GenericEntity<Set<Digestion>>(digestions) { };
    }

    private Response getByFKs(final Identifier enzyme, final Identifier protein, final Identifier peptide) {
        return respond(
            () -> dao.get(enzyme, protein, peptide),
            op -> op.option(status(NOT_FOUND), status(OK)::entity)
        );
    }

}
