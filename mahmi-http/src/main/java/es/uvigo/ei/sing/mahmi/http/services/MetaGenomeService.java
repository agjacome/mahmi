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
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
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
        return buildGet(Identifier.of(id));
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("projectId") @DefaultValue("0") final int projectId,
        @QueryParam("projectName") @DefaultValue("") final String projectName,
        @QueryParam("projectRepo") @DefaultValue("") final String projectRepo,
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return respond(
           ()  -> dao.search(
                project(Identifier.of(projectId), projectName, projectRepo),
                (page - 1) * size, size),
            mgs -> status(OK).entity(toGenericEntity(mgs))
        );
    }

    @GET
    @Path("/count")
    public Response count() {
        return buildCount();
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
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final MetaGenome metaGenome
    ) {
        val toUpdate = metaGenome.setId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @Override
    protected GenericEntity<List<MetaGenome>> toGenericEntity(
        final Collection<MetaGenome> metaGenomes
    ) {
        return new GenericEntity<List<MetaGenome>>(newArrayList(metaGenomes)){};
    }

}
