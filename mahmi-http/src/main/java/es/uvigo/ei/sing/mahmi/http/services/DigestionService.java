package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static es.uvigo.ei.sing.mahmi.common.utils.functions.NumericPredicates.between;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;
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
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.cutter.ProteinCutterController;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.http.wrappers.CutProteinsWrapper;

@Slf4j
@Path("/digestion")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class DigestionService extends DatabaseEntityAbstractService<Digestion, DigestionsDAO> {

    private final ProteinCutterController cutter;

    private DigestionService(
        final DigestionsDAO dao, final ProteinCutterController cutter
    ) {
        super(dao);
        this.cutter = cutter;
    }

    public static DigestionService digestionService(
        final DigestionsDAO dao, final ProteinCutterController cutter
    ) {
        return new DigestionService(dao, cutter);
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
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Digestion digestion
    ) {
        val toUpdate = digestion.setId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("peptideID") @DefaultValue("0") final int peptideId,
        @QueryParam("peptideSeq") @DefaultValue("") final String peptideSeq,
        @QueryParam("enzymeID")  @DefaultValue("0") final int enzymeId,
        @QueryParam("proteinID") @DefaultValue("0") final int proteinId,
        @QueryParam("metagenomeID") @DefaultValue("0") final int metagenomeId,
        @QueryParam("projectID") @DefaultValue("0") final int projectId,
        @QueryParam("projectName") @DefaultValue("") final String projectName,
        @QueryParam("projectRepo") @DefaultValue("") final String projectRepo,
        @QueryParam("page")    @DefaultValue( "1") final int page,
        @QueryParam("size")    @DefaultValue("50") final int size
    ) {
        // TODO: clean-up
        return respond(
                () -> dao.search(
                    protein(Identifier.of(proteinId),
                    AminoAcidSequence.empty()),
                    metagenome(Identifier.of(metagenomeId), project(Identifier.of(projectId),projectName, projectRepo), Fasta.empty()),
                    peptide(AminoAcidSequence.fromString(peptideSeq).some()),
                    enzyme(Identifier.of(enzymeId),""),
                    (page - 1) * size, size),
                ds -> status(OK).entity(toGenericEntity(ds))
            );
    }

    @POST
    @Path("/digest")
    public Response digest(final CutProteinsWrapper toDigest) {
        // FIXME: uglyness
        try {
            val project = toDigest.getProject();
            val enzymes = toDigest.getEnzymes();
            val minSize = toDigest.getMinSize();
            val maxSize = toDigest.getMaxSize();

            val future = cutter.cutProjectProteins(
                project, enzymes, between(minSize, maxSize)
            );

            future.exceptionally(err -> {
                log.error("Error while cutting proteins", err);
                return null;
            });

            return status(ACCEPTED).build();

        } catch (final Exception e) {
            log.error("Error while cutting proteins", e);
            return status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Override
    protected GenericEntity<List<Digestion>> toGenericEntity(
        final Collection<Digestion> digestions
    ) {
        return new GenericEntity<List<Digestion>>(newArrayList(digestions)) { };
    }

}
