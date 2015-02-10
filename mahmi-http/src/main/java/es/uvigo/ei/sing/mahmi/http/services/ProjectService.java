package es.uvigo.ei.sing.mahmi.http.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;

import java.nio.file.Paths;
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
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import es.uvigo.ei.sing.mahmi.http.wrappers.LoadProjectWrapper;
import es.uvigo.ei.sing.mahmi.loader.ProjectLoaderController;

@Slf4j
@Path("/project")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class ProjectService extends DatabaseEntityAbstractService<Project, ProjectsDAO> {

    private final ProjectLoaderController loader;

    private ProjectService(
        final ProjectsDAO dao, final ProjectLoaderController loader
    ) {
        super(dao);
        this.loader = loader;
    }

    public static ProjectService projectService(
        final ProjectsDAO dao, final ProjectLoaderController loader
    ) {
        return new ProjectService(dao, loader);
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") final int id) {
        return buildGet(Identifier.of(id));
    }

    @GET
    @Path("/search")
    public Response getName(
    	@QueryParam("name") @DefaultValue("") final String name,
    	@QueryParam("repo") @DefaultValue("") final String repo,
        @QueryParam("page") @DefaultValue("1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return respond(
            () -> dao.search(project(Identifier.empty(),name,repo), (page - 1) * size, size),
            as -> status(OK).entity(toGenericEntity(as))
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
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Project project
    ) {
        val toUpdate = project.setId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @POST
    @Path("/load")
    public Response load(final LoadProjectWrapper toLoad) {
        // FIXME: uglyness
        try {

            val project = toLoad.getProject();
            val path    = Paths.get(toLoad.getPath());

            val loading = loader.loadProject(project, path);
            val created = loading._1();
            val future  = loading._2();

            future.exceptionally(err -> {
                log.error("Error while loading project", err);
                return null;
            });

            return status(CREATED).entity(created).build();

        } catch (final Exception e) {
            log.error("Error while loading project", e);
            return status(INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Override
    protected GenericEntity<List<Project>> toGenericEntity(
        final Collection<Project> projects
    ) {
        return new GenericEntity<List<Project>>(newArrayList(projects)) { };
    }

}
