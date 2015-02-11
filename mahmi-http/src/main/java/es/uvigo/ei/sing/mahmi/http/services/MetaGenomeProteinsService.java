package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
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

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomeProteinsDAO;
import fj.data.Set;

@Path("/metagenomeproteins")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class MetaGenomeProteinsService extends DatabaseEntityAbstractService<MetaGenomeProteins, MetaGenomeProteinsDAO> {

    private MetaGenomeProteinsService(
        final MetaGenomeProteinsDAO dao
    ) {
        super(dao);
    }

    public static MetaGenomeProteinsService metaGenomeProteinsService(
        final MetaGenomeProteinsDAO dao
    ) {
        return new MetaGenomeProteinsService(dao);
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
    public Response insert(final MetaGenomeProteins digestion) {
        return buildInsert(digestion);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final MetaGenomeProteins digestion
    ) {
        val toUpdate = digestion.withId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("proteinID")  @DefaultValue("0") final int proteinId,
        @QueryParam("proteinSeq")  @DefaultValue("") final String proteinSeq,
        @QueryParam("metagenomeID") @DefaultValue("0") final int metagenomeId,
        @QueryParam("projectID") @DefaultValue("0") final int projectId,
        @QueryParam("projectName") @DefaultValue("") final String projectName,
        @QueryParam("projectRepo") @DefaultValue("") final String projectRepo,
        @QueryParam("page")    @DefaultValue( "1") final int page,
        @QueryParam("size")    @DefaultValue("50") final int size
    ) {
          return respond(()  ->
                dao.search(
                    protein(Identifier.of(proteinId), AminoAcidSequence.fromString(proteinSeq).some()),
                    metagenome(Identifier.of(metagenomeId), project(Identifier.of(projectId), projectName, projectRepo), Fasta.empty()),
                    (page - 1) * size,  size
                ),
                mgps -> status(OK).entity(toGenericEntity(mgps))
            );
    }

    @Override
    protected GenericEntity<java.util.List<MetaGenomeProteins>> toGenericEntity(
        final Set<MetaGenomeProteins> metagenomeproteins
    ) {
        return new GenericEntity<java.util.List<MetaGenomeProteins>>(
            newArrayList(metagenomeproteins)
        ) { };
    }

}
