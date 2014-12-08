package es.uvigo.ei.sing.mahmi.controller.controllers;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.xml;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.utils.Location;
import es.uvigo.ei.sing.mahmi.controller.Configuration;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class RemoteServiceAbstractController {

    protected final Configuration config;

    protected final WebTarget getTargetFor(final String serviceName) {
        val target   = newClient().target(config.getLocatorURI()).path("/" + serviceName);
        val location = target.request(MediaType.APPLICATION_XML).get(Location.class);

        return newClient().target(location.getUri());
    }

    protected final <A, B> B post(final WebTarget target, final A data, final Class<B> responseType) {
        return target.request().post(xml(data)).readEntity(responseType);
    }

}
