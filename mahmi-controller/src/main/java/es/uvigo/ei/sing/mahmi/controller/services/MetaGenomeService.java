package es.uvigo.ei.sing.mahmi.controller.services;

import static fj.Unit.unit;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Collections;
import java.util.Map;
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
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;

@Path("/metagenome")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class MetaGenomeService extends DatabaseEntityAbstractService<MetaGenome, MetaGenomesDAO> {

    private MetaGenomeService(final MetaGenomesDAO dao) {
        super(dao);
    }

    public static MetaGenomeService metaGenomeService(final MetaGenomesDAO dao) {
        return new MetaGenomeService(dao);
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
    public Response insert(final MetaGenome metaGenome) {
        return buildInsert(metaGenome);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<MetaGenome> metaGenomes) {
        return buildInsertAll(metaGenomes);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(id);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final int id, final MetaGenome metaGenome) {
        return buildUpdate(metaGenome.withId(id));
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("project") @DefaultValue("-1") final int projectId,
        @QueryParam("protein") @DefaultValue("-1") final int proteinId,
        @QueryParam("page")    @DefaultValue( "1") final int page,
        @QueryParam("size")    @DefaultValue("50") final int size
    ) {
        val project = projectId != -1 ? Identifier.of(projectId) : Identifier.empty();
        val protein = proteinId != -1 ? Identifier.of(proteinId) : Identifier.empty();

        if (project.isEmpty() && protein.isEmpty())
            return status(BAD_REQUEST).build();

        // FIXME: does not respect page/size, since we are adding up all results
        // in a common Set
        val start = (page - 1) * size;
        val found = Collections.<Identifier>emptySet();
        if (!project.isEmpty()) found.addAll(dao.getIdsByProjectId(project, start, size));
        if (!protein.isEmpty()) found.addAll(dao.getIdsByProteinId(protein, start, size));

        return status(OK).entity(mapIdSet(found)).build();
    }

    @POST
    @Path("/{id}")
    public Response addProteins(
        @PathParam("id") final int metaGenomeId, final Map<Identifier, Long> proteins
    ) {
        return respond(
            () -> { dao.addAllProteinsToMetaGenome(Identifier.of(metaGenomeId), proteins); return unit(); },
            u  -> status(CREATED)
        );
    }

    @DELETE
    @Path("/{metagenome}/{protein}")
    public Response deleteProtein(
        @PathParam("metagenome") final int metaGenomeId,
        @PathParam("protein")    final int proteinId
    ) {
        val metaGenome = Identifier.of(metaGenomeId);
        val protein    = Identifier.of(proteinId);

        return respond(
            () -> { dao.deleteProteinFromMetaGenome(metaGenome, protein); return unit(); },
            u  -> status(NO_CONTENT)
        );
    }

    @Override
    protected GenericEntity<Set<MetaGenome>> mapSet(final Set<MetaGenome> metaGenomes) {
        return new GenericEntity<Set<MetaGenome>>(metaGenomes) { };
    }

    private GenericEntity<Set<Identifier>> mapIdSet(final Set<Identifier> identifiers) {
        return new GenericEntity<Set<Identifier>>(identifiers) { };
    }

}
