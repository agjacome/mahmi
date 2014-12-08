package es.uvigo.ei.sing.mahmi.controller.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

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
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.ProjectToLoadWrapper;
import es.uvigo.ei.sing.mahmi.controller.controllers.LoaderServiceController;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;

@Path("/project")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class ProjectService extends DatabaseEntityAbstractService<Project, ProjectsDAO> {

    private final LoaderServiceController loader;

    private ProjectService(final ProjectsDAO dao, final LoaderServiceController loader) {
        super(dao);
        this.loader = loader;
    }

    public static ProjectService projectService(final ProjectsDAO dao, final LoaderServiceController loader) {
        return new ProjectService(dao, loader);
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
    public Response insert(final Project project) {
        return buildInsert(project);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<Project> projects) {
        return buildInsertAll(projects);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(id);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final int id, final Project project) {
        return buildUpdate(project.withId(id));
    }

    @POST
    @Path("/load")
    public Response load(final ProjectToLoadWrapper toLoad) {
        try {
            val project = loader.load(toLoad);
            return status(CREATED).entity(project).build();
        } catch (final Exception e) {
            return status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Override
    protected GenericEntity<Set<Project>> mapSet(final Set<Project> projects) {
        return new GenericEntity<Set<Project>>(projects) { };
    }

}
