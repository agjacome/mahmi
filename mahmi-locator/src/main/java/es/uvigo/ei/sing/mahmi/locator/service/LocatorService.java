package es.uvigo.ei.sing.mahmi.locator.service;

import static es.uvigo.ei.sing.mahmi.common.utils.Location.location;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.utils.Location;
import es.uvigo.ei.sing.mahmi.locator.Configuration;

@Path("/")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocatorService {

    private final Configuration config;

    public static LocatorService locatorService(final Configuration config) {
        return new LocatorService(config);
    }

    @GET
    public Response getCutter(@QueryParam("name") final String serviceName) {
        switch (serviceName.toLowerCase()) {
            case "loader" : return status(OK).entity(getLoader()).build();
            case "cutter" : return status(OK).entity(getCutter()).build();
            default       : return status(BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/cutter")
    public Location getCutter() {
        return location("Protein cutter service", config.getCutterURI());
    }

    @GET
    @Path("/loader")
    public Location getLoader() {
        return location("MetaGenome Project loader service", config.getLoaderURI());
    }

}
