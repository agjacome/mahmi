package es.uvigo.ei.sing.mahmi.http.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.nio.file.Paths;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.browser.Browser;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastAligment;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.http.wrappers.SearchWrapper;
import fj.data.Set;
import java.util.UUID;

@Path("/browser")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(IterableExtensionMethods.class)
public final class BrowserService extends DatabaseEntityAbstractService<Peptide, PeptidesDAO> {

	private Browser browser;
	
    private BrowserService( final PeptidesDAO dao, final Browser browser ) {
        super(dao);
        this.browser = browser;
    }

    public static BrowserService browserService( final PeptidesDAO dao , final Browser browser) {
        return new BrowserService(dao, browser);
    }
    
    @POST
    @Path("/search")
    public Response search(final SearchWrapper search){   
    	return respond(() -> browser.search( AminoAcidSequence.fromString(search.getSequence()).some(),
    					search.getDatabases(),
    					search.getThreshold(),
    					search.getBioactivity(),
    					Paths.get(search.getPath()+"/"+UUID.randomUUID().toString()) ), al -> status(OK).entity(toGenericEntity(al)));	
    }  

    @POST
    @Path("/aligment")
    public Response getAligment(final BlastAligment aligment){    	
    	return respond(() -> browser.getAligment(aligment), al -> status(OK).entity(al));	
    } 
    
    @Override
    protected GenericEntity<java.util.List<Peptide>> toGenericEntity(
        final Set<Peptide>peptides
    ) {
        return new GenericEntity<java.util.List<Peptide>>(
            newArrayList(peptides)
        ) { };
    }
    
    private GenericEntity<java.util.List<BlastAligment>> toGenericEntity(
    		final List<BlastAligment> aligments
    ) {
        return new GenericEntity<java.util.List<BlastAligment>>(
            newArrayList(aligments)
        ) { };
    }

}
