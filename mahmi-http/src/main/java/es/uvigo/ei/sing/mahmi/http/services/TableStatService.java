package es.uvigo.ei.sing.mahmi.http.services;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.TableStat;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.TableStatsDAO;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

@Path("/tableStats")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public class TableStatService extends DatabaseEntityAbstractService<TableStat, TableStatsDAO>{
    
    private TableStatService(final TableStatsDAO dao) {
        super(dao);
    }

    public static TableStatService tableStatService(final TableStatsDAO dao) {
        return new TableStatService(dao);
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

     @Override
     protected GenericEntity<java.util.List<TableStat>> toGenericEntity(
         final Set<TableStat> tableStats
     ) {
         return new GenericEntity<java.util.List<TableStat>>(
             newArrayList(tableStats)
         ) { };
     }

}
