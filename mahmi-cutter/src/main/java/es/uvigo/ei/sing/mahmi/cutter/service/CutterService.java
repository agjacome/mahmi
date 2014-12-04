package es.uvigo.ei.sing.mahmi.cutter.service;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.MultipleProteinsToCutWrapper;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.OneProteinToCutWrapper;
import es.uvigo.ei.sing.mahmi.cutter.cutters.CutterException;
import es.uvigo.ei.sing.mahmi.cutter.cutters.ProteinCutterController;
import fj.data.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

@Path("/")
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CutterService {

    private final ProteinCutterController cutterCtrl;

    public static CutterService cutterService(final ProteinCutterController cutterCtrl) {
        return new CutterService(cutterCtrl);
    }

    @POST
    public Response cut(
        final OneProteinToCutWrapper wrapper
    ) {
        return buildResponse(cutterCtrl.cutProtein(
            wrapper.getProtein(), wrapper.getEnzymes(), wrapper.getMinSize(), wrapper.getMaxSize()
        ));
    }

    @POST
    @Path("/all")
    public Response cutAll(
        final MultipleProteinsToCutWrapper wrapper
    ) {
        return buildResponse(cutterCtrl.cutAllProteins(
            wrapper.getProteins(), wrapper.getEnzymes(), wrapper.getMinSize(), wrapper.getMaxSize()
        ));
    }

    private Response buildResponse(final Validation<CutterException, Set<Digestion>> validation) {
        return validation.validation(
            status(INTERNAL_SERVER_ERROR)::entity, ds -> status(CREATED).entity(setMapper(ds))
        ).build();
    }

    private <A> GenericEntity<Set<A>> setMapper(final Set<A> as) {
        return new GenericEntity<Set<A>>(as) { };
    }

}
