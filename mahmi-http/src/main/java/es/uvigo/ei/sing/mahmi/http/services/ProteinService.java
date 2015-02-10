package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;
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
import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.data.Option;

@Path("/protein")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod({ Option.class, OptionExtensionMethods.class })
public final class ProteinService extends DatabaseEntityAbstractService<Protein, ProteinsDAO> {

    private ProteinService(final ProteinsDAO dao) {
        super(dao);
    }

    public static ProteinService proteinService(final ProteinsDAO dao) {
        return new ProteinService(dao);
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
    @Path("/search")
    public Response getMetagenomeId(
        @QueryParam("metagenomeId") @DefaultValue("0") final int metagenomeId,
        @QueryParam("projectId") @DefaultValue("0") final int projectId,
        @QueryParam("projectName") @DefaultValue("") final String projectName,
        @QueryParam("projectRepo") @DefaultValue("") final String projectRepo,
        @QueryParam("sequence") @DefaultValue("") final String sequence,
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return respond(
                () -> dao.search(MetaGenome.metagenome(Identifier.of(metagenomeId),
                project(Identifier.of(projectId),projectName,projectRepo),
                Fasta.empty()),
                AminoAcidSequence.fromString(sequence).orThrow(new IllegalArgumentException()),
                (page - 1) * size, size),
            as -> status(OK).entity(toGenericEntity(as))
        );
    }

    @GET
    @Path("/count")
    public Response count() {
        return buildCount();
    }

    @POST
    public Response insert(final Protein protein) {
        return buildInsert(protein);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<Protein> proteins) {
        return buildInsertAll(proteins);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Protein protein
    ) {
        val toUpdate = protein.setId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @Override
    protected GenericEntity<List<Protein>> toGenericEntity(
        final Collection<Protein> proteins
    ) {
        return new GenericEntity<List<Protein>>(newArrayList(proteins)) { };
    }

}
