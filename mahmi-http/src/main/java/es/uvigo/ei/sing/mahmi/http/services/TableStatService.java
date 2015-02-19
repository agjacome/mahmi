package es.uvigo.ei.sing.mahmi.http.services;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import es.uvigo.ei.sing.mahmi.common.entities.TableStat;
import es.uvigo.ei.sing.mahmi.database.daos.TableStatsDAO;
import fj.data.Set;


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
     /*
     @GET
     @Path("/{id}")
     public Response get(@PathParam("id") final int id) {
         return respond(
                 () -> dao.get(id),
                 status(OK)::entity,
                 status(NOT_FOUND)
             );
     }
     
     @GET
     public Response get() {
         return respond(
                 () -> dao.get(),
                 status(OK)::entity,
                 status(NOT_FOUND)
             );
     }*/

     @Override
     protected GenericEntity<java.util.List<TableStat>> toGenericEntity(
         final Set<TableStat> tableStats
     ) {
         return new GenericEntity<java.util.List<TableStat>>(
             newArrayList(tableStats)
         ) { };
     }

}
