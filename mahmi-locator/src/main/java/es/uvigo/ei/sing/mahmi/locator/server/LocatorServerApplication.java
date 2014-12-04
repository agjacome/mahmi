package es.uvigo.ei.sing.mahmi.locator.server;

import static es.uvigo.ei.sing.mahmi.locator.service.LocatorService.locatorService;

import java.util.Set;

import javax.ws.rs.core.Application;

import jersey.repackaged.com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import es.uvigo.ei.sing.mahmi.locator.Configuration;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class LocatorServerApplication extends Application {

    private final Configuration config;

    public static LocatorServerApplication locatorApplication(final Configuration config) {
        return new LocatorServerApplication(config);
    }

    @Override
    public Set<Object> getSingletons() {
        return Sets.newHashSet(locatorService(config));
    }

}
