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

import fj.data.Option;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import static fj.data.Set.iterableSet;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;

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
    @Path("/search")
    public Response search(
        @QueryParam("proteinId") @DefaultValue("0") final int proteinId,
        @QueryParam("metagenomeId") @DefaultValue("0") final int metagenomeId,
        @QueryParam("projectId") @DefaultValue("0") final int projectId,
        @QueryParam("projectName") @DefaultValue("") final String projectName,
        @QueryParam("projectRepo") @DefaultValue("") final String projectRepo,
        @QueryParam("sequence") @DefaultValue("") final String sequence,
        @QueryParam("enzymeId") @DefaultValue("0") final Integer enzymeId,
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        final Option<AminoAcidSequence> aas = AminoAcidSequence.fromString(sequence);
        if (aas.isNone()) throw new IllegalArgumentException();

        final Protein    p  = protein(Identifier.of(proteinId), AminoAcidSequence.empty());
        final Enzyme     e  = enzyme(Identifier.of(enzymeId),"");
        final MetaGenome mg = metagenome(
            Identifier.of(metagenomeId),
            project(Identifier.of(projectId), projectName, projectRepo),
            Fasta.empty()
        );

        return respond(
            () -> dao.search(p, mg, aas.some(), e, (page - 1) * size, size),
            as -> status(OK).entity(toGenericEntity(as))
        );
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
