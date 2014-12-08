package es.uvigo.ei.sing.mahmi.cutter.service;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.utils.wrappers.CutProteinsWrapper;
import es.uvigo.ei.sing.mahmi.cutter.cutters.CutterException;
import es.uvigo.ei.sing.mahmi.cutter.cutters.ProteinCutterController;

@Path("/")
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CutterService {

    // FIXME: run async, what to return??

    private final ProteinCutterController cutterCtrl;

    public static CutterService cutterService(final ProteinCutterController cutterCtrl) {
        return new CutterService(cutterCtrl);
    }

    @POST
    public Response cut(final CutProteinsWrapper wrapper) {
        val proteins = wrapper.getProteins();
        val enzymes  = wrapper.getEnzymes();
        val minSize  = wrapper.getMinSize();
        val maxSize  = wrapper.getMaxSize();

        try {
            val digestions = cutterCtrl.cutProteins(proteins, enzymes, minSize, maxSize);
            return success(digestions);
        } catch (final CutterException ce) {
            return fail(ce);
        }
    }


    private Response success(final Set<Digestion> digestions) {
        return status(CREATED).entity(set(digestions)).build();
    }

    private Response fail(final CutterException exception) {
        return status(INTERNAL_SERVER_ERROR).encoding(exception.getMessage()).build();
    }

    private <A> GenericEntity<Set<A>> set(final Set<A> as) {
        return new GenericEntity<Set<A>>(as) { };
    }

}
