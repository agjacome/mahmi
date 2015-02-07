package es.uvigo.ei.sing.mahmi.http.services;

import static es.uvigo.ei.sing.mahmi.common.utils.exceptions.PendingImplementationException.notYetImplemented;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenomeProteins;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomeProteinsDAO;

@Path("/metagenomeproteins")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public final class MetaGenomeProteinsService extends DatabaseEntityAbstractService<MetaGenomeProteins, MetaGenomeProteinsDAO> {

    private MetaGenomeProteinsService(
        final MetaGenomeProteinsDAO dao
    ) {
        super(dao);
    }

    public static MetaGenomeProteinsService metaGenomeProteinsService(
        final MetaGenomeProteinsDAO dao
    ) {
        return new MetaGenomeProteinsService(dao);
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

    @POST
    public Response insert(final MetaGenomeProteins digestion) {
        return buildInsert(digestion);
    }

    @POST
    @Path("/all")
    public Response insertAll(final Set<MetaGenomeProteins> digestions) {
        return buildInsertAll(digestions);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final int id) {
        return buildDelete(Identifier.of(id));
    }

    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") final int id, final MetaGenomeProteins digestion
    ) {
        val toUpdate = digestion.setId(Identifier.of(id));
        return buildUpdate(toUpdate);
    }

    @GET
    @Path("/search")
    public Response search(
        @QueryParam("enzyme")  @DefaultValue("-1") final int enzymeId,
        @QueryParam("protein") @DefaultValue("-1") final int proteinId,
        @QueryParam("peptide") @DefaultValue("-1") final int peptideId,
        @QueryParam("page")    @DefaultValue( "1") final int page,
        @QueryParam("size")    @DefaultValue("50") final int size
    ) {
        // TODO: implement
        throw notYetImplemented;
    }

    @Override
    protected GenericEntity<List<MetaGenomeProteins>> toGenericEntity(
        final Collection<MetaGenomeProteins> metagenomeproteins
    ) {
        return new GenericEntity<List<MetaGenomeProteins>>(newArrayList(metagenomeproteins)) { };
    }

}
