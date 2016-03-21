package es.uvigo.ei.sing.mahmi.http.services;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.browser.Browser;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastAlignment;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.mail.MailSender;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.http.wrappers.FeedbackWrapper;
import es.uvigo.ei.sing.mahmi.http.wrappers.SearchWrapper;
import fj.data.Set;

@Path("/browser")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(IterableExtensionMethods.class)
public final class BrowserService extends DatabaseEntityAbstractService<Peptide, PeptidesDAO> {

	private Browser    browser;
	private MailSender mailSender;
	
    private BrowserService( final PeptidesDAO dao, final Browser browser, final MailSender mailSender) {
        super(dao);
        this.browser = browser;
        this.mailSender = mailSender;
    }

    public static BrowserService browserService( final PeptidesDAO dao , final Browser browser, final MailSender mailSender) {
        return new BrowserService(dao, browser, mailSender);
    }
    
    @POST
    @Path("/search")
    public Response search(final SearchWrapper search){
    	final BlastOptions blastOptions = new BlastOptions( search.getNum_alignments(),
															search.getEvalue(),
															search.getBlast_threshold(),
															search.getWindow_size(),
															search.getWord_size(),
															search.getBest_hit_overhang(),
															search.getBest_hit_score_edge(),
															search.getGapextend(),
															search.getGapopen(),
															search.isUngapped() );
    	
    	return respond(() -> browser.search( AminoAcidSequence.fromString(search.getSequence()).some(),
					    				   	 search.getDatabases(),
					    					 search.getThreshold(),
					    					 search.getBioactivity(),
					    					 Paths.get(search.getPath()+"/"+UUID.randomUUID().toString()),
					    					 blastOptions ), al -> status(OK).entity(toGenericEntity(al)));	
    }   
    
    @POST
    @Path("/feedback")
    public Response feedback(final FeedbackWrapper feedback){
    	final String text = "Name: "+feedback.getName()+
    						"\nEmail: "+feedback.getEmail()+
    						"\nSubject: "+feedback.getSubject()+
    						"\n\n"+feedback.getText();
    	mailSender.send("aiblanco@uvigo.es", "MAHMI Feedback: "+feedback.getSubject(), text);    	
    	return Response.ok().build();
    }
    
    @Override
    protected GenericEntity<java.util.List<Peptide>> toGenericEntity(
        final Set<Peptide>peptides
    ) {
        return new GenericEntity<java.util.List<Peptide>>(
            newArrayList(peptides)
        ) { };
    }
    
    private GenericEntity<java.util.List<BlastAlignment>> toGenericEntity(
    		final List<BlastAlignment> aligments
    ) {
        return new GenericEntity<java.util.List<BlastAlignment>>(
            newArrayList(aligments)
        ) { };
    }

}
