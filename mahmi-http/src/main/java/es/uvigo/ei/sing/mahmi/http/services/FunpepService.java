package es.uvigo.ei.sing.mahmi.http.services;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.funpep.FunpepAnalyzer;

import static java.util.concurrent.CompletableFuture.runAsync;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Slf4j
@Path("/funpep")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
public class FunpepService {

    private final FunpepAnalyzer funpep;

    private FunpepService(final FunpepAnalyzer funpep) {
        this.funpep = funpep;
    }

    public static FunpepService funpepService(final FunpepAnalyzer funpep) {
        return new FunpepService(funpep);
    }

    @POST
    public Response analyze(
        @QueryParam("start") @DefaultValue("0") final int start,
        @QueryParam("count") @DefaultValue("10000") final int count
    ) {
        runAsync(() ->  {
            try {
                funpep.analyze(start, count);
            } catch (final IOException ioe) {
                log.error("FUNPEP: Error", ioe);
            }
        });

        return status(NO_CONTENT).build();
    }

}
