package es.uvigo.ei.sing.mahmi.http.services;
import static es.uvigo.ei.sing.mahmi.common.entities.User.user;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
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
import es.uvigo.ei.sing.mahmi.http.wrappers.BooleanWrapper;
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
     
     @GET
     @Path("/username/{username}")
     public Response get(@PathParam("username") final String username) {
         return respond(
                 () -> BooleanWrapper.wrap(dao.exists(
                         user(Identifier.empty(),"","",username,""))),
                 status(OK)::entity
             );
     }
     
     @POST
     @Path("/register")
     public Response register(final User user) {
         return respond(
                 () -> BooleanWrapper.wrap(dao.register(user)),
                 status(OK)::entity
             );
     }
     
     
     @POST
     @Path("/login")
     public Response login(final User user) {
         return respond(
                 () -> BooleanWrapper.wrap(dao.login(user)),
                 status(OK)::entity
             );
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
