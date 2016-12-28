package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	private Browser                browser;
	private ProteinsDAO            proteinDAO;
	private ProteinInformationsDAO proteinInfoDAO;
	private MetagenomeInformationsDAO metagenomeInfoDAO;
	private MetagenomeMIxSDAO      metagenomeMIxSDAO;
	
    private PublicService(final PeptidesDAO peptidesDAO, 
						  final ProteinsDAO proteinDAO, 
						  final ProteinInformationsDAO proteinInfoDAO,
						  final MetagenomeInformationsDAO metagenomeInfoDAO,
						  final MetagenomeMIxSDAO metagenomeMIxSDAO,
						  final Browser browser ) {
    	super(peptidesDAO);
        this.browser = browser;
        this.proteinDAO = proteinDAO;
        this.proteinInfoDAO = proteinInfoDAO;
        this.metagenomeInfoDAO = metagenomeInfoDAO;
        this.metagenomeMIxSDAO = metagenomeMIxSDAO;
    }

    public static PublicService publicService(final PeptidesDAO dao, 
    										  final ProteinsDAO proteinDAO, 
    										  final ProteinInformationsDAO proteinInfoDAO,
    										  final MetagenomeInformationsDAO metagenomeInfoDAO,
    										  final MetagenomeMIxSDAO metagenomeMIxSDAO,
    										  final Browser browser ) {
        return new PublicService(dao, proteinDAO, proteinInfoDAO, metagenomeInfoDAO, metagenomeMIxSDAO, browser);
    }
    
    @GET
    @Path("proteins/{id}")
    public Response getByPeptide(@PathParam("id") final int id){
        return respond(
                () -> proteinDAO.getByPeptide(peptide(Identifier.of(id), AminoAcidSequence.empty())),
                as -> Response.status(OK).entity(proteinToGenericEntity(as))
            );
    }

    @GET
    @Path("peptides/references")
    public Response getReferences() {
    	return respond(
            () -> dao.getReferences(),
            as -> status(OK).entity(referenceToGenericEntity(as))
        );
    }

    @GET
    @Path("peptides")
    public Response getBioactives(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("40000") final int size
    ) {
    	return respond(
            () -> dao.getBioactives((page - 1) * size, size),
            as -> status(OK).entity(bioactiveToGenericEntity(as))
        );
    }
    
    @GET
    @Path("private/peptides")
    public Response exploreBioactives(
        @QueryParam("page") @DefaultValue( "1") final int page,
        @QueryParam("size") @DefaultValue("40000") final int size,
        @QueryParam("filterType") @DefaultValue("") final String filterType,
        @QueryParam("filter") @DefaultValue("") final String filter
    ) {
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
    @Path("peptides/sourceProteins/{id}")
    public Response getProteinsInfo(@PathParam("id") final int id){
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
