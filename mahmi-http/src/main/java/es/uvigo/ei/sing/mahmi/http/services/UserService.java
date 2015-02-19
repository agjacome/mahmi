package es.uvigo.ei.sing.mahmi.http.services;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;

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

import es.uvigo.ei.sing.mahmi.common.entities.User;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.database.daos.UsersDAO;
import fj.data.Set;

@Path("/users")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public class UserService extends DatabaseEntityAbstractService<User, UsersDAO>{
    
    private UserService(final UsersDAO dao) {
        super(dao);
    }

    public static UserService userService(final UsersDAO dao) {
        return new UserService(dao);
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
     public Response insert(final User user) {
         return buildInsert(user);
     }

     @Override
     protected GenericEntity<java.util.List<User>> toGenericEntity(
         final Set<User> users
     ) {
         return new GenericEntity<java.util.List<User>>(
             newArrayList(users)
         ) { };
     }

}
