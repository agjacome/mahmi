package es.uvigo.ei.sing.mahmi.loader.service;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.ProjectToLoadWrapper;
import es.uvigo.ei.sing.mahmi.loader.loaders.LoaderException;
import es.uvigo.ei.sing.mahmi.loader.loaders.ProjectLoaderController;

@Path("/")
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoaderService {

    private final ProjectLoaderController loaderCtrl;

    public static LoaderService loaderService(final ProjectLoaderController loaderCtrl) {
        return new LoaderService(loaderCtrl);
    }

    @POST
    public Response load(final ProjectToLoadWrapper wrapper) {
        try {

            val createdProject = loaderCtrl.loadProject(wrapper.getProject(), Paths.get(wrapper.getPath()));
            return status(OK).entity(createdProject).build();

        } catch (final LoaderException le) {
            return status(INTERNAL_SERVER_ERROR).entity(le.getMessage()).build();
        }
    }

}
