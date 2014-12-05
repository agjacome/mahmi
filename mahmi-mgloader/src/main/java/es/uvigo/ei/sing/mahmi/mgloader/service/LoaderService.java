package es.uvigo.ei.sing.mahmi.mgloader.service;

import static java.util.concurrent.CompletableFuture.runAsync;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

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
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.ProjectToLoadWrapper;
import es.uvigo.ei.sing.mahmi.mgloader.loaders.LoaderException;
import es.uvigo.ei.sing.mahmi.mgloader.loaders.ProjectLoaderController;



@Slf4j
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
        val project = wrapper.getProject();
        val path    = Paths.get(wrapper.getPath());

        runAsync(() -> {
            try {
                loaderCtrl.loadProject(project, path);
            } catch (final LoaderException le) {
                log.error("Error while loading {} from path {}", project, path);
            }
        });

        return status(NO_CONTENT).build();
    }

}
