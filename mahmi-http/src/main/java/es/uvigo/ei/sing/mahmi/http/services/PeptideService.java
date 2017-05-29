package es.uvigo.ei.sing.mahmi.http.services;

import static fj.data.Set.iterableSet;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import javax.inject.Provider;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.Request;

import es.uvigo.ei.sing.mahmi.common.entities.BioactivePeptide;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.http.utils.AccessLogger;
import fj.data.Set;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@Path("/peptide")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(OptionExtensionMethods.class)
public final class PeptideService extends DatabaseEntityAbstractService<Peptide, PeptidesDAO> {

	@Context
    private Provider<Request> requestProvider;
	private AccessLogger	  accessLogger;
	
    private PeptideService(final PeptidesDAO dao, final AccessLogger accessLogger) {
        super(dao);
        this.accessLogger = accessLogger;
    }

    public static PeptideService peptideService(final PeptidesDAO dao, final AccessLogger accessLogger) {
        return new PeptideService(dao, accessLogger);
    }
    
    @GET
    @Path("explore")
    public Response exploreBioactives(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size,
        @QueryParam("filterType") @DefaultValue("") final String filterType,
        @QueryParam("filter") @DefaultValue("") final String filter
    ) {
    	accessLogger.log(requestProvider.get());    
    	switch(filterType){
    		case "organism":
    			return respond(
		            () -> dao.getBioactivesByOrganism((page - 1) * size, size, "%"+filter+"%"),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );    		
    		case "gene":
    			return respond(
		            () -> dao.getBioactivesByGene((page - 1) * size, size, "%"+filter+"%"),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );    			
    		case "protein":
    			return respond(
		            () -> dao.getBioactivesByProtein((page - 1) * size, size, "%"+filter+"%"),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );    		
    		case "length":
    			return respond(
		            () -> dao.getBioactivesByLength((page - 1) * size, size, filter),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );    			
    		default:
    			return respond(
		            () -> dao.getBioactives((page - 1) * size, size),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );    			
    	}
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") final int id) {
        return buildGet(Identifier.of(id));
    }

    @GET
    public Response get(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50") final int size
    ) {
        return buildGetAll(page, size);
    }

    @GET
    @Path("/count")
    public Response count() {
        return buildCount();
    }

    @POST
    public Response insert(final Peptide peptide) {
        return buildInsert(peptide);
    }

    @POST
    @Path("/all")
    public Response insertAll(final java.util.List<Peptide> peptides) {
        return buildInsertAll(iterableSet(Peptide.ord, peptides));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final Peptide peptide
    ) {
        val toUpdate = peptide.withId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }
    
    private GenericEntity<java.util.List<BioactivePeptide>> bioactiveToGenericEntity(
    		final Set<BioactivePeptide> peptides ) {
        return new GenericEntity<java.util.List<BioactivePeptide>>(
            newArrayList(peptides)
        ) { };
    }

    @Override
    protected GenericEntity<java.util.List<Peptide>> toGenericEntity(
        final Set<Peptide> peptides
    ) {
        return new GenericEntity<java.util.List<Peptide>>(
            newArrayList(peptides)
        ) { };
    }

}
