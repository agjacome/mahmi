package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.Request;

import es.uvigo.ei.sing.mahmi.browser.Browser;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastAlignment;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;
import es.uvigo.ei.sing.mahmi.common.entities.BioactivePeptide;
import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeInformation;
import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeMIxS;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.ProteinInformation;
import es.uvigo.ei.sing.mahmi.common.entities.ReferencePeptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.MetagenomeInformationsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetagenomeMIxSDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinInformationsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.http.utils.AccessLogger;
import es.uvigo.ei.sing.mahmi.http.wrappers.SearchWrapper;
import es.uvigo.ei.sing.mahmi.http.wrappers.SourceProtein;
import fj.data.Option;
import fj.data.Set;
import lombok.experimental.ExtensionMethod;

@Path("api")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@ExtensionMethod(OptionExtensionMethods.class)
public final class PublicService extends DatabaseEntityAbstractService<Peptide, PeptidesDAO>{
	
	@Context
    private Provider<Request> requestProvider;
	
	private Browser                browser;
	private ProteinsDAO            proteinDAO;
	private ProteinInformationsDAO proteinInfoDAO;
	private MetagenomeInformationsDAO metagenomeInfoDAO;
	private MetagenomeMIxSDAO      metagenomeMIxSDAO;
	private AccessLogger		   accessLogger;
	
    private PublicService(final PeptidesDAO peptidesDAO, 
						  final ProteinsDAO proteinDAO, 
						  final ProteinInformationsDAO proteinInfoDAO,
						  final MetagenomeInformationsDAO metagenomeInfoDAO,
						  final MetagenomeMIxSDAO metagenomeMIxSDAO,
						  final Browser browser,
						  final AccessLogger accessLogger) {
    	super(peptidesDAO);
        this.browser = browser;
        this.proteinDAO = proteinDAO;
        this.proteinInfoDAO = proteinInfoDAO;
        this.metagenomeInfoDAO = metagenomeInfoDAO;
        this.metagenomeMIxSDAO = metagenomeMIxSDAO;
        this.accessLogger      = accessLogger;
    }

    public static PublicService publicService(final PeptidesDAO dao, 
    										  final ProteinsDAO proteinDAO, 
    										  final ProteinInformationsDAO proteinInfoDAO,
    										  final MetagenomeInformationsDAO metagenomeInfoDAO,
    										  final MetagenomeMIxSDAO metagenomeMIxSDAO,
    										  final Browser browser,
    										  final AccessLogger accessLogger) {
        return new PublicService(dao, proteinDAO, proteinInfoDAO, metagenomeInfoDAO, metagenomeMIxSDAO, browser, accessLogger);
    }
    
    @GET
    @Path("proteins/{id}")
    public Response getByPeptide(@PathParam("id") final int id){
    	accessLogger.log(requestProvider.get());    
        return respond(
                () -> proteinDAO.getByPeptide(peptide(Identifier.of(id), AminoAcidSequence.empty())),
                as -> Response.status(OK).entity(proteinToGenericEntity(as))
            );
    }

    @GET
    @Path("peptides/reference")
    public Response getReferences() {
    	accessLogger.log(requestProvider.get());    
    	return respond(
            () -> dao.getReferences(),
            as -> status(OK).entity(referenceToGenericEntity(as))
        );
    }

    @GET
    @Path("peptides")
    public Response getBioactives(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("50000") final int size
    ) {
    	accessLogger.log(requestProvider.get());    
    	if(size<50000)
	    	return respond(
	            () -> dao.getBioactives((page - 1) * size, size),
	            as -> status(OK).entity(bioactiveToGenericEntity(as))
	        );
    	else
	    	return respond(
		            () -> dao.getBioactives((page - 1) * 50000, 50000),
		            as -> status(OK).entity(bioactiveToGenericEntity(as))
		        );
    		
    }
    
    @GET
    @Path("peptides/sourceProteins/{id}")
    public Response getProteinsInfo(@PathParam("id") final int id){
    	accessLogger.log(requestProvider.get());    
        final Set<Protein> proteins = proteinDAO.getByPeptide(Peptide.peptide(Identifier.of(id), AminoAcidSequence.empty()));
        final List<SourceProtein> sourceProteins = new ArrayList<>();
        proteins.forEach(p -> {
        	 final Option<ProteinInformation> proteinInfoO = proteinInfoDAO.getByProtein(p);
        	 final Option<MetagenomeInformation> metagenomeInfoO = metagenomeInfoDAO.getByProtein(p);
        	 final Option<MetagenomeMIxS> metagenomeMIxSO = metagenomeMIxSDAO.getByProtein(p);
        	 final ProteinInformation proteinInfo; 
        	 final MetagenomeInformation metagenomeInfo;
        	 final MetagenomeMIxS metagenomeMIxS;
        	 if(proteinInfoO.isNone()) proteinInfo = new ProteinInformation();
        	 else proteinInfo = proteinInfoO.some();
        	 if(metagenomeInfoO.isNone()) metagenomeInfo = new MetagenomeInformation();
        	 else metagenomeInfo = metagenomeInfoO.some();
        	 if(metagenomeMIxSO.isNone()) metagenomeMIxS = new MetagenomeMIxS();
        	 else metagenomeMIxS = metagenomeMIxSO.some();
        	 sourceProteins.add(SourceProtein.wrap(p, proteinInfo, metagenomeMIxS, metagenomeInfo));
         });
         return respond(() -> sourceProteins, al -> status(OK).entity(sourceProteinToGenericEntity(al)));
    }
    
    @POST
    @Path("peptides/search")
    public Response search(final SearchWrapper search){
    	accessLogger.log(requestProvider.get());    
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

    private GenericEntity<java.util.List<BlastAlignment>> toGenericEntity(
    		final List<BlastAlignment> aligments
    ) {
        return new GenericEntity<java.util.List<BlastAlignment>>(
            newArrayList(aligments)
        ) { };
    }

    private GenericEntity<java.util.List<SourceProtein>> sourceProteinToGenericEntity(
    		final List<SourceProtein> sourceProteins
    ) {
        return new GenericEntity<java.util.List<SourceProtein>>(
            newArrayList(sourceProteins)
        ) { };
    }

    private GenericEntity<java.util.List<Protein>> proteinToGenericEntity(
    		final Set<Protein> aligments
    ) {
        return new GenericEntity<java.util.List<Protein>>(
            newArrayList(aligments)
        ) { };
    }
    
    private GenericEntity<java.util.List<ReferencePeptide>> referenceToGenericEntity(
    final Set<ReferencePeptide> peptides
    ) {
        return new GenericEntity<java.util.List<ReferencePeptide>>(
            newArrayList(peptides)
        ) { };
    }
    
    private GenericEntity<java.util.List<BioactivePeptide>> bioactiveToGenericEntity(
    final Set<BioactivePeptide> peptides
    ) {
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
