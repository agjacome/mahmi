package es.uvigo.ei.sing.mahmi.http.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.mail.MailSender;
import es.uvigo.ei.sing.mahmi.http.utils.AccessLogger;
import es.uvigo.ei.sing.mahmi.http.wrappers.FeedbackWrapper;
import es.uvigo.ei.sing.mahmi.http.wrappers.WebLogWrapper;
import lombok.experimental.ExtensionMethod;

@Path("/utils")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(IterableExtensionMethods.class)
public final class UtilsService {

	private MailSender   mailSender;	
	private AccessLogger accessLogger;
	
    private UtilsService(final AccessLogger accessLogger, final MailSender mailSender) {
        this.accessLogger = accessLogger;
    	this.mailSender   = mailSender;
    }

    public static UtilsService utilsService(final AccessLogger accessLogger, final MailSender mailSender) {
        return new UtilsService(accessLogger, mailSender);
    }  
    
    @POST
    @Path("/feedback")
    public Response feedback(final FeedbackWrapper feedback){
    	final String text = "Name: "+feedback.getName()+
    						"\nEmail: "+feedback.getEmail()+
    						"\nSubject: "+feedback.getSubject()+
    						"\n\n"+feedback.getText();
    	mailSender.send("aiblanco@uvigo.es", feedback.getSubject(), text);    	
    	return Response.ok().build();
    }
       
    @POST
    @Path("/weblog")
    public Response webLog(final WebLogWrapper log) {
    	accessLogger.weblog(log);
    	return Response.ok().build();
    }
}
